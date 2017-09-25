package com.caraquri.android_bookmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AccountSettingActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1000;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        // 初回起動時はアカウント新規登録画面，そうでない時はログイン画面へ
        if (AppLaunchChecker.hasStartedFromLauncher(this)) {
            Log.d("AppLaunchChecker", "It's not first launch");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        } else {
            Log.d("AppLaunchChecker", "It's first launch");
            checkStoragePermission(); // ストレージ読み込み権限チェック(初回起動用)
        }
        AppLaunchChecker.onActivityCreate(this);

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
                finish();
                return true;
            case R.id.menu_save:
                Toast.makeText(this, "Save Succeeded!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 戻るキー押下時に前の状態に戻ってしまうので過去のActivityをクリア
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT <= 22) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return; // API 23以降でも許可がある場所は大丈夫
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            Toast toast = Toast.makeText(this, "We can't execute this application", Toast.LENGTH_SHORT);
            toast.show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_PERMISSION) {
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return; // 使用許可を得たので続行
        } else {
            // それでも拒否された時の対応
            Toast toast = Toast.makeText(this, "You will not be able to open storage", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
    }

}
