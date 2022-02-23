package com.example.cnctracking_2.ui.map.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cnctracking_2.MainActivity;
import com.example.cnctracking_2.R;
import com.example.cnctracking_2.config.APIManager;
import com.example.cnctracking_2.ui.ControlFragment;
import com.example.cnctracking_2.ui.map.LocationWithDetailAct;
import com.example.cnctracking_2.ui.map.MapsFragment;
import com.example.cnctracking_2.ui.report.ReportFragment;
import com.example.cnctracking_2.util.ConstantUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressBar progressBar;
    String  loginName, password, userRole;
    int unitID;
    TextView label1, label2, label3, label4, label5, label6, label7, label8, speedLabel, lastRecordTimeLabel;
    TextView device1, device2, device3, device4, device5, simpleChronometer_temp;
    public static final String DEFAULT = "N/A";
    String regNo, custName,dateTime;
    float latt,lngg, speed;
    boolean isNr;
    ImageView likeButton, shareButton, configButton, deviceStatusImg;
    LinearLayout btnReport;
    String  deviceType;
    int moduleId, userId;
    Chronometer simpleChronometer;
    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        label1 = (TextView) v.findViewById(R.id.adress);
        label2 = (TextView)  v.findViewById(R.id.satellite);
        label3 = (TextView)  v.findViewById(R.id.date);
        label4 = (TextView)  v.findViewById(R.id.odo);
        label5 = (TextView)  v.findViewById(R.id.battery);
        label6 = (TextView)  v.findViewById(R.id.driver);
        label7 = (TextView)  v.findViewById(R.id.temp);
        label8 = (TextView)  v.findViewById(R.id.fuel);
        likeButton = (ImageView) v.findViewById(R.id.like);
        shareButton = (ImageView) v.findViewById(R.id.share);
        configButton = (ImageView) v.findViewById(R.id.configure);
        btnReport = (LinearLayout) v.findViewById(R.id.btnReport);
        deviceStatusImg = (ImageView) v.findViewById(R.id.info_device_status);
        speedLabel = (TextView)  v.findViewById(R.id.speed_info);
        lastRecordTimeLabel = (TextView)  v.findViewById(R.id.last_packet_time_info);
        simpleChronometer   = (Chronometer) v.findViewById(R.id.simpleChronometer);
        simpleChronometer_temp  = (TextView)  v.findViewById(R.id.simpleChronometer_temp);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.GONE);
        simpleChronometer.setVisibility(View.GONE);
        configButton.setVisibility(View.GONE);

        getSharePrfDataForDevice();
        // label1.setText(message);
        label2.setText("");
        //  label3.setText(dateTime);
        label4.setText("");
        label5.setText("");
        label6.setText(custName);

        label7.setText("");
        label8.setText("");

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(), "Fav Button Click", Toast.LENGTH_SHORT).show();
                view.animate().setDuration(500).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                view.setAlpha(1);
                                // startActivity(i);

                            }
                        });
                saveFavDevice();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().setDuration(500).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                view.setAlpha(1);
                                // startActivity(i);

                            }
                        });
                // Uri uri = Uri.parse("http://maps.google.com?q="+latt+","+lngg); // missing 'http://' will cause crashed
                // Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                String url = "My Location: http://maps.google.com?q="+latt+","+lngg;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, url);
                intent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);
                //Toast.makeText(getActivity(), "Share Button Click", Toast.LENGTH_SHORT).show();
            }
        });
        btnReport.setOnClickListener(v1 ->
        {
            Bundle bundle = new Bundle();
            bundle.putInt(ConstantUtil.PREF_EXTRA_BUNDLE_1, moduleId);
            ((LocationWithDetailAct) getActivity()).changeFragment(new ReportFragment(), bundle);

        });
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().setDuration(500).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                view.setAlpha(1);
                                ControlFragment dialog = new ControlFragment();

                                dialog.show(getFragmentManager(), "ControlFragment");
                            }
                        });

                /*FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frm_layout, fragment);
                fragmentTransaction.commit();*/
                // Toast.makeText(getActivity(), "Config Button Click", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            SharedPreferences sp2 = getActivity().getSharedPreferences("InfoDevice", Context.MODE_PRIVATE);
            String report = sp2.getString("searchResult", DEFAULT);
            Log.d("InfoFrag_1_report", report);
            if (report != null && !report.equals("N/A")) {

/*
        String url = APIManager.getLiveTrackingAPI();
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
*/
                JSONObject jsonResponse;
                try {
                    Log.d("InfoFrag_1", "json Start");
                    Log.d("InfoFrag_1", report);
                    jsonResponse = new JSONObject(report);
                            /*jsonResponse = new JSONObject(response);
                            moving = jsonResponse.optInt("Moving");*/

                    label1.setText(jsonResponse.getString("refPoint"));
                    label2.setText(jsonResponse.getString("satellite"));
                    label3.setText(jsonResponse.getString("eventTime"));
                    label4.setText(jsonResponse.getString("odoMeter"));
                    label5.setText(jsonResponse.getString("voltage"));
                    speedLabel.setText(jsonResponse.getString("speed").toString() +" KM/h");
                    Log.d("InfoFrag_22", "" + jsonResponse.getInt("statusId"));


//                1. nr grey color D4D4D4
//                2. parked/stop blue 3498db
//                3. moving green   2ecc71
//                4. idling yellow  f1c40f
                    try {
                        int statusId = jsonResponse.getInt("statusId");
                        if (statusId == 1) {
                            deviceStatusImg.setImageResource(R.mipmap.nr_device_d);
                        } else if (statusId == 2) {
                            deviceStatusImg.setImageResource(R.mipmap.stop_device_d);
                        } else if (statusId == 3) {
                            deviceStatusImg.setImageResource(R.mipmap.running_device_d);
                            simpleChronometer_temp.setVisibility(View.GONE);
                            simpleChronometer.setVisibility(View.VISIBLE);
                            simpleChronometer.start();
                        } else if (statusId == 4) {
                            deviceStatusImg.setImageResource(R.mipmap.idling_device_d);
                        }
                        Log.d("InfoFrag_22", jsonResponse.getString("rcvdTimeDiffer").toString() + "-statusId-" + statusId);
                        lastRecordTimeLabel.setText(jsonResponse.getString("rcvdTimeDiffer").toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try{

                        //label6.setText(jsonResponse.getString("chassisNo").toString()); driver
                        label7.setText(jsonResponse.getString("temp"));
                        label8.setText(jsonResponse.getString("fuel"));
                        //  label8.setText(jsonResponse.getString("fuel").toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    //   progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // progressBar.setVisibility(View.GONE);
                    Log.d("InfoFrag", "Exception in info frag on sharedprf");
                    //Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_SHORT).show();

                }

            }
/*
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", loginName);
                params.put("psw", password);
                params.put("fleetName", deviceType);
                params.put("moduleId", ""+moduleId );
                params.put("isDeviceDate", "true" );
                return params;
            }

        };
        Volley.newRequestQueue(getActivity()).add(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
*/
        }catch(Exception ee){ee.printStackTrace();}

        Bundle args = getArguments(); // calling form map fragment
        String index = null;
        try{
            index = args.getString("index", DEFAULT);
            if(index != null && index.equals("LAST10RECORDS")){
                speedLabel.setText( args.getString("speed", DEFAULT));
                lastRecordTimeLabel.setText(args.getString("rcvdTimeDiffer", DEFAULT));
                label1.setText(args.getString("refPoint", DEFAULT));
            }

        }catch(Exception e){}

        return v;
    }



    public void saveFavDevice(){


        progressBar.setVisibility(View.VISIBLE);
        String url = APIManager.getSaveFavoriteFleetAPI();

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        JSONObject jsonResponse;


                        try {

                            jsonResponse = new JSONObject(response);
                            Toast.makeText(getActivity(), jsonResponse.getString("message").toString(),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Not Found!" ,Toast.LENGTH_SHORT).show();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Connection Problem", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", ""+userId);
                params.put("moduleId", ""+moduleId);

                return params;
            }

        }; Volley.newRequestQueue(getActivity()).add(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void setTimeSpeed(String speed, String diffTime) {
        speedLabel.setText(speed);
        lastRecordTimeLabel.setText(diffTime);
    }

    public void getSharePrfDataForDevice(){
        SharedPreferences sp = getActivity().getSharedPreferences("SelectedID", Context.MODE_PRIVATE);

        unitID = sp.getInt("unitID", 0);
        userId= sp.getInt("userId", 0);
        regNo = sp.getString("regNo", DEFAULT);
        custName = sp.getString("custName", DEFAULT);
        lngg = sp.getFloat("longitude", 0.0f);
        latt = sp.getFloat("latitude", 0.0f);
        dateTime = sp.getString("dateTime", DEFAULT);
        speed = sp.getFloat("speed", 0.0f);
        deviceType = sp.getString("deviceType", DEFAULT);
        moduleId = sp.getInt("moduleId", 0);
        password = sp.getString("password", DEFAULT);
        loginName = sp.getString("loginName", DEFAULT);
        isNr =  sp.getBoolean("isNr", FALSE);

    }
}