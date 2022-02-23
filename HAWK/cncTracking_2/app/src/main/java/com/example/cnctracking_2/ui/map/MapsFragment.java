package com.example.cnctracking_2.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
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
import com.example.cnctracking_2.data.model.LiveLocationMap;
import com.example.cnctracking_2.data.model.Parameters;
import com.example.cnctracking_2.data.model.User;
import com.example.cnctracking_2.data.model.VehilceSelection;
import com.example.cnctracking_2.data.service.DataFromSharedPreferences;
import com.example.cnctracking_2.data.service.LiveLocationService;
import com.example.cnctracking_2.data.service.VolleyCallback;
import com.example.cnctracking_2.ui.map.ui.InfoFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment  {
    public static final String DEFAULT = "N/A";

    User user;
    VehilceSelection vehilce;
    LiveLocationService service;
    DataFromSharedPreferences dataSP;
    LiveLocationMap liveLocationMap;
    ArrayList<Parameters> movementList;
    SupportMapFragment mapFragment;
    ProgressBar progressBar;
    private Activity mActivity;
    Handler mHandler = new Handler();
    Handler mHandler2 = new Handler();

    // Map
    LatLng prvLatLng, currentlatLng;
    private GoogleMap gMap;
    static Marker carMarker;

    boolean responseOk = false;
    boolean startAsyncTask = false;
    boolean isCancelled = false;boolean isCancelled2 = false;
    int direction, newDirection;
    int carMoveCounter = 0, prvSpeed= 0;
    long lastPacketMovementMillies=0;
    double bearingD;
    Marker myMarker2;
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
            LatLng latLng = new LatLng(vehilce.getLatitude(), vehilce.getLongitude());
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

        progressBar =  (ProgressBar)  getActivity().findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

        dataSP = new DataFromSharedPreferences(getActivity());
        user = dataSP.getUserDataSP();
        vehilce = dataSP.getVehicleDataSP();

        movementList = new ArrayList();
        carMoveCounter = 0;
        direction = 0;
        liveLocationMap = new LiveLocationMap();
        getData(); // from APIs
        getLastTimeSaved(); // for future movements

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            startRepeatingTask();
        }
    }
    public void getData(){
        getLastTimeSaved();
        progressBar.setVisibility(View.VISIBLE);

        service = new LiveLocationService(getActivity());
        service.getData(user, vehilce,carMoveCounter, lastPacketMovementMillies, new VolleyCallback(){
            @Override
            public void onSuccess(LiveLocationMap result){
                liveLocationMap = result;
                Log.d("MapFrag_", "getData "+liveLocationMap.getMessage());
                Log.d("MapFrag_", "getData "+liveLocationMap.getParametersList());

                if(liveLocationMap != null && liveLocationMap.getParametersList() !=null ) {
                    movementList.addAll(liveLocationMap.getParametersList());
                    // latt = liveLocationMap.getParametersList().get(carMoveCounter).getLatitude();
                    // lngg = liveLocationMap.getParametersList().get(carMoveCounter).getLongitude();
                }

                progressBar.setVisibility(View.GONE);
                responseOk = true;
                callInfoFrag();
                moveCar();

            }
        });

    }

    // work for smooth movement
    // calling after fetching data from APIs
    public void moveCar(){
        if(movementList == null){
            movementList = new ArrayList<>();
        }
        //currentlatLng = new LatLng(latt,lngg);
        isCancelled2 = false;
        mHandlerTask2.run();
    }
    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {

            if(startAsyncTask){
                if(getActivity() == null){
                    Log.d("TSTAct_", "getActivity null "+vehilce.getRegNo());
                    mActivity.finish();
                    return;
                }
                getData();
                Log.d("TSTAct_", "#mHandlerTask start async "+vehilce.getRegNo());
            }
            startAsyncTask = true;
            if(!isCancelled){
                int timeFetch = 30000;
                if(liveLocationMap.getStatusId() == 3  || vehilce.getSpeed() > 0 ){
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

            if(startAsyncTask){
                boolean runLoop = false;
                Log.d("TSTAct_", "In #mHandlerTask2222 start async "+vehilce.getRegNo());
                Log.d("TSTAct_", carMoveCounter+"In #mHandlerTask2222 "+movementList.size());

                if(carMoveCounter < movementList.size()) {
                    Parameters p = movementList.get(carMoveCounter);
                    currentlatLng = new LatLng(p.getLatitude(), p.getLongitude());
                    direction = p.getDirection();
                    callInfoFragWithoutAnimation(p );
                    sharedPreferencesForLastTimeMovement(p.getTimeInMillies());
                    changePositionSmoothly(carMarker, currentlatLng, p.getSpeedInt());

                }
                if(carMoveCounter < movementList.size()) {
                    carMoveCounter++;
                }else{
                    stopRepeatingTaskForArray();
                }

            }
            startAsyncTask = true;
            if(!isCancelled2){
                Log.d("TSTAct_2222", "#mHandlerTaskpostDelayed ");
                mHandler2.postDelayed(mHandlerTask2, 2300);
            }

        }
    };


    void changePositionSmoothly(final Marker myMarker, final LatLng newLatLng, final int speedInt) {
        Log.d("TSTAct_", "#changePositionSmoothly In");
        if(prvLatLng == null){
            prvSpeed = speedInt;
            prvLatLng =newLatLng;
            //direction = newDirection;
            myMarker2 =  gMap.addMarker(new MarkerOptions().position(newLatLng).title(liveLocationMap.getDateTime()+" "+liveLocationMap.getSpeedcurrent()+" KM/h").snippet(liveLocationMap.getMessage())
                    .icon(speedInt>0? service.getDirectionIconGreen(direction):service.getDirectionIcon(direction)).anchor(0.5f, 0.5f));
            gMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
        }
        // Toast.makeText(getActivity(), direction+" prvLatLng "+prvLatLng.latitude +" "+prvLatLng.longitude, Toast.LENGTH_SHORT).show();
        //  Toast.makeText(getActivity(), newDirection+" newLatLng "+prvLatLng.latitude +" "+prvLatLng.longitude, Toast.LENGTH_SHORT).show();
        final LatLng startPosition = new LatLng(prvLatLng.latitude, prvLatLng.longitude);
        final LatLng finalPosition = newLatLng;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 4000;
        boolean hideMarker = false;
        if(gMap == null ){
            return;
        }
        if(!(prvLatLng.latitude == newLatLng.latitude &&  prvLatLng.longitude == newLatLng.longitude)) {
            bearingD = service.angleFromCoordinate(prvLatLng.latitude, prvLatLng.longitude, newLatLng.latitude, newLatLng.longitude);

            direction = (int) bearingD;
            //  final Marker myMarker2 = googleMap.addMarker(new MarkerOptions().position(prvLatLng).title("Location: "+msg )
            //        .snippet("DateTime: "+dateTime + " Reg#: "+regNo+" Speed: "+speed).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }else{
            return;
        }
        //gMap.clear();
        gMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
        // cnc map

        if((prvSpeed == 0 && speedInt> 0) || (prvSpeed > 0 && speedInt == 0 )) {
            gMap.clear();
            myMarker2 = gMap.addMarker(new MarkerOptions().position(prvLatLng).title(liveLocationMap.getDateTime() + " " + liveLocationMap.getSpeedcurrent() + " KM/h").snippet(liveLocationMap.getMessage())
                    .icon(speedInt > 0 ? service.getDirectionIconGreen(direction) : service.getDirectionIcon(direction)).anchor(0.5f, 0.5f));
        }else {
            myMarker2.setTitle(liveLocationMap.getDateTime() + " " + liveLocationMap.getSpeedcurrent() + " KM/h");
            myMarker2.setSnippet(liveLocationMap.getMessage());
            myMarker2.setAnchor(0.5f, 0.5f);
        }
        prvSpeed = speedInt;
        //  .icon(speedInt>0?BitmapDescriptorFactory.fromResource(R.mipmap.location_icon2):BitmapDescriptorFactory.fromResource(R.mipmap.location_icon3)).anchor(0.5f, 0.5f));
        //.icon(speedInt>0?Bitm;apDescriptorFactory.fromResource(R.mipmap.location_icon2):BitmapDescriptorFactory.fromResource(R.mipmap.location_icon3)));
        // .icon(speedInt>0?getDirectionIconGreen():getDirectionIcon()).anchor(0.5f, 0.5f));

        // final Marker myMarker2 =  gMap.addMarker(new MarkerOptions().position(prvLatLng).title(dateTime+" "+speedInt+" KM/h").snippet(message));
        Log.d("TSTAct_bearing", newDirection+ " newDirection bearingD "+ bearingD +" Direction"+ direction);

/*
       gMap.addCircle(new CircleOptions()
                .center(prvLatLng)
                .radius(30)
                .strokeWidth(1f)
               .strokeColor(Color.parseColor("#0084d3"))
                .fillColor(Color.parseColor("#500084d3") )); //0x550000FF
*/

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
                myMarker2.setRotation((float) bearingD);
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
                    handler.postDelayed(this, 40);

                } else {
                    if (hideMarker) {
                        myMarker2.setVisible(false);
                    } else {
                        myMarker2.setVisible(true);
                    }
                }

            }
        });
        prvLatLng = newLatLng;
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
        Log.d("TSTAct_frag", "#onPause ");
    }


    public  void onStop() {
        super.onStop();
        stopRepeatingTask();
        Log.d("TSTAct_frag", "#onStop frag");
    }

    @Override
    public  void onResume() {
        super.onResume();
        Log.d("TSTAct_frag", isCancelled+ " isCancelled #onResume ");
        if(isCancelled){
            isCancelled = false;
            startRepeatingTask();
        }
    }

    @Override
    public  void onStart() {
        super.onStart();
        Log.d("TSTAct", "#onStart ");
    }


    public void getLastTimeSaved(){
        try {
            SharedPreferences lastPacketTimeSP = mActivity.getSharedPreferences("LastTimeMovement_" + vehilce.getModuleId(), Context.MODE_PRIVATE);
            lastPacketMovementMillies = lastPacketTimeSP.getLong("timeInMillies", 0);
        }catch(Exception e){}

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public void sharedPreferencesForLastTimeMovement(long timeInMillies){
        SharedPreferences sp = mActivity.getSharedPreferences("LastTimeMovement_"+ vehilce.getModuleId(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("timeInMillies", timeInMillies);
        Log.d("timeInMillies", ""+timeInMillies);
        editor.commit();
    }
    public void callInfoFrag(){
        Fragment fragment = new InfoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(carMoveCounter <2) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
            );
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.bottom_frag, fragment);
        fragmentTransaction.commit();
    }
    public void callInfoFragWithoutAnimation(Parameters p){
        Fragment fragment = new InfoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        Bundle args = new Bundle();
        args.putString("index", "LAST10RECORDS"); //means trip click
        args.putString("speed", p.getSpeedInt()+" KM/h" );
        args.putString("rcvdTimeDiffer", p.getDiffTime());
        args.putString("refPoint", p.getMessage());

        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.bottom_frag, fragment);
        fragmentTransaction.commit();
    }

}