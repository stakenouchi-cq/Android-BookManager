package com.caraquri.android_bookmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        emailEditText.setText("hoge@hoge.com");
        passwordEditText.setText("password");
        Button loginButton = (Button) findViewById(R.id.login_button);

        Toolbar toolbar = (Toolbar) findViewById (R.id.login_toolbar);
        toolbar.setTitle(R.string.book_lineup);
        setSupportActionBar(toolbar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Retrofit retrofit = Client.setRetrofit();

                UserClient client = retrofit.create(UserClient.class);

                Call<UserResponse> call = client.userLogin(new User(email, password));
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        Log.d("onResponse", response.toString());
                        if (!response.isSuccessful()) {
                            return;
                        }
                        UserResponse userResponse = response.body();
                        Log.d("userResponse", userResponse.toString());
                        Log.d("token", String.valueOf(userResponse.getToken()));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // ログインしたら，この画面にはもう戻らない
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

    }

}
