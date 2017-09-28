package com.caraquri.android_bookmanager;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static final String API_BASE_URL = "http://54.250.239.8";

    // 書籍管理サーバーとの接続をビルド
    public static Retrofit setRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()
                );
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit;
    }
}
