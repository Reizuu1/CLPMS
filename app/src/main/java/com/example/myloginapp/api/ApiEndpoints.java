package com.example.myloginapp.api;

import com.example.myloginapp.CreateAccount;
import com.example.myloginapp.LessorPaymentResponse;
import com.example.myloginapp.PayResponse;
import com.example.myloginapp.PaymentResponse;
import com.example.myloginapp.PropertyResponse;
import com.example.myloginapp.lessee.LesseeResponse;
import com.example.myloginapp.lessor.LessorResponse;
import com.example.myloginapp.manager.ManagerResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiEndpoints {
    //Lessee
    @POST("app/application-0-hchfu/endpoint/SignInLessee")
    Call<LesseeResponse> signinLessee(@Body RequestBody requestBody);

    //Lessor
    @POST("app/application-0-hchfu/endpoint/SignInLessor")
    Call<LessorResponse> signinLessor(@Body RequestBody requestBody);

    //Manager
    @POST("app/application-0-hchfu/endpoint/SignInManager")
    Call<ManagerResponse> signinManager(@Body RequestBody requestBody);

    @POST("app/application-0-hchfu/endpoint/Payment")
    Call<PayResponse> SendPayment(@Body RequestBody requestBody);

    @GET("app/application-0-hchfu/endpoint/getpayment")
    Call<List<PaymentResponse>> getPayment(@Query("lessorname") String fullname);


    @GET("app/application-0-hchfu/endpoint/getLessee")
    Call<List<PaymentResponse>> getPayment1(@Query("email") String email);

    @GET("app/application-0-hchfu/endpoint/property")
    Call<List<PropertyResponse>> getProperty(@Query("lease_account") String userEmail);

    @GET("app/application-0-hchfu/endpoint/propertyLessor")
    Call<List<PropertyResponse>> getProperty1(@Query("userEmail") String userEmail);

    @POST("app/application-0-hchfu/endpoint/paymentResponse")
    Call<PayResponse> updateStatus(@Body RequestBody requestBody);
}
