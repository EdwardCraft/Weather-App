package com.example.android.sunchine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.sunchine.app.sync.SunshineSyncAdapter;

public class MainActivity extends AppCompatActivity{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String DETAILFRAGMENT_TAG  = "DFTAG";


    private boolean mTwoPane;
    private String  mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        setContentView(R.layout.activity_main);


        if(findViewById(R.id.weather_detail_container) != null){
            //The detail container will be present only in the large-screen layouts
            //(res/layout-sw600dp). If this view is present, then the activity should be
            //in two-pane mode.
            mTwoPane = true;

            // In two pane mode,show the detail view in this activity by
            //adding or replacing the fragment using a fragment manager
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DailyActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();

            }
        }else{
            mTwoPane = false;
            getSupportActionBar().setElevation(0);
        }

        ForeCastFragment foreCastFragment = ((ForeCastFragment)getSupportFragmentManager()
                 .findFragmentById(R.id.fragment_forecast));


        foreCastFragment.setUseTodayLayout(!mTwoPane);
        SunshineSyncAdapter.initializeSyncAdapter(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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
            startActivity(new Intent(this, SttingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onResume(){
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        //update the location in our second  pane using the fragment manager
        if(location != null && !location.equals(mLocation)){
            ForeCastFragment ff = (ForeCastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if(null != ff){
                ff.onLocationChanged();
            }
            mLocation = location;
        }



    }


}
