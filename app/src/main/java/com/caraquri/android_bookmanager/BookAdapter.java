package com.caraquri.android_bookmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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
        Book book = bookList.get(i); // 選択した書籍に対するオブジェクト

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.custom_table_item, parent, false);
        } else {
            view = convertView;
        }

        TextView title = (TextView) view.findViewById(R.id.book_title);
        TextView price = (TextView) view.findViewById(R.id.book_price);
        TextView purchase_date = (TextView) view.findViewById(R.id.book_purchase_date);
        ImageView thumbnail = (ImageView) view.findViewById(R.id.book_thumbnail);

        // 各TextView及び書籍画像のImageviewをセットする
        title.setText(book.getTitle());
        price.setText(context.getString(R.string.price_notation, book.getPrice()));
        purchase_date.setText(book.getPurchaseDate());
        thumbnail.setImageBitmap(book.getImgBmp());

        return view;
    }
}
