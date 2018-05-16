package com.massacre.codigotutoria.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.massacre.codigotutoria.R;
import com.massacre.codigotutoria.adapter.LanguageHomeAdapter;
import com.massacre.codigotutoria.aysnctask.LoadStaticFiles;
import com.massacre.codigotutoria.dbhelper.ProgrammingContract;
import com.massacre.codigotutoria.dbhelper.ProgrammingLanguageDBHelper;
import com.massacre.codigotutoria.loader.ProgrammingLanguageHomeLoader;
import com.massacre.codigotutoria.models.ProgrammingLanguage;
import com.massacre.codigotutoria.utils.AutoFitGridLayoutManager;
import com.massacre.codigotutoria.utils.CodigoTutoriaConstant;
import com.massacre.codigotutoria.utils.ConfigUtils;
import com.massacre.codigotutoria.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.massacre.codigotutoria.utils.NetworkUtils.checkConnectivity;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ProgrammingLanguage>>, NavigationView.OnNavigationItemSelectedListener{
    Snackbar snackbar;
    AdView mAdView;
    BroadcastReceiver connectivityMonitoringReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            loadingOnReceive();
    }


    };
    LanguageHomeAdapter languageHomeAdapter;
    RecyclerView languageListHomeRecyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.home_activity_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //ADMOB
        MobileAds.initialize(this,"ca-app-pub-9162090196692742~5398849349");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest
//                .Builder().build();
        .Builder().addTestDevice("929510ABDE2833E3010E20CE0AF3267A").build();

        mAdView.loadAd(adRequest);

        //NEWTORK CHANGE LISTENER RECIEVER;
        registerNetworkChangeListener();

        //INITIALIZE LANGUAGE VIEW RECYCLER VIEW
        initializeRecyclerviewLanguageList();

        //
        new LoadStaticFiles().execute(getBaseContext());


        snackbar=Snackbar.make(findViewById(R.id.language_home_coordinator_layout),"No internet connection!!",Snackbar.LENGTH_INDEFINITE).setAction("Refresh",new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loadingOnReceive();

            }
        });

        //loadingOnReceive();

    }

    public void registerNetworkChangeListener(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        connectivityMonitoringReceiver.goAsync();
        this.registerReceiver(connectivityMonitoringReceiver,filter);

//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }

    public void initializeRecyclerviewLanguageList(){
        languageHomeAdapter=new LanguageHomeAdapter(new ArrayList<ProgrammingLanguage>(),this.getBaseContext());
        languageListHomeRecyclerview=(RecyclerView)findViewById(R.id.all_language_list_home_recycler_view);
        languageListHomeRecyclerview.setAdapter(languageHomeAdapter);

        GridLayoutManager gl=null;

        gl=new AutoFitGridLayoutManager(this,700);//new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        languageListHomeRecyclerview.setLayoutManager(gl);
        recyclerviewOnClickListener(languageListHomeRecyclerview);
        getSupportLoaderManager().initLoader(
                ProgrammingLanguageHomeLoader.PROGRAMMING_LANGUAGE_HOME_LOADER,
                null,
                this
        );
    }
    @Override
    public Loader<List<ProgrammingLanguage>> onCreateLoader ( int id, Bundle args){
    if (ProgrammingLanguageHomeLoader.PROGRAMMING_LANGUAGE_HOME_LOADER == id) {
        Log.e("Location","Inside ChatActivity/oncreateLoader");
        Loader<List<ProgrammingLanguage>> loader = new ProgrammingLanguageHomeLoader(getBaseContext());
        return loader;
    }
    return null;
    }

    @Override
    public void onLoadFinished(Loader<List<ProgrammingLanguage>> loader, List<ProgrammingLanguage> data) {
        Log.e("","HomeActivity.onLoadFinished(): => programmingLanguagelist.length()="+data.size());
        findViewById(R.id.language_home_progressbar).setVisibility(View.GONE);
        languageHomeAdapter.swapData(data);
        //runLayoutAnimation(this.languageListHomeRecyclerview);

    }
    @Override
    public void onLoaderReset(Loader<List<ProgrammingLanguage>> loader) {
        languageHomeAdapter.swapData(new ArrayList<ProgrammingLanguage>());
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(connectivityMonitoringReceiver);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animator_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    private void recyclerviewOnClickListener(RecyclerView recyclerView){
        final GestureDetector mGestureDetector=new GestureDetector(HomeActivity.this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
     recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
         @Override
         public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
             View view=rv.findChildViewUnder(e.getX(),e.getY());
             if(view!=null && mGestureDetector.onTouchEvent(e)){
                 Log.e("Location","HomeActivity.recyclerViewOnClickListener().onInterceptTouchEvent()");
                 //Toast.makeText(getBaseContext(),"touch Event",Toast.LENGTH_SHORT).show();
                 TextView programmingLanuageIdTv=(TextView)view.findViewById(R.id.texview_programminglanguage_id_home);
                 //UserProfile userProfile=new Gson().fromJson(userProfileTv.getText().toString(),UserProfile.class);
                 Intent intent=new Intent(getBaseContext(),LanguageViewDrawer.class);
//                    Log.e("SAURABH",userProfileTv.getText().toString());
                 intent.putExtra(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID,programmingLanuageIdTv.getText());
                 startActivity(intent);
                 return true;
             }
             return false;
         }

         @Override
         public void onTouchEvent(RecyclerView rv, MotionEvent e) {

         }

         @Override
         public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

         }
     });
    }

    public void loadingOnReceive(){
        snackbar.dismiss();
        Log.d("","HomeActivity.loadingOnRecieve()");
        findViewById(R.id.language_home_progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.language_home_no_internet_connection).setVisibility(View.GONE);
        if(checkConnectivity(getBaseContext())){
            updateProgrammingLanguage(getBaseContext());

        }else{
            Toast.makeText(getBaseContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            Log.e(":","HomeActivity().NOT CONNECTED");
            findViewById(R.id.language_home_progressbar).setVisibility(View.GONE);
            snackbar=Snackbar.make(findViewById(R.id.language_home_coordinator_layout),"Internet not Available!", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("Refresh",new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    loadingOnReceive();

                }
            });
            snackbar.show();
//            View view =findViewById(R.id.language_home_no_internet_connection);
//            View recycler=view.findViewById(R.id.all_language_list_home_recycler_view);
//            if(recycler!=null)recycler.setVisibility(View.GONE);
//            view.setVisibility(View.VISIBLE);
//            ((Button)view.findViewById(R.id.no_internet_connection_button)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    loadingOnReceive();
//                }
//            });

        }
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
