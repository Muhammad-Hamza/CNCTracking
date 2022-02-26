package com.example.cnctracking_2.data.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveLocationService {
    private Activity activity;
    public static final String DEFAULT = "N/A";
    public LiveLocationService(Activity activity) {
        this.activity = activity;
    }

    public void getData(User usr, VehilceSelection vehicle, int carMoveCounter, long lastPacketMovementMillies, final VolleyCallback callback){
        Log.d("MapFrag_", "getData start");
        LiveLocationMap liveLocationMap = new LiveLocationMap();
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
                            liveLocationMap.setLatt(jsonResponse.getDouble("latitude"));
                            liveLocationMap.setLngg(jsonResponse.getDouble("longitude"));
                            liveLocationMap.setDateTime(jsonResponse.getString("eventTime"));
                            liveLocationMap.setSpeedStr(jsonResponse.getString("speed") +" KM/h");
                            liveLocationMap.setSpeedcurrent(jsonResponse.getInt("speed"));
                            liveLocationMap.setMessage(jsonResponse.getString("refPoint"));
                            // direction = jsonResponse.getInt("direction");
                            liveLocationMap.setStatusId(jsonResponse.getInt("statusId"));
                            liveLocationMap.setRcvdTimeDiffer(jsonResponse.getString("rcvdTimeDiffer"));
                            JSONArray jsonArray = jsonResponse.optJSONArray("movement");

                            if( jsonArray == null){
                                jsonArray = new JSONArray();
                            }
                            Parameters prm;
                            List<Parameters> prmList = new ArrayList();
                            // movementList = new ArrayList();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jb = jsonArray.getJSONObject(i);

                                prm = new Parameters();
                                prm.setLongitude(jb.optDouble("longitude"));
                                prm.setLatitude(jb.optDouble("latitude"));
                                prm.setSpeedInt(jb.optInt("speed"));
                                prm.setDiffTime(jb.optString("rcvdTimeDiffer"));
                                prm.setDirection(jb.optInt("direction"));
                                prm.setMessage(jb.optString("refPoint").toString());
                                prm.setTimeInMillies(jb.optLong("currentTimeInMillies"));
                                prmList.add(prm);
                                // Collections.reverse(movementList);
                            }

                            Log.d("MapFrag_", "data added");
                            liveLocationMap.setParametersList(prmList);
                            sharedPreferencesMethod(response);
                            Log.d("MapFrag_", "getData json end");
                            callback.onSuccess(liveLocationMap);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", usr.getLoginName());
                params.put("psw", usr.getPassword());
                params.put("fleetName", vehicle.getModuleType());
                params.put("moduleId", ""+vehicle.getModuleId() );
                params.put("isDeviceDate", "true" );
                params.put("carMoveCounter", ""+carMoveCounter );
                params.put("lastPacketMovementMillies", ""+lastPacketMovementMillies );//lastPacketMovementMillies

                return params;
            }

        };
        Volley.newRequestQueue(activity).add(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.d("MapFrag_", "getData end");
    }

    public void sharedPreferencesMethod(String part1){
        try {
            Log.d("MapFrag_", "sharedPreferencesMethod start");
            SharedPreferences sp = activity.getSharedPreferences("InfoDevice", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("searchResult", part1);
            editor.commit();
            Log.d("MapFrag_", "sharedPreferencesMethod end");

            SharedPreferences sp2 = activity.getSharedPreferences("InfoDevice", Context.MODE_PRIVATE);
            String report = sp2.getString("searchResult", "N/A");
            Log.d("InfoFrag_1_service", report);

        }catch(Exception e){e.printStackTrace();}
    }

    public double angleFromCoordinate(double lat1, double long1, double lat2,
                                       double long2) {

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        // brng = 360 - brng; // count degrees counter-clockwise - remove to make clockwise

        return brng;
    }


    public BitmapDescriptor getDirectionIcon(int direction){
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
    public BitmapDescriptor getDirectionIconGreen(int direction){
        Log.d("TSTAct_direction_",""+direction);
        return BitmapDescriptorFactory.fromResource(R.mipmap.car_1_red);
    }

    public boolean isFindMyVehicleAllowed(Bundle args) {
        String index = null;
        try {
            index = args.getString("index", DEFAULT);
            if (index != null && index.equals("FIND_MY_VEHICLE")) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
