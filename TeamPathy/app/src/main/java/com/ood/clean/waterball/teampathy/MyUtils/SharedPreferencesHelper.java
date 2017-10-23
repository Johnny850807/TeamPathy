package com.ood.clean.waterball.teampathy.MyUtils;


import android.content.Context;
import android.content.SharedPreferences;

import com.ood.clean.waterball.teampathy.R;

public class SharedPreferencesHelper {
    public static final String NONE = "";
    private static SharedPreferences sharedPreferences;

    /**
     * The helper should be initialized to make sure that the sharedPreferences initialized.
     */
    public static void init(Context context){
        String name = context.getString(R.string.app_name);
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static void putString(String name, String value){
        sharedPreferences.edit().putString(name, value).apply();
    }

    public static void putBoolean(String name, boolean value){
        sharedPreferences.edit().putBoolean(name, value).apply();
    }

    public static String getString(String name){
        return sharedPreferences.getString(name, NONE);
    }

    public static boolean getBoolean(String name){
        return sharedPreferences.getBoolean(name, false);
    }

    public static boolean getBoolean(String name, boolean defaultBoolean){
        return sharedPreferences.getBoolean(name, defaultBoolean);
    }
}
