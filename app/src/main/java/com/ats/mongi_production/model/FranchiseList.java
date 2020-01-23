package com.ats.mongi_production.model;

import java.util.List;

public class FranchiseList {

    List<Franchisee> franchiseeList;
    ErrorMessage errorMessage;

    public List<Franchisee> getFranchiseeList() {
        return franchiseeList;
    }

    public void setFranchiseeList(List<Franchisee> franchiseeList) {
        this.franchiseeList = franchiseeList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "FranchiseList{" +
                "franchiseeList=" + franchiseeList +
                ", errorMessage=" + errorMessage +
                '}';
    }
}

