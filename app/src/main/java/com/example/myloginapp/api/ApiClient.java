package com.example.myloginapp.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private final String BASE_URL = "https://ap-southeast-1.aws.data.mongodb-api.com/";
    private static ApiEndpoints apiEndpoints;
    public ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiEndpoints = retrofit.create(ApiEndpoints.class);
    }
    public static ApiEndpoints getApiService() {
        return apiEndpoints;
    }


    //FOR CHAT APP
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/fcm/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

