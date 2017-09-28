package com.example.administrator.irrigationworks.UtilsTozals;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/4/13.
 */
public class NetUtil {
    /**
     * 通过判断wifi和mobile两种方式是否能够连接网络
     */
    public static boolean checkNetWork(Context context) {

        boolean isWIFI = isWIFI(context);
        boolean isMobile = isMobile(context);
        // 如果两个渠道都无法使用，提示用户设置网络信息
        if (isWIFI == false && isMobile == false) {
            return false;
        }
        return true;
    }
    /**
     * 判断是否WIFI处于连接状态
     *
     * @return
     */
    public static boolean isWIFI(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                return networkInfo.isConnected();
            }
        }
        return false;
    }
    /**
     * 判断是否APN列表中某个渠道处于连接状态
     *
     * @return
     */
    public static boolean isMobile(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.isConnected();
            }
        }
        return false;
    }
    // 判断wifi状态
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
            }
        }
        return false;
    }

    // 判断移动网络
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
            }
        }
        return false;
    }

    // 获取连接类型
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
            }
        }
        return -1;
    }

    // 判断网络连接状态
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
            }
        }
        return false;
    }
}
