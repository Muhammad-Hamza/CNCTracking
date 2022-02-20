package com.example.cnctracking_2.ui.report.component;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cnctracking_2.config.APIManager;
import com.example.cnctracking_2.data.model.ReportResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportViewModel extends AndroidViewModel {


    public ReportViewModel(@NonNull Application application) {
        super(application);
    }


    public void getReportsData(Activity activity, int moduleId, ReportFetchListener mListener) {
        //  Log.d("getHistory", "1");
//        SharedPreferences sp = activity.getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
//        int userId = sp.getInt("userId", 0);
//        String regNo = sp.getString("regNo", "N/A");
//        String deviceType = sp.getString("deviceType", "N/A");
//        int moduleId = sp.getInt("moduleId", 0);
//        String password = sp.getString("password", "N/A");
//        String loginName = sp.getString("loginName", "N/A");
        try {
            String url = APIManager.getReportsData();
            //    Log.d("Frag1_1", url +" - "+ loginName +" - "+ password);
            StringRequest sr = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonResponse;
                            // Log.d("Frag1_1", "2");
                            try {
                                ReportResponse reportResponse = new Gson().fromJson(response, ReportResponse.class);

                                mListener.onRequestComplete(reportResponse);
//                                showArrayList();

                            } catch (Exception e) {
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
//                    params.put("name", loginName);
                    params.put("name", "ZEESHAN");
//                    params.put("psw", password);
                    params.put("psw", "1234");
//                    params.put("fleetName", deviceType);
                    params.put("fleetName", "BUE-745");
//                    params.put("moduleId", ""+moduleId );
                    if (moduleId != -1) {
                        params.put("moduleId", "" + moduleId);
                    } else {
                        params.put("moduleId", "452");
                    }
                    return params;
                }

            };
            Volley.newRequestQueue(activity).add(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(
                    12000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            // progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, "Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public interface ReportFetchListener {

        void onRequestComplete(ReportResponse response);

    }

}
