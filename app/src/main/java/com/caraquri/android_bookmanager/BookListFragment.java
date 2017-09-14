package com.caraquri.android_bookmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return view;
    }
}