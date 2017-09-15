package com.caraquri.android_bookmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class BookEditFragment extends Fragment {

    EditText purchaseDate;

    public BookEditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("Test:","Here is book edit display");
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        // ツールバーの定義
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.book_edit);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        purchaseDate = (EditText) view.findViewById(R.id.form_purchasedate);

        purchaseDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                purchaseDate.setText(String.format("%d / %02d / %02d", i, i1+1, i2));
                            }
                        },
                        date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE)
                );
                datePickerDialog.show(); // show Dialog
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_add).setVisible(false);
        menu.findItem(R.id.menu_save).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                break;
            case R.id.menu_save:
                Toast.makeText(getActivity(), "Save Succeeded!!.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

}
