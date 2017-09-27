package com.caraquri.android_bookmanager;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("id")
    protected int userId;

    protected String email;
    protected String token;
}
