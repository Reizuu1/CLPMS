package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LesseeLoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_login_page);
    }

    public void goToDashboard(View view) {
        Intent intent = new Intent(this, LesseeDashboard.class);
        startActivity(intent);

        overridePendingTransition( 0, R.anim.splash_fade_out);
    }

}