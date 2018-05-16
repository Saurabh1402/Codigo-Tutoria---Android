package com.massacre.codigotutoria.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.massacre.codigotutoria.activity.LanguageViewDrawer;
import com.massacre.codigotutoria.loader.ProgrammingLanguageHomeLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by saurabh on 25/8/17.
 */

public class LocalStorage {
    public void saveImage(Bitmap bitmap, String path, String fileName, Context context) throws IOException {
        File file = new File(context.getExternalFilesDir(path), fileName);
        if (!isExternalStorageWritable(file)) {
            Log.e("getFileDir()",context.getFilesDir()+"");
            file = new File(context.getFilesDir()+CodigoTutoriaConstant.FORWARD_SLASH+
                    path, fileName);
            Log.e("external Storage", "external storage not available");
        }
        OutputStream os=new FileOutputStream(file);;
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();
        Intent broadcastLanguagesHome=new Intent(ProgrammingLanguageHomeLoader.HOME_LISTENER_INTENT_FILTER_STRING);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastLanguagesHome);
    }
    public Bitmap getImage(String path,String fileName,Context context){
        Bitmap bitmap=BitmapFactory.decodeFile(context.getExternalFilesDir(path)+CodigoTutoriaConstant.FORWARD_SLASH+
                        fileName);
        return bitmap;
    }

    public static boolean isFileAvailable(String fileName,String path,Context context){
        File file=new File(context.getExternalFilesDir(path),fileName);
        if(!isExternalStorageWritable(file)){
            file=new File(context.getFilesDir()+CodigoTutoriaConstant.FORWARD_SLASH+
                    path, fileName);
        }

        return file.exists();
    }
    public void saveFile(String content,  String fileName, String path,Context context) throws IOException {
        File file = new File(context.getExternalFilesDir(path), fileName);
        if (!isExternalStorageWritable(file)) {
            Log.e("getFileDir()",context.getFilesDir()+"");
            file = new File(context.getFilesDir()+CodigoTutoriaConstant.FORWARD_SLASH+
                    path, fileName);

            Log.e("external Storage", "external storage not available");
        }
        OutputStream os=new FileOutputStream(file);;
        OutputStreamWriter osw=new OutputStreamWriter(os);
        Log.e("inside SaveFile:"+fileName,content);
        osw.write(content);
        osw.flush();
        os.close();
        Intent broadcastLanguageview=new Intent(LanguageViewDrawer.LANGUAGE_VIEW_LOAD_WEBVIEW_INTENT_FILTER_STRING);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastLanguageview);
    }
    public static boolean isExternalStorageWritable(File file) {
        String state;
        if (Build.VERSION.SDK_INT > 21)
            state = Environment.getExternalStorageState(file);
        else  state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
