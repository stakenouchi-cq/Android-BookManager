package com.caraquri.android_bookmanager;

import com.google.gson.annotations.SerializedName;

public class EditBookResponse {

    @SerializedName("result")
    private BookResult bookResult;

    public EditBookResponse() {}

    public BookResult getBookResult() {
        return bookResult;
    }

}