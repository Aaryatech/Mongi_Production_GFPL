package com.ats.mongi_production.model;

public class Info {

    private String message;
    private boolean isError;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    @Override
    public String toString() {
        return "Info{" +
                "message='" + message + '\'' +
                ", isError=" + isError +
                '}';
    }
}
