package com.example.myloginapp.lessor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myloginapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LessorDashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    LessorHomeFragment lessorHomeFragment = new LessorHomeFragment();
    LessorPropertyFragment lessorPropertyFragment = new LessorPropertyFragment();
    LessorLesseeFragment lessorLesseeFragment = new LessorLesseeFragment();
    LessorReminderFragment lessorReminderFragment = new LessorReminderFragment();
    LessorProfileFragment lessorProfileFragment = new LessorProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessor_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationViewLessor);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,lessorHomeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,lessorHomeFragment).commit();
                        return true;
                    case R.id.property:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,lessorPropertyFragment).commit();
                        return true;
                    case R.id.lessee:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,lessorLesseeFragment).commit();
                        return true;
                    case R.id.reminder:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,lessorReminderFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,lessorProfileFragment).commit();
                        return true;
                }

                return false;
            }
        });

    }
}