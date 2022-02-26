package com.example.cnctracking_2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cnctracking_2.ui.dashboard.DashboardWithHeader;
import com.example.cnctracking_2.ui.notifications.NotifcationActivity;
import com.example.cnctracking_2.ui.report.ReportActivity;
import com.example.cnctracking_2.ui.search.Search;
import com.example.cnctracking_2.util.ConstantUtil;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    DrawerLayout drawer;
    String password, loginName, userRole;
    TextView userSideMenu, toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbarMain);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Overridefab

            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        drawer.addDrawerListener(drawerToggle);

//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.bringToFront();
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        toolbarTitle.setText("Dashboard");
        View headerView = navigationView.getHeaderView(0);
        userSideMenu = (TextView) headerView.findViewById(R.id.user_sidemenu);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            password = extras.getString("password");
            loginName = extras.getString("loginName");
            userRole = extras.getString("userRole");
            try {
                userSideMenu.setText(loginName);
            } catch (Exception e) {
            }
        }

        //  View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        //toolbar = getSupportActionBar();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        //  DashboardWithHeader bottomSheet = new DashboardWithHeader();
        //bottomSheet.show(getSupportFragmentManager(),
        // "ModalBottomSheet");

        Fragment fragment = new DashboardWithHeader();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

/*        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
        setNavigationViewListener();

        navigationView.bringToFront();
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_app_bar_open_drawer_description);
        //drawer.addDrawerListener(toggle);
       /* navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this,"a "+item.getItemId(), Toast.LENGTH_SHORT);
                switch(item.getItemId()){
                    case R.id.text_gallery:
                        Toast.makeText(MainActivity.this,"Gallery", Toast.LENGTH_SHORT);
                        break;
                    case R.id.nav_slideshow:
                        Toast.makeText(MainActivity.this,"SlideShow", Toast.LENGTH_SHORT);
                        break;
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this,"Home", Toast.LENGTH_SHORT);
                        break;

                }
                return true;
            }
        });*/
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void changeFragment(Fragment fr) {
        FrameLayout fl = (FrameLayout) findViewById(R.id.fragment_frame);
        fl.removeAllViews();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.add(R.id.fragment_frame, fr);
        transaction1.commit();
    }

    public void changeFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //  Toast.makeText(MainActivity.this,"a "+item.getItemId(), Toast.LENGTH_SHORT).show();
        Fragment fragment;

        switch (item.getItemId()) {
            case R.id.nav_search:

                fragment = new Search();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                // changeFragment(fragment);
                //  Toast.makeText(MainActivity.this,"Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_slideshow:
                //  Toast.makeText(MainActivity.this,"SlideShow Dashboard", Toast.LENGTH_SHORT).show();
                /* fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
               */// changeFragment(fragment);
                finish();
                break;
            case R.id.nav_home:
                fragment = new DashboardWithHeader();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                // changeFragment(fragment);
                // Toast.makeText(MainActivity.this,"Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_notifications:
                SharedPreferences sp = getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantUtil.PREF_EXTRA_BUNDLE_1,  sp.getInt("moduleId", 0));
                Intent intent = new Intent(getApplicationContext(), NotifcationActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;

        }
        item.setChecked(true);
        if (item.getTitle().equals("Slideshow")) {
            setTitle("Dashboard");
        } else {
            setTitle(item.getTitle());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        // Log.d("onBackPressed_1", ""+getSupportFragmentManager().getBackStackEntryCount());

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            showSettingsAlert();
        } else {
            super.onBackPressed();
        }
    }

    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle(R.string.heading_dialog);
            // Setting Dialog Message
            alertDialog.setMessage(R.string.exit_dialog);
//+" "+ Arrays.asList(eventCounts)
            // On pressing Settings button
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Dialog box is not opening right now, pls try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_maths: {
                //do somthing
                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }*/
}