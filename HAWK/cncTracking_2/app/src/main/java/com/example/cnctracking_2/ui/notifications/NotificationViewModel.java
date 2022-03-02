package com.example.cnctracking_2.ui.notifications;

import static com.example.cnctracking_2.ui.search.FavFragment.DEFAULT;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cnctracking_2.config.APIManager;
import com.example.cnctracking_2.data.model.ReportResponse;
import com.example.cnctracking_2.data.model.notifications.NotificationResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NotificationViewModel extends AndroidViewModel
{


    public NotificationViewModel(@NonNull Application application)
    {
        super(application);
    }


    public void getNotifications(Activity activity, int moduleId, NotificationsFetchListener mListener) {
        //  Log.d("getHistory", "1");
//        SharedPreferences sp = activity.getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
//        int userId = sp.getInt("userId", 0);
//        String regNo = sp.getString("regNo", "N/A");
//        String deviceType = sp.getString("deviceType", "N/A");
//        int moduleId = sp.getInt("moduleId", 0);
//        String password = sp.getString("password", "N/A");
//        String loginName = sp.getString("loginName", "N/A");
        try {
            String url = APIManager.getNotifications();
            //    Log.d("Frag1_1", url +" - "+ loginName +" - "+ password);
            StringRequest sr = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonResponse;
                            // Log.d("Frag1_1", "2");
                            try {
                                NotificationResponse reportResponse = new Gson().fromJson(response, NotificationResponse.class);

                                mListener.onRequestComplete(reportResponse);
//                                showArrayList();

                            }catch(Exception e){
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    SharedPreferences sp = activity.getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
                    int userId= sp.getInt("userId", 0);
                   String regNo = sp.getString("regNo", DEFAULT);
                    String deviceType = sp.getString("deviceType", DEFAULT);
                    int  moduleId = sp.getInt("moduleId", 0);
                    String password = sp.getString("password", DEFAULT);
                    String loginName = sp.getString("loginName", DEFAULT);
                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("name", loginName);
                    params.put("name", loginName);
//                    params.put("psw", password);
                    params.put("psw", password);
//                    params.put("fleetName", deviceType);
                    params.put("fleetName", deviceType);
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

    public interface NotificationsFetchListener {

        void onRequestComplete(NotificationResponse response);

    }

}