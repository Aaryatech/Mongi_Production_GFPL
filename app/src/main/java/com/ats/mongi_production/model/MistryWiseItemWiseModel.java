package com.ats.mongi_production.model;

public class MistryWiseItemWiseModel {

    private int ordId;
    private String frName;
    private String frCode;
    private String itemCode;
    private String itemName;
    private int inputKgFr;
    private int inputKgProd;
    private float weightDiff;
    private String empName;
    private Long startTimeStamp;
    private Long endTimeStamp;

    public int getOrdId() {
        return ordId;
    }

    public void setOrdId(int ordId) {
        this.ordId = ordId;
    }

    public String getFrName() {
        return frName;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }

    public String getFrCode() {
        return frCode;
    }

    public void setFrCode(String frCode) {
        this.frCode = frCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getInputKgFr() {
        return inputKgFr;
    }

    public void setInputKgFr(int inputKgFr) {
        this.inputKgFr = inputKgFr;
    }

    public int getInputKgProd() {
        return inputKgProd;
    }

    public void setInputKgProd(int inputKgProd) {
        this.inputKgProd = inputKgProd;
    }

    public float getWeightDiff() {
        return weightDiff;
    }

    public void setWeightDiff(float weightDiff) {
        this.weightDiff = weightDiff;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    @Override
    public String toString() {
        return "MistryWiseItemWiseModel{" +
                "ordId=" + ordId +
                ", frName='" + frName + '\'' +
                ", frCode='" + frCode + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", inputKgFr=" + inputKgFr +
                ", inputKgProd=" + inputKgProd +
                ", weightDiff=" + weightDiff +
                ", empName='" + empName + '\'' +
                ", startTimeStamp=" + startTimeStamp +
                ", endTimeStamp=" + endTimeStamp +
                '}';
    }
}
