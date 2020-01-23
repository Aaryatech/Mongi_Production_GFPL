package com.ats.mongi_production.model;

import java.util.List;

public class RouteOrderData {

    private String frName;
    private String frCode;
    private int frId;
    private int routeId;
    List<RegSpOrd> regSpCakeOrdList;
    List<SpOrder> spCakeOrdList;
    List<SpOrder> spCakeAlbumOrdList;

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

    public int getFrId() {
        return frId;
    }

    public void setFrId(int frId) {
        this.frId = frId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public List<RegSpOrd> getRegSpCakeOrdList() {
        return regSpCakeOrdList;
    }

    public void setRegSpCakeOrdList(List<RegSpOrd> regSpCakeOrdList) {
        this.regSpCakeOrdList = regSpCakeOrdList;
    }

    public List<SpOrder> getSpCakeOrdList() {
        return spCakeOrdList;
    }

    public void setSpCakeOrdList(List<SpOrder> spCakeOrdList) {
        this.spCakeOrdList = spCakeOrdList;
    }

    public List<SpOrder> getSpCakeAlbumOrdList() {
        return spCakeAlbumOrdList;
    }

    public void setSpCakeAlbumOrdList(List<SpOrder> spCakeAlbumOrdList) {
        this.spCakeAlbumOrdList = spCakeAlbumOrdList;
    }

    @Override
    public String toString() {
        return "RouteOrderData{" +
                "frName='" + frName + '\'' +
                ", frCode='" + frCode + '\'' +
                ", frId=" + frId +
                ", routeId=" + routeId +
                ", regSpCakeOrdList=" + regSpCakeOrdList +
                ", spCakeOrdList=" + spCakeOrdList +
                ", spCakeAlbumOrdList=" + spCakeAlbumOrdList +
                '}';
    }
}
