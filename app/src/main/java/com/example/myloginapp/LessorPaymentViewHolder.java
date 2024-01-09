package com.example.myloginapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

public class LessorPaymentViewHolder extends RecyclerView.ViewHolder{
    public ImageView screenshot;
  public TextView lessee, status, date, title, question, id;
  public AppCompatButton decline, accept;
    public LessorPaymentViewHolder(@NonNull View paymentView) {
        super(paymentView);
        screenshot = paymentView.findViewById(R.id.propertyimage);
        lessee = paymentView.findViewById(R.id.lessorname);
        status = paymentView.findViewById(R.id.status);
        date = paymentView.findViewById(R.id.date);
        decline = paymentView.findViewById(R.id.LogoutButton);
        accept = paymentView.findViewById(R.id.LogoutButton1);
        title = paymentView.findViewById(R.id.propertyname);
        question = paymentView.findViewById(R.id.payment);
        id = paymentView.findViewById(R.id.paymentid);
    }
}
