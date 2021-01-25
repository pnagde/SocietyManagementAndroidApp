package com.example.societymanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleAdapter extends FirebaseRecyclerAdapter<Member, RecycleAdapter.MyViewHolder> {

    public RecycleAdapter(@NonNull FirebaseRecyclerOptions<Member> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Member member) {

        Glide.with(holder.imageView.getContext()).load(member.getPic_url()).into(holder.imageView);
        holder.name.setText(member.getName());
        holder.sector.setText(member.getSector());
        holder.plotNo.setText(member.getPlot_number());
        holder.area.setText(member.getArea());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, sector, plotNo, area;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.proImage);
            name = (TextView) itemView.findViewById(R.id.nameView);
            sector = (TextView) itemView.findViewById(R.id.sectorView);
            plotNo = (TextView) itemView.findViewById(R.id.plotView);
            area = (TextView) itemView.findViewById(R.id.areaView);
        }
    }
}
