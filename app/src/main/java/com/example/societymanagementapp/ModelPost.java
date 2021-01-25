package com.example.societymanagementapp;

public class ModelPost {
    String name,responsiblity,doc_url;

    public ModelPost() {
    }

    public String getDoc_url() {
        return doc_url;
    }

    public void setDoc_url(String doc_url) {
        this.doc_url = doc_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponsiblity() {
        return responsiblity;
    }

    public void setResponsiblity(String responsiblity) {
        this.responsiblity = responsiblity;
    }

    public ModelPost(String name, String responsiblity,String doc_url) {
        this.name = name;
        this.responsiblity = responsiblity;
        this.doc_url=doc_url;
    }
}