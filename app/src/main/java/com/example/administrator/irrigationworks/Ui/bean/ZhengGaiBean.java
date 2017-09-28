package com.example.administrator.irrigationworks.Ui.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */
public class ZhengGaiBean {
    private String contect;
    private ArrayList<String> pics;

    public ZhengGaiBean() {
    }

    public ZhengGaiBean(String contect, ArrayList<String> pics) {
        this.contect = contect;
        this.pics = pics;
    }

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    @Override
    public String toString() {
        return "ZhengGaiBean{" +
                "contect='" + contect + '\'' +
                ", pics=" + pics +
                '}';
    }
}
