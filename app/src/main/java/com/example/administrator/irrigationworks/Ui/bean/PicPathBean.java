package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by Administrator on 2017/9/19.
 */
public class PicPathBean {
    private String path;

    public PicPathBean() {
    }

    public PicPathBean(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "PicPathBean{" +
                "path='" + path + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
