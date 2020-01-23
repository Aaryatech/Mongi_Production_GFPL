package com.ats.mongi_production.model;

import java.util.List;

public class StockTransferHeader {

    private int stockTransfHeaderId;
    private String stockDate;
    private int type;
    private int userId;
    private int aprUserId1;
    private int aprUserId2;
    private int aprUserId3;
    private String aprDate1;
    private String aprDate2;
    private String aprDate3;
    private int frId;
    private String frName;
    private int stockStatus;
    private int delStatus;
    private int exInt;
    private String exVar;
    private float exFloat;
    List<StockTransfDetail> stockTransfDetailList;

    public StockTransferHeader() {
    }

    public StockTransferHeader(int stockTransfHeaderId, String stockDate, int type, int userId, int aprUserId1, int aprUserId2, int aprUserId3, String aprDate1, String aprDate2, String aprDate3, int frId, String frName, int stockStatus, int delStatus, int exInt, String exVar, float exFloat, List<StockTransfDetail> stockTransfDetailList) {
        this.stockTransfHeaderId = stockTransfHeaderId;
        this.stockDate = stockDate;
        this.type = type;
        this.userId = userId;
        this.aprUserId1 = aprUserId1;
        this.aprUserId2 = aprUserId2;
        this.aprUserId3 = aprUserId3;
        this.aprDate1 = aprDate1;
        this.aprDate2 = aprDate2;
        this.aprDate3 = aprDate3;
        this.frId = frId;
        this.frName = frName;
        this.stockStatus = stockStatus;
        this.delStatus = delStatus;
        this.exInt = exInt;
        this.exVar = exVar;
        this.exFloat = exFloat;
        this.stockTransfDetailList = stockTransfDetailList;
    }

    public int getStockTransfHeaderId() {
        return stockTransfHeaderId;
    }

    public void setStockTransfHeaderId(int stockTransfHeaderId) {
        this.stockTransfHeaderId = stockTransfHeaderId;
    }

    public String getStockDate() {
        return stockDate;
    }

    public void setStockDate(String stockDate) {
        this.stockDate = stockDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAprUserId1() {
        return aprUserId1;
    }

    public void setAprUserId1(int aprUserId1) {
        this.aprUserId1 = aprUserId1;
    }

    public int getAprUserId2() {
        return aprUserId2;
    }

    public void setAprUserId2(int aprUserId2) {
        this.aprUserId2 = aprUserId2;
    }

    public int getAprUserId3() {
        return aprUserId3;
    }

    public void setAprUserId3(int aprUserId3) {
        this.aprUserId3 = aprUserId3;
    }

    public String getAprDate1() {
        return aprDate1;
    }

    public void setAprDate1(String aprDate1) {
        this.aprDate1 = aprDate1;
    }

    public String getAprDate2() {
        return aprDate2;
    }

    public void setAprDate2(String aprDate2) {
        this.aprDate2 = aprDate2;
    }

    public String getAprDate3() {
        return aprDate3;
    }

    public void setAprDate3(String aprDate3) {
        this.aprDate3 = aprDate3;
    }

    public int getFrId() {
        return frId;
    }

    public void setFrId(int frId) {
        this.frId = frId;
    }

    public String getFrName() {
        return frName;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }

    public int getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(int stockStatus) {
        this.stockStatus = stockStatus;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getExInt() {
        return exInt;
    }

    public void setExInt(int exInt) {
        this.exInt = exInt;
    }

    public String getExVar() {
        return exVar;
    }

    public void setExVar(String exVar) {
        this.exVar = exVar;
    }

    public float getExFloat() {
        return exFloat;
    }

    public void setExFloat(float exFloat) {
        this.exFloat = exFloat;
    }

    public List<StockTransfDetail> getStockTransfDetailList() {
        return stockTransfDetailList;
    }

    public void setStockTransfDetailList(List<StockTransfDetail> stockTransfDetailList) {
        this.stockTransfDetailList = stockTransfDetailList;
    }

    @Override
    public String toString() {
        return "StockTransferHeader{" +
                "stockTransfHeaderId=" + stockTransfHeaderId +
                ", stockDate='" + stockDate + '\'' +
                ", type=" + type +
                ", userId=" + userId +
                ", aprUserId1=" + aprUserId1 +
                ", aprUserId2=" + aprUserId2 +
                ", aprUserId3=" + aprUserId3 +
                ", aprDate1='" + aprDate1 + '\'' +
                ", aprDate2='" + aprDate2 + '\'' +
                ", aprDate3='" + aprDate3 + '\'' +
                ", frId=" + frId +
                ", frName='" + frName + '\'' +
                ", stockStatus=" + stockStatus +
                ", delStatus=" + delStatus +
                ", exInt=" + exInt +
                ", exVar='" + exVar + '\'' +
                ", exFloat=" + exFloat +
                ", stockTransfDetailList=" + stockTransfDetailList +
                '}';
    }
}
