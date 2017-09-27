package com.caraquri.android_bookmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountSettingActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.account_setting_toolbar);
        toolbar.setTitle(R.string.account_setting);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);

        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        passwordConfirmEditText = (EditText) findViewById(R.id.password_confirm_edit_text);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // アカウント設定画面は終了して戻る
                return true;
            case R.id.menu_save:
                Log.d("State", "It's going to sign up");
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordConfirm = passwordConfirmEditText.getText().toString();
                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(this, "Two passwords must be same", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Retrofit retrofit = Client.setRetrofit();
                UserClient client = retrofit.create(UserClient.class);
                Call<UserResponse> call = client.userSignUp(new User(email, password));
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        Log.d("onResponse", response.toString());
                        if (!response.isSuccessful()) {
                            return;
                        }
                        Toast.makeText(AccountSettingActivity.this, "Save Succeeded!!", Toast.LENGTH_SHORT).show();
                        UserResponse userResponse = response.body();
                        int userId = userResponse.getUserId();
                        String email = userResponse.getEmail();
                        String token = userResponse.getToken();
                        Log.d("User ID", String.valueOf(userId));
                        Log.d("email", email);
                        Log.d("token", token);
                        PreferenceUtil.setPreferences(AccountSettingActivity.this, userId, email, token);

                        Intent intent = new Intent(AccountSettingActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 戻るキー押下時に前の状態に戻ってしまうので過去のActivityをクリア
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
