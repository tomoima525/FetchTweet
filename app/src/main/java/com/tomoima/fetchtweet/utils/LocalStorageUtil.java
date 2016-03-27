package com.tomoima.fetchtweet.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tomoaki on 3/26/16.
 */
public class LocalStorageUtil {

    public static SharedPreferences getLocalStorage(Context context) {
        return context.getSharedPreferences("local_pref", Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key) {
        return getLocalStorage(context).getString(key, "");
    }

    public static void putString(Context context, String key, String value) {
        getLocalStorage(context).edit().putString(key, value).apply();
    }
}
