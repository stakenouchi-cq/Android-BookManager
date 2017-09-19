package com.caraquri.android_bookmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class BookListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ツールバーの定義
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.book_lineup);
        // 左上部の戻るキーを非表示
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        String imgPaths[] = {
                "cppbook.png",
                "javabook.png",
                "Oxford_Dict.png",
                "sw3book.png",
                "toeic_official.png",
                "toeicbook.png"
        };

        final ArrayList<Book> bookList = new ArrayList<>();
        for (int i=0; i<6; i++) {
            Book book = new Book();
            Bitmap imgBmp = convertBmpFromAssets(imgPaths[i]);
            book.setImgBmp(imgBmp);
            book.setImgStr(ImageUtil.encodeToBase64(imgBmp));
            book.setTitle("hoge"+i);
            book.setPrice((i+1)*1000);
            book.setPurchaseDate("2017/05/0"+(i+1));
            bookList.add(book);
        }

        ListView listView = (ListView) view.findViewById(R.id.listview_book);

        BookAdapter adapter = new BookAdapter(getActivity());
        adapter.setBookList(bookList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookEditFragment fragment = new BookEditFragment();

                // 書籍編集画面遷移用の値渡し
                Bundle args = new Bundle();
                args.putString("imgStr", String.valueOf(bookList.get(i).getImgStr()));
                args.putString("title", String.valueOf(bookList.get(i).getTitle()));
                args.putInt("price", bookList.get(i).getPrice());
                args.putString("purchaseDate", String.valueOf(bookList.get(i).getPurchaseDate()));
                fragment.setArguments(args);

                // 画面呼び出し
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_add).setVisible(true);
        menu.findItem(R.id.menu_save).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent intent = new Intent(getActivity(), BookAddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    private Bitmap convertBmpFromAssets(String imgPath) {
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