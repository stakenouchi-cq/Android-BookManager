package com.caraquri.android_bookmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


public class BookAdapter extends ArrayAdapter<Book> {
    private Context context;
    private int textViewResourceId;
    private LayoutInflater layoutInflater;

    public BookAdapter(Context context, int textViewResourceId, List<Book> bookList) {
        super(context, textViewResourceId, bookList);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view;
        Book book = getItem(i); // 選択した書籍に対するオブジェクト

        if (convertView == null) {
            view = layoutInflater.inflate(textViewResourceId, null);
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

        // Glideでの画像の読込時およびエラー発生時に表示する画像の指定
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.raw.now_loading)
                .error(R.drawable.ic_load_error);
        // GlideでURL上にある画像を取得して表示
        Glide.with(context)
                .load(book.getImageUrl())
                .apply(requestOptions)
                .into(thumbnail);

        return view;
    }

}
