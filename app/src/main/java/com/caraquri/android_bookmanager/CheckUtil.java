package com.caraquri.android_bookmanager;

import android.widget.EditText;

public class CheckUtil {

    public static boolean isNumber(EditText editText) {
        try {
            Integer.parseInt(editText.getText().toString());
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isNull(EditText editText) {
        return editText.getText().toString().equals("");
    }

}
