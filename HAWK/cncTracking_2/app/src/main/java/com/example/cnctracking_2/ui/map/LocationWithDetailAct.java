package com.example.cnctracking_2.ui.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cnctracking_2.MainActivity;
import com.example.cnctracking_2.R;
import com.example.cnctracking_2.config.APIManager;
import com.example.cnctracking_2.data.model.Parameters;
import com.example.cnctracking_2.data.model.TripsBean;
import com.example.cnctracking_2.ui.dashboard.DashboardWithHeader;
import com.example.cnctracking_2.ui.gallery.GalleryFragment;
import com.example.cnctracking_2.ui.home.HomeFragment;
import com.example.cnctracking_2.ui.map.ui.InfoFragment;
import com.example.cnctracking_2.ui.map.ui.MobilizerBottomFrag;
import com.example.cnctracking_2.ui.map.ui.MobilizerFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationWithDetailAct extends AppCompatActivity
{

    private static final String TAG = MainActivity.class.getSimpleName();

    BottomSheetBehavior sheetBehavior;

  /*  @BindView(R.id.btn_bottomsheet)
    Button btnBottomsheet;*/

    @BindView(R.id.temp_bottomsheet)
    LinearLayout layoutBottomSheet;

/*    @BindView(R.id.content)
    ConstraintLayout contentSheet;*/


    @BindView(R.id.info)
    LinearLayout info;

    @BindView(R.id.control)
    LinearLayout control;

    @BindView(R.id.history)
    LinearLayout history;

    @BindView(R.id.space)
    TextView space1;

    @BindView(R.id.space2)
    TextView space2;

    @BindView(R.id.space3)
    TextView space3;

    String loginName, password, userRole;
    public static final String DEFAULT = "N/A";
    String regNo, message, dateTime, deviceType, dateOnly;
    float latt, lngg, speed;
    boolean isNr;
    ProgressBar progressBar;
    ImageView searchImg, backButton;
    boolean historyFetched = false;
    int moduleId;
    Fragment mapfragment, infoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_with_detail);
        ButterKnife.bind(this);
        // BottomNavigationView navView = findViewById(R.id.nav_view);
        //   LinearLayout layoutBottomSheet = (LinearLayout) findViewById(R.id.temp_bottomsheet);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
/*        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();*/
        // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //       .findFragmentById(R.id.map);

        // mapFragment.getMapAsync(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

        backButton = (ImageView) findViewById(R.id.back);
        searchImg = (ImageView) findViewById(R.id.searchtop);
        // searchImg.setVisibility(View.GONE);
        SharedPreferences sp = getSharedPreferences("SelectedID", Context.MODE_PRIVATE);

        historyFetched = false;

        regNo = sp.getString("regNo", DEFAULT);
        message = sp.getString("location", DEFAULT);
        lngg = sp.getFloat("longitude", 0.0f);
        latt = sp.getFloat("latitude", 0.0f);
        dateTime = sp.getString("dateTime", DEFAULT);
        speed = sp.getFloat("speed", 0.0f);
        deviceType = sp.getString("deviceType", DEFAULT);
        moduleId = sp.getInt("moduleId", 0);
        // heading = sp.getInt("heading", 0);
        password = sp.getString("password", DEFAULT);
        loginName = sp.getString("loginName", DEFAULT);
        // isNr =  sp.getBoolean("isNr", FALSE);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateOnly = (month + 1) + "-" + day + "-" + year;

        sharedPreferencesMethod("", "", "", "", false);

        infoFragment = new InfoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_frag, infoFragment, infoFragment.getClass().getSimpleName()).setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        ).addToBackStack(null).commit();

        mapfragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mapfragment, mapfragment.getClass().getSimpleName()).setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        ).addToBackStack(null).commit();


