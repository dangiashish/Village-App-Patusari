package com.jhunjhunu.patusari.Adapters;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jhunjhunu.patusari.R;
import com.jhunjhunu.patusari.modelClass.vehicleModel;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;

import static android.Manifest.permission.CALL_PHONE;

public class vehicleAdapter extends FirebaseRecyclerAdapter<vehicleModel, vehicleAdapter.myViewHolder> {

    public vehicleAdapter(@NonNull FirebaseRecyclerOptions<vehicleModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final vehicleModel model) {
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.owner.setText(model.getOwner());
        Glide.with(holder.image.getContext()).load(model.getImg()).into(holder.image);

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_vehicle_singlerow, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, phone, owner;
        FloatingActionButton call, whatsapp;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgVehicle);
            name = (TextView) itemView.findViewById(R.id.textVehicleName);
            phone = (TextView) itemView.findViewById(R.id.textVehiclePhone);
            owner = (TextView)itemView.findViewById(R.id.textOwnerName);
            call = (FloatingActionButton) itemView.findViewById(R.id.fab_call);
            whatsapp = (FloatingActionButton) itemView.findViewById(R.id.fab_whatsapp);


        }
    }





}
