package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.myloginapp.adapters.PaymentAdapter;
import com.example.myloginapp.adapters.PropertyAdapter;
import com.example.myloginapp.api.ApiClient;
import com.example.myloginapp.api.ApiEndpoints;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class recyclerview_payment extends AppCompatActivity {
    private List<PaymentResponse> payment = new ArrayList<>();
    private PaymentAdapter paymentAdapter;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_payment);

        RecyclerView propertyRecyclerView = findViewById(R.id.paymentRecyclerView);
        propertyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentAdapter = new PaymentAdapter(this, payment);
        propertyRecyclerView.setAdapter(paymentAdapter);
        getUserEmailFromSharedPreferences();
        fetchProperty();
    }
    private void getUserEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        Log.d("Data", "Retrieved User Email: " + email);
    }
    private void fetchProperty() {

        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();

        Call<List<PaymentResponse>> call = apiService.getPayment1(email);

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
}