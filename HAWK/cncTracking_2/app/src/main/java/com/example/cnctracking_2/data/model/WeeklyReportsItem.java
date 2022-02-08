package com.example.cnctracking_2.data.model;

import java.util.List;

public class WeeklyReportsItem{
	private List<DataItem> data;
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
}