package com.example.myloginapp.lessee;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myloginapp.adminsignup;
import com.example.myloginapp.api.ApiEndpoints;
import com.example.myloginapp.R;
import com.example.myloginapp.databinding.ActivityLesseeLoginPageBinding;
import com.example.myloginapp.utilities.Constants;
import com.example.myloginapp.utilities.PreferenceManager;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LesseeLoginPage extends AppCompatActivity {
    private EditText username, password;
    private AppCompatButton signIn;
    private PreferenceManager preferenceManager;
    private ActivityLesseeLoginPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_login_page);
        binding = ActivityLesseeLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), LesseeDashboard.class);
            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.LesseeUsername);
        password = findViewById(R.id.LesseePassword);
        signIn = findViewById(R.id.login_lessee);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check valid username and password

                String getUsername = username.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                if (isValidSignIn()) {
                    signIn();
                    checkUsernameAndPassword(getUsername,getPassword);
                }
            }
        });
    }

    private void checkUsernameAndPassword(String username, String password) {
        if (username.isEmpty()) {
            Toast.makeText(LesseeLoginPage.this, "Please enter your username", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(LesseeLoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else if (!username.isEmpty() && !password.isEmpty()) {
            validateCredentials(username, password);
        }
    }
    private void validateCredentials(String username, String password) {
        if (username.equals("admin") && password.equals("admin")) {
            Intent intent = new Intent(LesseeLoginPage.this, adminsignup.class);
            startActivity(intent);
        } else {
            performNetworkValidation(username, password);
        }
    }
    private void performNetworkValidation(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ap-southeast-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<LesseeResponse> call = apiEndpoints.signinLessee(requestBody);

        call.enqueue(new Callback<LesseeResponse>() {
            @Override
            public void onResponse(Call<LesseeResponse> call, Response<LesseeResponse> response) {
                LesseeResponse createReponse = response.body();
                if (response.code() == 200) {

                    String userId = createReponse.getId();
                    String password = createReponse.getPassword();
                    String username = createReponse.getUsername();
                    String contactNumber = createReponse.getContactnumber();

                    storeUserData(userId, password, username, contactNumber);
                    Toast.makeText(LesseeLoginPage.this, "Successfully login", Toast.LENGTH_SHORT).show();
                    overridePendingTransition(0, R.anim.splash_fade_out);


                    // Finish the current SignIn activity
                    finish();
                    Intent intent = new Intent(getApplicationContext(),LesseeDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(LesseeLoginPage.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LesseeResponse> call, Throwable t) {
                Toast.makeText(LesseeLoginPage.this, "Server failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserData(String userId, String password, String username, String contactNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", userId);
        editor.putString("password", password);
        editor.putString("username", username);
        editor.putString("contact_number", contactNumber);

        editor.apply();
    }
    private Boolean isValidSignIn(){
        if (binding.LesseeUsername.getText().toString().trim().isEmpty()) {
            Toast.makeText(LesseeLoginPage.this, "Please enter your username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.LesseePassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(LesseeLoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }
    private void loading (Boolean isLoading) {
        if (isLoading) {
            binding.loginLessee.setVisibility(View.INVISIBLE);
            binding.lesseeProgressbar.setVisibility(View.VISIBLE);
        } else {
            binding.lesseeProgressbar.setVisibility(View.INVISIBLE);
            binding.loginLessee.setVisibility(View.VISIBLE);
        }
    }
    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.LesseeUsername.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.LesseePassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() !=null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));



                    } else {
                        loading(false);
                    }
                });
    }
}