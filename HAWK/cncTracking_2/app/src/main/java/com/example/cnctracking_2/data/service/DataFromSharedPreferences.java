package com.example.cnctracking_2.data.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.cnctracking_2.data.model.User;
import com.example.cnctracking_2.data.model.VehilceSelection;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;


public class DataFromSharedPreferences {
    public static final String DEFAULT = "N/A";
    Activity activity;
    public DataFromSharedPreferences(Activity activity) {
        this.activity = activity;
    }

    public User getUserDataSP(){
        SharedPreferences sp2 = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        Map<String, String> allowedFeature = new HashMap<>();
        allowedFeature.put( User.immobilizerAllow , ""+sp2.getBoolean(User.immobilizerAllow,false));
        User user = new User(
                sp2.getString("loginName", DEFAULT),
                sp2.getString("password", DEFAULT),
                sp2.getString("userRole", DEFAULT),
                sp2.getInt("userId", 0),
                allowedFeature
        );
        return user;
    }
    public VehilceSelection getVehicleDataSP(){
        SharedPreferences sp = activity.getSharedPreferences("SelectedID", Context.MODE_PRIVATE);

        VehilceSelection vehicle = new VehilceSelection(
                sp.getInt("moduleId", 0),
                (int) sp.getFloat("speed", 0f),
                0,
                sp.getFloat("longitude", 0.0f),
                sp.getFloat("latitude", 0.0f),
                sp.getBoolean("isNr", FALSE),
                "",
                sp.getString("dateTime", DEFAULT),
                sp.getString("deviceType", DEFAULT),
                sp.getString("location", DEFAULT),
                sp.getString("regNo", DEFAULT),
                sp.getString("custName", DEFAULT)
        );

        return vehicle;
    }
}
