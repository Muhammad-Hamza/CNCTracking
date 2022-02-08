package com.example.cnctracking_2.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.Parameters;


import java.util.ArrayList;

/**
 * Created by rd03 on 11/5/2015.
 */
public class MovementArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<Parameters> unitList;

    public MovementArrayAdapter(Context context, ArrayList unitList) {
        super(context, R.layout.trips_row_layout, unitList);
        this.context = context;
        this.unitList = unitList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


      View row = convertView;
 //       MyViewHolder holder = null;

         if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        //    row = inflater.inflate(R.layout.movement_row_layout, parent, false);
         //   holder = new MyViewHolder(row);
         //   row.setTag(holder);

        } else {
        //    holder = (MyViewHolder) row.getTag();
        }

      /*  MovementReport mr = new MovementReport();


        if (unitList.size() > 0) {
*//*            Object[] obj = (Object[]) unitList.get(position);
            Unit unt = (Unit) obj[0];
            Vehicle veh = (Vehicle) obj[1];
            Customer customer = (Customer) obj[2];*//*

            Parameters p = unitList.get(position);
            holder.textViewNum.setText("" + (position + 1));
            holder.textView3.setText(p.getTimeTotal() +"Hours");
            holder.textView4.setText(p.getStartTime());
            // holder.textView5.setText((p.getRoute() == null)?p.getRoute():"  -");
            holder.textView1.setText(p.getEndTime());
        }*/
        return row;
    }
    /* Parameters prm = new Parameters();
            prm.setTimeTotal(7);
            prm.setStartTime("00:00");
            prm.setEndTime("07:32");
            prm.setMileage(0.0);
            unitList.add(prm);*/

}