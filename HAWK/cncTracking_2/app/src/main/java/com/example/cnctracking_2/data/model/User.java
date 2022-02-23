package com.example.cnctracking_2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class User implements Parcelable {

    String  loginName;
    String password;
    String userRole;
    int userId;
    Map<String, String> allowedFeature;
    public final static String immobilizerAllow = "immobilizerAllow";
    public User() {
    }

    public User(String loginName, String password, String userRole, int userId, Map<String, String> allowedFeature) {
        this.loginName = loginName;
        this.password = password;
        this.userRole = userRole;
        this.userId = userId;
        this.allowedFeature = allowedFeature;
    }

    protected User(Parcel in) {
        loginName = in.readString();
        password = in.readString();
        userRole = in.readString();
        userId = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map<String, String> getAllowedFeature() {
        return allowedFeature;
    }

    public void setAllowedFeature(Map<String, String> allowedFeature) {
        this.allowedFeature = allowedFeature;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(loginName);
        dest.writeString(password);
        dest.writeString(userRole);
        dest.writeInt(userId);
    }
}
