package com.caraquri.android_bookmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class BookListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booklist, container, false);
        // ツールバーの定義
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_booklist);
        toolbar.setTitle(R.string.book_lineup);
        toolbar.inflateMenu(R.menu.menu_add);

        ArrayList<Book> bookList = new ArrayList<>();
        for (int i=0; i<20; i++) {
            Book book = new Book();
            book.setTitle("hoge"+i);
            book.setPrice(i);
            book.setPurchaseDate("2017/5/"+(i+1));
            bookList.add(book);
        }

        ListView listView = (ListView) view.findViewById(R.id.listview_book);

        // ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, books);
        BookAdapter adapter = new BookAdapter(getActivity());
        adapter.setBookList(bookList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    return true; // 戻るキーで終了しない
                }
                return false;
            }
        });

        return view;
    }

}