package com.example.myloginapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myloginapp.PropertyResponse;
import com.example.myloginapp.PropertyViewHolder;
import com.example.myloginapp.R;

import java.util.List;

public class LessorProperty extends RecyclerView.Adapter<PropertyViewHolder> {
    private List<PropertyResponse> properties;
    private Context context;
    private PropertyAdapter.PropertyClickListener propertyClickListener;



    public LessorProperty(Context context, List<PropertyResponse> properties) {
        this.properties = properties;
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        return new PropertyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        PropertyResponse property = properties.get(position);

        String imageData = property.getImage().getData();

        if (imageData != null) {
            byte[] imageDataBytes = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.length);
            holder.propertyImage.setImageBitmap(imageBitmap);
        } else {
            holder.propertyImage.setImageResource(R.drawable.bg);
        }

        holder.propertyName.setText(property.getPropertyname());
        holder.Lessor.setText("Lessee : " + property.getLessee());
        holder.Status.setText(property.getStatus());
        holder.Location.setText(property.getBarangay() + ", " + property.getStreet());

        if ("leased".equalsIgnoreCase(property.getStatus())) {
            holder.Status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.leasedTextColor));
        } else if ("available".equalsIgnoreCase(property.getStatus())) {
            holder.Status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.availableTextColor));
        }
    }
    @Override
    public int getItemCount() {
        return properties != null ? properties.size() : 0;
    }
}
