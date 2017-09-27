package com.caraquri.android_bookmanager;


import org.json.JSONObject;

import java.util.HashMap;

public class UserResponse {
    private Result result;
    private int userId;
    private String email;
    private String token;

    public UserResponse() {
    }

    public int getUserId() {
        return result.userId;
    }

    public String getEmail() {
        return result.email;
    }

    public String getToken() {
        return result.token;
    }

}