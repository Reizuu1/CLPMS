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
import com.example.myloginapp.lessor.LessorDashboard;
import com.example.myloginapp.lessor.LessorLoginPage;
import com.example.myloginapp.lessor.LessorResponse;
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
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_INLESSOR)) {
            Intent intent = new Intent(getApplicationContext(), LessorDashboard.class);
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
            Toast.makeText(ManagerLoginPage.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
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
        jsonObject.addProperty("email", "Lessor23@gmail.com");
        jsonObject.addProperty("password1", "Lessor23");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<LessorResponse> call = apiEndpoints.signinLessor(requestBody);
        call.enqueue(new Callback<LessorResponse>() {
            @Override
            public void onResponse(Call<LessorResponse> call, Response<LessorResponse> response) {
                LessorResponse createReponse = response.body();
                if (response.code() == 200) {
                    String userId = createReponse.getId();
                    String password = createReponse.getPassword();
                    String email = createReponse.getEmail();
                    String contactNumber = createReponse.getContactnumber();
                    String fullname = createReponse.getFullname();

                    storeUserData(userId, password, email, contactNumber, fullname);
                    Toast.makeText(ManagerLoginPage.this, "Successfully login", Toast.LENGTH_SHORT).show();

                    // Finish the current SignIn activity
                    finish();
                    Intent intent = new Intent(getApplicationContext(),LessorDashboard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ManagerLoginPage.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<LessorResponse> call, Throwable t) {
                Toast.makeText(ManagerLoginPage.this, "Server failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void storeUserData(String userId, String password, String email, String contactNumber, String fullname) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", userId);
        editor.putString("password", password);
        editor.putString("email", email);
        editor.putString("contact_number", contactNumber);
        editor.putString("fullname", fullname);

        editor.apply();
    }

    private Boolean isValidSignIn(){
        if (binding.ManagerUsername.getText().toString().trim().isEmpty()) {
            Toast.makeText(ManagerLoginPage.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
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
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_LESSORUSERS)
                .whereEqualTo(Constants.KEY_EMAIL, "Lessor23@gmail.com")
                .whereEqualTo(Constants.KEY_PASSWORD, "Lessor23")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() !=null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_INLESSOR,true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));

                    } else {
                    }
                });
    }
}
