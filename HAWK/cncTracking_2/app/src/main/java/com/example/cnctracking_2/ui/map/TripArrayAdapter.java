package com.example.cnctracking_2.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.TripsBean;

import java.util.ArrayList;

/**
 * Created by rd03 on 11/5/2015.
 */
public class TripArrayAdapter extends ArrayAdapter<String> {
private final Context context;
private final ArrayList<TripsBean> unitList;
    TextView textView1, textView2, textView2_avg, textView3, textView4, textView5, textViewTo;
    RelativeLayout parkedRow, runningRow;
    int avg = 0;
    String spaces = " ";
public TripArrayAdapter(Context context, ArrayList unitList) {
        super(context, R.layout.trips_row_layout, unitList);
        this.context = context;
        this.unitList = unitList;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        View rowView = inflater.inflate(R.layout.trips_row_layout, parent, false);

        //TextView textViewSerialNum = (TextView) rowView.findViewById(R.id.srNum);

         textView1 = (TextView) rowView.findViewById(R.id.label1);//location
         textView2 = (TextView) rowView.findViewById(R.id.label2);//speed
     textView2_avg = (TextView) rowView.findViewById(R.id.label2_avg);//speed
      textView3 = (TextView) rowView.findViewById(R.id.label3);
           textView4 = (TextView) rowView.findViewById(R.id.label4);
     textView5 = (TextView) rowView.findViewById(R.id.label5);
    textViewTo = (TextView) rowView.findViewById(R.id.text_to);
    parkedRow = (RelativeLayout) rowView.findViewById(R.id.stop_row);
    runningRow = (RelativeLayout) rowView.findViewById(R.id.running_row);
    /*TextView textView5 = (TextView) rowView.findViewById(R.id.label5);*/

    // ignition Off
   /* TextView textView6 = (TextView) rowView.findViewById(R.id.label6);//location
    TextView textView7 = (TextView) rowView.findViewById(R.id.label7);//speed
    TextView textView8 = (TextView) rowView.findViewById(R.id.label8);*/


//abc
        if (unitList.size() > 0) {
/*            Object[] obj = (Object[]) unitList.get(position);
            Unit unt = (Unit) obj[0];
            Vehicle veh = (Vehicle) obj[1];
            Customer customer = (Customer) obj[2];*/

        TripsBean p = unitList.get(position);

        /*if(p.getAvg() != null){
                avg =   Integer.parseInt(p.getAvg());
                if(avg> 9){
                    spaces = " ";
                }else{
                    spaces="";
                }
        }*/

       // textViewSerialNum.setText(""+(position+1));

            if(p.getTypeObject().equals("RUNNING")){
                textView1.setText(" "+p.getDistance()+" "+ p.getDuration());
                textView2.setText(" "+p.getIgntionStartTime());
                textView2_avg.setText(" "+p.getSpeed());
                parkedRow.setVisibility(View.GONE);
                runningRow.setVisibility(View.VISIBLE);
                /*textView3.setVisibility(View.GONE);
                textView4.setVisibility(View.GONE);
                textView5.setVisibility(View.GONE);
                textViewTo.setVisibility(View.GONE);*/
            }else{
                textView3.setText(" "+p.getIgntionOffDuration());
                textView4.setText(" "+p.getIgntiOFFStartTime());
                textView5.setText(" "+p.getIgntiOffEndTime());

                parkedRow.setVisibility(View.VISIBLE);
                runningRow.setVisibility(View.GONE);
            }
        //textView2.setText(""+p.getDistance()+" km");
          /*  textView6.setText(p.getParameters2().getReportText());
            textView7.setText(p.getParameters2().getStrDateTime());
            textView8.setText(p.getParameters2().getMessage());*/

        }

        return rowView;
        }
        }

            /* Parameters prm = new Parameters();
            prm.setTimeTotal(7);
            prm.setStartTime("00:00");
            prm.setEndTime("07:32");
            prm.setMileage(0.0);
            unitList.add(prm);*/