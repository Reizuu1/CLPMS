package com.example.myloginapp.lessor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myloginapp.MainActivity;
import com.example.myloginapp.R;
import com.example.myloginapp.lessee.LesseeDashboard;
import com.example.myloginapp.lessorpayment;

public class LessorProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lessor_profile, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the button by its ID
        Button btnNavigate = view.findViewById(R.id.LogoutButton);
        TextView send = view.findViewById(R.id.textview_payment_history);

        // Set a click listener on the button
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivityMethod();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), lessorpayment.class);
                startActivity(intent);
            }
        });
    }
    private void callActivityMethod() {
        LessorDashboard activity = (LessorDashboard) getActivity();

        if (activity != null) {
            activity.signOutLessor();
        }
    }
}