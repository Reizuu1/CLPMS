package com.example.myloginapp;

import static android.opengl.ETC1.encodeImage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloginapp.api.ApiEndpoints;
import com.example.myloginapp.lessee.LesseeDashboard;
import com.example.myloginapp.lessee.LesseeProfileFragment;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class lessee_sendpay extends AppCompatActivity {
    private ImageView imageProfile;
    private EditText lessee, lessor;
    private TextView textAddImage;
    private AppCompatButton send;
    private String encodedImage, lesseemail, formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_sendpay);

        lessee = findViewById(R.id.LesseeUsername);
        lessor = findViewById(R.id.Lessor);
        send = findViewById(R.id.LogoutButton);
        imageProfile = findViewById(R.id.addimage);
        textAddImage = findViewById(R.id.textAddImage);

        Date currentDate = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formattedDate = dateFormat.format(currentDate);
        getUserDataFromSharedPreferences();
        setListeners();
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageProfile.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private Boolean isValidSignUpDetails() {
        if (encodedImage == null) {
            showToast("Select a profile image");
            return false;
        } else if (lessee.getText().toString().trim().isEmpty()) {
            showToast("Enter lessee name");
            return false;
        } else if (lessor.getText().toString().trim().isEmpty()) {
            showToast("Enter lessor name");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setListeners() {
        send.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
                String getlessee = lessee.getText().toString().trim();
                String getlessor = lessor.getText().toString().trim();
                String getFirstname = lesseemail;
                signUp(getlessee, getlessor, getFirstname);
            }
        });

        imageProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void signUp(String lessee, String lessor, String Email) {
        String paymentId = UUID.randomUUID().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ap-southeast-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("paymentId", paymentId);
        jsonObject.addProperty("lesseename", lessee);
        jsonObject.addProperty("lessorname", lessor);
        jsonObject.addProperty("date", formattedDate);
        jsonObject.addProperty("screenshot", encodedImage);
        jsonObject.addProperty("email", Email);
        jsonObject.addProperty("status", "Pending..");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<PayResponse> call = apiEndpoints.SendPayment(requestBody);
        call.enqueue(new Callback<PayResponse>() {
            @Override
            public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(lessee_sendpay.this, "Payment Sent Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(lessee_sendpay.this, LesseeDashboard.class));
                    overridePendingTransition(0, R.anim.splash_fade_out);
                } else {
                    Toast.makeText(lessee_sendpay.this, "Payment Sent Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PayResponse> call, Throwable t) {
                Toast.makeText(lessee_sendpay.this, "Payment Sent failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String getFirstName = sharedPreferences.getString("email", "");

        if (!getFirstName.isEmpty()) {
            lesseemail = getFirstName;
        }
    }
}