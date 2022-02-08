package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by rd03 on 10/21/2015.
 */
public class Parameters implements Parcelable {
    //Get Current Position Param
    protected int unitId;
    protected Calendar dateTime; //YYYY/MM/DD hh:mm:ss
    protected Calendar sysDateTime;
    protected double longitude;
    protected double latitude;
    protected double speed;
    protected int altitude;
    protected int satellite;
    protected int reportId;
    protected String reportText;
    protected double mileage;
    protected String password;
    protected String strDateTime;
    protected String startTime;
    protected String endTime;
    protected int timeTotal;
    protected int statusId;
    protected String diffTime;

    boolean tempAllow;
    boolean fuelAllow;
    boolean SpeedAllow;
    protected double temperature;
    protected double fuel;
    protected long timeInMillies;
    //User Defined
    protected String message;
    protected boolean emergency;
    protected String crudeMessage;
    protected boolean deviation;
    protected String route;
    protected int direction;


    public Parameters() {
    }

    protected Parameters(Parcel in) {
        unitId = in.readInt();
        longitude = in.readDouble();
        latitude = in.readDouble();
        speed = in.readDouble();

        altitude = in.readInt();
        satellite = in.readInt();
        reportId = in.readInt();
        reportText = in.readString();
        mileage = in.readDouble();
        password = in.readString();
        strDateTime = in.readString();
        tempAllow = in.readByte() != 0;
        fuelAllow = in.readByte() != 0;
        SpeedAllow = in.readByte() != 0;
        temperature = in.readDouble();
        fuel = in.readDouble();
        message = in.readString();
        emergency = in.readByte() != 0;
        crudeMessage = in.readString();
        deviation = in.readByte() != 0;
        route = in.readString();

    }


    public String getStrDateTime() {
        return strDateTime;
    }

    public void setStrDateTime(String strDateTime) {
        this.strDateTime = strDateTime;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }



    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setSysDateTime(Calendar sysDateTime) {
        this.sysDateTime = sysDateTime;
    }

    public void setCrudeMessage(String crudeMessage) {
        this.crudeMessage = crudeMessage;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getSpeed() {
        return speed;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public int getReportId() {
        return reportId;
    }

    public boolean getEmergency() {
        return emergency;
    }



    public String getReportText() {
        return reportText;
    }

    public int getUnitId() {
        return unitId;
    }

    public Calendar getSysDateTime() {
        return sysDateTime;
    }

    public String getCrudeMessage() {
        return crudeMessage;
    }

    public String getPassword() {
        return password;
    }

    public boolean getDeviation() {
        return deviation;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getSatellite() {
        return satellite;
    }

    public void setSatellite(int satellite) {
        this.satellite = satellite;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public void setDeviation(boolean deviation) {
        this.deviation = deviation;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public boolean isTempAllow() {
        return tempAllow;
    }

    public void setTempAllow(boolean tempAllow) {
        this.tempAllow = tempAllow;
    }

    public boolean isFuelAllow() {
        return fuelAllow;
    }

    public void setFuelAllow(boolean fuelAllow) {
        this.fuelAllow = fuelAllow;
    }

    public boolean isSpeedAllow() {
        return SpeedAllow;
    }

    public void setSpeedAllow(boolean speedAllow) {
        SpeedAllow = speedAllow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(int timeTotal) {
        this.timeTotal = timeTotal;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public boolean isDeviation() {
        return deviation;
    }

    public String getDiffTime() {
        return diffTime;
    }

    public void setDiffTime(String diffTime) {
        this.diffTime = diffTime;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public long getTimeInMillies() {
        return timeInMillies;
    }

    public void setTimeInMillies(long timeInMillies) {
        this.timeInMillies = timeInMillies;
    }
}
