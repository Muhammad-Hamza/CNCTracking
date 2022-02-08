package com.example.cnctracking_2.data.model.local;

public class GraphModel {

    private int id;
    private int yValue;
    private String xValue;
    private String tagValue;

    public GraphModel(int id, String xValue, int yValue, String tagValue) {
        this.id = id;
        this.yValue = yValue;
        this.xValue = xValue;
        this.tagValue = tagValue;
    }

    public int getId() {
        return id;
    }

    public int getyValue() {
        return yValue;
    }

    public String getxValue() {
        return xValue;
    }

    public String getTagValue() {
        return tagValue;
    }
}
