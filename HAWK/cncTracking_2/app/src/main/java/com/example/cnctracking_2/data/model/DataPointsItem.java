package com.example.cnctracking_2.data.model;

import android.text.TextUtils;

import com.example.cnctracking_2.util.NumberUtility;
import com.google.gson.annotations.SerializedName;

public class DataPointsItem{

	@SerializedName("y")
	private String Y;

	@SerializedName("label")
	private String label;

	public void setY(String Y){
		this.Y = Y;
	}

	public float getY(){
		if (TextUtils.isEmpty(Y)){
			return 0;
		}
		return Float.parseFloat(Y);
//		return (int) NumberUtility.Companion.roundOff(x,0);
	}
	public String getStrY(){
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