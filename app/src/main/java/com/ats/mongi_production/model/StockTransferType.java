package com.ats.mongi_production.model;

public class StockTransferType {

    private int stockTransfTypeId;
    private String name;
    private String typeDesc;
    private String image1;
    private String image2;
    private int type;
    private int delStatus;
    private int exInt1;
    private int exInt2;
    private int exVar1;
    private int exVar2;
    private boolean checkedStatus;


    public int getStockTransfTypeId() {
        return stockTransfTypeId;
    }

    public void setStockTransfTypeId(int stockTransfTypeId) {
        this.stockTransfTypeId = stockTransfTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getExInt1() {
        return exInt1;
    }

    public void setExInt1(int exInt1) {
        this.exInt1 = exInt1;
    }

    public int getExInt2() {
        return exInt2;
    }

    public void setExInt2(int exInt2) {
        this.exInt2 = exInt2;
    }

    public int getExVar1() {
        return exVar1;
    }

    public void setExVar1(int exVar1) {
        this.exVar1 = exVar1;
    }

    public int getExVar2() {
        return exVar2;
    }

    public void setExVar2(int exVar2) {
        this.exVar2 = exVar2;
    }

    public boolean isCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
    }

    @Override
    public String toString() {
        return "StockTransferType{" +
                "stockTransfTypeId=" + stockTransfTypeId +
                ", name='" + name + '\'' +
                ", typeDesc='" + typeDesc + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", type=" + type +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1=" + exVar1 +
                ", exVar2=" + exVar2 +
                ", checkedStatus=" + checkedStatus +
                '}';
    }
}
