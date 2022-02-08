package com.example.cnctracking_2.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.cnctracking_2.MainActivity;
import com.example.cnctracking_2.R;
import com.example.cnctracking_2.config.APIManager;
import com.example.cnctracking_2.data.model.Customer;
import com.example.cnctracking_2.data.model.Parameters;
import com.example.cnctracking_2.data.model.Unit;
import com.example.cnctracking_2.data.model.Vehicle;
import com.example.cnctracking_2.data.model.YourFormatter;
import com.example.cnctracking_2.ui.ControlFragment;
import com.example.cnctracking_2.ui.gallery.GalleryFragment;
import com.example.cnctracking_2.ui.maintenance.MaintenanceFragment;
import com.example.cnctracking_2.ui.map.LocationWithDetailAct;
import com.example.cnctracking_2.ui.map.MovementReport;
import com.example.cnctracking_2.ui.report.ReportFragment;
import com.example.cnctracking_2.ui.search.FavFragment;
import com.example.cnctracking_2.ui.search.Search;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardWithHeader#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardWithHeader extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PieChart chart;
    LinearLayout b3, b1, b2, b4;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String password, loginName, userRole, search, regNo, name1, name2, name3, name4;
    static int userCode, unitID;
    ProgressBar progressBar;
    String return_text = "";
    int idling;
    int moving;
    int stopped;
    int totalDevices;
    int nr;
    public static final String DEFAULT = "N/A";
    ArrayList<PieEntry> entries1 = new ArrayList<>();
    ArrayList<PieEntry> entries1ForClick = new ArrayList<>();
    TextView centerCircle;

    public DashboardWithHeader() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardWithHeader.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardWithHeader newInstance(String param1, String param2) {
        DashboardWithHeader fragment = new DashboardWithHeader();
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
        View v = inflater.inflate(R.layout.fragment_dashboard_with_header, container, false);


        b1 = v.findViewById(R.id.vehicles);
        b2 = v.findViewById(R.id.trips);
        b3 = v.findViewById(R.id.reports);
        b4 = v.findViewById(R.id.summary);
        centerCircle = v.findViewById(R.id.circle_center);

        progressBar = (ProgressBar) v.findViewById(R.id.searchProgressBar);
        progressBar.setVisibility(View.GONE);

        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        password = sp.getString("password", DEFAULT);
        loginName = sp.getString("loginName", DEFAULT);
        // userRole = sp.getString("userRole", DEFAULT);
        name1 = name2 = name3 = name4 = "";
        setTitleFrag();
        getDevicesData();

        //return inflater.inflate(R.layout.fragment_dashboard_with_header, container, false);
        chart = v.findViewById(R.id.pieChart1);

        //  tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        //   Typeface tfLight = Typeface.createFromAsset(this.getAssets(), "OpenSans-Light.ttf");
        //     chart.setCenterTextTypeface(tf);
        // chart.setCenterText(generateCenterText());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
*/
                //Toast.makeText(getActivity(), "1 click", Toast.LENGTH_SHORT).show();
                Fragment fragment = new Search();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("index", ""); //means trip click
                fragment.setArguments(args);
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                fragmentTransaction.commit();

                // Intent i = new Intent(MainTracking.this,FindYouVehicle.class);
                //  i.putExtras(extras);
                //   startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new MaintenanceFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("index", ""); //means trip click
                fragment.setArguments(args);
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                fragmentTransaction.commit();
//                Toast.makeText(getActivity(), "Maintenance Page Is Under Development", Toast.LENGTH_SHORT).show();
                /*  final Intent i = new Intent(getActivity(), LocationWithDetailAct.class);
                 */
//                view.animate().setDuration(500).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                view.setAlpha(1);
//                             //   startActivity(i);
//
//                            }
//                        });

                /*Fragment fragment = new ControlFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                fragmentTransaction.commit();
*/

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(), "b3 click", Toast.LENGTH_SHORT).show();
//              Toast.makeText(getActivity(), "Reports Page Is Under Development", Toast.LENGTH_SHORT).show();
                // final Intent i = new Intent(getActivity(), MovementReport.class);

                Fragment fragment = new ReportFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("index", ""); //means trip click
                fragment.setArguments(args);
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                fragmentTransaction.commit();
//                view.animate().setDuration(500).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                view.setAlpha(1);
//                               // startActivity(i);
//
//                            }
//                        });
            }

        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(getActivity(), "b4 click", Toast.LENGTH_SHORT).show();
                Fragment fragment = new FavFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                fragmentTransaction.commit();

            }
        });
        centerCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
