package com.example.administrator.irrigationworks.Ui.bean;

/**
 * 图片解析类
 * Created by Administrator on 2017/9/8.
 */
public class PicBean {
    private String path;
    private String filetype;
    private String filename;
    private String Md5;

    public PicBean(String path, String filetype, String filename, String md5) {
        this.path = path;
        this.filetype = filetype;
        this.filename = filename;
        Md5 = md5;
    }

    public PicBean() {
    }

    @Override
    public String toString() {
        return "PicBean{" +
                "path='" + path + '\'' +
                ", filetype='" + filetype + '\'' +
                ", filename='" + filename + '\'' +
                ", Md5='" + Md5 + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMd5() {
        return Md5;
    }

    public void setMd5(String md5) {
        Md5 = md5;
    }
}
