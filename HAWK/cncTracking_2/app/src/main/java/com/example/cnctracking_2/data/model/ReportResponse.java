package com.example.cnctracking_2.data.model;

import java.util.List;

public class ReportResponse{
	private List<WeeklyReportsItem> weeklyReports;
	private boolean success;
	private String message;

	public void setWeeklyReports(List<WeeklyReportsItem> weeklyReports){
		this.weeklyReports = weeklyReports;
	}

	public List<WeeklyReportsItem> getWeeklyReports(){
		return weeklyReports;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}