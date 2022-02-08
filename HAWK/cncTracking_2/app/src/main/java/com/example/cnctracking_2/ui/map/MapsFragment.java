package com.example.cnctracking_2.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
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
import com.example.cnctracking_2.data.model.Unit;
import com.example.cnctracking_2.ui.ControlFragment;
import com.example.cnctracking_2.ui.map.ui.InfoFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;

public class MapsFragment extends Fragment  {
    public static final String DEFAULT = "N/A";
    String regNo, message,dateTime, speed;
    int direction, newDirection;
    double latt,lngg;
    boolean isNr;
    String  deviceType, rcvdTimeDiffer;
    int moduleId, userId;
    String  loginName, password, userRole;
    boolean responseOk = false;
    ArrayList<Parameters> movementList;
    LatLng prvLatLng, currentlatLng;
    boolean startAsyncTask = false;
    Handler mHandler = new Handler();
    Handler mHandler2 = new Handler();
    ArrayList<LatLng> markerPoints;
    private List<LatLng> points;
    boolean isCancelled = false;boolean isCancelled2 = false;
    private GoogleMap gMap;
    static Marker carMarker;
    private final static int LOCATION_REQUEST_CODE = 23;
    private final static int INTERVAL = (1000 * 5); //half minutes
    int carMoveCounter = 0;
    SupportMapFragment mapFragment;
    int speedcurrent = 0;
    int statusId = 0;
    long lastPacketMovementMillies=0;
    ProgressBar progressBar;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            LatLng latLng = new LatLng(latt, lngg);
            /*carMarker =  googleMap.addMarker(new MarkerOptions().position(latLng).title(dateTime+" "+speed).snippet(message)
                    .icon(getDirectionIcon()));*/

