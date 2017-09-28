package com.example.administrator.irrigationworks.Ui.Retrofit;

import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.squareup.okhttp.ResponseBody;


import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


/**
 * ApiUtil.java
 * 功能：图片请求控件工具类
 * 描述：
 *
 * 时间：2017/8/1 17:56
 * 邮箱：xinruzhishui1991@sina.com
 */
public class ApiUtil {
//    https://www.megabes.cn  只是我们公司的服务器地址，你改成你公司的就行了
    private static final String HOST = FinalTozal.host;//换成你上传用的服务器地址
    private static Retrofit retrofit;
    private static final int DEFAULT_TIMEOUT = 10;//超时时长，单位：秒


    /**
     * 获取根服务地址
     */
    public static String getHOST() {
        return HOST;
    }

    /**
     * 初始化 Retrofit
     */
    public static Retrofit getApiRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //使用自定义的mGsonConverterFactory
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(FinalTozal.host)
                    .build();
//            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
//            okHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//            retrofit = new Retrofit.Builder()
//                    .client(okHttpBuilder.build())
//                    .baseUrl(HOST)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
        }
        return retrofit;
    }






}
