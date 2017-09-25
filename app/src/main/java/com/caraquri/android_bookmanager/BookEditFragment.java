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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import java.io.InputStream;


public class BookEditFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_PICK_IMAGEFILE = 1001;
    private final static String ARGS_IMAGEPATH = "args_imagePath";
    private final static String ARGS_TITLE = "args_title";
    private final static String ARGS_PRICE = "args_price";
    private final static String ARGS_PURCHASEDATE = "args_purchaseDate";

    private ImageView bookThumbnail;
    private EditText titleEditText;
    private EditText priceEditText;
    private EditText purchaseDateEditText;

    public static BookEditFragment newInstance(String imagePath, String title, int price, String purchaseDate) {
        BookEditFragment fragment = new BookEditFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGEPATH, imagePath);
        args.putString(ARGS_TITLE, title);
        args.putInt(ARGS_PRICE, price);
        args.putString(ARGS_PURCHASEDATE, purchaseDate);
        fragment.setArguments(args);
        return fragment;
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
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back); // 戻るキーを表示
        toolbar.setTitle(R.string.book_edit);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        bookThumbnail = (ImageView) view.findViewById(R.id.book_thumbnail);
        titleEditText = (EditText) view.findViewById(R.id.title_edit_text);
        priceEditText = (EditText) view.findViewById(R.id.price_edit_text);
        purchaseDateEditText = (EditText) view.findViewById(R.id.purchase_date_edit_text);

        Bundle args = getArguments();
        String imagePath = args.getString(ARGS_IMAGEPATH);
        String title = args.getString(ARGS_TITLE);
        int price = args.getInt(ARGS_PRICE);
        String purchaseDate = args.getString(ARGS_PURCHASEDATE);

        // 画面遷移直後の初期値を設定
        titleEditText.setText(title);
        priceEditText.setText(String.valueOf(price));
        purchaseDateEditText.setText(purchaseDate);
        bookThumbnail.setImageBitmap(getBitmapFromAssets(imagePath));

        Button addThumbnailButton = (Button) view.findViewById(R.id.button_add_thumbnail);
        addThumbnailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ImageUtil.isExternalStorageReadable()) {
                    Toast.makeText(getActivity(), "Error: Not have permisson to storage!", Toast.LENGTH_SHORT).show();
                    return; // ストレージに権限が無いので終了
                }
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK_IMAGEFILE);
            }
        });

        purchaseDateEditText = (EditText) view.findViewById(R.id.purchase_date_edit_text);
        purchaseDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.setOnDatePickerDialogListener(BookEditFragment.this);
                datePickerDialogFragment.show(getFragmentManager().beginTransaction(), "datePicker");
            }
        });

    }

    private String getGalleryPath() {
        return Environment.getExternalStorageState() + "/" + Environment.DIRECTORY_DCIM + "/";
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri, "r");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_book_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
            case R.id.menu_save:
                Log.d("Data of the book", "Title: " + titleEditText.getText() + " Price: "+ priceEditText.getText() + " PurchaseDate: " + purchaseDateEditText.getText());
                // 全入力欄が空欄でないかつ金額が数字になっていれば保存
                if (TextUtils.isEmpty(titleEditText.getText()) || TextUtils.isEmpty(priceEditText.getText()) || !TextUtils.isDigitsOnly(priceEditText.getText()) || TextUtils.isEmpty(purchaseDateEditText.getText())) {
                    return false;
                }
                Toast.makeText(getActivity(), "Save Succeeded!!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Bitmap getBitmapFromAssets(String imagePath) {
        try {
            InputStream inputStream = getResources().getAssets().open(imagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
