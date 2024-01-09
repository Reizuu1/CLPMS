package com.example.myloginapp.lessor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myloginapp.PropertyResponse;
import com.example.myloginapp.R;
import com.example.myloginapp.adapters.LessorProperty;
import com.example.myloginapp.adapters.PropertyAdapter;
import com.example.myloginapp.api.ApiClient;
import com.example.myloginapp.api.ApiEndpoints;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessorPropertyFragment extends Fragment {
    private List<PropertyResponse> properties = new ArrayList<>();
    private LessorProperty propertyAdapter;
    private ProgressBar progressBar;
    private String userEmail = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lessee_property, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView propertyRecyclerView = view.findViewById(R.id.propertyRecyclerView);
        propertyRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        propertyAdapter = new LessorProperty(getContext(), properties);
        propertyRecyclerView.setAdapter(propertyAdapter);

        getUserEmailFromSharedPreferences();

        if (!userEmail.isEmpty()) {
            fetchProperty();
        } else {
            Log.e("Data", "User email is empty. Cannot make API call.");
        }

        return view;
    }
    private void getUserEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "");
        Log.d("Data", "Retrieved User Email: " + userEmail);
    }

    private void fetchProperty() {
        showLoading();

        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();

        Call<List<PropertyResponse>> call = apiService.getProperty1(userEmail);

        call.enqueue(new Callback<List<PropertyResponse>>() {
            @Override
            public void onResponse(Call<List<PropertyResponse>> call, Response<List<PropertyResponse>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<PropertyResponse> fetchedProperty = response.body();

                    if (fetchedProperty != null && !fetchedProperty.isEmpty()) {
                        properties.clear();
                        properties.addAll(fetchedProperty);
                        propertyAdapter.notifyDataSetChanged();

                        Log.d("Data", "Fetched Property Size: " + fetchedProperty.size());
                        Log.d("Data", "Data List: " + properties.toString());
                    } else {
                        Log.d("Data", "Fetched data is null or empty");
                    }
                } else {
                    Log.e("Data", "Unsuccessful response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PropertyResponse>> call, Throwable t) {
                hideLoading();
                t.printStackTrace();
                Log.e("Data", "Failed to fetch property data", t);
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
