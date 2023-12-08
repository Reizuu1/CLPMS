package com.example.myloginapp.lessor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myloginapp.MainActivity;
import com.example.myloginapp.R;
import com.example.myloginapp.databinding.ActivityLessorDashboardBinding;
import com.example.myloginapp.utilities.Constants;
import com.example.myloginapp.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class LessorDashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    LessorHomeFragment lessorHomeFragment = new LessorHomeFragment();
    LessorPropertyFragment lessorPropertyFragment = new LessorPropertyFragment();
    LessorLesseeFragment lessorLesseeFragment = new LessorLesseeFragment();
    LessorReminderFragment lessorReminderFragment = new LessorReminderFragment();
    LessorProfileFragment lessorProfileFragment = new LessorProfileFragment();

    private PreferenceManager preferenceManager;
    private ActivityLessorDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessorDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        bottomNavigationView = findViewById(R.id.bottomNavigationViewLessor);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, lessorHomeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, lessorHomeFragment).commit();
                        return true;
                    case R.id.property:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, lessorPropertyFragment).commit();
                        return true;
                    case R.id.lessee:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, lessorLesseeFragment).commit();
                        return true;
                    case R.id.reminder:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, lessorReminderFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, lessorProfileFragment).commit();
                        return true;
                }

                return false;
            }
        });
        getToken();
    }
        private void showToast(String message) {
            Toast.makeText(getApplicationContext(), message ,Toast.LENGTH_SHORT).show();
        }
        private void getToken(){
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
        }
        private void updateToken(String token) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference documentReference =
                    database.collection(Constants.KEY_COLLECTION_LESSORUSERS).document(
                            preferenceManager.getString(Constants.KEY_USER_ID)
                    );
            documentReference.update(Constants.KEY_FCM_TOKEN,token);
        }
        public void signOutLessor() {
            showToast("Signing out...");
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference documentReference =
                    database.collection(Constants.KEY_COLLECTION_LESSORUSERS).document(
                            preferenceManager.getString(Constants.KEY_USER_ID)
                    );
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(updates)
                    .addOnSuccessListener(unused -> {
                        preferenceManager.clear();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> showToast("Unable to sign out"));
        }
}