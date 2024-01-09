package com.example.myloginapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentViewHolder extends RecyclerView.ViewHolder {

    public ImageView screenshot;
    public TextView lessor, status, date, payment;

    public PaymentViewHolder(@NonNull View paymentView) {
        super(paymentView);
        screenshot = paymentView.findViewById(R.id.imageView4);
        lessor = paymentView.findViewById(R.id.textView17);
        status = paymentView.findViewById(R.id.status);
        date = paymentView.findViewById(R.id.textView18);
        payment = paymentView.findViewById(R.id.payment);

    }
}