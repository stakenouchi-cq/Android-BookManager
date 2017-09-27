package com.caraquri.android_bookmanager;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookClient {

    @Headers("Content-Type: application/json")
    @GET("/books?")
    Call<BookResponse> getBookList(
            @Header("Authorization") String token,
            @Query("limit") int limit,
            @Query("page") int page
    );

}
