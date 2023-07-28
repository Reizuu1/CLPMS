package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ManagerLoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login_page);
    }
    public void goToDashboardManager(View view) {
        Intent intent = new Intent(this, ManagerDashboard.class);
        startActivity(intent);

        overridePendingTransition( 0, R.anim.splash_fade_out);
    }
}