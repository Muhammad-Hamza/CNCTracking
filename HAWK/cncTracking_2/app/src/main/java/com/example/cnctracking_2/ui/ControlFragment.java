package com.example.cnctracking_2.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.example.cnctracking_2.ui.login.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner spinner1;
    int speed, timerFat,timerIdling, geoFenceData, nightGuardAllow;
    String startTimeNight, endTimeNight;
    boolean isSPeedStatusOn, isGeoFenceOn, isFatigueTimerOn, isIdlingTimerOn,isNightGuardOn  = false;
    ImageView speedSwitch, geoFence, fatigueTime, idlingTime, nightGuard;
    Spinner spinner_speed,  spinner_fatigue, spinner_idling, spinner_night_start,spinner_night_end;
    ArrayAdapter<Integer> speedAdapter,  idlingAdapter , fatigueAdapter= null;
    ArrayAdapter<String> nightAdapter1, nightAdapter2;
    ProgressBar progressBar;
    Button loginButton;
    String regNo, loginName, psw, message;
    int moduleId, userId;
    TextView regNumber, cancel;
    List<Integer> speedArray = new ArrayList();
    List<Integer> timerArray = new ArrayList();
    List<String> nightArray = new ArrayList();

    public static final String DEFAULT = "N/A";
    public ControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlFragment newInstance(String param1, String param2) {
        ControlFragment fragment = new ControlFragment();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_control, container, false);
         getActivity().setTitle("Control");


        loginButton =(Button) v.findViewById(R.id.saveConf);
        progressBar =(ProgressBar) v.findViewById(R.id.loading);

        spinner_speed = (Spinner) v.findViewById(R.id.speed_spinner);
        spinner_fatigue = (Spinner) v.findViewById(R.id.fatigue_spinner);
        spinner_idling = (Spinner) v.findViewById(R.id.idling_spinner);
        spinner_night_start = (Spinner) v.findViewById(R.id.night_spinner_start);
        spinner_night_end = (Spinner) v.findViewById(R.id.night_spinner_end);

         speedSwitch = (ImageView) v.findViewById(R.id.speed_switch);
         geoFence = (ImageView) v.findViewById(R.id.geo_switch);
        fatigueTime = (ImageView) v.findViewById(R.id.fatigue_switch);
        idlingTime = (ImageView) v.findViewById(R.id.idling_switch);
        nightGuard = (ImageView) v.findViewById(R.id.night_switch);

        regNumber = (TextView) v.findViewById(R.id.regno);
        cancel = (TextView) v.findViewById(R.id.cancel_conf);

        spinner_speed.setOnItemSelectedListener(this);
        spinner_fatigue.setOnItemSelectedListener(this);
        spinner_idling.setOnItemSelectedListener(this);
        spinner_night_start.setOnItemSelectedListener(this);
        spinner_night_end.setOnItemSelectedListener(this);

        speed= timerFat= timerIdling= geoFenceData=  nightGuardAllow = 0;
        startTimeNight = endTimeNight = "";

        SharedPreferences sp = getActivity().getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
        moduleId = sp.getInt("moduleId", 0);
        userId= sp.getInt("userId", 0);
        regNo = sp.getString("regNo", DEFAULT);
        psw = sp.getString("password", DEFAULT);
        loginName = sp.getString("loginName", DEFAULT);

        regNumber.setText("Reg# : "+regNo);

        speed = 0;
        isSPeedStatusOn = false;
        speedArray.add(0);
        for(int i=70; i<=200;){
            speedArray.add(i);
            i +=10;
        }


        timerArray.add(0);
        for(int i=5; i<=120;){
            timerArray.add(i);
            i +=5;
        }

        nightArray.add("");
        for(int i=0; i<=6;){
            nightArray.add("0"+i+":00");
            nightArray.add("0"+i+":30");
            i +=1;
        }
        getDevicesData();
        speedAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, speedArray);
        speedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fatigueAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, timerArray);
        fatigueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        idlingAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, timerArray);
        idlingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nightAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nightArray);
        nightAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nightAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nightArray);
        nightAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_night_start.setAdapter(nightAdapter1);
        spinner_night_end.setAdapter(nightAdapter2);
        spinner_idling.setAdapter(idlingAdapter);
        spinner_fatigue.setAdapter(fatigueAdapter);
        spinner_speed.setAdapter(speedAdapter);

       // setDisableAll();

        speedSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speedBtnOnOff();
            }
        });
        geoFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geoFenceBtnOnOff();
            }
        });
        fatigueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatigueTimeBtnOnOff();
            }
        });
        idlingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idlingTimeBtnOnOff();
            }
        });
        nightGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nightGuardBtnOnOff();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().setDuration(300).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                saveDevicesData();
                                v.setAlpha(1);
                            }
                        });
            }
        });
        progressBar.setVisibility(View.GONE);

        return v;
    }

    public void speedBtnOnOff(){
        if (isSPeedStatusOn){
            speedSwitch.setImageResource(R.mipmap.off);
            speed = 0;
            isSPeedStatusOn = false;
            spinner_speed.setEnabled(false);
            spinner_speed.setClickable(false);
        } else  {
            speedSwitch.setImageResource(R.mipmap.onn);
            isSPeedStatusOn = true;
            spinner_speed.setEnabled(true);
            spinner_speed.setClickable(true);
        }
    }
    public void geoFenceBtnOnOff(){
        if (isGeoFenceOn){
            geoFence.setImageResource(R.mipmap.off);
            isGeoFenceOn = false;
            geoFenceData = 0;
        } else  {
            geoFence.setImageResource(R.mipmap.onn);
            isGeoFenceOn = true;
            geoFenceData = 1;
        }
    }
    public void fatigueTimeBtnOnOff(){
        if (isFatigueTimerOn){
            fatigueTime.setImageResource(R.mipmap.off);
            isFatigueTimerOn = false;
            timerFat = 0;
            spinner_fatigue.setEnabled(false);
            spinner_fatigue.setClickable(false);
        } else  {
            fatigueTime.setImageResource(R.mipmap.onn);
            isFatigueTimerOn = true;
            spinner_fatigue.setEnabled(true);
            spinner_fatigue.setClickable(true);
        }
    }
    public void idlingTimeBtnOnOff(){
        if (isIdlingTimerOn){
            idlingTime.setImageResource(R.mipmap.off);
            isIdlingTimerOn = false;
            timerIdling = 0;
            spinner_idling.setEnabled(false);
            spinner_idling.setClickable(false);
        } else  {
            idlingTime.setImageResource(R.mipmap.onn);
            isIdlingTimerOn = true;
            spinner_idling.setEnabled(true);
            spinner_idling.setClickable(true);
        }
    }
    public void nightGuardBtnOnOff(){
        if (isNightGuardOn){
            nightGuardAllow = 0;
            nightGuard.setImageResource(R.mipmap.off);
            isNightGuardOn = false;
            spinner_night_start.setEnabled(false);
            spinner_night_start.setClickable(false);

            spinner_night_end.setEnabled(false);
            spinner_night_end.setClickable(false);
        } else  {
            nightGuard.setImageResource(R.mipmap.onn);
            nightGuardAllow = 1;
            isNightGuardOn = true;
            spinner_night_start.setEnabled(true);
            spinner_night_start.setClickable(true);

            spinner_night_end.setEnabled(true);
            spinner_night_end.setClickable(true);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        //Log.d("onItemSelected",parent.getId()+" id: " + item);
        int value = 0;
        try{
            value = Integer.parseInt(item);
        }catch (Exception e){}
       // Toast.makeText(parent.getContext(),  parent.getId()+" id: " + item, Toast.LENGTH_LONG).show();
        //Log.d("onItemSelected2",parent.getSelectedItemId()+"Selected: " + item);
        switch(parent.getId()){
            case R.id.speed_spinner :
                 speed = value;
               // Toast.makeText(parent.getContext(),  parent.getSelectedItemId()+"Selected: " + item, Toast.LENGTH_LONG).show();
                break;
            case R.id.fatigue_spinner :
                timerFat =value;
                break;
            case R.id.idling_spinner :
                timerIdling =value;
                break;
            case R.id.night_spinner_start :
                startTimeNight = item;
                break;
            case R.id.night_spinner_end :
                endTimeNight = item;
                break;
        }
        // Showing selected spinner item
      //  Toast.makeText(parent.getContext(),  parent.getSelectedItemId()+"Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void setValues(){
        if(speed >0) {
            isSPeedStatusOn = true;
        }
        if(isSPeedStatusOn) {
            int spinnerPosition =  speedArray.indexOf(speed);
            //Log.d("getDevicesData_", ""+spinnerPosition);
            spinner_speed.setSelection(spinnerPosition);
            speedSwitch.setImageResource(R.mipmap.onn);
            spinner_speed.setEnabled(true);
            spinner_speed.setClickable(true);
        }
        if(timerFat >0) {
            isFatigueTimerOn = true;
        }
        if(isFatigueTimerOn) {
            int spinnerPosition =  timerArray.indexOf(timerFat);
            spinner_fatigue.setSelection(spinnerPosition);
            fatigueTime.setImageResource(R.mipmap.onn);
            spinner_fatigue.setEnabled(true);
            spinner_fatigue.setClickable(true);
        }
        if(timerIdling >0) {
            isIdlingTimerOn = true;
        }
        if(isIdlingTimerOn) {
            int spinnerPosition =  timerArray.indexOf(timerIdling);
            spinner_idling.setSelection(spinnerPosition);
            idlingTime.setImageResource(R.mipmap.onn);
            spinner_idling.setEnabled(true);
            spinner_idling.setClickable(true);
        }
        if(nightGuardAllow >0) {
            isNightGuardOn = true;
        }
        if(isNightGuardOn) {
            int spinnerPosition =  nightArray.indexOf(startTimeNight);
            spinner_night_start.setSelection(spinnerPosition);
            spinnerPosition =  nightArray.indexOf(endTimeNight);
            spinner_night_end.setSelection(spinnerPosition);
           // Log.d("getDevicesData_2", ""+spinnerPosition);
            nightGuard.setImageResource(R.mipmap.onn);
            spinner_night_start.setEnabled(true);
            spinner_night_start.setClickable(true);
            spinner_night_end.setEnabled(true);
            spinner_night_end.setClickable(true);
        }
        if(geoFenceData >0) {
            isGeoFenceOn = true;
            geoFence.setImageResource(R.mipmap.onn);
        }
    }

    public void setDisableAll(){
        spinner_night_start.setEnabled(false);
        spinner_night_start.setClickable(false);

        spinner_night_end.setEnabled(false);
        spinner_night_end.setClickable(false);

        spinner_idling.setEnabled(false);
        spinner_idling.setClickable(false);

        spinner_fatigue.setEnabled(false);
        spinner_fatigue.setClickable(false);

        spinner_speed.setEnabled(false);
        spinner_speed.setClickable(false);

    }
    public void getDevicesData(){

        setDisableAll();
        progressBar.setVisibility(View.VISIBLE);
        String url = APIManager.getFleetControlAPI();

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        JSONObject jsonResponse;


                        try {

                         //   Log.d("getDevicesData", response);
                            jsonResponse = new JSONObject(response);
                            speed=  jsonResponse.optInt("speedLimit");
                           geoFenceData=  jsonResponse.optInt("geoFence");
                            timerFat=  jsonResponse.optInt("fatigueTimer");
                            timerIdling  = jsonResponse.optInt("idlingTimer");
                            startTimeNight = jsonResponse.optString("nightGuardStart");
                            endTimeNight= jsonResponse.optString("nightGuardEnd");
                            nightGuardAllow = jsonResponse.optInt("nightGuardAllow");
                            setValues();
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
                params.put("psw", psw);
                params.put("name", loginName);
                params.put("moduleId", ""+moduleId);
                return params;
            }

        }; Volley.newRequestQueue(getActivity()).add(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void saveDevicesData(){


        progressBar.setVisibility(View.VISIBLE);
        String url = APIManager.getSaveFleetControlAPI();
      //  Log.d("test1", "t3");
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        JSONObject jsonResponse;
                        try {
                            jsonResponse = new JSONObject(response);
                            message =  jsonResponse.getString("message").toString();
                            Toast.makeText(getActivity(), message ,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("psw", psw);
                params.put("name", loginName);
                params.put("userId", ""+userId);
                params.put("moduleId", ""+moduleId);
                params.put("speedLimit", ""+speed);
                params.put("geoFence", ""+geoFenceData);
                params.put("fatigueTimer", ""+timerFat);
                params.put("idlingTimer", ""+timerIdling);
                params.put("nightGuardAllow", ""+nightGuardAllow);
                params.put("nightGuardStart", startTimeNight);
                params.put("nightGuardEnd", endTimeNight);
                return params;
            }

        }; Volley.newRequestQueue(getActivity()).add(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}