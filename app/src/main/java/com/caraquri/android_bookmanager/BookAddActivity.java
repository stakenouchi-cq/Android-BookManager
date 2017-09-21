package com.caraquri.android_bookmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import java.util.Calendar;

public class BookAddActivity extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1001;
    private EditText titleEditText;
    private EditText priceEditText;
    private EditText purchaseDateEditText;
    private ImageView bookTmb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        Toolbar toolbar = (Toolbar) findViewById (R.id.book_add_toolbar);
        toolbar.setTitle(R.string.book_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        titleEditText = (EditText) findViewById(R.id.title_edit_text);
        priceEditText = (EditText) findViewById(R.id.price_edit_text);
        purchaseDateEditText = (EditText) findViewById(R.id.purchase_date_edit_text);

        bookTmb = (ImageView) findViewById(R.id.book_thumbnail);
        bookTmb.setImageBitmap(getBmpFromAssets("no_image.png"));

        Button addTmbButton = (Button) findViewById(R.id.button_add_thumbnail);
        addTmbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });

        purchaseDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }

    private String getGalleryPath() {
        return Environment.getExternalStorageState() + "/" + Environment.DIRECTORY_DCIM + "/";
    }

    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i("", "Uri: " + uri.toString());
                try {
                    Bitmap bm = getBitmapFromUri(uri);
                    bookTmb.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_add).setVisible(false);
        menu.findItem(R.id.menu_save).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_save:
                Log.d("Data of the book", "Title: " + titleEditText.getText() + " Price: "+ priceEditText.getText() + " PurchaseDate: " + purchaseDateEditText.getText());
                // 全入力欄が空欄でないかつ金額が数字になっていれば保存
                if (TextUtils.isEmpty(titleEditText.getText()) || !CheckUtil.isNumber(priceEditText) || TextUtils.isEmpty(purchaseDateEditText.getText())) {
                    return false;
                }
                Toast.makeText(this, "Save Succeeded!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    private Bitmap getBmpFromAssets(String imgPath) {
        try {
            InputStream inputStream = getResources().getAssets().open(imgPath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
