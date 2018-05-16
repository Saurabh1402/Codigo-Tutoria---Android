package com.massacre.codigotutoria.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.massacre.codigotutoria.dbhelper.ProgrammingLanguageDBHelper;
import com.massacre.codigotutoria.models.ProgrammingLanguage;

import java.text.ParseException;
import java.util.List;

/**
 * Created by saurabh on 25/8/17.
 */

public class ProgrammingLanguageHomeLoader extends AsyncTaskLoader<List<ProgrammingLanguage>> {
    Context context;
    public static int PROGRAMMING_LANGUAGE_HOME_LOADER=1;
    public static String HOME_LISTENER_INTENT_FILTER_STRING="com.massacre.PROGRAMMING_LANGUAGE_HOME_LISTENER";
    List<ProgrammingLanguage> cached;
    private BroadcastReceiver prgrammingLanguageHomeListenerReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            forceLoad();
        }
    };
    public ProgrammingLanguageHomeLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter=new IntentFilter(HOME_LISTENER_INTENT_FILTER_STRING);
        localBroadcastManager.registerReceiver(prgrammingLanguageHomeListenerReceiver,filter);
        if(cached==null){
            forceLoad();
        }else{
            deliverResult(cached);

        }}

    @Override
    public List<ProgrammingLanguage> loadInBackground() {
        List<ProgrammingLanguage> programmingLanguageList=null;
        try {
            programmingLanguageList=new ProgrammingLanguageDBHelper(getContext()).getProgrammingLanguageInSQLite();
            if(programmingLanguageList.size()>0)
            Log.e("loadInBackground:title",programmingLanguageList.get(0).getTitle());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return programmingLanguageList;
    }

    @Override
    public void deliverResult(List<ProgrammingLanguage> data) {
        cached=data;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(prgrammingLanguageHomeListenerReceiver);
    }
}
