package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by rd03 on 10/21/2015.
 */
public class Person  {
    protected int code;

    protected String gender;
    protected String department;
    protected String designation;

    protected String firstName;
    protected String lastName;
    protected String middleName;
    protected String pstnNo;
    protected String cellNo;
    protected String newContactNo;
    protected String address;
    protected String newAddress;
    protected boolean invalidAddr;
    protected String cnicNo;
    protected Calendar birthDate;

    protected String emergencyName;
    protected String emergencyRelation;
    protected String emergencyPstnNo;
    protected String emergencyCellNo;

    protected String companyName;
    protected String companyAddress;
    protected String companyPstnNo;

    public Person() {
    }

    public Person(String firstName, String middleName, String lastName){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public String getCellNo() {
        return cellNo;
    }

    public String getCnicNo() {
        return cnicNo;
    }

    public int getCode() {
        return code;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyPstnNo() {
        return companyPstnNo;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public String getEmergencyCellNo() {
        return emergencyCellNo;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public String getEmergencyPstnNo() {
        return emergencyPstnNo;
    }

    public String getEmergencyRelation() {
        return emergencyRelation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGender() {
        return gender;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPstnNo() {
        return pstnNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyPstnNo(String companyPstnNo) {
        this.companyPstnNo = companyPstnNo;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setEmergencyCellNo(String emergencyCellNo) {
        this.emergencyCellNo = emergencyCellNo;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public void setEmergencyPstnNo(String emergencyPstnNo) {
        this.emergencyPstnNo = emergencyPstnNo;
    }

    public void setEmergencyRelation(String emergencyRelation) {
        this.emergencyRelation = emergencyRelation;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setPstnNo(String pstnNo) {
        this.pstnNo = pstnNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean equals(Object obj){
        if(obj instanceof Person){
            Person person = (Person)obj;
            return this.firstName.equalsIgnoreCase(person.firstName) &&
                    this.middleName.equalsIgnoreCase(person.middleName) &&
                    this.lastName.equalsIgnoreCase(person.lastName);
        }
        return false;
    }

    public String toString(){
        return ((this.firstName == null)? "" : this.firstName) +
                ((this.middleName == null)? "" : this.middleName) +
                ((this.lastName == null)? "" : this.lastName);
    }

    public String getNewContactNo() {
        return newContactNo;
    }

    public void setNewContactNo(String newContactNo) {
        this.newContactNo = newContactNo;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public boolean isInvalidAddr() {
        return invalidAddr;
    }

    public void setInvalidAddr(boolean invalidAddr) {
        this.invalidAddr = invalidAddr;
    }


}

