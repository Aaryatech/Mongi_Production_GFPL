package com.ats.mongi_production.model;

import java.util.List;

public class GeneratedBillList {

    List<GenerateBill> generateBills;
    Info info;

    public List<GenerateBill> getGenerateBills() {
        return generateBills;
    }

    public void setGenerateBills(List<GenerateBill> generateBills) {
        this.generateBills = generateBills;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "GeneratedBillList{" +
                "generateBills=" + generateBills +
                ", info=" + info +
                '}';
    }
}
