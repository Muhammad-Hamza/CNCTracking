package com.example.cnctracking_2.util;

import com.example.cnctracking_2.data.model.local.ChartModel;
import com.example.cnctracking_2.data.model.local.GraphModel;
import com.example.cnctracking_2.data.model.local.MaintenanceModel;

import java.util.ArrayList;
import java.util.List;

public class ConstantUtil {

    public static final String PREF_EXTRA_BUNDLE_1 = "extra_1";
    public static List<ChartModel> getDummyChartModel() {
        List<ChartModel> list = new ArrayList<>();

        List<GraphModel> graph1 = new ArrayList<>();
        graph1.add(new GraphModel(0, "Saturday", 27, null));
        graph1.add(new GraphModel(1, "Sunday", 2, null));
        graph1.add(new GraphModel(2, "Monday", 4, null));
        graph1.add(new GraphModel(3, "Tuesday", 6, null));
        graph1.add(new GraphModel(4, "Wednesday", 3, null));
        graph1.add(new GraphModel(5, "Thursday", 8, null));
        graph1.add(new GraphModel(6, "Friday", 12, null));
        list.add(new ChartModel(0, "Weekly Trip Count Graph", "Days", "Trip Count", graph1));

        List<GraphModel> graph2 = new ArrayList<>();
        graph2.add(new GraphModel(0, "Saturday", 27, null));
        graph2.add(new GraphModel(1, "Sunday", 60, null));
        graph2.add(new GraphModel(2, "Monday", 35, null));
        graph2.add(new GraphModel(3, "Tuesday", 75, null));
        graph2.add(new GraphModel(4, "Wednesday", 80, null));
        graph2.add(new GraphModel(5, "Thursday", 30, null));
        graph2.add(new GraphModel(6, "Friday", 135, null));
        list.add(new ChartModel(1, "Weekly Mileage Graph (KM)", "Days", "Mileage", graph2));

        List<GraphModel> graph3 = new ArrayList<>();
        graph3.add(new GraphModel(0, "Saturday", 5, null));
        graph3.add(new GraphModel(1, "Sunday", 6, null));
        graph3.add(new GraphModel(2, "Monday", 8, null));
        graph3.add(new GraphModel(3, "Tuesday", 5, null));
        graph3.add(new GraphModel(4, "Wednesday", 10, null));
        graph3.add(new GraphModel(5, "Thursday", 3, null));
        graph3.add(new GraphModel(6, "Friday", 7, null));
        list.add(new ChartModel(2, "Weekly Over-Speeding Details", "Days", "Over-Speeding", graph3));

        List<GraphModel> graph4 = new ArrayList<>();
        graph4.add(new GraphModel(0, "Saturday", 5, "4:13 PM"));
        graph4.add(new GraphModel(1, "Sunday", 6, "8:12 PM"));
        graph4.add(new GraphModel(2, "Monday", 8, "8:20 PM"));
        graph4.add(new GraphModel(3, "Tuesday", 5, "7:59 PM"));
        graph4.add(new GraphModel(4, "Wednesday", 10, "8:02 PM"));
        graph4.add(new GraphModel(5, "Thursday", 3, "00:09 AM"));
        graph4.add(new GraphModel(6, "Friday", 7, "10:47 AM"));
        list.add(new ChartModel(3, "Weekly ignition On time report", "Days", "Time", graph4));

        List<GraphModel> graph5 = new ArrayList<>();
        graph5.add(new GraphModel(0, "Saturday", 5, null));
        graph5.add(new GraphModel(1, "Sunday", 6, null));
        graph5.add(new GraphModel(2, "Monday", 8, null));
        graph5.add(new GraphModel(3, "Tuesday", 5, null));
        graph5.add(new GraphModel(4, "Wednesday", 10, null));
        graph5.add(new GraphModel(5, "Thursday", 3, null));
        graph5.add(new GraphModel(6, "Friday", 7, null));
        list.add(new ChartModel(4, "Weekly Engine running Hrs", "Days", "Hours", graph5));

        List<GraphModel> graph6 = new ArrayList<>();
        graph6.add(new GraphModel(0, "Saturday", 100, null));
        graph6.add(new GraphModel(1, "Sunday", 150, null));
        graph6.add(new GraphModel(2, "Monday", 60, null));
        graph6.add(new GraphModel(3, "Tuesday", 25, null));
        graph6.add(new GraphModel(4, "Wednesday", 175, null));
        graph6.add(new GraphModel(5, "Thursday", 35, null));
        graph6.add(new GraphModel(6, "Friday", 77, null));
        list.add(new ChartModel(5, "Weekly Vehicle driving behavior events count(idling, overspeeding, cornering, harsh braking, harsh acceleration) in bar graph", "Days", "Count", graph6));

        List<GraphModel> graph7 = new ArrayList<>();
        graph7.add(new GraphModel(0, "Saturday", 75, null));
        graph7.add(new GraphModel(1, "Sunday", 125, null));
        graph7.add(new GraphModel(2, "Monday", 60, null));
        graph7.add(new GraphModel(3, "Tuesday", 45, null));
        graph7.add(new GraphModel(4, "Wednesday", 35, null));
        graph7.add(new GraphModel(5, "Thursday", 38, null));
        graph7.add(new GraphModel(6, "Friday", 80, null));
        list.add(new ChartModel(6, "Weekly Fence counts graph", "Days", "Count", graph7));

        List<GraphModel> graph8 = new ArrayList<>();
        graph8.add(new GraphModel(0, "Saturday", 50, null));
        graph8.add(new GraphModel(1, "Sunday", 68, null));
        graph8.add(new GraphModel(2, "Monday", 70, null));
        graph8.add(new GraphModel(3, "Tuesday", 30, null));
        graph8.add(new GraphModel(4, "Wednesday", 45, null));
        graph8.add(new GraphModel(5, "Thursday", 25, null));
        graph8.add(new GraphModel(6, "Friday", 85, null));
        list.add(new ChartModel(7, "Avg Speed graph For week", "Days", "Avg Speed", graph8));


        List<GraphModel> graph9 = new ArrayList<>();
        graph9.add(new GraphModel(0, "Saturday", 100, null));
        graph9.add(new GraphModel(1, "Sunday", 120, null));
        graph9.add(new GraphModel(2, "Monday", 80, null));
        graph9.add(new GraphModel(3, "Tuesday", 95, null));
        graph9.add(new GraphModel(4, "Wednesday", 89, null));
        graph9.add(new GraphModel(5, "Thursday", 35, null));
        graph9.add(new GraphModel(6, "Friday", 70, null));
        list.add(new ChartModel(8, "Max speed graph For week", "Days", "Max Speed", graph9));

        return list;
    }

    public static final List<MaintenanceModel> getMaintenanceList() {
        List<MaintenanceModel> list = new ArrayList<>();

        list.add(new MaintenanceModel(0, "Type Service", "Due Now", "1 day overdue", true));
        list.add(new MaintenanceModel(1, "Oil Change", "4000 KM", "500 KM", false));
        list.add(new MaintenanceModel(2, "Service", "5th Feb", "7 Days", false));
        return list;
    }
}
