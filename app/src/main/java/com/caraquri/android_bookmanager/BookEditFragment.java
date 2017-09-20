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
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Calendar;


public class BookEditFragment extends Fragment {

    private static final int RESULT_PICK_IMAGEFILE = 1001;
    private ImageView bookTmb;
    private EditText titleEditText;
    private EditText priceEditText;
    private EditText purchaseDateEditText;

    public BookEditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_edit, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        // ツールバーの定義
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.book_edit);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookTmb = (ImageView) view.findViewById(R.id.book_thumbnail);
        titleEditText = (EditText) view.findViewById(R.id.title_edit_text);
        priceEditText = (EditText) view.findViewById(R.id.price_edit_text);
        purchaseDateEditText = (EditText) view.findViewById(R.id.purchase_date_edit_text);

        // 各テキストボックスにデフォルト値を設定
        Bundle args = getArguments();
        String imgStr = args.getString("imgStr");
        String title = args.getString("title");
        int price = args.getInt("price");
        String purchaseDate = args.getString("purchaseDate");

        // 画面遷移直後の初期値を設定
        titleEditText.setText(title);
        priceEditText.setText(String.valueOf(price));
        purchaseDateEditText.setText(purchaseDate);
        bookTmb.setImageBitmap(ImageUtil.decodeToBase64(imgStr));

        Button addTmbButton = (Button) view.findViewById(R.id.button_add_thumbnail);
        addTmbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });

        purchaseDateEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                purchaseDateEditText.setText(String.format("%d/%02d/%02d", year, month+1, day));
                            }
                        },
                        date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE)
                );
                datePickerDialog.show(); // show Dialog
            }
        });

    }

    private String getGalleryPath() {
        return Environment.getExternalStorageState() + "/" + Environment.DIRECTORY_DCIM + "/";
    }

    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_add).setVisible(false);
        menu.findItem(R.id.menu_save).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                break;
            case R.id.menu_save:
                Log.d("Data of the book", "Title: " + titleEditText.getText() + " Price: "+ priceEditText.getText() + " PurchaseDate: " + purchaseDateEditText.getText());
                // 全入力欄が空欄でないかつ金額が数字になっていれば保存
                if (CheckUtil.isNull(titleEditText) || !CheckUtil.isNumber(priceEditText) || CheckUtil.isNull(purchaseDateEditText)) {
                    return false;
                }
                Toast.makeText(getActivity(), "Save Succeeded!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

}
