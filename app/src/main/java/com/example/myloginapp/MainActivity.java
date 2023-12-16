package com.example.myloginapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, R.anim.splash_fade_out);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit confirmation");
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", (dialog, id) -> {
            finishAffinity();
        });

        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.setOnShowListener(dialogInterface -> {
            Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);

            positiveButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            negativeButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        });

        alert.show();
    }


}
