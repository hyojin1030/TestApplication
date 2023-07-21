package com.test.offlinetest;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {

    public static void setValue(Context context, String key, String value) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref), context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValue(Context context, String key) {
        String value = "";
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_pref), context.MODE_PRIVATE);
            value = sharedPreferences.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            value= "";
        }
        return value;
    }
}
