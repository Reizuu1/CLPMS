package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, R.anim.splash_fade_out);
    }
}
