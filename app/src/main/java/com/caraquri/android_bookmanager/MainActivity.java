package com.caraquri.android_bookmanager;

import android.content.Intent;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初回起動時はアカウント新規登録画面，そうでない時はログイン画面へ
        if (AppLaunchChecker.hasStartedFromLauncher(this)) {
            Log.d("AppLaunchChecker", "It's not first launch");
            intent = new Intent(MainActivity.this, LoginActivity.class);
        } else {
            Log.d("AppLaunchChecker", "It's first launch");
            intent = new Intent(MainActivity.this, AccountSettingActivity.class);
        }

        AppLaunchChecker.onActivityCreate(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

}
