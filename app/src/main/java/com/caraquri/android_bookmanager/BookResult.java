package com.caraquri.android_bookmanager;

import com.google.gson.annotations.SerializedName;

public class BookResult {

    @SerializedName("id")
    protected int bookId;

    protected String name;

    @SerializedName("image")
    protected String imageUrl;

    protected int price;

    @SerializedName("purchase_date")
    protected String purchaseDate;

}
