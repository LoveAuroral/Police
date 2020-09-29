package com.dark_yx.policemain.nfcDemo;

import android.content.Context;
import android.preference.PreferenceManager;

public class SpHelper {
    private Context context;
    public String mCardID = "Card_ID";

    public SpHelper(Context context) {
        this.context = context;
    }

    public boolean putString(String key, String value) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public String getString(String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }
}
