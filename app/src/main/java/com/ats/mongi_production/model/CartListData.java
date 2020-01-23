package com.ats.mongi_production.model;

public class CartListData {

    private int id;
    private String itemId;
    private String itemName;
    private String itemImage;
    private double itemRate;
    private int quantity;

    private String catId;
    private String subCatId;
    private double itemRate1;
    private double itemRate2;
    private double itemRate3;


    public CartListData() {
    }

    public CartListData(int id, String itemId, String itemName, String itemImage, double itemRate, int quantity) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemRate = itemRate;
        this.quantity = quantity;
    }

    public CartListData(int id, String itemId, String itemName, String itemImage, double itemRate, int quantity, String catId, String subCatId, double itemRate1, double itemRate2, double itemRate3) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemRate = itemRate;
        this.quantity = quantity;
        this.catId = catId;
        this.subCatId = subCatId;
        this.itemRate1 = itemRate1;
        this.itemRate2 = itemRate2;
        this.itemRate3 = itemRate3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public double getItemRate() {
        return itemRate;
    }

    public void setItemRate(double itemRate) {
        this.itemRate = itemRate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public double getItemRate1() {
        return itemRate1;
    }

    public void setItemRate1(double itemRate1) {
        this.itemRate1 = itemRate1;
    }

    public double getItemRate2() {
        return itemRate2;
    }

    public void setItemRate2(double itemRate2) {
        this.itemRate2 = itemRate2;
    }

    public double getItemRate3() {
        return itemRate3;
    }

    public void setItemRate3(double itemRate3) {
        this.itemRate3 = itemRate3;
    }

    @Override
    public String toString() {
        return "CartListData{" +
                "id=" + id +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemImage='" + itemImage + '\'' +
                ", itemRate=" + itemRate +
                ", quantity=" + quantity +
                ", catId='" + catId + '\'' +
                ", subCatId='" + subCatId + '\'' +
                ", itemRate1=" + itemRate1 +
                ", itemRate2=" + itemRate2 +
                ", itemRate3=" + itemRate3 +
                '}';
    }
}
