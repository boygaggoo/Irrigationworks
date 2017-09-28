package com.example.administrator.irrigationworks.Ui.UserPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.irrigationworks.Application.NimUIKit;


/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class Preference {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";
    private static final String USER_INFO = "user_info";
    private static final String TEACHERID = "teacher_id";
    private static final String USERID = "user_id";
    private static final String AUTOLOGIN = "auto_login";
    private static final String TELE = "tele";
    private static final String LAG = "lag";
    private static final String LNG = "lng";
//保存用户名
    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }
    //保存密码
    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    //地理坐标
    public static void savalng(String lng) {
        saveString(LNG, lng);
    }

    public static String getlng() {
        return getString(LNG);
    }
    //地理坐标
    public static void savaLat(String lag) {
        saveString(LAG, lag);
    }

    public static String getLat() {
        return getString(LAG);
    }




    public static void saveAutoLogin(Boolean account) {
        saveBoolean(AUTOLOGIN, account);
    }

    public static boolean getsaveAutoLogin() {
        return getBoolean(AUTOLOGIN);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static void saveBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static Boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }


    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        return MyCache.getContext().getSharedPreferences("Demo", Context.MODE_PRIVATE);
    }


    // 清空记录
    public static void clear() {
        SharedPreferences sp = NimUIKit.getContext().getSharedPreferences(USER_INFO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