//
        // btnBottomsheet.setVisibility(View.GONE);
        // fragment_map
        // navView.setOnNavigationItemSelectedListener(this);
        //  navView.setSelectedItemId(R.id.navigation_home);
        space1.setVisibility(View.VISIBLE);
        space2.setVisibility(View.GONE);
        space3.setVisibility(View.GONE);


        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView text = (TextView) findViewById(R.id.toolbarTitle);
        text.setText(regNo);


        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setPeekHeight(600);

        /*int contenteight = contentSheet.getHeight() - layoutBottomSheet.getHeight();
        contentSheet.getLayoutParams().height = contenteight;*/
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    {
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED:
                    {
                        //  btnBottomsheet.setText("Close Sheet");

                        break;
                    }
                    case BottomSheetBehavior.PEEK_HEIGHT_AUTO:
                    {

                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    {
                        // btnBottomsheet.setText("Expend Sheet");

                        break;
                    }

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {

            }
        });
       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //  final Intent i = new Intent(LocationWithDetailAct.this, MainActivity.class);
                // startActivity(i);
                view.animate().setDuration(500).alpha(0).withEndAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        view.setAlpha(1);
                        finish();
                    }
                });

            }
        });
        searchImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.animate().setDuration(500).alpha(0).withEndAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        view.setAlpha(1);
                        finish();
                    }
                });
                //   Toast.makeText(LocationWithDetailAct.this, "Search Button Click", Toast.LENGTH_SHORT).show();
            }
        });
        info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hideSpaceBar(1);
              /*  Fragment fragment2 = new InfoFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottom_frag, fragment2, fragment2.getClass().getSimpleName()).addToBackStack(null).commit();
*/
                mapfragment = new MapsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, mapfragment, mapfragment.getClass().getSimpleName()).setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).addToBackStack(null).commit();


            }
        });
        control.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hideSpaceBar(2);
                //  Fragment fragment2 = new Fragment1();
                //   getSupportFragmentManager().beginTransaction()
                //         .replace(R.id.bottom_frag, fragment2, fragment2.getClass().getSimpleName()).addToBackStack(null).commit();
                if (mapfragment != null)
                {
                    mapfragment.onStop();
                } else
                {
                    new MapsFragment().onStop();
                }

                changeToMObilizer();

            }
        });
        history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hideSpaceBar(3);
                if (mapfragment != null)
                {
                    mapfragment.onStop();
                } else
                {
                    new MapsFragment().onStop();
                }
                if (!historyFetched)
                {
                    getHistory();
                } else
                {
                    changeToHistory();
                }
                sheetBehavior.setPeekHeight(600);
            }
        });


    }

    public void hideSpaceBar(int i)
    {
        space1.setVisibility(View.GONE);
        space2.setVisibility(View.GONE);
        space3.setVisibility(View.GONE);
        switch (i)
        {
            case 1:
                space1.setVisibility(View.VISIBLE);
                break;
            case 2:
                space2.setVisibility(View.VISIBLE);
                break;
            case 3:
                space3.setVisibility(View.VISIBLE);
                break;
        }
    }

