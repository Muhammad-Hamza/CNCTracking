package com.example.cnctracking_2.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DriverBehavior{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("title")
	private Title title;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setTitle(Title title){
		this.title = title;
	}

	public Title getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"DriverBehavior{" + 
			"data = '" + data + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}