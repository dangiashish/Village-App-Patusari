package com.jhunjhunu.patusari.utility;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPrefUtil {
    private static final String PREFERENCES_NAME = "patusari-app";

    public static void setString(String key, String value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static String getString(String key, String defaultValue, Context context) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static void setInt(String key, int value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defaultValue, Context context) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue, Context context) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void clearKey(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clearAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}
