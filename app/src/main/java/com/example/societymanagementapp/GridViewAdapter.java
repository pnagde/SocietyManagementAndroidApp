package com.example.societymanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    Context context;
    int logo[];
    String list[];
    LayoutInflater inflater;

    public GridViewAdapter(Context context, int logo[],String list[])
    {
        this.context = context;
        this.logo = logo;
        this.list=list;
        inflater= (LayoutInflater.from(context));
    }

    @Override
    public int getCount()
    {
        return logo.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        view = inflater.inflate(R.layout.grid_view_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textView =(TextView)view.findViewById(R.id.dashText);
        imageView.setImageResource(logo[position]);
        textView.setText(list[position]);
        return view;
    }

}
