package com.massacre.codigotutoria.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.massacre.codigotutoria.R;
import com.massacre.codigotutoria.adapter.LanguagviewDrawerAdapter;
import com.massacre.codigotutoria.dbhelper.ProgrammingContract;
import com.massacre.codigotutoria.dbhelper.ProgrammingLanguageDBHelper;
import com.massacre.codigotutoria.models.ProgrammingLanguage;
import com.massacre.codigotutoria.utils.CodigoTutoriaConstant;
import com.massacre.codigotutoria.utils.ConfigUtils;
import com.massacre.codigotutoria.utils.LocalStorage;
import com.massacre.codigotutoria.utils.NetworkUtils;

import java.io.IOException;
import java.text.ParseException;

public class LanguageViewDrawer extends AppCompatActivity{
    LanguagviewDrawerAdapter languagviewDrawerAdapter;
    RecyclerView drawerRecyclerLanguageView;
    long programmingLanguageId;
    ProgrammingLanguageDBHelper dbHelper;
    public static long languageHeaderId=1;
    public static long languageIndexId =1;
    AdView mAdView;
    public ProgrammingLanguage programmingLanguage;

    public static String LANGUAGE_VIEW_LOAD_WEBVIEW_INTENT_FILTER_STRING ="com.massacre.LANGUAGE_VIEW_LOAD_WEBVIEW_LISTENER";
    private BroadcastReceiver languageViewOnRecyclerViewTouchWebviewReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long headerId=intent.getLongExtra(ProgrammingContract.LanguageHeaderDefinition.COLUMN_LANGUAGE_HEADER_ID,-1);
            long indexId=intent.getLongExtra(ProgrammingContract.LanguageIndexDefinition.COLUMN_LANGUAGE_INDEX_ID,-1);
            Log.e(":","LanguageViewDrawer.languageViewOnRecyclerViewTouch...onRecieve()=>headerId="+headerId);
            Log.e(":","LanguageViewDrawer.languageViewOnRecyclerViewTouch...onRecieve()=>indexId="+indexId);
            loadWebpage();
        }
    };
    private BroadcastReceiver languageViewLoadWebviewReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadWebpage();
        }
    };

    public static String LANGUAGE_VIEW_UPDATE_WEBVIEW_STRING ="com.massacre.LANGUAGE_VIEW_UPDATE_WEBVIEW_LISTENER";
    private BroadcastReceiver languageViewUpdateWebviewReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("","LanguageViewDrawer.languageViewUpdateWeViewRecierver.onReceive(): now calling updateWebview()");
            updateWebview();
        }
    };


    //boolean isWebpageAvailableAndLoaded=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper=new ProgrammingLanguageDBHelper(this);
