package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LiveLocationMap implements Parcelable {
    String regNo;
    String message;
    String dateTime;
    String speedStr;
    int direction;
    int newDirection;
    int moduleId;

    int carMoveCounter;
    int speedcurrent;
    int statusId;
    String  deviceType;
    String rcvdTimeDiffer;
    boolean responseOk = false;
    double latt;
    double lngg;
    List<Parameters> parametersList;

    public LiveLocationMap() {}
    protected LiveLocationMap(Parcel in) {
        regNo = in.readString();
        message = in.readString();
        dateTime = in.readString();
        speedStr = in.readString();
        direction = in.readInt();
        newDirection = in.readInt();
        moduleId = in.readInt();
        carMoveCounter = in.readInt();
        speedcurrent = in.readInt();
        statusId = in.readInt();
        deviceType = in.readString();
        rcvdTimeDiffer = in.readString();
        responseOk = in.readByte() != 0;
        latt = in.readDouble();
        lngg = in.readDouble();
        parametersList = in.createTypedArrayList(Parameters.CREATOR);
    }

    public static final Creator<LiveLocationMap> CREATOR = new Creator<LiveLocationMap>() {
        @Override
        public LiveLocationMap createFromParcel(Parcel in) {
            return new LiveLocationMap(in);
        }

        @Override
        public LiveLocationMap[] newArray(int size) {
            return new LiveLocationMap[size];
        }
    };

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSpeedStr() {
        return speedStr;
    }

    public void setSpeedStr(String speedStr) {
        this.speedStr = speedStr;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getNewDirection() {
        return newDirection;
    }

    public void setNewDirection(int newDirection) {
        this.newDirection = newDirection;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getCarMoveCounter() {
        return carMoveCounter;
    }

    public void setCarMoveCounter(int carMoveCounter) {
        this.carMoveCounter = carMoveCounter;
    }

    public int getSpeedcurrent() {
        return speedcurrent;
    }

    public void setSpeedcurrent(int speedcurrent) {
        this.speedcurrent = speedcurrent;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getRcvdTimeDiffer() {
        return rcvdTimeDiffer;
    }

    public void setRcvdTimeDiffer(String rcvdTimeDiffer) {
        this.rcvdTimeDiffer = rcvdTimeDiffer;
    }

    public boolean isResponseOk() {
        return responseOk;
    }

    public void setResponseOk(boolean responseOk) {
        this.responseOk = responseOk;
    }

    public double getLatt() {
        return latt;
    }

    public void setLatt(double latt) {
        this.latt = latt;
    }

    public double getLngg() {
        return lngg;
    }

    public void setLngg(double lngg) {
        this.lngg = lngg;
    }

    public List<Parameters> getParametersList() {
        return parametersList;
    }

    public void setParametersList(List<Parameters> parametersList) {
        this.parametersList = parametersList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(regNo);
        dest.writeString(message);
        dest.writeString(dateTime);
        dest.writeString(speedStr);
        dest.writeInt(direction);
        dest.writeInt(newDirection);
        dest.writeInt(moduleId);
        dest.writeInt(carMoveCounter);
        dest.writeInt(speedcurrent);
        dest.writeInt(statusId);
        dest.writeString(deviceType);
        dest.writeString(rcvdTimeDiffer);
        dest.writeByte((byte) (responseOk ? 1 : 0));
        dest.writeDouble(latt);
        dest.writeDouble(lngg);
        dest.writeTypedList(parametersList);
    }
}
