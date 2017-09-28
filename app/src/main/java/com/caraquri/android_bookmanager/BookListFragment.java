package com.caraquri.android_bookmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class BookListFragment extends Fragment {

    private static final int LOAD_LIMIT = 3;
    private ListView listView;
    private BookAdapter adapter;
    private final List<Book> bookList = new ArrayList<>();
    private int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ツールバーの定義
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(null); // 戻るキーは非表示
        toolbar.setTitle(R.string.book_lineup);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        listView = (ListView) view.findViewById(R.id.book_list_view);
        adapter = new BookAdapter(getActivity(), R.layout.custom_table_item, bookList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Book selectedBook = bookList.get(i);
                String imagePath = selectedBook.getImagePath();
                String title = selectedBook.getTitle();
                int price = selectedBook.getPrice();
                String purchaseDate = selectedBook.getPurchaseDate();

                // 画面呼び出し
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, BookEditFragment.newInstance(imagePath, title, price, purchaseDate));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button loadMoreButton = (Button) getActivity().findViewById(R.id.load_button);
        loadMoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("Page", String.valueOf(page));
                Log.d("Length of list", String.valueOf(bookList.size()));
                page += 1;
                getBookDatas(page);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // 書籍一覧画面に入ったら，書籍データを格納した配列をクリアして1ページ目からのデータを取得
        bookList.clear();
        adapter.notifyDataSetChanged();
        page = 1;
        getBookDatas(page);
    }

    private void getBookDatas(int pageNum) {
        // まずは，tokenを取ってくる
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.PreferenceKeys.DATA_KEY, Context.MODE_PRIVATE);
        final String token = preferences.getString(Constants.PreferenceKeys.TOKEN, "");
        Log.d("Token", token);
        // 指定のページ番号における書籍リストを取得
        Retrofit retrofit = Client.setRetrofit();
        BookClient client = retrofit.create(BookClient.class);
        Call<BookResponse> call = client.getBookList(token, LOAD_LIMIT, pageNum);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Log.d("onResponse", response.toString());
                if (!response.isSuccessful()) {
                    return;
                }
                BookResponse bookResponse = response.body();
                for (BookResult bookResult: bookResponse.getBookResult()) {
                    Book book = new Book();
                    int bookId = bookResult.bookId;
                    String name = bookResult.name;
                    String imageUrl = bookResult.imageUrl;
                    int price = bookResult.price;
                    String purchaseDate = bookResult.purchaseDate;
                    Log.d("Book ID", String.valueOf(bookId));
                    Log.d("name", name);
                    Log.d("image", imageUrl);
                    Log.d("Price", String.valueOf(price));
                    Log.d("Purchase Date", String.valueOf(purchaseDate));
                    book.setBookId(bookId);
                    book.setImagePath(imageUrl);
                    book.setTitle(name);
                    book.setPrice(price);
                    book.setPurchaseDate(purchaseDate);
                    bookList.add(book);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Loading book data has been failed", Toast.LENGTH_SHORT).show();
                if (page >= 2) {
                    page -= 1; // 読み込みに失敗したため，足したページを1つ戻す
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_book_lineup, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent intent = new Intent(getActivity(), BookAddActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}