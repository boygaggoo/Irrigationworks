package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by Administrator on 2017/9/6.
 */
public class TaskChechItembean {
     private String keys;
     private String objs;

    public TaskChechItembean() {

    }

    public TaskChechItembean(String keys, String objs) {
        this.keys = keys;
        this.objs = objs;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getObjs() {
        return objs;
    }

    public void setObjs(String objs) {
        this.objs = objs;
    }

    @Override
    public String toString() {
        return "TaskChechItembean{" +
                "keys='" + keys + '\'' +
                ", objs='" + objs + '\'' +
                '}';
    }
}
