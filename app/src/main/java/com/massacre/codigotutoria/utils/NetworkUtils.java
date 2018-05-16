package com.massacre.codigotutoria.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.massacre.codigotutoria.activity.LanguageViewDrawer;
import com.massacre.codigotutoria.activity.SplashScreen;
import com.massacre.codigotutoria.dbhelper.ProgrammingLanguageDBHelper;
import com.massacre.codigotutoria.loader.ProgrammingLanguageHomeLoader;
import com.massacre.codigotutoria.models.ProgrammingLanguage;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * Created by saurabh on 6/8/17.
 */

public class NetworkUtils {
    public void downloadImageList(List<ProgrammingLanguage> languageList, Context context){
        for(ProgrammingLanguage language:languageList){
            Log.e("Location","Inside DownlaodImage of NetworkUtils");
            /*try {
                String urlArray[]=new ConfigUtils().getProperties(new String[]{
                                                    CodigoTutoriaConstant.HTTPS,
                                                    CodigoTutoriaConstant.STATIC_HOST_ADDRESS,CodigoTutoriaConstant.STATIC_HOST_PORT,
                                                    CodigoTutoriaConstant.IMAGE_LOCATION_SERVER},context);
                String url=urlArray[0]+urlArray[1]+urlArray[2]+urlArray[3]+"/" +
                        "pl."+language.getLanguageId()+"."+language.getImageResource();
                String path= CodigoTutoriaConstant.IMAGE_FOLDER;
                download(url,path,context,CodigoTutoriaConstant.TYPE_IMAGE);
            }catch (IOException e){
                e.printStackTrace();
            }*/
            downloadImage(language.getLanguageId(),language.getImageResource(),context);
        }
    }
    public void downloadImage(long languageId,String imageResource,Context context){
        try {
            String urlArray[]=new ConfigUtils().getProperties(new String[]{
                    CodigoTutoriaConstant.HTTPS,
                    CodigoTutoriaConstant.STATIC_HOST_ADDRESS,CodigoTutoriaConstant.STATIC_HOST_PORT,
                    CodigoTutoriaConstant.IMAGE_LOCATION_SERVER},context);
            String url=urlArray[0]+urlArray[1]+urlArray[2]+urlArray[3]+"/" +
                    "pl."+languageId+"."+imageResource;
            String path= CodigoTutoriaConstant.IMAGE_FOLDER;
            download(url,path,context,CodigoTutoriaConstant.TYPE_IMAGE);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void networkJSONRequesterForAllProgrammingLanguage(final ProgrammingLanguageDBHelper dbHelper, final Context context, String url, JSONArray jsonArray){
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, url,jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("ResponseJSON", response.toString());
                        try {
                            List<ProgrammingLanguage> programmingLanguageList=ProgrammingLanguage.getListFromJson(response);
                            Log.e("","NetworksUtils.networkJSONRequestForAllProgrammingLanguage() - Size of Programming Language Download: "+programmingLanguageList.size());
                            //calling to insert in database
                            dbHelper.insertAllProgrammingLanguage(programmingLanguageList);
                            //calling to download picture
                            downloadImageList(programmingLanguageList,context);
                            if(programmingLanguageList.size()>0){
                                Intent broadcastLanguagesHome=new Intent(ProgrammingLanguageHomeLoader.HOME_LISTENER_INTENT_FILTER_STRING);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastLanguagesHome);
                            }
                            Intent broadcastLanguagesSplash=new Intent(SplashScreen.SPLASH_LISTENER_INTENT_FILTER_STRING);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastLanguagesSplash);
                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error Fetching Data",Toast.LENGTH_SHORT).show();
                Intent broadcastLanguagesHome=new Intent(ProgrammingLanguageHomeLoader.HOME_LISTENER_INTENT_FILTER_STRING);
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastLanguagesHome);
                Intent broadcastLanguagesSplash=new Intent(SplashScreen.SPLASH_LISTENER_INTENT_FILTER_STRING);
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastLanguagesSplash);
            }

        }
        );
        requestQueue.add(jsonObjectRequest);

    }
    public static boolean checkConnectivity(Context context) {

//        try {
//            String values= new ConfigUtils().getProperty(CodigoTutoriaConstant.HOST_PING,context);
//            Log.e("PINGING ADDRESS",values);
//
//            return (Runtime.getRuntime().exec("ping -c 1 "+values).waitFor() == 0);
//
//        }catch (IOException e){e.printStackTrace();
//
//        }
//        catch(InterruptedException e){e.printStackTrace();}
        return true;
    }
    public void download(final String url,final String path,final Context context,final int type) throws IOException {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if(checkConnectivity(context)){
                    try {
                        Log.e("inside Asyn download",url+", "+path+" ");
                        URL Url = new URL(url);
                        HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream is = conn.getInputStream();
                            Log.e("No of Byte",conn.getContentLength()+"");
                            byte[] data = new byte[1000000];
                            if(type==CodigoTutoriaConstant.TYPE_IMAGE) {
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                new LocalStorage().saveImage(bitmap,path,Uri.parse(url).getLastPathSegment(),context);
    //                        os.write(data);
                            }else if(type==CodigoTutoriaConstant.TYPE_WEBPAGE){
                                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                                String content="";
                                String temp;
                                while((temp=br.readLine())!=null){
                                    content+=temp;
                                }
                                new LocalStorage().saveFile(content,Uri.parse(url).getLastPathSegment(),path,context);
                                //os.write(data);
                            }
                            is.close();

                        }
                        else{
                        Log.d("HTTP status:","NetworkUtils.download.doInBackground():"+conn.getResponseCode());
                        }

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d("","NetworkUtils.download.doInBackground():No Internet Connection"+"  returning null");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("","NetworkUtils.download.onPostExeute(): "+"sending Broadcast");
                Intent broadcastLanguagesUpdateWebview=new Intent(LanguageViewDrawer.LANGUAGE_VIEW_UPDATE_WEBVIEW_STRING);
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastLanguagesUpdateWebview);

            }
        }.execute();

    }

}
