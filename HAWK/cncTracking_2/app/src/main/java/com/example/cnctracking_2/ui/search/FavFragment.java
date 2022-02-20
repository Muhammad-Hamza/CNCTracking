package com.example.cnctracking_2.ui.search;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.cnctracking_2.data.model.Customer;
import com.example.cnctracking_2.data.model.Parameters;
import com.example.cnctracking_2.data.model.Unit;
import com.example.cnctracking_2.data.model.Vehicle;
import com.example.cnctracking_2.ui.SearchArrayAdapter;
import com.example.cnctracking_2.ui.map.LocationWithDetailAct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FavFragment extends Fragment implements SearchArrayAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText searchText;
    Button searchBtn;
    private String Error = null;
    String password, loginName, userRole, search, regNo, name, name1;
    static int userCode, unitID, userId;
    ProgressBar progressBar;
    String return_text="";
    ArrayList unitList = new ArrayList();
    SearchArrayAdapter adapter;
    private static final String KEY_INDEX = "index";
    ListView list;
    public static final String DEFAULT = "N/A";
    public FavFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavFragment newInstance(String param1, String param2) {
        FavFragment fragment = new FavFragment();
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
         View v = inflater.inflate(R.layout.fragment_search2, container, false);
        list = (ListView) v.findViewById(R.id.listView);


        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        password = sp.getString("password", DEFAULT);
        loginName = sp.getString("loginName", DEFAULT);
        userRole = sp.getString("userRole", DEFAULT);
        userId= sp.getInt("userId", 0);
        progressBar = (ProgressBar) v.findViewById(R.id.searchProgressBar);
        progressBar.setVisibility(View.GONE);

        setTitleFrag();
        adapter = new SearchArrayAdapter(getActivity(), unitList, userRole,this);

        getDevicesData();

        return v;
        // not in use
    }


    public void showArrayList(){


        list.setAdapter(adapter);


        // list view menu
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                Object[] obj = (Object[]) unitList.get(position);
                final Unit unt = (Unit) obj[0];
                Vehicle veh = (Vehicle) obj[1];
                Customer customer = (Customer) obj[2];
                Parameters prm = (Parameters) obj[3];

                String Slecteditem= ""+veh.getRegNo();
            //    Toast.makeText(getActivity(), Slecteditem, Toast.LENGTH_SHORT).show();


                // Shared Prefrences
                SharedPreferences sp = getActivity().getSharedPreferences("SelectedID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("loginName", loginName);
                editor.putString("password", password);
                editor.putInt("userId", userId);
                editor.putString("regNo", veh.getRegNo());
                editor.putString("location", prm.getMessage());

                editor.putString("custName", customer.getFirstName());

                editor.putFloat("longitude", (float) (prm.getLongitude()));
                editor.putFloat("latitude", (float) prm.getLatitude());
                editor.putString("dateTime", prm.getStrDateTime());
                editor.putString("deviceType", unt.getUnitType());
                editor.putFloat("speed", (float) prm.getSpeed());
                editor.putBoolean("isNr",veh.isNr());
                editor.putInt("moduleId", (int) unt.getUnitId());
                editor.commit();

                //Toast.makeText(getApplicationContext(), "data save", Toast.LENGTH_LONG).show();

                final Intent i = new Intent(getActivity(), LocationWithDetailAct.class);
               /*
                        i.putExtra("unitID", unt.getUnitId());
                        i.putExtra("regNo", veh.getRegNo());
                        i.putExtra("location", prm.getMessage());
                        i.putExtra("longitude", prm.getLongitude());
                        i.putExtra("latitude", prm.getLatitude());
                        i.putExtra("dateTime", prm.getStrDateTime());
                i.putExtra("unitID", unt.getUnitId());
                i.putExtra("userRole", userRole);
                i.putExtra("loginName", loginName);
                i.putExtra("password", password);
                i.putExtra("regNo", veh.getRegNo());*/

                view.animate().setDuration(500).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // list.remove(item);
                                //adapter.notifyDataSetChanged();
                                view.setAlpha(1);


                                startActivity(i);

                            }
                        });

            }
        });


    }

    public void getDevicesData(){
       // search = searchText.getText().toString();

            progressBar.setVisibility(View.VISIBLE);
            String url = APIManager.getFavoriteFleetsAPI();
          //  Log.d("FavFrag", "t3");
            StringRequest sr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            progressBar.setVisibility(View.GONE);

                            JSONObject jsonResponse;

                            unitList.clear();

                            try {
                             //   Log.d("FavFrag", "t4");
                                jsonResponse = new JSONObject(response);
                              //  Log.d("FavFrag", "t4"+jsonResponse);
                                JSONArray jsonArray = jsonResponse.optJSONArray("searchResult");
                                if( jsonArray == null){
                                    jsonArray = new JSONArray();
                                }
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jb = jsonArray.getJSONObject(i);

                                    Unit unt = new Unit();
                                    unt.setUnitId( jb.optInt("moduleId"));
                                    unt.setUnitType( jb.optString("name"));

                                    Vehicle veh = new Vehicle();
                                    veh.setRegNo(jb.optString("regNo").toString());

                                    veh.setNr(jb.optInt("active") ==0);
                                    // veh.setEngineNo(rs.getString("vech_engineNo"))

                                    Customer cust = new Customer();
                                    cust.setFirstName(jb.optString("clientName").toString());

                                    Parameters prm = new Parameters();
                                    // prm.setMessage(jb.optString("location").toString());
                                    prm.setLongitude(jb.optDouble("longitude"));
                                    prm.setLatitude(jb.optDouble("latitude"));
                                    prm.setStrDateTime(jb.optString("RecieveDate").toString());
                                    prm.setSpeed(jb.optDouble("speed"));
                                    prm.setDiffTime(jb.optString("rcvdTimeDiffer").toString());

                                    prm.setStatusId(jb.optInt("statusId"));
                                    //prm.setHeading(jb.optInt("heading"));
                                    // prm.setDirectionAllow((Boolean) jb.optBoolean("isDirectionAllow"));
                                    unitList.add(new Object[]{unt, veh, cust, prm});

                                }
                                if(unitList.size()>0)
                                    Toast.makeText(getActivity(), "Size: "+unitList.size() ,Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getActivity(), "Not Found!" ,Toast.LENGTH_SHORT).show();


                            } catch (Exception e) {
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Not Found!" ,Toast.LENGTH_SHORT).show();
                            }

                            showArrayList();


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
                    params.put("search", "");
                    params.put("psw", password);
                    params.put("name", loginName);
                    return params;
                }

            }; Volley.newRequestQueue(getActivity()).add(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(
                    8000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    public void setTitleFrag() {
        try {
            TextView txt = getActivity().findViewById(R.id.toolbar_title);
            txt.setText("Favorite");
            getActivity().setTitle("");
        }catch (Exception e){}
    }

    @Override
    public void onItemClick(Object o) {
//        Log.e("asd","asdasdasdas");
    }
}