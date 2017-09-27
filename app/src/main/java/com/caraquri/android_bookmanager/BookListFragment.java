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

    private ListView listView;
    private static final int LOAD_LIMIT = 3;
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
        page = 1;

        SharedPreferences preferences = getContext().getSharedPreferences(Constants.PreferenceKeys.DATA_KEY, Context.MODE_PRIVATE);
        final String token = preferences.getString(Constants.PreferenceKeys.TOKEN, "");
        Log.d("Token", token);

        // ツールバーの定義
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(null); // 戻るキーは非表示
        toolbar.setTitle(R.string.book_lineup);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        String imagePaths[] = {
                "cppbook.png",
                "javabook.png",
                "Oxford_Dict.png",
                "sw3book.png",
                "toeic_official.png",
                "toeicbook.png"
        };

        final List<Book> bookList = new ArrayList<>();
        for (int i=0; i<6; i++) {
            Book book = new Book();
            book.setImagePath(imagePaths[i]);
            book.setTitle("hoge"+i);
            book.setPrice((i+1)*1000);
            book.setPurchaseDate("2017/05/0"+(i+1));
            bookList.add(book);
        }

        listView = (ListView) view.findViewById(R.id.book_list_view);

        BookAdapter adapter = new BookAdapter(getActivity(), R.layout.custom_table_item, bookList);
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
                Retrofit retrofit = Client.setRetrofit();
                BookClient client = retrofit.create(BookClient.class);
                Call<BookResponse> call = client.getBookList(token, LOAD_LIMIT, page);
                call.enqueue(new Callback<BookResponse>() {
                    @Override
                    public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                        Log.d("onResponse", response.toString());
                        if (!response.isSuccessful()) {
                            return;
                        }
                        BookResponse bookResponse = response.body();
                        for (BookResult bookResult: bookResponse.getBookResult()) {
                            Log.d("Book ID", String.valueOf(bookResult.bookId));
                            Log.d("name", bookResult.name);
                            Log.d("image", bookResult.imageUrl);
                            Log.d("Price", String.valueOf(bookResult.price));
                            Log.d("Purchase Date", bookResult.purchaseDate);
                        }
                    }
                    @Override
                    public void onFailure(Call<BookResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                Toast.makeText(getActivity(), "More books will be loaded.", Toast.LENGTH_SHORT).show();
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