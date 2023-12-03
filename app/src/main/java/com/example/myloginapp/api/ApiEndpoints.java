package com.example.myloginapp.api;

import com.example.myloginapp.lessee.LesseeResponse;
import com.example.myloginapp.lessor.LessorResponse;
import com.example.myloginapp.manager.ManagerResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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

}
