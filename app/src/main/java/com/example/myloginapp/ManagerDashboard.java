package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ManagerDashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ManagerHomeFragment managerHomeFragment = new ManagerHomeFragment();
    ManagerPropertyFragment managerPropertyFragment = new ManagerPropertyFragment();
    ManagerLesseeFragment managerLesseeFragment = new ManagerLesseeFragment();
    ManagerReminderFragment managerReminderFragment = new ManagerReminderFragment();
    ManagerProfileFragment managerProfileFragment = new ManagerProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationViewManager);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,managerHomeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,managerHomeFragment).commit();
                        return true;
                    case R.id.property:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,managerPropertyFragment).commit();
                        return true;
                    case R.id.lessee:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,managerLesseeFragment).commit();
                        return true;
                    case R.id.reminder:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,managerReminderFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,managerProfileFragment).commit();
                        return true;
                }

                return false;
            }
        });

    }
}