/*    @OnClick(R.id.btn_bottomsheet)
    public void toggleButtonSheet(){
        if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            btnBottomsheet.setText("Close Sheet");

        }else{
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            btnBottomsheet.setText("Open Sheet");

        }
    }*/


    private void changeFragment(Fragment fr)
    {
        FrameLayout fl = (FrameLayout) findViewById(R.id.nav_host_fragment);
        fl.removeAllViews();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.add(R.id.nav_host_fragment, fr);
        transaction1.commit();
    }
   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(LocationWithDetailAct.this, "a " + item.getItemId(), Toast.LENGTH_SHORT).show();
        Fragment fragment;

        switch (item.getItemId()) {
            case R.id.navigation_notifications:{

                fragment = new MapsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                // changeFragment(fragment);
                Toast.makeText(LocationWithDetailAct.this, "Gallery", Toast.LENGTH_SHORT).show();
                break;

        }case R.id.navigation_dashboard: {
                Intent intent = new Intent(LocationWithDetailAct.this, MovementReport.class);
                startActivity(intent);
                break;
            }
          //  case R.id.navigation_home:
               // final ItemListDialogFragment myBottomSheet = ItemListDialogFragment.newInstance(2);
            //    myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());
             //   break;


        }
        try{
        item.setChecked(true);
        if (item.getTitle().equals("Slideshow")) {
            setTitle("Dashboard");
        } else {
            setTitle(item.getTitle());
        }
    }catch(Exception e){e.printStackTrace();}
        return false;
    }*/

   /* @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng sydney = new LatLng(24.8602333, 67.0609333);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            /*Intent upIntent = new Intent(this, MainActivity.class);

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
            }*/
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(Fragment fragment, Bundle bundle)
    {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fmHolder, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    public void getHistory()
    {
        //   Log.d("getHistory", "1");
        try
        {
            progressBar.setVisibility(View.VISIBLE);
            String url = APIManager.getTrackLogsAPI();
            //    Log.d("getHistory", url +" - "+ loginName +" - "+ password);
            StringRequest sr = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>()
                    {

                        @Override
                        public void onResponse(String response)
                        {
                            JSONObject jsonResponse;
                            //         Log.d("getHistory", "2");
                            try
                            {
                                jsonResponse = new JSONObject(response);
                                //     Log.d("getHistory", response);
                                JSONArray jsonArray = jsonResponse.optJSONArray("searchResult");
                                JSONArray jsonArray2 = jsonResponse.optJSONArray("tripResults");
                                String totalRunning = jsonResponse.optString("totalTripsMileage").toString();
                                if (totalRunning != null)
                                {
                                    totalRunning = totalRunning + "@" + jsonResponse.optString("totalTripsDuration").toString();
                                }
                                String totalStop = jsonResponse.optString("totalStops").toString();
                                if (totalStop != null)
                                {
                                    totalStop = totalStop + "@" + jsonResponse.optString("totalStopDuration").toString();
                                }
                                //     Log.d("getHistory", totalRunning);
                                //     Log.d("getHistory3", totalStop);

                                historyFetched = true;
                                progressBar.setVisibility(View.GONE);
                                sharedPreferencesMethod(jsonArray.toString(), jsonArray2.toString(), totalRunning, totalStop, true);

                                //showArrayList();

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                                //     Log.d("getHistory", "Exception found in locationWithDetailAct getHistory");
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "History is empty", Toast.LENGTH_SHORT).show();
                                changeToHistory();
                            }

                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    progressBar.setVisibility(View.GONE);
                    // Log.d("getHistory", "Exception found volleyError in locationWithDetailAct getHistory");
                    Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", loginName);
                    params.put("psw", password);
                    params.put("fleetName", deviceType);
                    params.put("moduleId", "" + moduleId);
                    params.put("isDeviceDate", "true");
                    params.put("fromDate", dateOnly + " 12:00:00 AM");
                    params.put("toDate", dateOnly + " 11:59:59 PM");

                    return params;
                }

            };
            Volley.newRequestQueue(getApplicationContext()).add(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e)
        {
            // progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
        }
        //      Log.d("getHistory", "fromDate="+dateOnly+" 12:00:00 AM&toDate="+dateOnly+" 11:59:59 PM&moduleId="+moduleId);
    }

    public void sharedPreferencesMethod(String part1, String part2, String totalRunning, String totalStop, boolean changePage)
    {
        SharedPreferences sp = getSharedPreferences("Report", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("searchResult", part1);
        editor.putString("tripResults", part2);
        editor.putString("totalRunning", totalRunning);
        editor.putString("totalStop", totalStop);
        editor.commit();

        if (changePage)
            changeToHistory();

    }

    public void changeToHistory()
    {
        Fragment fragment2 = new Fragment1();
        Bundle args = new Bundle();
        args.putString("index", "");
        fragment2.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_frag, fragment2, fragment2.getClass().getSimpleName()).setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        ).addToBackStack(null).commit();

        Fragment fragment = new MapsFragment_history();
        Bundle args2 = new Bundle();
        args.putString("index", "");
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment, fragment.getClass().getSimpleName()).setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        ).addToBackStack(null).commit();

    }

    public void changeToMObilizer()
    {
        Fragment fragment2 = new MobilizerBottomFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_frag, fragment2, fragment2.getClass().getSimpleName()).setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        ).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed()
    {
        //  Log.d("onBackPressed", ""+getSupportFragmentManager().getBackStackEntryCount());
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 2)
        {
            // showSettingsAlert();
            finish();
        } else
        {
            finish();
        }
    }

    public void showSettingsAlert()
    {
        try
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle(R.string.heading_dialog);
            // Setting Dialog Message
            alertDialog.setMessage(R.string.exit_dialog);
//+" "+ Arrays.asList(eventCounts)
            // On pressing Settings button
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    finish();
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Dialog box is not opening right now, pls try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}