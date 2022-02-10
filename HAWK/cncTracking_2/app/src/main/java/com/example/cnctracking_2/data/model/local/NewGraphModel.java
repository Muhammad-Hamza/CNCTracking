package com.example.cnctracking_2.data.model.local;

import com.example.cnctracking_2.data.model.DataItem;
import com.example.cnctracking_2.data.model.DataPointsItem;

import java.util.List;
import java.util.Map;

public class NewGraphModel {
    private int id;
    private String title;
    private String barLabel;
    private boolean isMultipleAdded;
    private String details;
    //    private Map<String, List<DataPointsItem>> multipleMapping;
    private List<DataPointsItem> listOfDataPoint;
    private String color;
    private String firstHeading;
    private String secondHeading;

    private List<DataItem> dataItems;

    public NewGraphModel(int id, String title, String details, boolean isMultipleAdded, List<DataItem> dataItems) {
        this.id = id;
        this.title = title;
        this.dataItems = dataItems;
//        this.multipleMapping = multipleMapping;
        this.isMultipleAdded = isMultipleAdded;
        this.details = details;
    }

    public NewGraphModel(int id, String title, String details, boolean isMultipleAdded, List<DataPointsItem> listOfDataPoint, String color, String firstHeading, String secondHeading) {
        this.id = id;
        this.title = title;
        this.listOfDataPoint = listOfDataPoint;
        this.isMultipleAdded = isMultipleAdded;
        this.details = details;
        this.color = color;
        this.firstHeading = firstHeading;
        this.secondHeading = secondHeading;
    }

    public String getSecondHeading() {
        return secondHeading;
    }

    public String getFirstHeading() {
        return firstHeading;
    }

    public String getColor() {
        return color;
    }

    public String getDetails() {
        return details;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBarLabel() {
        return barLabel;
    }

    public boolean isMultipleAdded() {
        return isMultipleAdded;
    }

    public List<DataItem> getDataItems() {
        return dataItems;
    }

    //    public Map<String, List<DataPointsItem>> getMultipleMapping() {
//        return multipleMapping;
//    }

    public List<DataPointsItem> getListOfDataPoint() {
        return listOfDataPoint;
    }
}
