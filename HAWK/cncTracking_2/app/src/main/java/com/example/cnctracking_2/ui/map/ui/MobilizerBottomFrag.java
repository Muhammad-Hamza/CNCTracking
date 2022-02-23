package com.example.cnctracking_2.ui.map.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MobilizerBottomFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MobilizerBottomFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressBar progressBar;
    String psw, name;
    int moduleId;
    public static final String DEFAULT = "N/A";
    boolean isMobilzerAllow;
    TextView textIm;
    public MobilizerBottomFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MobilizerBottomFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MobilizerBottomFrag newInstance(String param1, String param2) {
        MobilizerBottomFrag fragment = new MobilizerBottomFrag();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mobilizer_bottom, container, false);

        AppCompatButton  onButton = (AppCompatButton) v.findViewById(R.id.on_veh);
        AppCompatButton  offButton = (AppCompatButton) v.findViewById(R.id.off_veh);
        textIm = v.findViewById(R.id.immobilzerText);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar1);
        SharedPreferences sp = getActivity().getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
        moduleId = sp.getInt("moduleId", 0);

        SharedPreferences sp2 = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        psw = sp2.getString("password", DEFAULT);
        name = sp2.getString("loginName", DEFAULT);
        isMobilzerAllow = sp2.getBoolean("immobilizerAllow",false);
        //  offDevice(5);
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.animate().setDuration(300).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                v.setAlpha(1);
                                offDevice(1);
                            }
                        });
            }
        });
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.animate().setDuration(300).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                v.setAlpha(1);
                                offDevice(0);
                            }
                        });
            }
        });

        if(!isMobilzerAllow){
            onButton.setVisibility(View.GONE);
            offButton.setVisibility(View.GONE);
            textIm.setText("Feature Not Available!");
        }

        return v;
    }

    public void offDevice(int command){

        progressBar.setVisibility(View.VISIBLE);
        String url = APIManager.getImmobilzerCommandAPI();

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        JSONObject jsonResponse;
                        try {

                            jsonResponse = new JSONObject(response);
                            Toast.makeText(getActivity(), jsonResponse.getString("message").toString(),Toast.LENGTH_SHORT).show();
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
                params.put("name", name);
                params.put("moduleId", ""+moduleId);
                params.put("psw", psw);
                params.put("command", ""+command);



                return params;
            }

        }; Volley.newRequestQueue(getActivity()).add(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}