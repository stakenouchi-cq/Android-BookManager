package com.caraquri.android_bookmanager;

import android.graphics.Bitmap;

public class Book {

    private int bookId;
    private String imageUrl; // 画像がある場所のpath
    private String title; // 書籍名
    private int price; // 価格
    private String purchaseDate; // 購入日

    public Book(int bookId, String imageUrl, String title, int price, String purchaseDate) {
        this.bookId = bookId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.price = price;
        this.purchaseDate = purchaseDate;
    }

    public int getBookId() {
        return bookId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }


}
