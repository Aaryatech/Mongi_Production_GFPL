package com.ats.mongi_production.model;

import java.util.List;

public class WorkerList {

    List<Employee> getEmpList;
    ErrorMessage errorMessage;

    public List<Employee> getGetEmpList() {
        return getEmpList;
    }

    public void setGetEmpList(List<Employee> getEmpList) {
        this.getEmpList = getEmpList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "WorkerList{" +
                "getEmpList=" + getEmpList +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
