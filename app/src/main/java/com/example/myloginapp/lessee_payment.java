package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class lessee_payment extends AppCompatActivity {

    private TextView send, history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_payment);

        send = findViewById(R.id.textview_personal_details);
        history = findViewById(R.id.textview_payment_history);


        send.setOnClickListener(v -> {
            Intent intent = new Intent(lessee_payment.this, lessee_sendpay.class);
            startActivity(intent);
        });
        history.setOnClickListener(v -> {
            Intent intent = new Intent(lessee_payment.this, recyclerview_payment.class);
            startActivity(intent);
        });
    }
}