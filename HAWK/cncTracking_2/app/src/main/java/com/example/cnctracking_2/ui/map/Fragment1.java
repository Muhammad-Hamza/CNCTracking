package com.example.cnctracking_2.ui.map;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cnctracking_2.R;
import com.example.cnctracking_2.config.APIManager;
import com.example.cnctracking_2.data.model.Parameters;
import com.example.cnctracking_2.data.model.TripsBean;
import com.example.cnctracking_2.ui.ControlFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Saylani_IT on 18/10/2015.
 */
public class Fragment1 extends Fragment {
    ProgressBar progressBar;
    Button btn1;
    View view;
    String password, loginName;
    int unitID;
    ArrayList<TripsBean> unitList = new ArrayList<TripsBean>();
    private static final String KEY_INDEX = "index";
    public static final String DEFAULT = "N/A";

    ListView list;
    TripArrayAdapter adapter;
    TextView changeDate, runningTotal, stopTotal, alertTotal, runningTotalHr, stopTotalHr;

    boolean tempAllow = false;
    boolean fuelAllow = false;
    boolean SpeedAnalysisAllow = false;
    DatePickerDialog.OnDateSetListener sDateSetListener;
    String changeDateStr;

    String regNo, custName,dateTime;
    float latt,lngg, speed;
    String  deviceType;
    int moduleId, userId;
    boolean firstTimeLoad = true;
    Map<String, Integer> eventCounts = new HashMap<>();
    int totalEventCounts = 0;
    LinearLayout bellButton;
    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;
    int counter = 0; int prevPosition = 0; int currentPosition = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment1, container, false);
        changeDate = (TextView) view.findViewById(R.id.change_date);
        runningTotalHr = (TextView) view.findViewById(R.id.runningduration_total);
        runningTotal = (TextView) view.findViewById(R.id.distance_total);
        stopTotal = (TextView) view.findViewById(R.id.stop_total);
        stopTotalHr = (TextView) view.findViewById(R.id.stopduration_total);
        alertTotal = (TextView) view.findViewById(R.id.alerts_total);
        list = (ListView) view.findViewById(R.id.listView);
        bellButton = (LinearLayout) view.findViewById(R.id.bell_history);

        layoutBottomSheet = (LinearLayout)getActivity().findViewById(R.id.temp_bottomsheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        /* sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);*/
        //sheetBehavior.setPeekHeight(600);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        changeDateStr = (month+1)+"-"+day+"-"+year;
        changeDate.setText((month+1)+"-"+day+"-"+year);
        progressBar = (ProgressBar) view.findViewById(R.id.loading);
        progressBar.setVisibility(View.GONE);

       /* Parameters prm = new Parameters();
        prm.setTimeTotal(7);
        prm.setStartTime("00:00");
        prm.setEndTime("07:32");
        prm.setMileage(0.0);
        unitList.add(prm);*/

        SharedPreferences sp = getActivity().getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
        userId= sp.getInt("userId", 0);
        regNo = sp.getString("regNo", DEFAULT);
        deviceType = sp.getString("deviceType", DEFAULT);
        moduleId = sp.getInt("moduleId", 0);
        password = sp.getString("password", DEFAULT);
        loginName = sp.getString("loginName", DEFAULT);

        adapter = new TripArrayAdapter(getActivity(), unitList);

        //    list.setAdapter(adapter);
        bellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingsAlert();
            }
        });
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        sDateSetListener, year, month, day);
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.show();
            }
        });
        sDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                changeDateStr = (month+1)+"-"+day+"-"+year;
                changeDate.setText((month+1)+"-"+day+"-"+year);
                // sheetBehavior.setPeekHeight(600);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(firstTimeLoad){
                    firstTimeLoad = false;
                }else{
                    getHistory();
                }

            }
        };
        getHistoryFromSharedPref();
        firstTimeLoad = false;
        return view;
    }




    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArrayList(KEY_INDEX, unitList);

    }

    public void showArrayList(){
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                try {
                    if (counter == 0) {
                        prevPosition = position;
                    } else if (counter == 1) {
                        currentPosition = position;
                    } else {
                        prevPosition = currentPosition;
                        currentPosition = position;
                    }
                    list.getChildAt(prevPosition).setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundColor(Color.LTGRAY);
                    counter++;
                }catch(Exception e){}
                // sheetBehavior.setPeekHeight(600);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                view.animate().setDuration(500).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // list.remove(item);
                                //adapter.notifyDataSetChanged();
                                view.setAlpha(1);

                                getLocationsForMapFromTripData( unitList.get(position).getTripStartTimeMS(), unitList.get(position).getTripEndTimeMS());

                            }
                        });

            }
        });
    }

    public void getHistoryForAlerts(){
        SharedPreferences sp = getActivity().getSharedPreferences("Report", Context.MODE_PRIVATE);
        String report = sp.getString("searchResult", DEFAULT);
        totalEventCounts = 0;
        eventCounts.clear();
        //   Log.d("Frag_", "getHistoryForAlerts "+report);
        boolean emergAlert = false;
        JSONObject jsonResponse;
        try {
            if(report == null){report = "";}
            // jsonResponse = new JSONObject(report);
            JSONArray jsonArray = new JSONArray(report);
            if( jsonArray == null){
                jsonArray = new JSONArray(report);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jb = jsonArray.getJSONObject(i);
                emergAlert = jb.optBoolean("emergency");
                //    Log.d("Frag_", jb.optBoolean("emergency")+"-- "+emergAlert);
                if(emergAlert) {
                    if(eventCounts.get(jb.optString("event").toString()) == null) {
                        eventCounts.put(jb.optString("event").toString(), 1);
                    }else{
                        eventCounts.put(jb.optString("event").toString(), eventCounts.get(jb.optString("event").toString())+1);
                    }
                    totalEventCounts++;

                }
            }
            alertTotal.setText(totalEventCounts+" Alerts");
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Not Found!", Toast.LENGTH_SHORT).show();
            //progressBar.setVisibility(View.GONE);
            //     Log.d ("check json", "Json Problem");
            //e.printStackTrace();
        }
    }
    public void getHistoryFromSharedPref(){
        //   Log.d("Frag1","getHistory");
        getHistoryForAlerts();
        SharedPreferences sp = getActivity().getSharedPreferences("Report", Context.MODE_PRIVATE);
        String report = sp.getString("tripResults", DEFAULT);
        String totalRunning = sp.getString("totalRunning", DEFAULT);
        String totalStop = sp.getString("totalStop", DEFAULT);
        //      Log.d("Frag1_trip",report);
        //    Log.d("Frag1_trip_run",totalRunning);
        if( report == null){
            report = "";
        }

        //        Log.d("Frag1", "IGNITION OFF");
        JSONObject jsonResponse;
        unitList.clear();
        try {
            //jsonResponse = new JSONObject(report);
            JSONArray jsonArray = new JSONArray(report);// jsonResponse.optJSONArray("tripReport");
            if( jsonArray == null){
                jsonArray = new JSONArray(report);
            }

            progressBar.setVisibility(View.GONE);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jb = jsonArray.getJSONObject(i);

                TripsBean teb = new TripsBean();
                teb.setSerialNo(jb.optInt("tripConter"));
                teb.setTypeObject(jb.optString("obj_type").toString());
                teb.setTripStartTimeMS(jb.optString("eventStartTimeMS").toString());
                teb.setTripEndTimeMS(jb.optString("eventEndTimeMS").toString());

                if(teb.getTypeObject().equals("RUNNING")) {
                    teb.setDistance(jb.optString("running_mileage").toString());
                    teb.setDuration(jb.optString("running_duration").toString());
                    teb.setIgntionStartTime(jb.optString("running_eventTime").toString());
                    teb.setSpeed(jb.optString("high_speed").toString());
                }else {
                    teb.setIgntiOFFStartTime(jb.optString("ignOff_eventStartTime").toString());
                    teb.setIgntiOffEndTime(jb.optString("ignOff_eventEndTime").toString());
                    teb.setIgntionOffDuration(jb.optString("ignOff_duration").toString());
                }
                unitList.add(teb);
            }

            if(totalRunning != null && totalStop != null) {
                if(totalRunning.contains("@")) {
                    runningTotal.setText(totalRunning.split("@")[0] + " Km");
                    runningTotalHr.setText(totalRunning.split("@")[1]);
                }
                if(totalStop.contains("@")) {
                    stopTotal.setText(totalStop.split("@")[0] + " Stop");
                    stopTotalHr.setText(totalStop.split("@")[1]);
                }
            }

            showArrayList();
        } catch (JSONException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Not Found!", Toast.LENGTH_SHORT).show();
            Log.d("check json", "Json Problem");
            //e.printStackTrace();
        }

    }
    public void getLocationsForMapFromTripData(String fromDateTimeMS, String toDateTimeMS){
        //  Log.d("getHistory", "1");
        try {
            progressBar.setVisibility(View.VISIBLE);
            String url = APIManager.getLatLongTripClickAPI();
            //    Log.d("Frag1_1", url +" - "+ loginName +" - "+ password);
            StringRequest sr = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonResponse;
                            // Log.d("Frag1_1", "2");
                            try {
                                jsonResponse = new JSONObject(response);
                                //    Log.d("Frag1_1", response);
                                JSONArray jsonArray = jsonResponse.optJSONArray("searchResult");

                                progressBar.setVisibility(View.GONE);
                                sharedPreferencesMethod(jsonArray.toString());

                                //showArrayList();

                            }catch(Exception e){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
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
                    params.put("fromDateTimeMS", fromDateTimeMS );
                    params.put("toDateTimeMS", toDateTimeMS );
                    return params;
                }

            };
            Volley.newRequestQueue(getActivity()).add(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(
                    12000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //    Log.d("Frag1_1_getHistory","fromDateTimeMS="+ fromDateTimeMS+"&"+ "toDate="+ toDateTimeMS +"&moduleId="+ moduleId+"&fleetName="+ deviceType);

        } catch (Exception e) {
            // progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
        }

    }
    public void sharedPreferencesMethod(String part1){

        SharedPreferences sp = getActivity().getSharedPreferences("TripClickLatLong", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("searchResult", part1);
        editor.commit();

        changeToHistoryForTrip();

    }
    public void sharedPreferencesMethod(String part1, String part2,String totalRunning,String totalStop){
        SharedPreferences sp = getActivity().getSharedPreferences("Report", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("searchResult", part1);
        editor.putString("tripResults", part2);
        editor.putString("totalRunning", totalRunning);
        editor.putString("totalStop", totalStop);
        editor.commit();

        changeToHistory();

    }
    public void changeToHistoryForTrip(){

        Fragment fragment = new MapsFragment_history();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("index", "TripClick"); //means trip click
        fragment.setArguments(args);
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();

    }
    public void changeToHistory(){
        Fragment fragment = new MapsFragment_history();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();


    }
    public void getHistory(){
        //     Log.d("Frag1_DT_getHistory", "1");
        try {
            progressBar.setVisibility(View.VISIBLE);
            String url = APIManager.getTrackLogsAPI();
            //     Log.d("Frag1_DT_getHistory", url +" - "+ loginName +" - "+ password);
            StringRequest sr = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonResponse;
                            // Log.d("Frag1_DT_getHistory", "2");
                            try {
                                jsonResponse = new JSONObject(response);
                                //      Log.d("Frag1_DT_getHistory", response);
                                JSONArray jsonArray = jsonResponse.optJSONArray("searchResult");
                                JSONArray jsonArray2 = jsonResponse.optJSONArray("tripResults");
                                //       Log.d("Frag1_DT_getHistory_tr", jsonArray2.toString());

                                String totalRunning = jsonResponse.optString("totalTripsMileage").toString();
                                totalRunning = totalRunning + "@" + jsonResponse.optString("totalTripsDuration").toString();

                                String totalStop = jsonResponse.optString("totalStops").toString();
                                totalStop = totalStop + "@" + jsonResponse.optString("totalStopDuration").toString();
                                //Log.d("Frag1_DT_totalRunning", totalRunning +"-"+totalStop);

                                progressBar.setVisibility(View.GONE);
                                sharedPreferencesMethod(jsonArray.toString(), jsonArray2.toString(),totalRunning,totalStop  );
                                getHistoryFromSharedPref();
                                //showArrayList();

                            }catch(Exception e){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), " History is empty", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
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
                    params.put("fromDate", changeDateStr+" 12:00:00 AM" );
                    params.put("toDate", changeDateStr+" 11:59:59 PM"  );

                    //    Log.d("Frag1_DT_getHistory","fromDate="+ changeDateStr+" 12:00:00 AM" +"&"+ "toDate="+ changeDateStr+" 11:59:59 PM"  +"&moduleId="+ moduleId+"&fleetName="+ deviceType);
                    return params;
                }

            };
            Volley.newRequestQueue(getActivity()).add(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(
                    12000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //    Log.d("getHisotry", "end");
        } catch (Exception e) {
            // progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void showSettingsAlert(){
        try{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

            // Setting Dialog Title
            alertDialog.setTitle("Emergency Alerts");

            Set<String> keys = eventCounts.keySet();
            StringBuffer data = new StringBuffer("");

            for(String key: keys){
                data.append(key +" : ");
                data.append( eventCounts.get(key) +"\n");
            }

            // Setting Dialog Message
            alertDialog.setMessage(data.toString());
//+" "+ Arrays.asList(eventCounts)
            // On pressing Settings button
       /* alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
            }
        });*/

            // on pressing cancel button
            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Dialog box is not opening right now, pls try again later.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
