package com.example.myloginapp;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myloginapp.adapters.LessorAdaptor;
import com.example.myloginapp.adapters.PaymentAdapter;
import com.example.myloginapp.api.ApiClient;
import com.example.myloginapp.api.ApiEndpoints;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class lessorpayment extends AppCompatActivity implements LessorAdaptor.OnButtonClickListener {
    private List<PaymentResponse> payment = new ArrayList<>();
    private LessorAdaptor paymentAdapter;
    private String email, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessorpayment);


        RecyclerView propertyRecyclerView = findViewById(R.id.paymentRecyclerView);
        propertyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentAdapter = new LessorAdaptor(this, payment);
        propertyRecyclerView.setAdapter(paymentAdapter);
        getUserEmailFromSharedPreferences();
        fetchProperty();
    }

    private void getUserEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        fullname = sharedPreferences.getString("fullname", "");

        Log.d("Data", "Retrieved User Email: " + email);
    }

    private void fetchProperty() {

        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();

        Call<List<PaymentResponse>> call = apiService.getPayment(fullname);

        call.enqueue(new Callback<List<PaymentResponse>>() {
            @Override
            public void onResponse(Call<List<PaymentResponse>> call, Response<List<PaymentResponse>> response) {

                if (response.isSuccessful()) {
                    List<PaymentResponse> fetchedProperty = response.body();

                    if (fetchedProperty != null && !fetchedProperty.isEmpty()) {
                        payment.clear();
                        payment.addAll(fetchedProperty);
                        paymentAdapter.notifyDataSetChanged();

                        Log.d("Data", "Fetched Property Size: " + fetchedProperty.size());
                        Log.d("Data", "Data List: " + payment.toString());
                    } else {
                        Log.d("Data", "Fetched data is null or empty");
                    }
                } else {
                    Log.e("Data", "Unsuccessful response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PaymentResponse>> call, Throwable t) {
                t.printStackTrace();
                Log.e("Data", "Failed to fetch property data", t);
            }
        });
    }

    @Override
    public void onButton1Click(int position, String paymentId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ap-southeast-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("paymentId", paymentId);
        jsonObject.addProperty("status", "Failed");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<PayResponse> call = apiEndpoints.updateStatus(requestBody);
        call.enqueue(new Callback<PayResponse>() {
            @Override
            public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(lessorpayment.this, "Payment successfully.", Toast.LENGTH_SHORT).show();
                    reloadActivity();

                } else {
                    Toast.makeText(lessorpayment.this, "Payment failed.", Toast.LENGTH_SHORT).show();
                    reloadActivity();

                }
            }

            @Override
            public void onFailure(Call<PayResponse> call, Throwable t) {
                Toast.makeText(lessorpayment.this, "Error: Payment failed.", Toast.LENGTH_SHORT).show();
                reloadActivity();

            }
        });
    }

    @Override
    public void onButton2Click(int position, String paymentId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ap-southeast-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("paymentId", paymentId);
        jsonObject.addProperty("status", "Success");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<PayResponse> call = apiEndpoints.updateStatus(requestBody);
        call.enqueue(new Callback<PayResponse>() {
            @Override
            public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(lessorpayment.this, "Payment successfully.", Toast.LENGTH_SHORT).show();
                    reloadActivity();
                } else {
                    Toast.makeText(lessorpayment.this, "Payment failed.", Toast.LENGTH_SHORT).show();
                    reloadActivity();
                }
            }
            @Override
            public void onFailure(Call<PayResponse> call, Throwable t) {
                Toast.makeText(lessorpayment.this, "Error: Payment failed.", Toast.LENGTH_SHORT).show();
                reloadActivity();
            }
        });
    }
    public void reloadActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}