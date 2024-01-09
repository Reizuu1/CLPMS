package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class PropertyDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        String propertyName = getIntent().getStringExtra("propertyname");
        String lessor = getIntent().getStringExtra("lessor");
        String status = getIntent().getStringExtra("status");
        String location = getIntent().getStringExtra("address");
        String floors = getIntent().getStringExtra("floors");
        String floorOcu = getIntent().getStringExtra("floorOcu");
        String lessee = getIntent().getStringExtra("lessee");
        String unit = getIntent().getStringExtra("unit");
        String imageReference = getIntent().getStringExtra("image");

        TextView propertyNameTextView = findViewById(R.id.propertyname);
        TextView lessorTextView = findViewById(R.id.namelessor);
        TextView statusTextView = findViewById(R.id.statustext);
        TextView locationTextView = findViewById(R.id.location);
        TextView floorsTextView = findViewById(R.id.floors);
        TextView floorOcuTextView = findViewById(R.id.floorsocu);
        TextView lesseeTextView = findViewById(R.id.lessee);
        TextView unitTextView = findViewById(R.id.unit);
        ImageView propertyImageView = findViewById(R.id.propertyimage);

        propertyNameTextView.setText(propertyName);
        lessorTextView.setText(getString(R.string.lessor_label) + lessor);
        statusTextView.setText(status);
        locationTextView.setText(location);
        floorsTextView.setText(floors);
        floorOcuTextView.setText(floorOcu);
        lesseeTextView.setText(getString(R.string.lessee_label) + lessee);
        unitTextView.setText(getString(R.string.unit) + unit);

        // Check if the image reference is not empty before loading
        if (imageReference != null && !imageReference.isEmpty()) {
            // Load the image using Picasso
            Picasso.get().load(imageReference).into(propertyImageView);
        } else {
            propertyImageView.setImageResource(R.drawable.rectangle_10);
        }
    }
}
