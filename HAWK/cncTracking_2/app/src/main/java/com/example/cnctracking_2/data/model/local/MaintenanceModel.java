package com.example.cnctracking_2.data.model.local;

public class MaintenanceModel {

    private int id;
    private String title;
    private String dueOn;
    private String remainingDays;
    private boolean isShowMarkAction;

    public MaintenanceModel(int id, String title, String dueOn, String remainingDays, boolean isShowMarkAction) {
        this.id = id;
        this.title = title;
        this.dueOn = dueOn;
        this.remainingDays = remainingDays;
        this.isShowMarkAction = isShowMarkAction;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDueOn() {
        return dueOn;
    }

    public String getRemainingDays() {
        return remainingDays;
    }

    public boolean isShowMarkAction() {
        return isShowMarkAction;
    }

    public void setShowMarkAction(boolean showMarkAction) {
        isShowMarkAction = showMarkAction;
    }
}
