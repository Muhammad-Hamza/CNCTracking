package com.example.cnctracking_2.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.Parameters;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsFragment_history extends Fragment {
    public static final String DEFAULT = "N/A";

    ArrayList<Parameters> listReport = new ArrayList<Parameters>();
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
          /*  LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
            LatLng latLng = new LatLng(-34, 151);

            if(listReport != null && listReport.size()>0){
                PolylineOptions line = new PolylineOptions().width(6).color(Color.BLUE).geodesic(true);
                int total = listReport.size() - 1;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < listReport.size(); i++) {


                    latLng = new LatLng(listReport.get(i).getLatitude(), listReport.get(i).getLongitude());
                    line.add(latLng);
                    builder.include(latLng);
                    /*if (listReport.get(i).getReportText().contains("IGNITION ON")) {
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Ignition On") //listReport.get(i).getMessage()
                                .snippet(": " + listReport.get(i).getStrDateTime()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    }*/
                }
                googleMap.addPolyline(line);

                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.redpin_map)));
                //.title("END")
                // .snippet("" + listReport.get(total).getStrDateTime()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                latLng = new LatLng(listReport.get(0).getLatitude(), listReport.get(0).getLongitude());
                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.greenpin)));
                //.title("START")//+ listReport.get(0).getMessage()
                //.snippet("" + listReport.get(0).getStrDateTime()));

                LatLngBounds bounds = builder.build();
                //int padding = 0; // offset from edges of the map in pixels
                //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                googleMap.moveCamera(cu);

                // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                /*CameraPosition cameraPosition = CameraPosition.builder()
                        .target(latLng)
                        .zoom(13)
                        .bearing(360)
                        .tilt(50)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                        2000, null);*/
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sp = null;
        String report = null;

        Bundle args = getArguments();
        String index = null;
        try{
            index = args.getString("index", DEFAULT);
        }catch(Exception e){}


        if(index != null && index.equals("TripClick")){ // means click it from trip report
            sp = getActivity().getSharedPreferences("TripClickLatLong", Context.MODE_PRIVATE);
            report = sp.getString("searchResult", DEFAULT);
            Log.d("frag_map", "Trip click");

        }else{
            sp = getActivity().getSharedPreferences("Report", Context.MODE_PRIVATE);
            report = sp.getString("searchResult", DEFAULT);
            Log.d("frag_map", "All history");
        }
        Log.d("frag_map", report);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


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
                Parameters prm = new Parameters();
                prm.setMessage(jb.optString("refPoint").toString());
                prm.setLongitude(jb.optDouble("longitude"));
                prm.setLatitude(jb.optDouble("latitude"));
                prm.setStrDateTime(jb.optString("eventTime").toString());
                prm.setSpeed(jb.optDouble("speed"));
                prm.setReportText(jb.optString("event").toString());
                //prm.setRoute(jb.optString("route").toString());
                prm.setDirection(jb.optInt("direction"));

                listReport.add(prm);
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Not Found!", Toast.LENGTH_SHORT).show();
            //progressBar.setVisibility(View.GONE);
            Log.d("check json", "Json Problem");
            //e.printStackTrace();
        }

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}