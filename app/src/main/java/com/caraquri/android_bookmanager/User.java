package com.caraquri.android_bookmanager;

public class User {
    public String email;
    public String token;

    public User(String email, String token){
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

}
