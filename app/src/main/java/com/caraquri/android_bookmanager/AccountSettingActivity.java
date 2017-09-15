package com.caraquri.android_bookmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

public class AccountSettingActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        // 初回起動時はアカウント新規登録画面，そうでない時はログイン画面へ
        if (AppLaunchChecker.hasStartedFromLauncher(this)) {
            Log.d("AppLaunchChecker", "It's not first launch");
            intent = new Intent(AccountSettingActivity.this, LoginActivity.class);
            startActivity(intent);
            AccountSettingActivity.this.finish();
        } else {
            Log.d("AppLaunchChecker", "It's first launch");
        }
        AppLaunchChecker.onActivityCreate(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_accountset);
        toolbar.setTitle(R.string.account_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 戻るボタン(矢印)
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
