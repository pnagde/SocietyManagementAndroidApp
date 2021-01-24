package com.example.societymanagementapp;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.List;

public class Adapter extends ArrayAdapter {
    private Activity mContext;
    List<String> bills;
    Adapter(Activity mContext,List<String> bills){
        super(mContext,R.layout.society_list,bills);
        this.mContext=mContext;
        this.bills=bills;
    }
}
