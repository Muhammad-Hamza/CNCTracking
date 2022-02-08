package com.example.cnctracking_2.ui.map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.cnctracking_2.MainActivity;
import com.example.cnctracking_2.R;
import com.example.cnctracking_2.config.APIManager;
import com.example.cnctracking_2.data.model.Parameters;
import com.google.android.material.tabs.TabLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.viewpager.widget.ViewPager;

public class MovementReport extends AppCompatActivity {
    ProgressBar progressBar;
    String message, reportText, route, dateTime, loginName, password, regNo;
    float latitude, longitude, speed;
    int unitID;
    ArrayList<Parameters> unitList = new ArrayList<Parameters>();
    //Intent intent;
    Bundle extras;

    boolean TripReportAllow = false;
    boolean HistoryOnMapAllow = false;
    boolean SpeedAnalysisAllow = false;
    boolean tempAllow = false;
    boolean fuelAllow = false;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //intent = getIntent();
        setContentView(R.layout.activity_movement_report);

     //   Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
      //  setSupportActionBar(toolbar);

       // TextView text  = (TextView) findViewById(R.id.toolbarTitle);
      //  text.setText("History");


     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
// Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        List<String> titles = new ArrayList<String>();
        titles.add("Movement");
        titles.add("Trip");
        titles.add("Speed");



        String[] titlesList = new String[titles.size()];

        for (int i = 0; i < titles.size(); i++) {
            titlesList[i] = titles.get(i);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles.size(), titlesList, true, true, HistoryOnMapAllow, tempAllow, fuelAllow, "T20");
        viewPager.setAdapter(adapter);
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_movement_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/
/*        if (id == android.R.id.home){

            //NavUtils.navigateUpFromSameTask(this);
            NavUtils.navigateUpTo(this, intent);
            finish();
            return true;

        }*/

        if (id == android.R.id.home) {
            Intent upIntent = new Intent(this, MainActivity.class);
            upIntent.putExtras(extras);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is not part of the application's task, so create a new task
                // with a synthesized back stack.
                TaskStackBuilder.from(this)
                        // If there are ancestor activities, they should be added here.
                        .addNextIntent(upIntent)
                        .startActivities();
                finish();
            } else {
                // This activity is part of the application's task, so simply
                // navigate up to the hierarchical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
