package com.caraquri.android_bookmanager;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookManagerApiInterface {
    String END_POINT = "http://54.250.239.8";

    @Headers("Content-Type: application/json")
    
    @FormUrlEncoded
    @POST("/sign_up")
    Call<User> execSignUp(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/login")
    Call<User> execLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/books")
    Call<Book> getBookDatas(@Header("Authorization") String token, @Field("name") String name, @Field("image") String image, @Field("price") int price, @Field("purchase_date") String purchaseDate);

    @PUT("/books/{id}")
    Call<Book> refreshBookData(@Header("Authorization") String token, @Path("id") int bookId, @Field("name") String name, @Field("image") String image, @Field("price") int price, @Field("purchase_date") String purchaseDate);

    @GET("/books?limit={limit}&page={page}")
    Call<Book> getBookData(@Header("Authorization") String token, @Path("limit") int limit, @Path("page") int page);

}