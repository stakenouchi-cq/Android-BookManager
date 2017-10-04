package com.caraquri.android_bookmanager;


import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("result")
    private UserResult userResult;

    private int userId;
    private String email;
    private String token;

    public UserResponse() {
    }

    public int getUserId() {
        return userResult.userId;
    }

    public String getEmail() {
        return userResult.email;
    }

    public String getToken() {
        return userResult.token;
    }

}