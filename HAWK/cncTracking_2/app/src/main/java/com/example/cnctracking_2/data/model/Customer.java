package com.example.cnctracking_2.data.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rd03 on 10/21/2015.
 */
public class Customer  implements Parcelable {

    protected String firstName;

    public Customer() {
    }
    public Customer(Parcel in) {
        firstName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
