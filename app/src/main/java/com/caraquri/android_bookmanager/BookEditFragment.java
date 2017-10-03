package com.caraquri.android_bookmanager;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class BookEditFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = BookEditFragment.class.getSimpleName();
    private static final int REQUEST_PICK_IMAGEFILE = 1;
    private static final int REQUEST_PICK_PERMISSION = 2;
    private static final String ARGS_BOOKID = "args_bookId";
    private static final String ARGS_IMAGEURL = "args_imageUrl";
    private static final String ARGS_TITLE = "args_title";
    private static final String ARGS_PRICE = "args_price";
    private static final String ARGS_PURCHASEDATE = "args_purchaseDate";

    private EditText titleEditText;
    private EditText priceEditText;
    private EditText purchaseDateEditText;
    private ImageView bookThumbnailImageView;

    public static BookEditFragment newInstance(int bookId, String imageUrl, String title, int price, String purchaseDate) {
        BookEditFragment fragment = new BookEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_BOOKID, bookId);
        args.putString(ARGS_IMAGEURL, imageUrl);
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

        bookThumbnailImageView = (ImageView) view.findViewById(R.id.book_thumbnail);
        titleEditText = (EditText) view.findViewById(R.id.title_edit_text);
        priceEditText = (EditText) view.findViewById(R.id.price_edit_text);
        purchaseDateEditText = (EditText) view.findViewById(R.id.purchase_date_edit_text);

        Bundle args = getArguments();
        String imageUrl = args.getString(ARGS_IMAGEURL);
        String title = args.getString(ARGS_TITLE);
        int price = args.getInt(ARGS_PRICE);
        String purchaseDate = args.getString(ARGS_PURCHASEDATE);

        // 画面遷移直後の初期値を設定
        titleEditText.setText(title);
        priceEditText.setText(String.valueOf(price));
        purchaseDateEditText.setText(purchaseDate);

        // GlideでURL上にある画像を取得して表示
        Glide.with(getActivity())
                .load(Uri.parse(imageUrl))
                .apply(ImageUtil.getRequestOptionsOfBookThumbnail())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(bookThumbnailImageView);

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

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 許可されていない
            ActivityCompat.requestPermissions(getActivity(),
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            checkPermission();
            Uri uri = data.getData();
            Log.i("", "Uri: " + uri.toString());
            // Glideでギャラリーにある画像のURIを取得して表示
            Glide.with(this)
                    .load(Uri.parse(uri.toString()))
                    .apply(ImageUtil.getRequestOptionsOfBookThumbnail())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(bookThumbnailImageView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PICK_PERMISSION) {
            // requestPermissionsで設定した順番で結果が格納されています。
            if(!(grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 許可されていないので外部ストレージパーミッションの確認ダイアログを表示
                Toast.makeText(getActivity(), "Not have perimission to storage.", Toast.LENGTH_SHORT).show();
            }
            return; // パーミッションが既にあれば大丈夫なので終わり
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                // 全入力欄が空欄でないかつ金額が数字になっていれば保存
                if (TextUtils.isEmpty(titleEditText.getText()) || TextUtils.isEmpty(priceEditText.getText()) || !TextUtils.isDigitsOnly(priceEditText.getText()) || TextUtils.isEmpty(purchaseDateEditText.getText())) {
                    return false;
                }
                // 書籍データの保存に入る
                String name = titleEditText.getText().toString();
                int price = Integer.valueOf(priceEditText.getText().toString()).intValue();
                String purchaseDate = purchaseDateEditText.getText().toString();
                Bitmap bookThumbnailBitmap = ((BitmapDrawable) bookThumbnailImageView.getDrawable()).getBitmap();
                String image = ImageUtil.encodeToBase64(bookThumbnailBitmap);

                // tokenを取得
                SharedPreferences preferences = getContext().getSharedPreferences(Constants.PreferenceKeys.DATA_KEY, Context.MODE_PRIVATE);
                final String token = preferences.getString(Constants.PreferenceKeys.TOKEN, "");
                Log.d("Token", token);

                Retrofit retrofit = Client.setRetrofit();
                BookClient client = retrofit.create(BookClient.class);
                Call<JSONObject> call = client.editBookData(token, getArguments().getInt(ARGS_BOOKID), new BookRequest(name, image, price, purchaseDate));
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        Toast.makeText(getContext(), "Save Succeeded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        Log.e(LOG_TAG, Constants.LogMessages.CALLBACK_RETROFIT, t);
                        Toast.makeText(getContext(), "Save failed", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
