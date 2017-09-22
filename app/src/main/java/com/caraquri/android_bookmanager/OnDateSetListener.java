package com.caraquri.android_bookmanager;

import android.widget.DatePicker;

public interface OnDateSetListener {
    // DatePickerのダイアログ用のリスナー
    public void onDateSet(DatePicker datePicker, int year, int month, int day);
}
