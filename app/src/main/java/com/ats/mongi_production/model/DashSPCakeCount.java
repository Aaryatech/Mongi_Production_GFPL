package com.ats.mongi_production.model;

public class DashSPCakeCount {

    private int status;
    private int spOrderCount;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSpOrderCount() {
        return spOrderCount;
    }

    public void setSpOrderCount(int spOrderCount) {
        this.spOrderCount = spOrderCount;
    }

    @Override
    public String toString() {
        return "DashSPCakeCount{" +
                "status=" + status +
                ", spOrderCount=" + spOrderCount +
                '}';
    }
}
