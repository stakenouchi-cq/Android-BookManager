package com.caraquri.android_bookmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class BookAddActivity extends AppCompatActivity {

    EditText purchaseDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar_bookadd);
        toolbar.setTitle(R.string.book_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        purchaseDate = (EditText) findViewById(R.id.form_purchasedate);

        purchaseDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BookAddActivity.this,
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_add).setVisible(false);
        menu.findItem(R.id.menu_save).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_save:
                Toast.makeText(this, "Save Succeeded!!.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

}
