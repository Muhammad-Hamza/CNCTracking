package com.example.cnctracking_2.data.model;

import org.json.JSONArray;

/**
 * Created by rd03 on 11/4/2015.
 */
public class PassHistoryReportData {

    static JSONArray tripDataLog;
    static JSONArray historyDataLog;
    public static JSONArray getTripDataLog() {
        return tripDataLog;
    }

    public void setTripDataLog(JSONArray tripDataLog) {
        this.tripDataLog = tripDataLog;
    }

    public static JSONArray getMovementDataLog() {
        return historyDataLog;
    }

    public static void setMovementDataLog(JSONArray movementDataLog) {
        PassHistoryReportData.historyDataLog = movementDataLog;
    }
}
