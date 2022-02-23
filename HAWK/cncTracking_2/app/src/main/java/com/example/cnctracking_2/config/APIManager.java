package com.example.cnctracking_2.config;

import android.content.Context;
import android.content.SharedPreferences;

public class APIManager {


   //public static String domain = "http://192.169.143.173:9999";
   //public static String appName = "/mirage/";
   public static String domain = "http://web.hawkitracking.com";
    public static String appName = "/hawki/";


    public static String loginAPI() {

        return domain + appName + "UserLoginAPI";
    }

    public static String getDashboardAPI() {

        return domain + appName + "DashboardAPI";
    }

    public static String getSearchFleetsAPI() {

        return domain + appName + "SearchFleetsAPI";
    }
    public static String getFavoriteFleetsAPI() {

        return domain + appName + "FavoriteFleetsAPI";
    }
    public static String getSaveFavoriteFleetAPI() {

        return domain + appName + "SaveFavoriteFleetAPI";
    }
    public static String getLiveTrackingAPI() {

        return domain + appName + "LiveTrackingAPI";
    }

    public static String getTrackLogsAPI() {

        return domain + appName + "TrackLogsAPI";
    }

    public static String getFleetControlAPI() {

        return domain + appName + "FleetControlAPI";
    }
    public static String getSaveFleetControlAPI() {

        return domain + appName + "SaveFleetControlAPI";
    }

    public static String getLatLongTripClickAPI() {

        return domain + appName + "TripLogsAPI";
    }

    public static String getImmobilzerCommandAPI() {

        return domain + appName + "ImmobilzerCommandAPI";
    }

    public static String getReportsData() {

        return domain + appName + "WeeklyReportsAPI";
    }
    public static String getMaintenanceData() {

        return domain + appName + "MaintenanceViewAPI";
    }


    private static final String PREFS_NAME = "SP_FILE";

    public static boolean setPreference(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }
}