//        ProgrammingLanguage programmingLanguage=loadProgrammingLanguage();
//        getWindow().setStatusBarColor(Color.parseColor(programmingLanguage.getColorAccent()));
//        getWindow().setNavigationBarColor(Color.parseColor(programmingLanguage.getColorAccent()));

        setContentView(R.layout.activity_language_view_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setBackgroundColor(Color.parseColor(programmingLanguage.getColorPrimary()));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //Registering Broadcast Receiver to Refresh WebView
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest
//                .Builder().build();
                .Builder().addTestDevice("929510ABDE2833E3010E20CE0AF3267A").build();

        mAdView.loadAd(adRequest);

        drawerRecyclerLanguageView=(RecyclerView)findViewById(R.id.language_view_drawer_recyclerview);
        dbHelper=new ProgrammingLanguageDBHelper(this);


        registerListener();
        //INITIALIZE VIEW
        initializeActivity();

        setTitle(toTitleCase(programmingLanguage.getTitle()));


    }
    public String toTitleCase(String s){
        String splits[]=s.split(" ");
        String result="";
        for (int i = 0; i < splits.length; i++) {
            splits[i]=splits[i].charAt(0)+splits[i].substring(1,splits[i].length()).toLowerCase();
            result+=splits[i]+" ";
        }

        return result;
    }
    public void initializeActivity(){
        programmingLanguage=loadProgrammingLanguage();

        languageHeaderId=programmingLanguage.getHeaders().get(0).getLanguageHeaderId();
        languageIndexId=programmingLanguage.getHeaders().get(0).getIndex().get(0).getLanguageIndexId();
        Log.e("","LanguageViewDrawer.initializeAcitivity(): WEBVIEW:language Header"+languageHeaderId+"");
        Log.e("","LanguageViewDrawer.initializeAcitivity(): WEBVIEW:language Index"+languageIndexId+"");
        initializeDrawer(programmingLanguage);
//        getWindow().setStatusBarColor(Color.parseColor(programmingLanguage.getColorAccent()));
//        getWindow().setNavigationBarColor(Color.parseColor(programmingLanguage.getColorPrimary()));
        loadWebpage();
    }

    public ProgrammingLanguage loadProgrammingLanguage(){
        ProgrammingLanguage programmingLanguage= null;
        Intent intent=getIntent();
        String programmingLanguageIdString=intent.getStringExtra(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID);
        if(programmingLanguageIdString.equals("")||programmingLanguageIdString==null){
            onBackPressed();
        }
        programmingLanguageId=Long.parseLong(programmingLanguageIdString);
        try {
            programmingLanguage = dbHelper.getLanguageByIdInSQLite(programmingLanguageId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(programmingLanguage==null){
            //When required Programming Language is not present in the SQLite database
            // with the required programingLanguageId
            onBackPressed();
        }
        return programmingLanguage;
    }

    public void initializeDrawer(ProgrammingLanguage programmingLanguage){
        languagviewDrawerAdapter=new LanguagviewDrawerAdapter(programmingLanguage,getBaseContext());
        drawerRecyclerLanguageView.setAdapter(languagviewDrawerAdapter);
        LinearLayoutManager ll=new LinearLayoutManager(this);
        drawerRecyclerLanguageView.setLayoutManager(ll);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.language_view_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //LoadWebpage is called when starting the activity or clicking on the drawer item or change in network connecting...
    // NOTE THAT IT WILL LOAD WEBPAGE ONLY WHEN IT IS AVAILABLE else it can a function Newtork.download which will download and
    //send broadcast and call updateWebpage() method
    public void loadWebpage(){
        findViewById(R.id.langauge_view_progressbar).setVisibility(View.VISIBLE);
        View noInternetConnection=findViewById(R.id.langauge_view_no_internet_connection);
        noInternetConnection.setVisibility(View.GONE);
        WebView languageViewWebpage=(WebView)findViewById(R.id.language_view_webview);
        languageViewWebpage.getSettings().setJavaScriptEnabled(true);
        languageViewWebpage.clearCache(true);
        languageViewWebpage.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }


        Log.e("","Inside LanguageViewDrawer.loadWebpage():"+languageHeaderId+" "+languageIndexId);

        String fileName=CodigoTutoriaConstant.ABBR_LANGUAGE_INDEX+
                        CodigoTutoriaConstant.DOT+
                        languageIndexId+
                        CodigoTutoriaConstant.DOT+CodigoTutoriaConstant.EXTENSTION_HTML;

        String path=CodigoTutoriaConstant.WEBPAGE_FOLDER+CodigoTutoriaConstant.FORWARD_SLASH+programmingLanguage.getTitle();
        String webPageLocationLocally="file://"+ getExternalFilesDir(path)+CodigoTutoriaConstant.FORWARD_SLASH+fileName;
        webPageLocationLocally=webPageLocationLocally.replaceAll(" ","%20");
        if(LocalStorage.isFileAvailable(fileName,path,getBaseContext())){
                //Log.d("", "Inside LanguageViewDrawer.loadWebpage(): " + isWebpageAvailableAndLoaded);
                Log.e("Webpage:AVAIL", "Inside LanguageViewDrawer.loadWebpage(): " + webPageLocationLocally);

                findViewById(R.id.langauge_view_progressbar).setVisibility(View.GONE);
                findViewById(R.id.langauge_view_no_internet_connection).setVisibility(View.GONE);

            languageViewWebpage.setVisibility(View.VISIBLE);
            languageViewWebpage.loadUrl(webPageLocationLocally);
                //isWebpageAvailableAndLoaded = true;

        }
        else{
            try {
                String urlArray[] = new ConfigUtils().getProperties(new String[]{
                        CodigoTutoriaConstant.HTTPS,//0
                        CodigoTutoriaConstant.STATIC_HOST_ADDRESS,//1
                        CodigoTutoriaConstant.STATIC_HOST_PORT,//2
                        CodigoTutoriaConstant.WEBPAGE_LOCATION_SERVER//3
                }, getBaseContext());
                String url = urlArray[0] + urlArray[1] + urlArray[2] + urlArray[3] +
                        CodigoTutoriaConstant.FORWARD_SLASH+
                        programmingLanguage.getTitle()+
                        CodigoTutoriaConstant.FORWARD_SLASH +
                        fileName;
                url=url.replaceAll(" ","%20");
                new NetworkUtils().download(
                        url,
                        path,
                        getBaseContext(),
                        CodigoTutoriaConstant.TYPE_WEBPAGE
                );


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void updateWebview(){
        Log.e("","LanguageViewDrawer.updateWebview(): ");
        View noInternetConnection = findViewById(R.id.langauge_view_no_internet_connection);
        WebView languageViewWebpage = (WebView) findViewById(R.id.language_view_webview);
        languageViewWebpage.getSettings().setJavaScriptEnabled(true);
        languageViewWebpage.clearCache(true);
        String fileName = CodigoTutoriaConstant.ABBR_LANGUAGE_INDEX +
                    CodigoTutoriaConstant.DOT +
                    languageIndexId +
                    CodigoTutoriaConstant.DOT + CodigoTutoriaConstant.EXTENSTION_HTML;

        String path = CodigoTutoriaConstant.WEBPAGE_FOLDER + CodigoTutoriaConstant.FORWARD_SLASH + programmingLanguage.getTitle();
        String webPageLocationLocally = "file://" + getExternalFilesDir(path) + CodigoTutoriaConstant.FORWARD_SLASH + fileName;
        webPageLocationLocally=webPageLocationLocally.replaceAll(" ","%20");
        if ( LocalStorage.isFileAvailable(fileName, path, getBaseContext())) {
                //Log.d("", "Inside LanguageViewDrawer.updateWebpage(): " + isWebpageAvailableAndLoaded);
            findViewById(R.id.langauge_view_progressbar).setVisibility(View.GONE);
            languageViewWebpage.loadUrl(webPageLocationLocally);
            languageViewWebpage.loadUrl(webPageLocationLocally);
            languageViewWebpage.setVisibility(View.VISIBLE);
                //isWebpageAvailableAndLoaded = true;


        }else{
            Log.e("","LanguageViewDrawer.updateWebview():=>Webpage:Not Download"+ webPageLocationLocally);
            languageViewWebpage.setVisibility(View.GONE);
            findViewById(R.id.langauge_view_progressbar).setVisibility(View.GONE);
            noInternetConnection.setVisibility(View.VISIBLE);
            Button noInternetButton = (Button) noInternetConnection.findViewById(R.id.no_internet_connection_button);
            noInternetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadWebpage();
                }
            });
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.item_animator_from_right);
            noInternetConnection.startAnimation(animation);
        }

    }
    public void registerListener(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        this.registerReceiver(languageViewLoadWebviewReceiver,filter);
        LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this.getBaseContext());
        IntentFilter loadWebviewfilter=new IntentFilter(LANGUAGE_VIEW_LOAD_WEBVIEW_INTENT_FILTER_STRING);
        IntentFilter updateWebviewfilter=new IntentFilter(LANGUAGE_VIEW_UPDATE_WEBVIEW_STRING);
        localBroadcastManager.registerReceiver(languageViewOnRecyclerViewTouchWebviewReceiver,loadWebviewfilter);
        localBroadcastManager.registerReceiver(languageViewUpdateWebviewReceiver,updateWebviewfilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this.getBaseContext()).unregisterReceiver(languageViewOnRecyclerViewTouchWebviewReceiver);
        LocalBroadcastManager.getInstance(this.getBaseContext()).unregisterReceiver(languageViewUpdateWebviewReceiver);
        unregisterReceiver(languageViewLoadWebviewReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
    }
}
