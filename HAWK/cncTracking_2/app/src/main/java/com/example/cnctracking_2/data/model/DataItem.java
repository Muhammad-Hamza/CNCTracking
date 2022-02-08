package com.example.cnctracking_2.data.model;

import java.util.List;

public class DataItem{
	private List<DataPointsItem> dataPoints;
	private String type;
	private String name;

	public void setDataPoints(List<DataPointsItem> dataPoints){
		this.dataPoints = dataPoints;
	}

	public List<DataPointsItem> getDataPoints(){
		return dataPoints;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}