package com.example.cnctracking_2.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class WeeklyReportsItem{

	@SerializedName("dataPoints")
	private List<DataPointsItem> dataPoints;

	@SerializedName("title")
	private Title title;

	@SerializedName("color")
	private String color;

	@SerializedName("secondHeading")
	private String secondHeading;

	@SerializedName("firstHeading")
	private String firstHeading;

	@SerializedName("type")
	private String type;

	public void setDataPoints(List<DataPointsItem> dataPoints){
		this.dataPoints = dataPoints;
	}

	public List<DataPointsItem> getDataPoints(){
		return dataPoints;
	}

	public void setTitle(Title title){
		this.title = title;
	}

	public Title getTitle(){
		return title;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public String getColor() {
		return color;
	}

	public String getFirstHeading() {
		return firstHeading;
	}

	public String getSecondHeading() {
		return secondHeading;
	}

	@Override
 	public String toString(){
		return 
			"WeeklyReportsItem{" + 
			"dataPoints = '" + dataPoints + '\'' + 
			",title = '" + title + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}