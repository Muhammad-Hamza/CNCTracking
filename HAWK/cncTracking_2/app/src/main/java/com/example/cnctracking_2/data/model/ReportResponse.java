package com.example.cnctracking_2.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.cnctracking_2.data.model.local.NewGraphModel;
import com.google.gson.annotations.SerializedName;

public class ReportResponse {

    @SerializedName("weeklyReports")
    private List<WeeklyReportsItem> weeklyReports;

    @SerializedName("driverBehavior")
    private DriverBehavior driverBehavior;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public void setWeeklyReports(List<WeeklyReportsItem> weeklyReports) {
        this.weeklyReports = weeklyReports;
    }

    public List<WeeklyReportsItem> getWeeklyReports() {
        return weeklyReports;
    }

    public void setDriverBehavior(DriverBehavior driverBehavior) {
        this.driverBehavior = driverBehavior;
    }

    public DriverBehavior getDriverBehavior() {
        return driverBehavior;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public List<NewGraphModel> getAllData() {
        List<NewGraphModel> list = new ArrayList<>();
        if (driverBehavior != null && driverBehavior.getData() != null && driverBehavior.getData().size() > 0) {
            Map<String, List<DataPointsItem>> mapDriverBehaviour = new HashMap<>();
            for (int i = 0; i < driverBehavior.getData().size(); i++) {
                for (int j = 0; j < driverBehavior.getData().get(i).getDataPoints().size(); j++) {
                    if (mapDriverBehaviour.containsKey(driverBehavior.getData().get(i).getDataPoints().get(j).getLabel())) {
                        List<DataPointsItem> nestedGraphListNew = mapDriverBehaviour.get(driverBehavior.getData().get(i).getDataPoints().get(j).getLabel());
                        nestedGraphListNew.add(driverBehavior.getData().get(i).getDataPoints().get(j));
                        mapDriverBehaviour.put(driverBehavior.getData().get(i).getDataPoints().get(j).getLabel(), nestedGraphListNew);
                    } else {
                        List<DataPointsItem> nestedGraphList = new ArrayList<>();
                        nestedGraphList.add(driverBehavior.getData().get(i).getDataPoints().get(j));
                        mapDriverBehaviour.put(driverBehavior.getData().get(i).getDataPoints().get(j).getLabel(), nestedGraphList);
                    }
                }
            }
            list.add(new NewGraphModel(0, driverBehavior.getTitle().getText(), "Driver Behaviour Details", true, mapDriverBehaviour));
        }
        if (weeklyReports != null && weeklyReports.size() > 0) {
            for (int i = 0; i < weeklyReports.size(); i++) {
                list.add(new NewGraphModel((i + 1), weeklyReports.get(i).getTitle().getText(), "See the chart below for past 7 days.", false, weeklyReports.get(i).getDataPoints(), weeklyReports.get(i).getColor(), weeklyReports.get(i).getFirstHeading(), weeklyReports.get(i).getSecondHeading()));
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return
                "ReportResponse{" +
                        "weeklyReports = '" + weeklyReports + '\'' +
                        ",driverBehavior = '" + driverBehavior + '\'' +
                        ",success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }

}