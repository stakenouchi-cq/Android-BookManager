package com.caraquri.android_bookmanager;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LoadBookResponse {

    @SerializedName("result")
    private List<BookResult> bookResults;

    public LoadBookResponse() {}

    public List<BookResult> getBookResults() {
        return bookResults;
    }
    
}
