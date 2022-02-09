package com.example.cnctracking_2.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ReportResponse{

	@SerializedName("weeklyReports")
	private List<WeeklyReportsItem> weeklyReports;

	@SerializedName("driverBehavior")
	private DriverBehavior driverBehavior;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public void setWeeklyReports(List<WeeklyReportsItem> weeklyReports){
		this.weeklyReports = weeklyReports;
	}

	public List<WeeklyReportsItem> getWeeklyReports(){
		return weeklyReports;
	}

	public void setDriverBehavior(DriverBehavior driverBehavior){
		this.driverBehavior = driverBehavior;
	}

	public DriverBehavior getDriverBehavior(){
		return driverBehavior;
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

	@Override
 	public String toString(){
		return 
			"ReportResponse{" + 
			"weeklyReports = '" + weeklyReports + '\'' + 
			",driverBehavior = '" + driverBehavior + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}