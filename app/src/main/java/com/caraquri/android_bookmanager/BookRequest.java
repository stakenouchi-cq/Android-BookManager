package com.caraquri.android_bookmanager;

import com.google.gson.annotations.SerializedName;

public class BookRequest {
    private String name;
    private String image;
    private int price;

    // @SerializedName("purchase_date")
    private String purchase_date;

    public BookRequest(String name, String image, int price, String purchase_date) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.purchase_date = purchase_date;
    }

}