*/
                //  Toast.makeText(getActivity(), " center click", Toast.LENGTH_SHORT).show();
                Fragment fragment = new Search();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("index", ""); //means trip click
                fragment.setArguments(args);
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                fragmentTransaction.commit();

                // Intent i = new Intent(MainTracking.this,FindYouVehicle.class);
                //  i.putExtras(extras);
                //   startActivity(i);
            }
        });
        // return inflater.inflate(R.layout.fragment_dashboard, container, false);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                int x = (int) h.getX();
                int clickValue = 0;


                try {
                    if (e.getData() != null) {
                        Log.d("Dashboard_SELECTED", e.getData().toString());
                        clickValue = Integer.parseInt(e.getData().toString());
                    }
                } catch (Exception ex) {
                }
                float n = h.getX();
                PieEntry pie = entries1.get(x);
                Log.d("Dashboard_SELECTED", clickValue + " n " + x + "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex());

                /*if(pie.getLabel().equals(name4)){
                    clickValue = 1;
                }else if(pie.getLabel().equals(name3)){
                    clickValue = 2;
                }else if(pie.getLabel().equals(name1)){
                    clickValue = 3;
                }else if(pie.getLabel().equals(name2)){
                    clickValue = 4;
                }*/

                Fragment fragment = new Search();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("index", "CLICK"); //means trip click
                args.putInt("CLICK", clickValue);
                fragment.setArguments(args);
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_frame, fragment);
                fragmentTransaction.commit();

            }

            @Override
            public void onNothingSelected() {
                // legendtext.setVisibility(View.INVISIBLE);
                //set.setValueTextSize(5.0f);
            }
        });
        // refresh

        return v;
    }

    private SpannableString generateCenterText() {
        float txtSize = 5.7f;
        if (totalDevices >= 1000) {
            txtSize = 2.7f;
        }
     /*   SpannableString s = new SpannableString("Device Alerts\nTotal 20");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 12, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 12, s.length(), 0);*/
        // SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        SpannableString s = new SpannableString("" + totalDevices);//
       /* ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // do some thing
                Toast.makeText(getActivity(), "SPan Click" ,Toast.LENGTH_SHORT).show();
            }
        };

        s.setSpan(span1, 0, s.length(), 0);*/
        s.setSpan(new RelativeSizeSpan(5.7f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 13, s.length() - 14, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 13, s.length() - 14, 0);
        // s.setSpan(new StyleSpan(Typeface.ITALIC), 6, s.length(), 0);
        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 6, s.length(), 0);

        chart.invalidate(); // referesh chart
        return s;
    }

    protected PieData generatePieData() {
        pieChartSet();
        int count = 4;
        entries1.clear();
        //entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Moving" ));
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            if (i == 1 && nr > 0) {
                entries1.add(new PieEntry(nr, nr == 0 ? "" : name4, 1));
                colors.add(ColorTemplate.rgb("#D4D4D4"));
            }
            if (i == 2 && stopped > 0) {
                entries1.add(new PieEntry(stopped, stopped == 0 ? "" : name3, 2));
                colors.add(ColorTemplate.rgb("#3498db"));
            }
            if (i == 3 && moving > 0) {
                entries1.add(new PieEntry(moving, moving == 0 ? "" : name1, 3));
                colors.add(ColorTemplate.rgb("#2ecc71"));
            }
            if (i == 4 && idling > 0) {
                entries1.add(new PieEntry(idling, idling == 0 ? "" : name2, 4));
                colors.add(ColorTemplate.rgb("#f1c40f"));
            }
        }
//                1. nr grey color D4D4D4
//                2. parked/stop blue 3498db
//                3. moving green   2ecc71
//                4. idling yellow  f1c40f
        if (colors.size() == 0) {
            colors.add(ColorTemplate.rgb("#2ecc71"));
        }
        PieDataSet ds1 = new PieDataSet(entries1, "");

/*        colors.add(ColorTemplate.rgb("#D4D4D4")); //nr
        colors.add(ColorTemplate.rgb("#3498db")); //parked/stop
        colors.add(ColorTemplate.rgb("#2ecc71")); //moving green
        colors.add(ColorTemplate.rgb("#f1c40f")); //idling yellow*/

/*        for (int c : ColorTemplate.MATERIAL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.VORDIPLOM_COLORS){
            colors.add(c);
        }*/
        ds1.setColors(colors);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(14f);

        PieData d = new PieData(ds1);
        //  d.setValueTypeface(tf);
        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        };
        d.setValueFormatter(vf);

        LegendEntry l1 = new LegendEntry(name4, Legend.LegendForm.CIRCLE, 10f, 2f, null, ColorTemplate.rgb("#D4D4D4"));
        LegendEntry l2 = new LegendEntry(name3, Legend.LegendForm.CIRCLE, 10f, 2f, null, ColorTemplate.rgb("#3498db"));
        LegendEntry l3 = new LegendEntry(name1, Legend.LegendForm.CIRCLE, 10f, 2f, null, ColorTemplate.rgb("#2ecc71"));
        LegendEntry l4 = new LegendEntry(name2, Legend.LegendForm.CIRCLE, 10f, 2f, null, ColorTemplate.rgb("#f1c40f"));
        chart.getLegend().setCustom(new LegendEntry[]{l1, l2, l3, l4});

        return d;
    }

    public void getDevicesData() {


        progressBar.setVisibility(View.VISIBLE);
        String url = APIManager.getDashboardAPI();
        // Log.d("test1", "t3");
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        JSONObject jsonResponse;


                        try {
                            //Log.d("test1", "t4");
                            jsonResponse = new JSONObject(response);
                            moving = jsonResponse.optInt("Moving");
                            idling = jsonResponse.optInt("Idling");
                            stopped = jsonResponse.optInt("Stopped");
                            nr = jsonResponse.optInt("NR");
                            totalDevices = jsonResponse.optInt("totalDevices");
                            name1 = jsonResponse.optString("m1").toString();
                            name2 = jsonResponse.optString("m2").toString();
                            name3 = jsonResponse.optString("m3").toString();
                            name4 = jsonResponse.optString("m4").toString();

                            chart.setData(generatePieData());
                            chart.setCenterText(generateCenterText());
                        } catch (JSONException e) {
                            pieChartSet();
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Not Found!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                pieChartSet();
                Toast.makeText(getActivity(), "Connection Problem", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("psw", password);
                params.put("name", loginName);
                return params;
            }

        };
        Volley.newRequestQueue(getActivity()).add(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void setTitleFrag() {
        try {
            TextView txt = getActivity().findViewById(R.id.toolbar_title);
            txt.setText("Dashboard");
            getActivity().setTitle("");
        } catch (Exception e) {
        }
    }

    public void pieChartSet() {
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        chart.getLegend().setEnabled(true);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
        chart.setDrawEntryLabels(false);

    }
}