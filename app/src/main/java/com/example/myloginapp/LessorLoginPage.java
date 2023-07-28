package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LessorLoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessor_login_page);
    }

    public void goToDashboardLessor(View view) {
        Intent intent = new Intent(this, LessorDashboard.class);
        startActivity(intent);

        overridePendingTransition( 0, R.anim.splash_fade_out);
    }
}