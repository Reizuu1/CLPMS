package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myloginapp.lessee.LesseeLoginPage;
import com.example.myloginapp.lessor.LessorLoginPage;
import com.example.myloginapp.manager.ManagerLoginPage;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

    }

    public void LoginAsLessee(View view) {
        Intent intent = new Intent(this, LesseeLoginPage.class);
        startActivity(intent);
        finish();
        overridePendingTransition( 0, R.anim.splash_fade_out);

    }

    public void LoginAsLessor(View view) {
        Intent intent = new Intent(this, LessorLoginPage.class);
        startActivity(intent);
        finish();
        overridePendingTransition( 0, R.anim.splash_fade_out);

    }

    public void LoginAsManager(View view) {
        Intent intent = new Intent(this, ManagerLoginPage.class);
        startActivity(intent);
        finish();
        overridePendingTransition( 0, R.anim.splash_fade_out);

    }
}