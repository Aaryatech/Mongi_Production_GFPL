package com.ats.mongi_production.model;

public class FrWiseSpCakeOrd {

    private int tSpCakeSupNo;
    private String frName;
    private String frCode;
    private String spCode;
    private String spName;
    private int inputKgFr;
    private int inputKgProd;
    private float weightDiff;

    public int gettSpCakeSupNo() {
        return tSpCakeSupNo;
    }

    public void settSpCakeSupNo(int tSpCakeSupNo) {
        this.tSpCakeSupNo = tSpCakeSupNo;
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

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
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

    @Override
    public String toString() {
        return "FrWiseSpCakeOrd{" +
                "tSpCakeSupNo=" + tSpCakeSupNo +
                ", frName='" + frName + '\'' +
                ", frCode='" + frCode + '\'' +
                ", spCode='" + spCode + '\'' +
                ", spName='" + spName + '\'' +
                ", inputKgFr=" + inputKgFr +
                ", inputKgProd=" + inputKgProd +
                ", weightDiff=" + weightDiff +
                '}';
    }
}
