package com.example.myloginapp.lessee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myloginapp.MainActivity;
import com.example.myloginapp.R;
import com.example.myloginapp.databinding.ActivityLesseeDashboardBinding;
import com.example.myloginapp.utilities.Constants;
import com.example.myloginapp.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class LesseeDashboard extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private ActivityLesseeDashboardBinding binding;


    BottomNavigationView bottomNavigationView;

    LesseeHomeFragment lesseeHomeFragment = new LesseeHomeFragment();
    LesseePropertyFragment lesseePropertyFragment = new LesseePropertyFragment();
    LesseeConcernFragment lesseeConcernFragment = new LesseeConcernFragment();
    LesseeProfileFragment lesseeProfileFragment = new LesseeProfileFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);
        binding = ActivityLesseeDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());


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
        getToken();
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message ,Toast.LENGTH_SHORT).show();
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
        );
                documentReference.update(Constants.KEY_FCM_TOKEN,token);
    }
    public void signOut() {
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
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