package com.example.administrator.irrigationworks.Ui.UserPreferences;

import android.content.Context;


public class MyCache {
    private static Context context;

    private static String account;


    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }



    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyCache.context = context.getApplicationContext();
    }


}
