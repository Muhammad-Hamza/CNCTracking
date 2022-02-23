package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rd03 on 10/21/2015.
 */
public class Unit  implements Parcelable {

    private int code;
    private int unitId;
    private String unitType;


    public Unit() {
    }


    public Unit(int unitId) {
        this.unitId = unitId;
    }

    public Unit(int unitId, String unitType) {
        this.unitId = unitId;
        this.unitType = unitType;
    }


    protected Unit(Parcel in) {
        code = in.readInt();
        unitId = in.readInt();
        unitType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeInt(unitId);
        dest.writeString(unitType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getUnitId() {
        return this.unitId;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitType() {
        return this.unitType;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public int hashCode() {
        return unitId;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Unit) {
            return this.unitId == ((Unit) obj).getUnitId();
        }
        return false;
    }

    public String toString() {
        return this.unitId + "/" + this.unitType;
    }

    public int getCode() {
        return code;
    }
}
