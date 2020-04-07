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
        String s = preferences.getString("username", null);
        return s;
    }

}
