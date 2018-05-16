package com.massacre.codigotutoria.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.massacre.codigotutoria.R;
import com.massacre.codigotutoria.dbhelper.ProgrammingContract;
import com.massacre.codigotutoria.dbhelper.ProgrammingLanguageDBHelper;
import com.massacre.codigotutoria.models.ProgrammingLanguage;
import com.massacre.codigotutoria.utils.CodigoTutoriaConstant;
import com.massacre.codigotutoria.utils.ConfigUtils;
import com.massacre.codigotutoria.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    public static String SPLASH_LISTENER_INTENT_FILTER_STRING="com.massacre.PROGRAMMING_LANGUAGE_SPASH_LISTENER";
    List<ProgrammingLanguage> cached;
    private BroadcastReceiver prgrammingLanguageHomeListenerReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(prgrammingLanguageHomeListenerReceiver);
            Log.e("","fdasfaf");
            Thread t=new Thread(){
                public void run(){

                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){e.printStackTrace();}
                    finally{

                        Intent homeIntent=new Intent(getBaseContext(),HomeActivity.class);
                        startActivity(homeIntent);

                    }
                }
            };
            t.start();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryExtraDark));
        ImageView imageView=(ImageView)findViewById(R.id.splash_screen_spinner);
        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatCount(10000);
        rotate.setInterpolator(new LinearInterpolator());
        imageView.startAnimation(rotate);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getBaseContext());
        IntentFilter filter = new IntentFilter(SPLASH_LISTENER_INTENT_FILTER_STRING);
        localBroadcastManager.registerReceiver(prgrammingLanguageHomeListenerReceiver, filter);
        updateProgrammingLanguage(getBaseContext());


    }
    private void updateProgrammingLanguage(Context context){
        ProgrammingLanguageDBHelper dbHelper;
        try {
            Log.e("", "HomeActivity.updateProgrammingLanguage()");
            String values[] = new ConfigUtils().getProperties(new String[]{
                    CodigoTutoriaConstant.HTTP,
                    CodigoTutoriaConstant.HOST_ADDRESS,
                    CodigoTutoriaConstant.HOST_PORT,
                    CodigoTutoriaConstant.ALL_PROGRAMMING_LANGUAGE
            }, context);
            String url =  values[0]+values[1]+values[2]+values[3];

            Log.e("URL",url);
            dbHelper=new ProgrammingLanguageDBHelper(context);

            //Calling getLanguageInSQLite to get all the language in the local machine
            JSONArray jsonArray=null;
            jsonArray=dbHelper.getLanguagesIdDateInSQLite();
            Log.e("on Receive",jsonArray.toString());

            //calling to request new language and insert in database
            new NetworkUtils().networkJSONRequesterForAllProgrammingLanguage(dbHelper,context,url,jsonArray);

            //calling to request UPDATE on the existing Language;

        }catch (IOException e){e.printStackTrace();}
        catch (JSONException e){e.printStackTrace();}

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
