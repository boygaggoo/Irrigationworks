package com.example.administrator.irrigationworks.UtilsTozals;

/**
 * Created by Administrator on 2017/7/5.
 */
public class ThreadUtils {
    private static Thread instance = null;

    private ThreadUtils(){}

    public static Thread getInstance() {
        if(instance == null){//懒汉式
            instance = new Thread();
        }
        return instance;
    }
}
