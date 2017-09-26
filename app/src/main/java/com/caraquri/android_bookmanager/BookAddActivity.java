package com.caraquri.android_bookmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
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

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

public class BookAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_PICK_IMAGEFILE = 1001;
    private EditText titleEditText;
    private EditText priceEditText;
    private EditText purchaseDateEditText;
    private ImageView bookThumbnail;

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

        bookThumbnail = (ImageView) findViewById(R.id.book_thumbnail);
        bookThumbnail.setImageBitmap(ImageUtil.getBitmapFromAssets(getApplicationContext(), "no_image.png"));

        Button addThumbnailButton = (Button) findViewById(R.id.button_add_thumbnail);
        addThumbnailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ImageUtil.isExternalStorageReadable()) {
                    Toast.makeText(BookAddActivity.this, "Error: Not have permisson to storage!", Toast.LENGTH_SHORT).show();
                    return; // ストレージに権限が無いので終了
                }
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

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
            Uri uri = data.getData();
            Log.i("", "Uri: " + uri.toString());
            try {
                Bitmap bm = getBitmapFromUri(uri);
                bookThumbnail.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                Log.d("Data of the book", "Title: " + titleEditText.getText() + " Price: "+ priceEditText.getText() + " PurchaseDate: " + purchaseDateEditText.getText());
                // 全入力欄が空欄でないかつ金額が数字になっていれば保存
                if (TextUtils.isEmpty(titleEditText.getText()) || TextUtils.isEmpty(priceEditText.getText()) || !TextUtils.isDigitsOnly(priceEditText.getText()) || TextUtils.isEmpty(purchaseDateEditText.getText())) {
                    return false;
                }
                Toast.makeText(this, "Save Succeeded!!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
