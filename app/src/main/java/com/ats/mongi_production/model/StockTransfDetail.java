package com.ats.mongi_production.model;

public class StockTransfDetail {

    private int stockTransDetailId;
    private int stockTransfHeaderId;
    private int itemId;
    private int catId;
    private int subCatId;
    private int menuId;
    private float qty;
    private float aprQty1;
    private float aprQty2;
    private float aprQty3;
    private float price;
    private float value;
    private int delStatus;
    private int exInt1;
    private int exInt2;
    private float exFloat1;
    private String exChar1;

    public StockTransfDetail() {
    }

    public StockTransfDetail(int stockTransDetailId, int stockTransfHeaderId, int itemId, int catId, int subCatId, int menuId, float qty, float aprQty1, float aprQty2, float aprQty3, float price, float value, int delStatus, int exInt1, int exInt2, float exFloat1, String exChar1) {
        this.stockTransDetailId = stockTransDetailId;
        this.stockTransfHeaderId = stockTransfHeaderId;
        this.itemId = itemId;
        this.catId = catId;
        this.subCatId = subCatId;
        this.menuId = menuId;
        this.qty = qty;
        this.aprQty1 = aprQty1;
        this.aprQty2 = aprQty2;
        this.aprQty3 = aprQty3;
        this.price = price;
        this.value = value;
        this.delStatus = delStatus;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exFloat1 = exFloat1;
        this.exChar1 = exChar1;
    }

    public int getStockTransDetailId() {
        return stockTransDetailId;
    }

    public void setStockTransDetailId(int stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }

    public int getStockTransfHeaderId() {
        return stockTransfHeaderId;
    }

    public void setStockTransfHeaderId(int stockTransfHeaderId) {
        this.stockTransfHeaderId = stockTransfHeaderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getAprQty1() {
        return aprQty1;
    }

    public void setAprQty1(float aprQty1) {
        this.aprQty1 = aprQty1;
    }

    public float getAprQty2() {
        return aprQty2;
    }

    public void setAprQty2(float aprQty2) {
        this.aprQty2 = aprQty2;
    }

    public float getAprQty3() {
        return aprQty3;
    }

    public void setAprQty3(float aprQty3) {
        this.aprQty3 = aprQty3;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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

    public float getExFloat1() {
        return exFloat1;
    }

    public void setExFloat1(float exFloat1) {
        this.exFloat1 = exFloat1;
    }

    public String getExChar1() {
        return exChar1;
    }

    public void setExChar1(String exChar1) {
        this.exChar1 = exChar1;
    }

    @Override
    public String toString() {
        return "StockTransfDetail{" +
                "stockTransDetailId=" + stockTransDetailId +
                ", stockTransfHeaderId=" + stockTransfHeaderId +
                ", itemId=" + itemId +
                ", catId=" + catId +
                ", subCatId=" + subCatId +
                ", menuId=" + menuId +
                ", qty=" + qty +
                ", aprQty1=" + aprQty1 +
                ", aprQty2=" + aprQty2 +
                ", aprQty3=" + aprQty3 +
                ", price=" + price +
                ", value=" + value +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exFloat1=" + exFloat1 +
                ", exChar1='" + exChar1 + '\'' +
                '}';
    }
}
