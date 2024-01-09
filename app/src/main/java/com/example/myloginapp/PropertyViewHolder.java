package com.example.myloginapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PropertyViewHolder extends RecyclerView.ViewHolder {


    public ImageView propertyImage;
    public TextView propertyName, Lessor, Status, Location;
    public CardView cardview;

    public PropertyViewHolder(@NonNull View propertyView) {
        super(propertyView);
        propertyImage = propertyView.findViewById(R.id.propertyimage);
        propertyName = propertyView.findViewById(R.id.propertyname);
        Lessor = propertyView.findViewById(R.id.lessorname);
        Status = propertyView.findViewById(R.id.status);
        Location = propertyView.findViewById(R.id.location);
        cardview = propertyView.findViewById(R.id.property_container);


    }
}
