package com.caraquri.android_bookmanager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    public static void setPreferences(Context context, int userId, String email, String token) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PreferenceKeys.DATA_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.PreferenceKeys.USER_ID, userId);
        editor.putString(Constants.PreferenceKeys.EMAIL, email);
        editor.putString(Constants.PreferenceKeys.TOKEN, token);
        editor.apply();
    }

}
