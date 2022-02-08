package com.example.cnctracking_2.data.model;


import android.os.Parcelable;

/**
 * Created by rd03 on 10/21/2015.
 */
public class Customer extends Person  implements Parcelable {
    private String corporateName;
    private String corporateBranchName;
    private String accountName;

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public String getCorporateBranchName() {
        return corporateBranchName;
    }

    public void setCorporateBranchName(String corporateBranchName) {
        this.corporateBranchName = corporateBranchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
