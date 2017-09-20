package com.caraquri.android_bookmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class BookAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater = null;
    private List<Book> bookList;

    public BookAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int i) {
        return bookList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return bookList.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view = null;

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.custom_table_item, parent, false);
        } else {
            view = convertView;
        }

        // テキストサイズの指定
        ((TextView) view.findViewById(R.id.book_title)).setTextSize(30);
        ((TextView) view.findViewById(R.id.book_price)).setTextSize(20);
        ((TextView) view.findViewById(R.id.book_purchase_date)).setTextSize(20);
        ((TextView) view.findViewById(R.id.text_sign)).setTextSize(25);
        // TextViewへの入力内容
        ((TextView) view.findViewById(R.id.book_title)).setText(bookList.get(i).getTitle());
        ((TextView) view.findViewById(R.id.book_price)).setText(context.getResources().getString(R.string.price_notation, bookList.get(i).getPrice()));
        ((TextView) view.findViewById(R.id.book_purchase_date)).setText(bookList.get(i).getPurchaseDate());

        // 書籍のサムネイルを表示
        ((ImageView) view.findViewById(R.id.book_thumbnail)).setImageBitmap(bookList.get(i).getImgBmp());

        return view;
    }
}
