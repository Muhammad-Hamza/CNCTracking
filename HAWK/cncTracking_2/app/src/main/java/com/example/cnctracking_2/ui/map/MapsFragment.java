package com.example.cnctracking_2.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
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
import com.example.cnctracking_2.R;
import com.example.cnctracking_2.config.APIManager;
import com.example.cnctracking_2.data.model.LiveLocationMap;
import com.example.cnctracking_2.data.model.Parameters;
import com.example.cnctracking_2.data.model.User;
import com.example.cnctracking_2.data.model.VehilceSelection;
import com.example.cnctracking_2.data.service.DataFromSharedPreferences;
import com.example.cnctracking_2.data.service.FindMyVehicle;
import com.example.cnctracking_2.data.service.LiveLocationService;
import com.example.cnctracking_2.data.service.VolleyCallback;
import com.example.cnctracking_2.ui.map.ui.InfoFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsFragment extends Fragment
{
    public static final String DEFAULT = "N/A";

    User user;
    VehilceSelection vehilce;
    LiveLocationService service;
    DataFromSharedPreferences dataSP;
    LiveLocationMap liveLocationMap;
    FindMyVehicle findVehicleService;
    ArrayList<Parameters> movementList;
    SupportMapFragment mapFragment;
    ProgressBar progressBar;
    private Activity mActivity;
    Handler mHandler = new Handler();
    Handler mHandler2 = new Handler();
    GPSTracker gps;
    // Map
    LatLng prvLatLng, currentlatLng, myLocationLattLong;
    private GoogleMap gMap;
    static Marker carMarker;
    FusedLocationProviderClient fusedLocationClient;

    boolean responseOk = false;
    boolean startAsyncTask = false;
    boolean isCancelled = false;
    boolean isCancelled2 = false;
    boolean isFindLocation = false;
    boolean isBoundarySet = false;
    int direction, newDirection;
    int carMoveCounter = 0, prvSpeed = 0;
    long lastPacketMovementMillies = 0;
    double bearingD;
    Marker myMarker2;
    String stringUrl;
    TextView distanceTxt;
    LinearLayout distanceLayout;
    Polyline polyline;
    private OnMapReadyCallback callback = new OnMapReadyCallback()
    {

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
        public void onMapReady(GoogleMap googleMap)
        {
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
        distanceTxt = view.findViewById(R.id.distance_txt);
        distanceLayout = view.findViewById(R.id.distance_layout);

        distanceLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        dataSP = new DataFromSharedPreferences(getActivity());
        user = dataSP.getUserDataSP();
        vehilce = dataSP.getVehicleDataSP();
        findVehicleService = new FindMyVehicle();
        movementList = new ArrayList();
        carMoveCounter = 0;
        direction = 0;
        // start work for find my vehicle
        liveLocationMap = new LiveLocationMap();
        service = new LiveLocationService(getActivity());
        Bundle args = getArguments();
        isFindLocation = service.isFindMyVehicleAllowed(args);
        //Log.d("MapFrag_L", "isFindLocation "+isFindLocation);

        // end work for find my vehicle
        if (mapFragment != null)
        {
            mapFragment.getMapAsync(callback);
            startRepeatingTask();
        }
        getData(); // from APIs
        getLastTimeSaved(); // for future movements
        getMyLocation();

        if (isFindLocation)
        {
            distanceLayout.setVisibility(View.VISIBLE);
            // startFindMyVehicle();
        }
    }

    public void getCurrentLocation()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

        }
     Task<Location> tasks = fusedLocationClient.getLastLocation();
     tasks.addOnSuccessListener(new OnSuccessListener<Location>() {
         @Override
         public void onSuccess(Location location) {
             if(location != null){
                 myLocationLattLong = new LatLng(location.getLatitude(), location.getLongitude());
             }
         }
     });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
        }
    }

    public void getData()
    {
        getLastTimeSaved();
        progressBar.setVisibility(View.VISIBLE);

        service.getData(user, vehilce, carMoveCounter, lastPacketMovementMillies, new VolleyCallback()
        {
            @Override
            public void onSuccess(LiveLocationMap result)
            {
                liveLocationMap = result;
                Log.d("MapFrag_", "getData " + liveLocationMap.getMessage());
                Log.d("MapFrag_", "getData " + liveLocationMap.getParametersList());

                if (liveLocationMap != null && liveLocationMap.getParametersList() != null)
                {
                    movementList.addAll(liveLocationMap.getParametersList());
                    // latt = liveLocationMap.getParametersList().get(carMoveCounter).getLatitude();
                    // lngg = liveLocationMap.getParametersList().get(carMoveCounter).getLongitude();
                }

                progressBar.setVisibility(View.GONE);
                responseOk = true;
                callInfoFrag();
                moveCar();
                if (isFindLocation)
                {
                    startFindMyVehicle();
                    if (!isBoundarySet)
                    {
                        setBoundariesOfMap();
                    }
                }
            }
        });

    }

    // work for smooth movement
    // calling after fetching data from APIs
    public void moveCar()
    {
        if (movementList == null)
        {
            movementList = new ArrayList<>();
        }
        //currentlatLng = new LatLng(latt,lngg);
        isCancelled2 = false;
        mHandlerTask2.run();
    }

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run()
        {

            if (startAsyncTask)
            {
                if (getActivity() == null)
                {
                    Log.d("TSTAct_", "getActivity null " + vehilce.getRegNo());
                    mActivity.finish();
                    return;
                }
                getData();
                Log.d("TSTAct_", "#mHandlerTask start async " + vehilce.getRegNo());
            }
            startAsyncTask = true;
            if (!isCancelled)
            {
                int timeFetch = 30000;
                if (liveLocationMap.getStatusId() == 3 || vehilce.getSpeed() > 0)
                {
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
        public void run()
        {

            if (startAsyncTask)
            {
                boolean runLoop = false;
                Log.d("TSTAct_", "In #mHandlerTask2222 start async " + vehilce.getRegNo());
                Log.d("TSTAct_", carMoveCounter + "In #mHandlerTask2222 " + movementList.size());

                if (carMoveCounter < movementList.size())
                {
                    Parameters p = movementList.get(carMoveCounter);
                    currentlatLng = new LatLng(p.getLatitude(), p.getLongitude());
                    direction = p.getDirection();
                    callInfoFragWithoutAnimation(p);
                    sharedPreferencesForLastTimeMovement(p.getTimeInMillies());
                    changePositionSmoothly(carMarker, currentlatLng, p.getSpeedInt());

                }
                if (carMoveCounter < movementList.size())
                {
                    carMoveCounter++;
                } else
                {
                    stopRepeatingTaskForArray();
                }

            }
            startAsyncTask = true;
            if (!isCancelled2)
            {
                Log.d("TSTAct_2222", "#mHandlerTaskpostDelayed ");
                mHandler2.postDelayed(mHandlerTask2, 2300);
            }

        }
    };


    void changePositionSmoothly(final Marker myMarker, final LatLng newLatLng, final int speedInt)
    {
        Log.d("TSTAct_", "#changePositionSmoothly In");
        if (prvLatLng == null)
        {
            prvSpeed = speedInt;
            prvLatLng = newLatLng;
            //direction = newDirection;
            myMarker2 = gMap.addMarker(new MarkerOptions().position(newLatLng).title(liveLocationMap.getDateTime() + " " + liveLocationMap.getSpeedcurrent() + " KM/h").snippet(liveLocationMap.getMessage()).icon(speedInt > 0 ? service.getDirectionIconGreen(direction) : service.getDirectionIcon(direction)).anchor(0.5f, 0.5f));
            if (!isFindLocation)
            {
                gMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
            }
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
        if (gMap == null)
        {
            return;
        }
        if (!(prvLatLng.latitude == newLatLng.latitude && prvLatLng.longitude == newLatLng.longitude))
        {
            bearingD = service.angleFromCoordinate(prvLatLng.latitude, prvLatLng.longitude, newLatLng.latitude, newLatLng.longitude);

            direction = (int) bearingD;
            //  final Marker myMarker2 = googleMap.addMarker(new MarkerOptions().position(prvLatLng).title("Location: "+msg )
            //        .snippet("DateTime: "+dateTime + " Reg#: "+regNo+" Speed: "+speed).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        } else
        {
            return;
        }
        //gMap.clear();
        if (!isFindLocation || speedInt > 0)
        {
            gMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
        }
        // cnc map

        if ((prvSpeed == 0 && speedInt > 0) || (prvSpeed > 0 && speedInt == 0))
        {
            gMap.clear();
            myMarker2 = gMap.addMarker(new MarkerOptions().position(prvLatLng).title(liveLocationMap.getDateTime() + " " + liveLocationMap.getSpeedcurrent() + " KM/h").snippet(liveLocationMap.getMessage()).icon(speedInt > 0 ? service.getDirectionIconGreen(direction) : service.getDirectionIcon(direction)).anchor(0.5f, 0.5f));
        } else
        {
            myMarker2.setTitle(liveLocationMap.getDateTime() + " " + liveLocationMap.getSpeedcurrent() + " KM/h");
            myMarker2.setSnippet(liveLocationMap.getMessage());
            myMarker2.setAnchor(0.5f, 0.5f);
        }
        prvSpeed = speedInt;
        //  .icon(speedInt>0?BitmapDescriptorFactory.fromResource(R.mipmap.location_icon2):BitmapDescriptorFactory.fromResource(R.mipmap.location_icon3)).anchor(0.5f, 0.5f));
        //.icon(speedInt>0?Bitm;apDescriptorFactory.fromResource(R.mipmap.location_icon2):BitmapDescriptorFactory.fromResource(R.mipmap.location_icon3)));
        // .icon(speedInt>0?getDirectionIconGreen():getDirectionIcon()).anchor(0.5f, 0.5f));

        // final Marker myMarker2 =  gMap.addMarker(new MarkerOptions().position(prvLatLng).title(dateTime+" "+speedInt+" KM/h").snippet(message));
        Log.d("TSTAct_bearing", newDirection + " newDirection bearingD " + bearingD + " Direction" + direction);

/*
       gMap.addCircle(new CircleOptions()
                .center(prvLatLng)
                .radius(30)
                .strokeWidth(1f)
               .strokeColor(Color.parseColor("#0084d3"))
                .fillColor(Color.parseColor("#500084d3") )); //0x550000FF
*/

        if (carMoveCounter == 0)
        {
            return;
        }
        Log.d("TSTAct_", "#changePositionSmoothly after marker apply");
        // gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlatLng, 16));
        // cnc map end
        handler.post(new Runnable()
        {
            long elapsed;
            float t;
            float v;

            @Override
            public void run()
            {
                myMarker2.setRotation((float) bearingD);
                // Calculate progress using interpolator
                Log.d("TSTAct_", "#changePositionSmoothly run method");
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(startPosition.latitude * (1 - t) + finalPosition.latitude * t, startPosition.longitude * (1 - t) + finalPosition.longitude * t);

                myMarker2.setPosition(currentPosition);

                Log.d("TSTAct_", "#changePositionSmoothly run method" + t + " latitude" + startPosition.latitude + " longitude" + startPosition.longitude);
                // Repeat till progress is complete.
                if (t < 1)
                {
                    // Post again 16ms later.
                    handler.postDelayed(this, 40);

                } else
                {
                    if (hideMarker)
                    {
                        myMarker2.setVisible(false);
                    } else
                    {
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
        Log.d("TSTAct_cancel111", "" + isCancelled);
        Log.d("TSTAct_cancel2333", "" + isCancelled2);
        mHandler.removeCallbacks(mHandlerTask);
        stopRepeatingTaskForArray();
    }

    void stopRepeatingTaskForArray()
    {
        Log.d("TSTAct_cancel333", "" + isCancelled);
        Log.d("TSTAct_cancel24444", "" + isCancelled2);
        isCancelled2 = true;
        mHandler2.removeCallbacks(mHandlerTask2);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        stopRepeatingTask();
        Log.d("TSTAct_frag", "#onPause ");
    }


    public void onStop()
    {
        super.onStop();
        stopRepeatingTask();
        Log.d("TSTAct_frag", "#onStop frag");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("TSTAct_frag", isCancelled + " isCancelled #onResume ");
        if (isCancelled)
        {
            isCancelled = false;
            startRepeatingTask();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d("TSTAct", "#onStart ");
    }


    public void getLastTimeSaved()
    {
        try
        {
            SharedPreferences lastPacketTimeSP = mActivity.getSharedPreferences("LastTimeMovement_" + vehilce.getModuleId(), Context.MODE_PRIVATE);
            lastPacketMovementMillies = lastPacketTimeSP.getLong("timeInMillies", 0);
        } catch (Exception e)
        {
        }

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mActivity = activity;
    }

    public void sharedPreferencesForLastTimeMovement(long timeInMillies)
    {
        SharedPreferences sp = mActivity.getSharedPreferences("LastTimeMovement_" + vehilce.getModuleId(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("timeInMillies", timeInMillies);
        Log.d("timeInMillies", "" + timeInMillies);
        editor.commit();
    }

    public void callInfoFrag()
    {
        Fragment fragment = new InfoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (carMoveCounter < 2)
        {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
            );
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.bottom_frag, fragment);
        fragmentTransaction.commit();
    }

    public void callInfoFragWithoutAnimation(Parameters p)
    {
        Fragment fragment = new InfoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        Bundle args = new Bundle();
        args.putString("index", "LAST10RECORDS"); //means trip click
        args.putString("speed", p.getSpeedInt() + " KM/h");
        args.putString("rcvdTimeDiffer", p.getDiffTime());
        args.putString("refPoint", p.getMessage());

        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.bottom_frag, fragment);
        fragmentTransaction.commit();
    }


    // Direction API Work Start
    public void getMyLocation()
    {
        gps = new GPSTracker(this.getContext());
        gps.getLocation();
        if (gps.canGetLocation)
        {
            myLocationLattLong = new LatLng(gps.getLatitude(), gps.getLongitude());
            Log.d("MapFrag_myloc", +gps.getLatitude() + "," + gps.getLongitude());
        } else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public LatLng getLastVehicleLatLong()
    {
        LatLng vCurrentLatLng = new LatLng(vehilce.getLatitude(), vehilce.getLongitude());
        if (movementList != null & movementList.size() > 0)
        {
            Parameters p = movementList.get(movementList.size() - 1);
            vCurrentLatLng = new LatLng(p.getLatitude(), p.getLongitude());
        }
        return vCurrentLatLng;
    }

    public void startFindMyVehicle()
    {
        getMyLocation();

        stringUrl = findVehicleService.getDirectionsUrl(myLocationLattLong, getLastVehicleLatLong());
        Log.d("MapFrag_myloc", stringUrl);
        try
        {
            new GetDirection().execute();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setBoundariesOfMap()
    {
        if (myLocationLattLong != null)
        {
//            gMap.addMarker(new MarkerOptions().title("My Location").position(myLocationLattLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_icon3)));
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(getLastVehicleLatLong());
        builder.include(myLocationLattLong);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        Log.d("ACT_height", "" + height);
        int padding = (int) (width * 0.16); // offset from edges of the map 10% of screen
        int paddingHeight = (int) (height * 0.40);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, (height - paddingHeight), padding);
        gMap.moveCamera(cu);
        isBoundarySet = true;
    }

    class GetDirection extends AsyncTask<String, String, List<List<HashMap<String, String>>>>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected List<List<HashMap<String, String>>> doInBackground(String... args)
        {
            List<List<HashMap<String, String>>> routesParsed = null;
            StringBuilder response = new StringBuilder();

            if (stringUrl != null)
            {
                Log.d("stringUrl", stringUrl);
            }

            try
            {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null)
                    {
                        response.append(strLine);
                    }
                    input.close();
                }


                String jsonOutput = response.toString();
                if (jsonOutput != null)
                {
                    Log.d("ACT_jsonOutput", jsonOutput);
                }

                JSONObject jsonObject = new JSONObject(jsonOutput);

                JSONArray routeArray = jsonObject.getJSONArray("routes");

                JSONObject routes = routeArray.getJSONObject(0);

                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                String encodedString = overviewPolylines.getString("points");

                List<LatLng> points = findVehicleService.decodePoly(encodedString);
                // Starts parsing data
                routesParsed = findVehicleService.parse(jsonObject);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return routesParsed;

        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result)
        {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            String address = "";

            if (result == null || result.size() < 1)
            {
                Toast.makeText(getActivity(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++)
            {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++)
                {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0)
                    {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1)
                    { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    } else if (j == 2)
                    { // Get duration from the list
                        address = (String) point.get("address");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.BLUE);
            }
            Log.d("TSTAct_myLoc", "Distance:" + distance + ", Duration:" + duration);
            distanceTxt.setText("Distance\n" + distance + "\nDuration\n" + duration);
           /* if(destMarker != null) {
                destMarker.setTitle("Dest.: " + address);
            }*/

            if (polyline != null)
            {
                polyline.remove();
            }
            polyline = gMap.addPolyline(lineOptions);
            //   dialog.dismiss();
        }

    }
}