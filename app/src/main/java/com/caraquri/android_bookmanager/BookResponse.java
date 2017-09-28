package com.caraquri.android_bookmanager;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BookResponse {

    @SerializedName("result")
    private List<BookResult> bookResult;

    public BookResponse() {}

    public List<BookResult> getBookResult() {
        return bookResult;
    }
    
}
