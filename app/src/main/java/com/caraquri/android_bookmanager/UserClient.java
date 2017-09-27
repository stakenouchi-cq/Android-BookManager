package com.caraquri.android_bookmanager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserClient {

    @Headers("Content-Type: application/json")
    @POST("/sign_up")
    Call<UserResponse> userSignUp(
            @Body User user
    );

    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<UserResponse> userLogin(
            @Body User user
    );

}