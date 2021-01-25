package com.example.societymanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class SocietyList extends AppCompatActivity {
    public ListView listView;
    String[] listItem;
    FirebaseDatabase database;
    DatabaseReference reference;
    Society society;
    RecyclerView recyclerView;
    RecyclerSociety myadapter;
    Window window;
    int billPerMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_list);
        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryDark));
        }
        setTitle("Society List");
        society=new Society();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Society> options =
                new FirebaseRecyclerOptions.Builder<Society>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Society_details"), Society.class)
                        .build();
        myadapter = new RecyclerSociety(options);
        recyclerView.setAdapter(myadapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myadapter.stopListening();
    }
}