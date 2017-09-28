package com.example.administrator.irrigationworks.Application;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.UserPreferences.MyCache;
import com.example.administrator.irrigationworks.Ui.sign.ResolutionUtil;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePalApplication;

import java.io.File;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/8/10.
 */
public class NimUIKit extends LitePalApplication {
    public static int position;//图片位置
    public static ArrayList<String> urlList; //图片集合
    private static Context context;
    private static String mid = "a1489955";
    private static String role = "";//保存登录角色
    private static String constructionid = "";//保存登录角色
    private static String name = "";//保存角色名字
    private static String face = "";//设置人脸识别
    private static String[][] re = null;//坐标

    public static String[][] getRe() {
        return re;
    }

    public static void setRe(String[][] re) {
        NimUIKit.re = re;
    }

    public static String getFace() {
        return face;
    }

    public static void setFace(String face) {
        NimUIKit.face = face;
    }

    private static String auditState = "";//审核状态
    private static Bitmap auditbitmape = null;//图片

    public static Bitmap getAuditbitmape() {
        return auditbitmape;
    }

    public static void setAuditbitmape(Bitmap auditbitmape) {
        NimUIKit.auditbitmape = auditbitmape;
    }

    public static String getAuditState() {
        return auditState;
    }

    public static void setAuditState(String auditState) {
        NimUIKit.auditState = auditState;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        NimUIKit.name = name;
    }

    public static String getConstructionid() {
        return constructionid;
    }

    public static void setConstructionid(String constructionid) {
        NimUIKit.constructionid = constructionid;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        NimUIKit.role = role;
    }

    public static String getMid() {
        return mid;
    }

    public static void setMid(String mid) {
        NimUIKit.mid = mid;
    }

    private static Gson gson;
    private static boolean handlestop;
    // 拍照得到的照片文件
    private static File mPictureFile = null;

    public static File getmPictureFile() {
        return mPictureFile;
    }

    public static void setmPictureFile(File mPictureFile) {
        NimUIKit.mPictureFile = mPictureFile;
    }

    private static byte[] mImageData;
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        NimUIKit.token = token;
    }

    public static byte[] getmImageData() {
        return mImageData;
    }

    public static void setmImageData(byte[] mImageData) {
        NimUIKit.mImageData = mImageData;
    }

    public static boolean isHandlestop() {
        return handlestop;
    }

    public static void setHandlestop(boolean handlestop) {
        NimUIKit.handlestop = handlestop;
    }

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        NimUIKit.gson = gson;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        NimUIKit.context = context;
    }

    private static RequestQueue que;

    public static RequestQueue getQue() {
        return que;
    }

    public static void setQue(RequestQueue que) {
        NimUIKit.que = que;
    }

    public static ArrayList<String> getUrlList() {
        return urlList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MyCache.setContext(context);
        gson = new Gson();
        ResolutionUtil.getInstance().init(this);

        que = Volley.newRequestQueue(getApplicationContext());
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(getApplicationContext());
        ImageLoader.getInstance().init(configuration);
/**
 * 极光推送
 */
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
//        JPushInterface.stopPush(getApplicationContext());//开始停止接收消息

        // 在程序入口处传入appid，初始化SDK
        SpeechUtility.createUtility(context, "appid=" + getString(R.string.app_id));
    }
}
