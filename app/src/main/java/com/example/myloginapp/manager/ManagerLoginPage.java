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
import com.example.myloginapp.databinding.ActivityLessorLoginPageBinding;
import com.example.myloginapp.databinding.ActivityManagerLoginPageBinding;
import com.example.myloginapp.lessee.LesseeDashboard;
import com.example.myloginapp.lessee.LesseeLoginPage;
import com.example.myloginapp.lessor.LessorLoginPage;
import com.example.myloginapp.utilities.Constants;
import com.example.myloginapp.utilities.PreferenceManager;
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

public class ManagerLoginPage extends AppCompatActivity {
    private EditText username, password;
    private AppCompatButton signIn;
    private ActivityManagerLoginPageBinding binding;
    private PreferenceManager preferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login_page);
        binding = ActivityManagerLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_INMANAGER)) {
            Intent intent = new Intent(getApplicationContext(), LesseeDashboard.class);
            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.ManagerUsername);
        password = findViewById(R.id.ManagerPassword);
        signIn = findViewById(R.id.login_manager);

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
                    Toast.makeText(ManagerLoginPage.this, "Successfully login", Toast.LENGTH_SHORT).show();

                    // Finish the current SignIn activity
                    finish();
                    Intent intent = new Intent(getApplicationContext(), ManagerDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
            binding.loginManager.setVisibility(View.INVISIBLE);
            binding.managerProgressbar.setVisibility(View.VISIBLE);
        } else {
            binding.managerProgressbar.setVisibility(View.VISIBLE);
            binding.loginManager.setVisibility(View.INVISIBLE);
        }
    }

    private Boolean isValidSignIn(){
        if (binding.ManagerUsername.getText().toString().trim().isEmpty()) {
            Toast.makeText(ManagerLoginPage.this, "Please enter your username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.ManagerPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(ManagerLoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }
    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_MANAGERUSERS)
                .whereEqualTo(Constants.KEY_USERNAME, binding.ManagerUsername.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.ManagerPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() !=null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_INMANAGER,true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));

                    } else {
                        loading(false);
                    }
                });
    }
}
