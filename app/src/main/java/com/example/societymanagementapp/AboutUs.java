package com.example.societymanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

public class AboutUs extends AppCompatActivity {

    Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("About US");
        setContentView(R.layout.activity_about_us);
        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryDark));
        }

    }
}