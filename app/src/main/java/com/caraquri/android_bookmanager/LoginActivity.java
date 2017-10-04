package com.caraquri.android_bookmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初回起動時はアカウント新規登録画面，そうでない時はログイン画面へ
        if (AppLaunchChecker.hasStartedFromLauncher(this)) {
            Log.d("AppLaunchChecker", "It's not first launch");
        } else {
            Log.d("AppLaunchChecker", "It's first launch");
            Intent intent = new Intent(this, AccountSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
        AppLaunchChecker.onActivityCreate(this);

        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        Button loginButton = (Button) findViewById(R.id.login_button);

        Toolbar toolbar = (Toolbar) findViewById (R.id.login_toolbar);
        toolbar.setTitle(R.string.book_lineup);
        setSupportActionBar(toolbar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();
            }
        });

    }

    private void startLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Retrofit retrofit = Client.getRetrofit();
        UserClient client = retrofit.create(UserClient.class);
        Call<UserResponse> call = client.userLogin(new User(email, password));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.d("onResponse", response.toString());
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Failed (response error)", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserResponse userResponse = response.body();
                int userId = userResponse.getUserId();
                String email = userResponse.getEmail();
                String token = userResponse.getToken();
                Log.d("User ID", String.valueOf(userId));
                Log.d("email", email);
                Log.d("token", token);
                PreferenceUtil.setPreferences(LoginActivity.this, userId, email, token);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // ログインしたら，この画面にはもう戻らない
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e(LOG_TAG, Constants.LogMessages.CALLBACK_RETROFIT, t);
                Toast.makeText(LoginActivity.this, "Login Failed (request error)", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
