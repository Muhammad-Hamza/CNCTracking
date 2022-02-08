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
    private String commType;
    private int modemCode;
    private long IMEI;
    private String serialNo;
    private String firmware;
    private String packageName;
    private String password;

    public Unit() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public Unit(int unitId) {
        this.unitId = unitId;
    }

    public Unit(int unitId, String unitType) {
        this.unitId = unitId;
        this.unitType = unitType;
    }


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


    public void setCommType(String commType) {
        this.commType = commType;
    }

    public void setModemCode(int modemCode) {
        this.modemCode = modemCode;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIMEI(long IMEI) {
        this.IMEI = IMEI;
    }

    public String getCommType() {
        return commType;
    }

    public int getModemCode() {
        return modemCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public int getCode() {
        return code;
    }

    public String getPassword() {
        return password;
    }

    public long getIMEI() {
        return IMEI;
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

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware= firmware;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }



}
