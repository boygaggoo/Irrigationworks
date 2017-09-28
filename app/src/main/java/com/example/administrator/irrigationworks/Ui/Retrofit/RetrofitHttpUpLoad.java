package com.example.administrator.irrigationworks.Ui.Retrofit;

import android.content.Context;

import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.google.gson.Gson;

import org.litepal.util.LogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/9/6.
 */
public class RetrofitHttpUpLoad {
    /**
     * 超时时间60s
     */
    private static final long DEFAULT_TIMEOUT = 60;
    private volatile static RetrofitHttpUpLoad mInstance;
    public Retrofit mRetrofit;
    public RequestServes mHttpService;
    private static Map<String, RequestBody> params;

    private RetrofitHttpUpLoad() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(FinalTozal.host)
                .client(genericClient())
                .build();
        mHttpService = mRetrofit.create(RequestServes.class);
    }

    public static RetrofitHttpUpLoad getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitHttpUpLoad.class) {
                if (mInstance == null)
                    mInstance = new RetrofitHttpUpLoad();
                params = new HashMap<String, RequestBody>();
            }
        }
        return mInstance;
    }

    /**
     * 添加统一超时时间,http日志打印
     *
     * @return
     */
    private OkHttpClient genericClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }



    /**
     * 添加参数
     * 根据传进来的Object对象来判断是String还是File类型的参数
     */
    public RetrofitHttpUpLoad addParameter(String key, Object o) {

        if (o instanceof String) {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), (String) o);
            params.put(key, body);
        } else if (o instanceof File) {
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), (File) o);
            params.put(key + "\"; filename=\"" + ((File) o).getName() + "", body);
        }
        return this;
    }

    /**
     * 构建RequestBody
     */
    public Map<String, RequestBody> bulider() {

        return params;
    }

    public void clear(){
        params.clear();
    }
}
