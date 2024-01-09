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

import com.example.myloginapp.PaymentResponse;
import com.example.myloginapp.PaymentViewHolder;
import com.example.myloginapp.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentViewHolder> {

    private List<PaymentResponse> payments; // Change the generic type here
    private Context context;

    public PaymentAdapter(Context context, List<PaymentResponse> payments) {
        this.payments = payments;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewpayment, parent, false);
        return new PaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentResponse payment = payments.get(position);

        String imageData = payment.getImage();

        if (imageData != null) {
            byte[] imageDataBytes = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.length);
            holder.screenshot.setImageBitmap(imageBitmap);
        } else {
            holder.screenshot.setImageResource(R.drawable.background_imagesquare);
        }
        holder.status.setText(payment.getStatus());
        holder.lessor.setText("Lessor: " + payment.getLessor());
        holder.date.setText(payment.getDate());
        holder.payment.setText("Payment ID: " + payment.getPaymentId());

        if ("Failed".equalsIgnoreCase(payment.getStatus())) {
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.leasedTextColor));
        } else if ("Success".equalsIgnoreCase(payment.getStatus())) {
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.availableTextColor));
        }
    }

    @Override
    public int getItemCount() {
        return payments != null ? payments.size() : 0;
    }
}
