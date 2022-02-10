package com.example.cnctracking_2.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("name")
	private String name;

	@SerializedName("dataPoints")
	private List<DataPointsItem> dataPoints;

	@SerializedName("color")
	private String color;

	@SerializedName("type")
	private String type;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

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

	public String getColor() {
		return color;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"name = '" + name + '\'' + 
			",dataPoints = '" + dataPoints + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}