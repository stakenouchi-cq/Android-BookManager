package com.caraquri.android_bookmanager;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_PICK_IMAGEFILE = 1;
    private static final int REQUEST_PICK_PERMISSION = 2;
    private EditText titleEditText;
    private EditText priceEditText;
    private EditText purchaseDateEditText;
    private ImageView bookThumbnailImageView;
    private Bitmap bookThumbnailBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        // ツールバーの設定
        Toolbar toolbar = (Toolbar) findViewById (R.id.book_add_toolbar);
        toolbar.setTitle(R.string.book_add);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back); // 戻るキーを表示
        setSupportActionBar(toolbar);

        titleEditText = (EditText) findViewById(R.id.title_edit_text);
        priceEditText = (EditText) findViewById(R.id.price_edit_text);

        bookThumbnailImageView = (ImageView) findViewById(R.id.book_thumbnail);
        bookThumbnailBitmap = ImageUtil.getBitmapFromAssets(getBaseContext(), "no_image.png");
        bookThumbnailImageView.setImageBitmap(bookThumbnailBitmap);

        Button addThumbnailButton = (Button) findViewById(R.id.button_add_thumbnail);
        addThumbnailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK_IMAGEFILE);
            }
        });

        purchaseDateEditText = (EditText) findViewById(R.id.purchase_date_edit_text);
        purchaseDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.setOnDatePickerDialogListener(BookAddActivity.this);
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 許可されていない
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PICK_PERMISSION);
            return;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        purchaseDateEditText.setText(String.format("%d/%02d/%02d", year, month+1, day));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            checkPermission();
            Uri uri = data.getData();
            Log.i("", "Uri: " + uri.toString());
            try {
                bookThumbnailBitmap = ImageUtil.getBitmapFromUri(this, uri);
                bookThumbnailImageView.setImageBitmap(bookThumbnailBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PICK_PERMISSION) {
            // requestPermissionsで設定した順番で結果が格納されています。
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 許可されたので処理を続行
                return;
            } else {
                Toast.makeText(this, "Not have perimission to storage.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save:
                // 全入力欄が空欄でないかつ金額が数字になっていれば保存
                if (TextUtils.isEmpty(titleEditText.getText()) || TextUtils.isEmpty(priceEditText.getText()) || !TextUtils.isDigitsOnly(priceEditText.getText()) || TextUtils.isEmpty(purchaseDateEditText.getText())) {
                    return false;
                }
                // 書籍データの保存に入る
                String name = titleEditText.getText().toString();
                int price = Integer.valueOf(priceEditText.getText().toString()).intValue();
                String purchaseDate = purchaseDateEditText.getText().toString();
                String image = ImageUtil.encodeToBase64(bookThumbnailBitmap);

                // tokenを取得
                SharedPreferences preferences = this.getSharedPreferences(Constants.PreferenceKeys.DATA_KEY, Context.MODE_PRIVATE);
                final String token = preferences.getString(Constants.PreferenceKeys.TOKEN, "");
                Log.d("Token", token);

                Retrofit retrofit = Client.setRetrofit();
                BookClient client = retrofit.create(BookClient.class);
                Call<BookResponse> call = client.addBookData(token, new BookRequest(name, image, price, purchaseDate));
                call.enqueue(new Callback<BookResponse>() {
                    @Override
                    public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                        Toast.makeText(getBaseContext(), "Save Succeeded", Toast.LENGTH_SHORT).show();
                        Log.d("ID of this book", response.body().getBookResult().toString());
                    }

                    @Override
                    public void onFailure(Call<BookResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getBaseContext(), "Save failed", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
