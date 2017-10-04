package com.caraquri.android_bookmanager;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @Headers("Content-Type: application/json")
    @POST("/books")
    Call<JSONObject> addBookData(
            @Header("Authorization") String token,
            @Body BookRequest bookRequest
    );

    @Headers("Content-Type: application/json")
    @PUT("/books/{id}")
    Call<JSONObject> editBookData(
            @Header("Authorization") String token,
            @Path("id") int bookId,
            @Body BookRequest bookRequest
    );

}
