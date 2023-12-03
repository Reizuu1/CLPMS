package com.example.myloginapp.lessee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myloginapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LesseeDashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    LesseeHomeFragment lesseeHomeFragment = new LesseeHomeFragment();
    LesseePropertyFragment lesseePropertyFragment = new LesseePropertyFragment();
    LesseeConcernFragment lesseeConcernFragment = new LesseeConcernFragment();
    LesseeProfileFragment lesseeProfileFragment = new LesseeProfileFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationViewLessee);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,lesseeHomeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,lesseeHomeFragment).commit();
                    return true;
                case R.id.property:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,lesseePropertyFragment).commit();
                    return true;
                case R.id.concern:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,lesseeConcernFragment).commit();
                    return true;
                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,lesseeProfileFragment).commit();
                    return true;
            }

            return false;
            }
        });

    }
}