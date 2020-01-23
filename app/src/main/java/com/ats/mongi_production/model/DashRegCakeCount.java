package com.ats.mongi_production.model;

public class DashRegCakeCount {

    private int status;
    private int regSpOrderCount;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRegSpOrderCount() {
        return regSpOrderCount;
    }

    public void setRegSpOrderCount(int regSpOrderCount) {
        this.regSpOrderCount = regSpOrderCount;
    }

    @Override
    public String toString() {
        return "DashRegCakeCount{" +
                "status=" + status +
                ", regSpOrderCount=" + regSpOrderCount +
                '}';
    }
}
