package com.example.societymanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerSociety extends FirebaseRecyclerAdapter<Society, RecyclerSociety.MyViewHolder> {
    public RecyclerSociety(@NonNull FirebaseRecyclerOptions<Society> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerSociety.MyViewHolder holder, int position, @NonNull Society model) {
        Glide.with(holder.imageView.getContext()).load(model.getSociety_pic_url()).into(holder.imageView);
        holder.nameSociety.setText(model.getFullName());
        holder.bills.setText(model.getAmount());


    }

    @NonNull
    @Override
    public RecyclerSociety.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.society, parent, false);
        return new MyViewHolder(view);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView bills,nameSociety;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.proImage);
            nameSociety=itemView.findViewById(R.id.nameView);
            bills=itemView.findViewById(R.id.bill);

        }
    }
}
