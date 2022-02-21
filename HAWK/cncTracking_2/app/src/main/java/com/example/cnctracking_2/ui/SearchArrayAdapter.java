package com.example.cnctracking_2.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.Customer;
import com.example.cnctracking_2.data.model.Parameters;
import com.example.cnctracking_2.data.model.Unit;
import com.example.cnctracking_2.data.model.Vehicle;

import java.util.ArrayList;

public class SearchArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList unitList;
    private final String userRole;
    private OnItemClickListener mListener;


    public SearchArrayAdapter(Context context, ArrayList unitList, String role, OnItemClickListener mListener) {
        super(context, R.layout.search_adapter, unitList);
        this.context = context;
        this.unitList = unitList;
        userRole = role;
        this.mListener = mListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        View rowView = inflater.inflate(R.layout.listitem_search, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.label1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.label2);
        TextView textView3 = (TextView) rowView.findViewById(R.id.label3);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconImage);
        TextView lastTime = (TextView) rowView.findViewById(R.id.lasttime);
        ImageView eyesImg = (ImageView) rowView.findViewById(R.id.eyes);
        Button btnHistory = (Button) rowView.findViewById(R.id.btnHistory);
        LinearLayout llContent = (LinearLayout) rowView.findViewById(R.id.llContent);

        if (unitList.size() > 0) {

            llContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onViewClick(unitList.get(position), position, view);
                }
            });
            btnHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(unitList.get(position));
                }
            });
            Object[] obj = (Object[]) unitList.get(position);
            Unit unt = (Unit) obj[0];
            Vehicle veh = (Vehicle) obj[1];
            Customer customer = (Customer) obj[2];
            Parameters prm = (Parameters) obj[3];

            // textView1.setText("Irfan");
            //  textView2.setText("Irfan");
            //  textView3.setText("Irfan");
            //  imageView.setImageResource(R.drawable.location);
            textView1.setText(veh.getRegNo());
            textView2.setText(customer.getFirstName());
            textView3.setText("" + prm.getStrDateTime());
            lastTime.setText(prm.getDiffTime());

           /*if(!veh.isNr()){
               imageView.setImageResource(R.mipmap.res_device);
           }else if(veh.isNr()){
                imageView.setImageResource(R.mipmap.nr_device);
            }*/

            if (prm.getStatusId() == 1) {
                imageView.setImageResource(R.mipmap.nr_device);
            } else if (prm.getStatusId() == 2) {
                imageView.setImageResource(R.mipmap.stop_device);
            } else if (prm.getStatusId() == 3) {
                imageView.setImageResource(R.mipmap.running_device);
            } else if (prm.getStatusId() == 4) {
                imageView.setImageResource(R.mipmap.idling_device);
            }


        }
        return rowView;
    }

    public interface OnItemClickListener {
        void onItemClick(Object o);

        void onViewClick(Object o, int position, View view);
    }
}
