package com.example.societymanagementapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class ListAdaper extends ArrayAdapter {
    private Activity mContext;
    List<String> name;
    public ListAdaper(Activity mContext,List<String> name){
        super(mContext,R.layout.member_list,name);
        this.mContext=mContext;
        this.name=name;
    }
}
