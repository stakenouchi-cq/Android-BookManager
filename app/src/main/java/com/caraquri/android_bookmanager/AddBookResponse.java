package com.caraquri.android_bookmanager;

import com.google.gson.annotations.SerializedName;

public class AddBookResponse {

    @SerializedName("result")
    private BookResult bookResult;

    public AddBookResponse() {}

    public BookResult getBookResult() {
        return bookResult;
    }

}
