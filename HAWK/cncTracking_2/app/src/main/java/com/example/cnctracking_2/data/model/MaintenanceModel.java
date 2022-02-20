package com.example.cnctracking_2.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MaintenanceModel {

    @SerializedName("maintenanceDue")
    private List<MaintenanceDue> maintenanceDue;

    public List<MaintenanceDue> getMaintenanceDue() {
        return maintenanceDue;
    }

    public class MaintenanceDue {
        @SerializedName("regNo")
        private String regNo;

        @SerializedName("date")
        private String date;

        @SerializedName("threshold")
        private String threshold;

        @SerializedName("label")
        private String label;

        @SerializedName("remaining")
        private String remaining;

        @SerializedName("status")
        private String status;

        public String getRegNo() {
            return regNo;
        }

        public String getDate() {
            return date;
        }

        public String getThreshold() {
            return threshold;
        }

        public String getLabel() {
            return label;
        }

        public String getRemaining() {
            return remaining;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
