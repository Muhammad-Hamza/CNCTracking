package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by rd03 on 10/21/2015.
 */
public class Vehicle implements Parcelable {

    private String regNo;
    private boolean nr;

    public Vehicle() {

    }
    protected Vehicle(Parcel in) {
        regNo = in.readString();
        nr = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(regNo);
        dest.writeByte((byte) (nr ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public boolean isNr() {
        return nr;
    }

    public void setNr(boolean nr) {
        this.nr = nr;
    }
}
