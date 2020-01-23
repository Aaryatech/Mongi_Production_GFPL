package com.ats.mongi_production.model;

public class MistryWiseReportModel {

    private int mistryId;
    private String empName;
    private int noOfCakes;
    private float timeRequired;
    private String prodDate;

    public int getMistryId() {
        return mistryId;
    }

    public void setMistryId(int mistryId) {
        this.mistryId = mistryId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getNoOfCakes() {
        return noOfCakes;
    }

    public void setNoOfCakes(int noOfCakes) {
        this.noOfCakes = noOfCakes;
    }

    public float getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(float timeRequired) {
        this.timeRequired = timeRequired;
    }

    public String getProdDate() {
        return prodDate;
    }

    public void setProdDate(String prodDate) {
        this.prodDate = prodDate;
    }

    @Override
    public String toString() {
        return "MistryWiseReportModel{" +
                "mistryId=" + mistryId +
                ", empName='" + empName + '\'' +
                ", noOfCakes=" + noOfCakes +
                ", timeRequired=" + timeRequired +
                ", prodDate='" + prodDate + '\'' +
                '}';
    }
}
