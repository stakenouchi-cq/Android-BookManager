package com.caraquri.android_bookmanager;

import com.google.gson.annotations.SerializedName;

public class UserResult {
    @SerializedName("id")
    protected int userId;

    protected String email;
    protected String token;
}
