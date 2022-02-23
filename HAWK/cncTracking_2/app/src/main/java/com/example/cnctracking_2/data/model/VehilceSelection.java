package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VehilceSelection implements Parcelable {

    private int moduleId;
    protected int speed;
    protected int statusId;
    protected double longitude;
    protected double latitude;
    private boolean nr;
    protected String diffTime;
    protected String strDateTime;
    private String moduleType;
    protected String message;
    private String regNo;
    private String customerName;
    public VehilceSelection() {
    }

    public VehilceSelection(int moduleId, int speed, int statusId, double longitude, double latitude, boolean nr, String diffTime, String strDateTime, String moduleType, String message, String regNo, String customerName) {
        this.moduleId = moduleId;
        this.speed = speed;
        this.statusId = statusId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nr = nr;
        this.diffTime = diffTime;
        this.strDateTime = strDateTime;
        this.moduleType = moduleType;
        this.message = message;
        this.regNo = regNo;
        this.customerName = customerName;
    }

    protected VehilceSelection(Parcel in) {
        moduleId = in.readInt();
        speed = in.readInt();
        statusId = in.readInt();
        longitude = in.readDouble();
        latitude = in.readDouble();
        nr = in.readByte() != 0;
        diffTime = in.readString();
        strDateTime = in.readString();
        moduleType = in.readString();
        message = in.readString();
        regNo = in.readString();
        customerName = in.readString();
    }

    public static final Creator<VehilceSelection> CREATOR = new Creator<VehilceSelection>() {
        @Override
        public VehilceSelection createFromParcel(Parcel in) {
            return new VehilceSelection(in);
        }

        @Override
        public VehilceSelection[] newArray(int size) {
            return new VehilceSelection[size];
        }
    };

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isNr() {
        return nr;
    }

    public void setNr(boolean nr) {
        this.nr = nr;
    }

    public String getDiffTime() {
        return diffTime;
    }

    public void setDiffTime(String diffTime) {
        this.diffTime = diffTime;
    }

    public String getStrDateTime() {
        return strDateTime;
    }

    public void setStrDateTime(String strDateTime) {
        this.strDateTime = strDateTime;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(moduleId);
        dest.writeInt(speed);
        dest.writeInt(statusId);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeByte((byte) (nr ? 1 : 0));
        dest.writeString(diffTime);
        dest.writeString(strDateTime);
        dest.writeString(moduleType);
        dest.writeString(message);
        dest.writeString(regNo);
        dest.writeString(customerName);
    }
}
