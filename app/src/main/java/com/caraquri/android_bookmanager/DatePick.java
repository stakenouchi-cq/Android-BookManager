package com.caraquri.android_bookmanager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import java.util.Calendar;

public class DatePick extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year, month, dayOfMonth);

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
    }
}
