package com.example.cnctracking_2.data.model.local;

import java.util.List;

public class ChartModel {

    private int id;
    private String chartName;
    private List<GraphModel> graphModels;
    private String xHeading;
    private String yHeading;

    public ChartModel(int id, String chartName, String xHeading, String yHeading, List<GraphModel> graphModels) {
        this.id = id;
        this.chartName = chartName;
        this.graphModels = graphModels;
        this.xHeading = xHeading;
        this.yHeading = yHeading;
    }

    public int getId() {
        return id;
    }

    public String getChartName() {
        return chartName;
    }

    public List<GraphModel> getGraphModels() {
        return graphModels;
    }


    public String getxHeading() {
        return xHeading;
    }

    public String getyHeading() {
        return yHeading;
    }

}
