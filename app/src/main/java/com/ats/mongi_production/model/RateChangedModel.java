package com.ats.mongi_production.model;

public class RateChangedModel {

    private int tSpCakeSupNo;
    private String frName;
    private String frCode;
    private String prodDate;
    private String spName;
    private String spCode;


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

    public String getProdDate() {
        return prodDate;
    }

    public void setProdDate(String prodDate) {
        this.prodDate = prodDate;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    @Override
    public String toString() {
        return "RateChangedModel{" +
                "tSpCakeSupNo=" + tSpCakeSupNo +
                ", frName='" + frName + '\'' +
                ", frCode='" + frCode + '\'' +
                ", prodDate='" + prodDate + '\'' +
                ", spName='" + spName + '\'' +
                ", spCode='" + spCode + '\'' +
                '}';
    }
}
