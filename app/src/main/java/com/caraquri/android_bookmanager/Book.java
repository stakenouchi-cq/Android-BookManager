package com.caraquri.android_bookmanager;

import android.graphics.Bitmap;

public class Book {
    long id;

    private String imgStr; // 画像をbase64で表現した時の文字列
    private Bitmap imgBmp;
    private String title;
    private int price;
    private String purchaseDate;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgStr() {
        return imgStr;
    }

    public void setImgStr(String imgStr) {
        this.imgStr = imgStr;
    }

    public Bitmap getImgBmp() {
        return imgBmp;
    }

    public void setImgBmp(Bitmap imgBmp) {
        this.imgBmp = imgBmp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}
