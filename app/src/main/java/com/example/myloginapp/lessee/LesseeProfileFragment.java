package com.example.myloginapp.lessee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.myloginapp.MainActivity;
import com.example.myloginapp.R;
import com.example.myloginapp.databinding.ActivityLesseeDashboardBinding;
import com.example.myloginapp.databinding.FragmentLesseeConcernBinding;
import com.example.myloginapp.databinding.FragmentLesseeProfileBinding;
import com.example.myloginapp.lessee_payment;
import com.example.myloginapp.utilities.Constants;
import com.example.myloginapp.utilities.PreferenceManager;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;


public class LesseeProfileFragment extends Fragment {

    private PreferenceManager preferenceManager;
    private FragmentLesseeProfileBinding binding;
    private TextView send;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentLesseeProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnNavigate = view.findViewById(R.id.LogoutButton);
        TextView send = view.findViewById(R.id.textview_payment_history);

        send.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), lessee_payment.class);
            startActivity(intent);
        });
        btnNavigate.setOnClickListener(v -> {
            callActivityMethod();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }
    private void callActivityMethod() {
        LesseeDashboard activity = (LesseeDashboard) getActivity();

        if (activity != null) {
            activity.signOut();
        }
    }
}