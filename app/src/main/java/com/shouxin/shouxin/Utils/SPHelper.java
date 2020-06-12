package com.shouxin.shouxin.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.shouxin.shouxin.DataModel.User;

import static android.content.Context.MODE_PRIVATE;

public class SPHelper {

    private static final String PREFS_NAME = "USERINFOFILE";

    public static void saveUserInfo(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", user.getName());
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    public static void setInitStatus(Context context, boolean status) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("initialized", status);
        editor.apply();
    }

    public static boolean getInitStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean("initialized", false);
    }

}
