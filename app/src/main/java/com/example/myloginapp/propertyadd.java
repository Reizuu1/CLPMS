package com.example.myloginapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myloginapp.api.ApiEndpoints;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class propertyadd extends AppCompatActivity {

    private AppCompatButton addproperty;
    private EditText propertyname, lessor, floors, address, useremail, status, lessee, unit;
    private FrameLayout image;
    private ImageView imageProfile;
    private TextView textAddImage;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propertyadd);

        propertyname = findViewById(R.id.inputpropertyname);
        lessor = findViewById(R.id.inputlessor);
        floors = findViewById(R.id.inputfloors);
        address = findViewById(R.id.inputaddress);
        useremail = findViewById(R.id.inputuseremail);
        status = findViewById(R.id.inputstatus);
        lessee = findViewById(R.id.inputlessee);
        unit = findViewById(R.id.inputunit);
        image = findViewById(R.id.layoutImage);
        imageProfile = findViewById(R.id.imageProfile);
        textAddImage = findViewById(R.id.textAddImage);
        addproperty = findViewById(R.id.buttonSignUp);


        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        addproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getPropertyName = propertyname.getText().toString().trim();
                String getLessor = lessor.getText().toString().trim();
                String getFloors = floors.getText().toString().trim();
                String getAddress = address.getText().toString().trim();
                String getUseremail = useremail.getText().toString().trim();
                String getStatus = status.getText().toString().trim();
                String getLessee = lessee.getText().toString().trim();
                String getUnit = unit.getText().toString().trim();
                if(isValidSignUpDetails()) {
                    signUp(getPropertyName, getLessor, getFloors,getAddress,getUseremail,getStatus,getLessee,getUnit);
                }
            }
        });
    }
    private void signUp(String getPropertyName, String getLessor, String getFloors, String getAddress, String getUseremail, String getStatus, String getLessee, String getUnit) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ap-southeast-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("propertyname", getPropertyName);
        jsonObject.addProperty("lessor", getLessor);
        jsonObject.addProperty("floors", getFloors);
        jsonObject.addProperty("address", getAddress);
        jsonObject.addProperty("floorOcu", 0);
        jsonObject.addProperty("imageFilename", "");
        jsonObject.addProperty("image", encodedImage);
        jsonObject.addProperty("userEmail", getUseremail);
        jsonObject.addProperty("status", getStatus);
        jsonObject.addProperty("lessee", getLessee);
        jsonObject.addProperty("unit", getUnit);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<CreateAccount> call = apiEndpoints.CreateAccount(requestBody);
        call.enqueue(new Callback<CreateAccount>() {
            @Override
            public void onResponse(Call<CreateAccount> call, Response<CreateAccount> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(propertyadd.this, "Account Created Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(propertyadd.this, MainActivity.class));
                    overridePendingTransition(0, R.anim.splash_fade_out);

                } else {
                    Toast.makeText(propertyadd.this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<CreateAccount> call, Throwable t) {
                Toast.makeText(propertyadd.this, "Account Creation failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Boolean isValidSignUpDetails(){
        if (encodedImage == null) {
            showToast("Select property image");
            return false;
        } else if (propertyname == null) {
            showToast("Enter property name");
            return false;
        } else if (lessor == null) {
            showToast("Enter lessor name");
            return false;
        } else if (address == null) {
            showToast("Enter address");
            return false;
        } else if (useremail == null) {
            showToast("Enter user email");
            return false;
        } else if (status == null) {
            showToast("Enter status");
            return false;
        } else if (lessee == null) {
            showToast("Enter lessee name");
            return false;
        } else if (unit == null) {
            showToast("Enter unit");
            return false;
        } else
            return true;
    }
    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress (Bitmap. CompressFormat .JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
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
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}