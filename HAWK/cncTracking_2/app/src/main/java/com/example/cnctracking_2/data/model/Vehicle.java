package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by rd03 on 10/21/2015.
 */
public class Vehicle implements Parcelable {

    private int code;

    private String regNo;
    private String engineNo;
    private String chassisNo;
    private String salesNo;
    private  Unit unit;

    private String manufacturer;
    private String model;
    private String color;
    private String type;

    private Calendar letterDate;
    private Calendar contactDate;
    private Calendar complaintDate;
    private String status;

    private String instruction;
    private Calendar salesDate;

    private String contractCancel;
    private String deviceUninstall;

    private String owner;

    private String jammerDetector;

    private String fareacode;

    private String fareaname;

    private int idealRouteCode;

    private boolean nr;

    public boolean isNr() {
        return nr;
    }

    public void setNr(boolean nr) {
        this.nr = nr;
    }





    public int getIdealRouteCode() {
        return idealRouteCode;
    }

    public void setIdealRouteCode(int idealRouteCode) {
        this.idealRouteCode = idealRouteCode;
    }

    public String getFareaname() {
        return fareaname;
    }

    public void setFareaname(String fareaname) {
        this.fareaname = fareaname;
    }

    public String getFareacode() {
        return fareacode;
    }

    public void setFareacode(String fareacode) {
        this.fareacode = fareacode;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getCode() {
        return code;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public String getSalesNo() {
        return salesNo;
    }

    public Unit getUnit() {
        return unit;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Calendar getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(Calendar letterDate) {
        this.letterDate = letterDate;
    }

    public Calendar getContactDate() {
        return contactDate;
    }

    public void setContactDate(Calendar contactDate) {
        this.contactDate = contactDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Calendar getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Calendar salesDate) {
        this.salesDate = salesDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContractCancel() {
        return contractCancel;
    }

    public void setContractCancel(String contractCancel) {
        this.contractCancel = contractCancel;
    }

    public String getDeviceUninstall() {
        return deviceUninstall;
    }

    public void setDeviceUninstall(String deviceUninstall) {
        this.deviceUninstall = deviceUninstall;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Calendar getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(Calendar complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getJammerDetector() {
        return jammerDetector;
    }

    public void setJammerDetector(String jammerDetector) {
        this.jammerDetector = jammerDetector;
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
}
