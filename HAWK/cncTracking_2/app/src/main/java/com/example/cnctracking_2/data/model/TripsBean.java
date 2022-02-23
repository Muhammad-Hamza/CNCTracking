package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rd03 on 11/5/2015.
 */
public class TripsBean implements Parcelable {


    int serialNo;
    String distance;
    String duration;
    String igntionStartTime;
    //String igntionEndTime;
    String speed;

    String igntionOffDuration;
    String igntiOFFStartTime;
    String igntiOffEndTime;

    String tripStartTimeMS;
    String tripEndTimeMS;
    String typeObject;
    public TripsBean() {}

    protected TripsBean(Parcel in) {
        serialNo = in.readInt();
        distance = in.readString();
        duration = in.readString();
        igntionStartTime = in.readString();
        speed = in.readString();
        igntionOffDuration = in.readString();
        igntiOFFStartTime = in.readString();
        igntiOffEndTime = in.readString();
        tripStartTimeMS = in.readString();
        tripEndTimeMS = in.readString();
        typeObject = in.readString();
    }

    public static final Creator<TripsBean> CREATOR = new Creator<TripsBean>() {
        @Override
        public TripsBean createFromParcel(Parcel in) {
            return new TripsBean(in);
        }

        @Override
        public TripsBean[] newArray(int size) {
            return new TripsBean[size];
        }
    };

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIgntionStartTime() {
        return igntionStartTime;
    }

    public void setIgntionStartTime(String igntionStartTime) {
        this.igntionStartTime = igntionStartTime;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getIgntionOffDuration() {
        return igntionOffDuration;
    }

    public void setIgntionOffDuration(String igntionOffDuration) {
        this.igntionOffDuration = igntionOffDuration;
    }

    public String getIgntiOFFStartTime() {
        return igntiOFFStartTime;
    }

    public void setIgntiOFFStartTime(String igntiOFFStartTime) {
        this.igntiOFFStartTime = igntiOFFStartTime;
    }

    public String getIgntiOffEndTime() {
        return igntiOffEndTime;
    }

    public void setIgntiOffEndTime(String igntiOffEndTime) {
        this.igntiOffEndTime = igntiOffEndTime;
    }

    public String getTripStartTimeMS() {
        return tripStartTimeMS;
    }

    public void setTripStartTimeMS(String tripStartTimeMS) {
        this.tripStartTimeMS = tripStartTimeMS;
    }

    public String getTripEndTimeMS() {
        return tripEndTimeMS;
    }

    public void setTripEndTimeMS(String tripEndTimeMS) {
        this.tripEndTimeMS = tripEndTimeMS;
    }

    public String getTypeObject() {
        return typeObject;
    }

    public void setTypeObject(String typeObject) {
        this.typeObject = typeObject;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(serialNo);
        dest.writeString(distance);
        dest.writeString(duration);
        dest.writeString(igntionStartTime);
        dest.writeString(speed);
        dest.writeString(igntionOffDuration);
        dest.writeString(igntiOFFStartTime);
        dest.writeString(igntiOffEndTime);
        dest.writeString(tripStartTimeMS);
        dest.writeString(tripEndTimeMS);
        dest.writeString(typeObject);
    }
}