            //  googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
               googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        SharedPreferences sp = getActivity().getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
        progressBar =  (ProgressBar)  getActivity().findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.GONE);

        userId= sp.getInt("userId", 0);
        regNo = sp.getString("regNo", DEFAULT);
        message = sp.getString("location", DEFAULT);
        lngg = sp.getFloat("longitude", 0.0f);
        latt = sp.getFloat("latitude", 0.0f);
        deviceType = sp.getString("deviceType", DEFAULT);
        moduleId = sp.getInt("moduleId", 0);
        password = sp.getString("password", DEFAULT);
        loginName = sp.getString("loginName", DEFAULT);
        dateTime = sp.getString("dateTime", DEFAULT);
        speed = ""+sp.getFloat("speed", 0.0f);

        movementList = new ArrayList();

        getData();

        SharedPreferences lastPacketTimeSP = getActivity().getSharedPreferences("LastTimeMovement_"+moduleId, Context.MODE_PRIVATE);
        lastPacketMovementMillies =  lastPacketTimeSP.getLong("timeInMillies", 0);
        Log.d("timeInMillies_1", ""+lastPacketMovementMillies);
        if (mapFragment != null) {

             mapFragment.getMapAsync(callback);
            startRepeatingTask();
        }
    }

    public void getData(){
        progressBar.setVisibility(View.VISIBLE);
        sharedPreferencesMethod("");
        String url = APIManager.getLiveTrackingAPI();
        StringRequest sr = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        JSONObject jsonResponse;

                        try {
                            Log.d("MapFrag_",response);
                            jsonResponse = new JSONObject(response);
                            latt = jsonResponse.getDouble("latitude");
                            lngg = jsonResponse.getDouble("longitude");
                            dateTime = jsonResponse.getString("eventTime").toString();
                            speed = jsonResponse.getString("speed").toString() +" KM/h";
                            message = jsonResponse.getString("refPoint").toString();
                            direction = jsonResponse.getInt("direction");
                             statusId = jsonResponse.getInt("statusId");
                            JSONArray jsonArray = jsonResponse.optJSONArray("movement");

                            if( jsonArray == null){
                                jsonArray = new JSONArray();
                            }
                            Parameters prm;
                           // movementList = new ArrayList();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jb = jsonArray.getJSONObject(i);

                                prm = new Parameters();
                                prm.setLongitude(jb.optDouble("longitude"));
                                prm.setLatitude(jb.optDouble("latitude"));
                                prm.setSpeed(jb.optDouble("speed"));
                                prm.setDiffTime(jb.optString("rcvdTimeDiffer"));
                                prm.setDirection(jb.optInt("direction"));
                                prm.setMessage(jb.optString("refPoint").toString());
                                prm.setTimeInMillies(jb.optLong("currentTimeInMillies"));
                                movementList.add(prm);
                               // Collections.reverse(movementList);
                            }
                            if(movementList.size()> carMoveCounter) {
                                latt = movementList.get(carMoveCounter).getLatitude();
                                lngg = movementList.get(carMoveCounter).getLongitude();
                            }

                            sharedPreferencesMethod(response);
                            progressBar.setVisibility(View.GONE);
                            responseOk = true;
                            callInfoFrag();
                            moveCar();
                        } catch (JSONException e) {
                            // progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            callInfoFrag();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                   progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_SHORT).show();
                callInfoFrag();
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
                params.put("carMoveCounter", ""+carMoveCounter );

                return params;
            }

        };
        Volley.newRequestQueue(getActivity()).add(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void sharedPreferencesMethod(String part1){
        SharedPreferences sp = getActivity().getSharedPreferences("InfoDevice", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("searchResult", part1);
        editor.commit();
    }
    public void sharedPreferencesForLastTimeMovement(long timeInMillies){
        SharedPreferences sp = getActivity().getSharedPreferences("LastTimeMovement_"+moduleId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("timeInMillies", timeInMillies);
        Log.d("timeInMillies", ""+timeInMillies);
                editor.commit();
    }
    public void callInfoFrag(){
        Fragment fragment = new InfoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.bottom_frag, fragment);
        fragmentTransaction.commit();
    }
    public void callInfoFragWithoutAnimation(Parameters p){
        Log.d("TSTAct_cancel",""+isCancelled);
        Log.d("TSTAct_cancel2",""+isCancelled2);
        Fragment fragment = new InfoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        Bundle args = new Bundle();
        args.putString("index", "LAST10RECORDS"); //means trip click
        args.putString("speed", p.getSpeed()+" KM/h" );
        args.putString("rcvdTimeDiffer", p.getDiffTime());
        args.putString("refPoint", p.getMessage());

        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.bottom_frag, fragment);
        fragmentTransaction.commit();
    }
    public BitmapDescriptor getDirectionIcon(){
        Log.d("TSTAct_direction_",""+direction);
        if (direction == 0) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_1);
        } else if (direction >= 337 || direction < 22) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_1);
        } else if (direction >= 22 && direction < 67) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_2);
        } else if (direction >= 67 && direction < 112) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_3);
        } else if (direction >= 112 && direction < 157) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_4);
        } else if (direction >= 157 && direction < 202) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_5);
        } else if (direction >= 202 && direction < 247) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_6);
        } else if (direction >= 247 && direction < 292) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_7);
        } else if (direction >= 292 && direction < 337) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.car_8);
        }
        Log.d("direction_1",""+direction);
        return BitmapDescriptorFactory.fromResource(R.mipmap.car_1);
    }




    // work for smooth movement

    public void moveCar(){


        if(movementList == null){
            movementList = new ArrayList<>();
        }
        //else{
            currentlatLng = new LatLng(latt,lngg);
        isCancelled2 = false;
        mHandlerTask2.run();
      //  carMarker =  gMap.addMarker(new MarkerOptions().position(currentlatLng).title(dateTime+" "+speed).snippet(message)
          //      .icon(getDirectionIcon()));
//
        //  googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
      //  gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlatLng, 16));
          //  changePositionSmoothly(carMarker, currentlatLng, 90);
       // }
       /* for(Parameters p:movementList){
            gMap.clear();
            currentlatLng = new LatLng(p.getLatitude(),p.getLongitude());
            prvLatLng = currentlatLng;
            Log.d("TSTAct_moveCar",""+p.getLatitude() +" "+ p.getLongitude());
            direction = p.getDirection();
            changePositionSmoothly(carMarker, currentlatLng, 90);

        }*/

    }
    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {

            if(startAsyncTask){
                getData();
                Log.d("TSTAct_", "#mHandlerTask start async "+regNo);
            }
            startAsyncTask = true;
            if(!isCancelled){
                Log.d("TSTAct_", "#mHandlerTaskpostDelayed 120000");
                int timeFetch = 30000;
                if(statusId == 3  && carMoveCounter >5){
                    timeFetch = 15000;
                }

               // Toast.makeText(getActivity(), "Fetch New Data", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(mHandlerTask, timeFetch);
            }

        }
    };

    Runnable mHandlerTask2 = new Runnable()
    {
        @Override
        public void run() {
            Log.d("TSTAct_", "#mHandlerTask2222 start async "+regNo);
            Log.d("TSTAct_cancel",""+isCancelled);
            Log.d("TSTAct_cancel2",""+isCancelled2);
            Log.d("startAsyncTask",""+startAsyncTask);
            if(startAsyncTask){
                boolean runLoop = false;
                Log.d("TSTAct_", "In #mHandlerTask2222 start async "+regNo);
                Log.d("TSTAct_", carMoveCounter+"In #mHandlerTask2222 "+movementList.size());

                for(Parameters p1: movementList){
                    if(p1.getTimeInMillies() >= lastPacketMovementMillies || (carMoveCounter+2 > movementList.size()) ) {
                        break;
                    }else{
                        carMoveCounter++;
                    }
                }
                if(carMoveCounter < movementList.size()) {
                    Parameters p = movementList.get(carMoveCounter);
                        currentlatLng = new LatLng(p.getLatitude(), p.getLongitude());
                        newDirection = p.getDirection();
                        callInfoFragWithoutAnimation(p );
                        sharedPreferencesForLastTimeMovement(p.getTimeInMillies());
                        changePositionSmoothly(carMarker, currentlatLng, 90);
                        // carMarker =  gMap.addMarker(new MarkerOptions().position(currentlatLng).title(dateTime+" "+speed).snippet(message)
                        //    .icon(getDirectionIcon()));
                        //Log.d("TSTAct_mHandlerTask222", "" + p.getLatitude() + " " + p.getLongitude());

                }
                    if(carMoveCounter < movementList.size()) {
                        carMoveCounter++;
                    }else{
                        stopRepeatingTaskForArray();
                    }

            }
            startAsyncTask = true;
            if(!isCancelled2){
             //   Toast.makeText(getActivity(), "Running "+carMoveCounter, Toast.LENGTH_SHORT).show();
                Log.d("TSTAct_2222", "#mHandlerTaskpostDelayed ");
                mHandler2.postDelayed(mHandlerTask2, 2300);
            }

        }
    };


    void changePositionSmoothly(final Marker myMarker, final LatLng newLatLng, final int bearing) {
        Log.d("TSTAct_", "#changePositionSmoothly In");
        if(prvLatLng == null){
            prvLatLng =newLatLng;
            direction = newDirection;

        }
        final LatLng startPosition = new LatLng(prvLatLng.latitude, prvLatLng.longitude);
        final LatLng finalPosition = newLatLng;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 2000;
        boolean hideMarker = false;
        if(gMap == null){
            return;
        }
     //  double bearingD = angleFromCoordinate(prvLatLng.latitude, prvLatLng.longitude, newLatLng.latitude, newLatLng.longitude);
      //  final Marker myMarker2 = googleMap.addMarker(new MarkerOptions().position(prvLatLng).title("Location: "+msg )
        //        .snippet("DateTime: "+dateTime + " Reg#: "+regNo+" Speed: "+speed).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        gMap.clear();
        gMap.animateCamera(CameraUpdateFactory.newLatLng(prvLatLng));
        // cnc map
        final Marker myMarker2 =  gMap.addMarker(new MarkerOptions().position(prvLatLng).title(dateTime+" "+speed).snippet(message)
                .icon(getDirectionIcon()));

        prvLatLng = newLatLng;
        direction = newDirection;
        if(carMoveCounter == 0){
            return;
        }
        Log.d("TSTAct_", "#changePositionSmoothly after marker apply");
       // gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlatLng, 16));
        // cnc map end
        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
              //  myMarker2.setRotation((float) bearingD);
                // Calculate progress using interpolator
                Log.d("TSTAct_", "#changePositionSmoothly run method");
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + finalPosition.latitude * t,
                        startPosition.longitude * (1 - t) + finalPosition.longitude * t);

                myMarker2.setPosition(currentPosition);

                Log.d("TSTAct_", "#changePositionSmoothly run method"+t +" latitude"+startPosition.latitude+" longitude"+startPosition.longitude);
                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 50);

                } else {
                    if (hideMarker) {
                        myMarker2.setVisible(false);
                    } else {
                        myMarker2.setVisible(true);
                    }
                }

            }
        });
    }
    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        isCancelled = true;
        Log.d("TSTAct_cancel111",""+isCancelled);
        Log.d("TSTAct_cancel2333",""+isCancelled2);
        mHandler.removeCallbacks(mHandlerTask);
        stopRepeatingTaskForArray();
    }

    void stopRepeatingTaskForArray()
    {
        Log.d("TSTAct_cancel333",""+isCancelled);
        Log.d("TSTAct_cancel24444",""+isCancelled2);
        isCancelled2 = true;
        mHandler2.removeCallbacks(mHandlerTask2);
    }
    @Override
    public  void onPause() {
        super.onPause();
        stopRepeatingTask();
        Log.d("TSTAct_frag", "#onPause "+regNo);
    }


    public  void onStop() {
        super.onStop();
        stopRepeatingTask();
        Log.d("TSTAct_frag", "#onStop frag"+regNo);
    }

    @Override
    public  void onResume() {
        super.onResume();
     //   startRepeatingTask();
        Log.d("TSTAct_frag", "#onResume "+regNo);
    }

    @Override
    public  void onStart() {
        super.onStart();
        Log.d("TSTAct", "#onStart "+regNo);
    }

    private double angleFromCoordinate(double lat1, double long1, double lat2,
                                       double long2) {

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 360 - brng; // count degrees counter-clockwise - remove to make clockwise

        return brng;
    }
}