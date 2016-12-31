package com.mymobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mrka on 16-12-28.
 */

public class SpTools {
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.MYCONFIG, context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String deValue) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.MYCONFIG, context.MODE_PRIVATE);
        return sp.getString(key, deValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.MYCONFIG, context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBooean(Context context, String key, boolean deValue) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.MYCONFIG, context.MODE_PRIVATE);
        return sp.getBoolean(key, deValue);
    }

}
