package com.schoolmanagement.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.schoolmanagement.android.BuildConfig;

/**
 *
 */
public class ApplicationPreferences {

    private static ApplicationPreferences instance;
    private final SharedPreferences sharedPrefs;

    private ApplicationPreferences(Context context) {
        String sharedPrefsFileName = BuildConfig.APPLICATION_ID + ".prefs";
        this.sharedPrefs = context.getSharedPreferences(sharedPrefsFileName, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new ApplicationPreferences(context);
        }
    }

    public static ApplicationPreferences get() {
        if (instance == null) {
            throw new IllegalStateException("ApplicationPreferences should be initialized by calling init()");
        }
        return instance;
    }

    public void clearSharedPreferences() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }

    public void setBooleanValue(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sharedPrefs.getBoolean(key, false);
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return sharedPrefs.getString(key, null);
    }

    public void setLongValue(String key, long value) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLongValue(String key) {
        return sharedPrefs.getLong(key, 0L);
    }
}
