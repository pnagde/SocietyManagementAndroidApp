package com.example.societymanagementapp;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

public class Member {


    String name, address, email_address, password, gender, Home_Or_Plot, User_phoneNumber, plot_number, sector, role, post, area;
    String pic_url;

    public Member() {
    }

    public Member(String name, String address, String email_address, String password, String gender, String home_Or_Plot, String user_phoneNumber,
                  String plot_number, String sector, String role, String post, String area, String pic_url) {
        this.name = name;
        this.address = address;
        this.email_address = email_address;
        this.password = password;
        this.gender = gender;
        this.Home_Or_Plot = home_Or_Plot;
        this.User_phoneNumber = user_phoneNumber;
        this.plot_number = plot_number;
        this.sector = sector;
        this.role = role;
        this.post = post;
        this.area = area;
        this.pic_url = pic_url;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHome_Or_Plot() {
        return Home_Or_Plot;
    }

    public void setHome_Or_Plot(String home_Or_Plot) {
        Home_Or_Plot = home_Or_Plot;
    }

    public String getUser_phoneNumber() {
        return User_phoneNumber;
    }

    public void setUser_phoneNumber(String user_phoneNumber) {
        User_phoneNumber = user_phoneNumber;
    }

    public String getPlot_number() {
        return plot_number;
    }

    public void setPlot_number(String plot_number) {
        this.plot_number = plot_number;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
