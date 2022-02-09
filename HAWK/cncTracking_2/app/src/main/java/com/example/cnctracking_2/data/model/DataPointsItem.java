package com.example.cnctracking_2.data.model;

import com.google.gson.annotations.SerializedName;

public class DataPointsItem{

	@SerializedName("y")
	private String Y;

	@SerializedName("label")
	private String label;

	public void setY(String Y){
		this.Y = Y;
	}

	public String getY(){
		return Y;
	}

	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	@Override
 	public String toString(){
		return 
			"DataPointsItem{" + 
			"y = '" + Y + '\'' + 
			",label = '" + label + '\'' + 
			"}";
		}
}