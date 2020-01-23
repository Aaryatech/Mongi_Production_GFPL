package com.ats.mongi_production.model;

public class GateSaleCount {

    private int subCatId;
    private int catId;
    private String subCatName;
    private String catName;
    private float inQty;
    private float saleQty;
    private float opQty;

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public float getInQty() {
        return inQty;
    }

    public void setInQty(float inQty) {
        this.inQty = inQty;
    }

    public float getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(float saleQty) {
        this.saleQty = saleQty;
    }

    public float getOpQty() {
        return opQty;
    }

    public void setOpQty(float opQty) {
        this.opQty = opQty;
    }

    @Override
    public String toString() {
        return "GateSaleCount{" +
                "subCatId=" + subCatId +
                ", catId=" + catId +
                ", subCatName='" + subCatName + '\'' +
                ", catName='" + catName + '\'' +
                ", inQty=" + inQty +
                ", saleQty=" + saleQty +
                ", opQty=" + opQty +
                '}';
    }
}
