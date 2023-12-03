package com.example.myloginapp.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myloginapp.api.ApiEndpoints;
import com.example.myloginapp.R;
import com.example.myloginapp.databinding.ActivityManagerLoginPageBinding;
import com.example.myloginapp.lessor.LessorDashboard;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerLoginPage extends AppCompatActivity {
    private EditText username, password;
    private AppCompatButton signIn;
    private ActivityManagerLoginPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessor_login_page);

        username = findViewById(R.id.LesseeUsername);
        password = findViewById(R.id.LesseePassword);
        signIn = findViewById(R.id.login_lessee);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check valid username and password
                loading(true);
                String getUsername = username.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                checkUsernameAndPassword(getUsername, getPassword);
            }
        });
    }
    private void checkUsernameAndPassword(String username, String password){
        if(username.isEmpty()){
            Toast.makeText(ManagerLoginPage.this, "Please enter your username", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(ManagerLoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }else if(!username.isEmpty() && !password.isEmpty()){
            validateCredentials(username, password);
        }
    }
    private void validateCredentials(String username, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ap-southeast-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<ManagerResponse> call = apiEndpoints.signinManager(requestBody);
        call.enqueue(new Callback<ManagerResponse>() {
            @Override
            public void onResponse(Call<ManagerResponse> call, Response<ManagerResponse> response) {
                ManagerResponse createReponse = response.body();
                if (response.code() == 200) {
                    String userId = createReponse.getId();
                    String password = createReponse.getPassword();
                    String username = createReponse.getUsername();
                    String contactNumber = createReponse.getContactnumber();

                    storeUserData(userId, password, username, contactNumber);
                    loading(false);
                    Toast.makeText(ManagerLoginPage.this, "Successfully login", Toast.LENGTH_SHORT).show();

                    // Finish the current SignIn activity
                    finish();
                    startActivity(new Intent(ManagerLoginPage.this, ManagerDashboard.class));
                } else {
                    Toast.makeText(ManagerLoginPage.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ManagerResponse> call, Throwable t) {
                Toast.makeText(ManagerLoginPage.this, "Server failed!", Toast.LENGTH_SHORT).show();
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
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.loginmanager.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        } else {
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.loginmanager.setVisibility(View.INVISIBLE);
        }
    }
}