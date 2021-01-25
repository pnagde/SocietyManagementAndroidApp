package com.example.societymanagementapp;

public class ExpModel {
    String opt,exName,exAmount,exDate,exDescrip;

    public ExpModel() {
    }

    public ExpModel(String opt, String exName, String exAmount, String exDate, String exDescrip) {
        this.opt = opt;
        this.exName = exName;
        this.exAmount = exAmount;
        this.exDate = exDate;
        this.exDescrip = exDescrip;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getExAmount() {
        return exAmount;
    }

    public void setExAmount(String exAmount) {
        this.exAmount = exAmount;
    }

    public String getExDate() {
        return exDate;
    }

    public void setExDate(String exDate) {
        this.exDate = exDate;
    }

    public String getExDescrip() {
        return exDescrip;
    }

    public void setExDescrip(String exDescrip) {
        this.exDescrip = exDescrip;
    }
}
