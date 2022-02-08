package com.example.cnctracking_2.ui.map;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.Parameters;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;


public class Fragment3 extends Fragment implements OnMapReadyCallback {
    float latt, lngg;
    int unitID;
    String loginName, password;
    private Button map_hybrid, map_normal;
    SupportMapFragment mapFragment;
    Context context;
    public static final String DEFAULT = "N/A";
    Intent intent;
    ArrayList<Parameters> unitList2 = new ArrayList<Parameters>();
    View view;

    public Fragment3() {
        // Required empty public constructor
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment3, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("Report", Context.MODE_PRIVATE);
        String report = sp.getString("movementReportString", DEFAULT);

        map_hybrid = (Button) view.findViewById(R.id.map_hybrid);
        map_normal = (Button) view.findViewById(R.id.map_normal);


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.myMap);


        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(report);
            JSONArray jsonArray = jsonResponse.optJSONArray("movementReport");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jb = jsonArray.getJSONObject(i);
                Parameters prm = new Parameters();
                prm.setMessage(jb.optString("message").toString());
                prm.setLongitude(jb.optDouble("longitude"));
                prm.setLatitude(jb.optDouble("latitude"));
                prm.setStrDateTime(jb.optString("dateTime").toString());
                prm.setSpeed(jb.optDouble("speed"));
                prm.setReportText(jb.optString("reportText").toString());
                prm.setRoute(jb.optString("route").toString());


                unitList2.add(prm);
            }

            mapFragment.getMapAsync(this);


        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Not Found!", Toast.LENGTH_SHORT).show();
            //progressBar.setVisibility(View.GONE);
            Log.d("check json", "Json Problem");
            //e.printStackTrace();
        }

        return view;

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {


        LatLng latLng = new LatLng(latt, lngg);

        PolylineOptions line = new PolylineOptions().width(6).color(Color.BLUE).geodesic(true);
        int total = unitList2.size() - 1;
        for (int i = 0; i < unitList2.size(); i++) {


            latLng = new LatLng(unitList2.get(i).getLatitude(), unitList2.get(i).getLongitude());
            line.add(latLng);
            if (unitList2.get(i).getReportText().contains("IGNITION ON")) {
                googleMap.addMarker(new MarkerOptions().position(latLng).title("" + unitList2.get(i).getMessage())
                        .snippet("Ignition On: " + unitList2.get(i).getStrDateTime()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            }
        }
        googleMap.addPolyline(line);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("" + unitList2.get(total).getMessage())
                .snippet("START! DateTime: " + unitList2.get(total).getStrDateTime()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        latLng = new LatLng(unitList2.get(0).getLatitude(), unitList2.get(0).getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).title("" + unitList2.get(0).getMessage())
                .snippet("END! DateTime: " + unitList2.get(0).getStrDateTime()));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(13)
                .bearing(90)
                .tilt(50)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);


        map_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        map_hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

    }


  /*  private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(24.8615, 67.0099388)).title("Marker"));
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    */
}