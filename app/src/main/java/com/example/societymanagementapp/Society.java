package com.example.societymanagementapp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Society {
    public Society() {
    }
    private String fullName,mobileNumber,society_pic_url,cityName,societyAddress,pinCode,emailIdSociety,billPerMonth,startMonth,userStatus,year,month;

    public Society(String fullName, String mobileNumber, String society_pic_url, String cityName,
                   String societyAddress, String pinCode, String emailIdSociety, String billPerMonth,
                   String startMonth, String userStatus,String year,String month) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.society_pic_url = society_pic_url;
        this.cityName = cityName;
        this.societyAddress = societyAddress;
        this.pinCode = pinCode;
        this.emailIdSociety = emailIdSociety;
        this.billPerMonth = billPerMonth;
        this.startMonth = startMonth;
        this.userStatus = userStatus;
        this.year=year;
        this.month=month;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSociety_pic_url() {
        return society_pic_url;
    }

    public void setSociety_pic_url(String society_pic_url) {
        this.society_pic_url = society_pic_url;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSocietyAddress() {
        return societyAddress;
    }

    public void setSocietyAddress(String societyAddress) {
        this.societyAddress = societyAddress;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getEmailIdSociety() {
        return emailIdSociety;
    }

    public void setEmailIdSociety(String emailIdSociety) {
        this.emailIdSociety = emailIdSociety;
    }

    public String getBillPerMonth() {
        return billPerMonth;
    }

    public void setBillPerMonth(String billPerMonth) {
        this.billPerMonth = billPerMonth;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount()
    {
    String  total;
    double bpm=Integer.parseInt(billPerMonth);
    int mm=Integer.parseInt(month);
    int yy=Integer.parseInt(year);
        Calendar c = Calendar.getInstance();
        Calendar birthDay = new GregorianCalendar(yy, mm, 15);
        Calendar today =
            new GregorianCalendar();
        today.setTime(new Date());
            int yearsInBetween = today.get(Calendar.YEAR) -
            birthDay.get(Calendar.YEAR);
            int monthsDiff = today.get(Calendar.MONTH) - birthDay.get(Calendar.MONTH);
            long ageInMonths = yearsInBetween*12 + monthsDiff;
            long age = yearsInBetween;
        total=String.valueOf((double)bpm*(ageInMonths+1)+" INR");
        return total;
    }

}
