package com.caraquri.android_bookmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateSetListener listener = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        return new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Dialog,
                this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (this.listener == null) {
            return;
        }
        this.listener.onDateSet(datePicker, year, month, day);
    }

    public void setOnDatePickerDialogListner(OnDateSetListener listner) {
        this.listener = listner;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}