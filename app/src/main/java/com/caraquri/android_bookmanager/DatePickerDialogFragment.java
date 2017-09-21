package com.caraquri.android_bookmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment {

    private Context applicationContext;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog dateBuilder = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        EditText editText = (EditText)getActivity().findViewById(R.id.purchase_date_edit_text);
                        editText.setText(String.format("%d/%02d/%02d", year, month+1, day));
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        return dateBuilder;
    }
}