package com.example.administrator.irrigationworks.Ui.litepal;

import org.litepal.crud.DataSupport;

/**
 * 临时使用面部ID
 * Created by Administrator on 2017/9/6.
 */
public class MianBuID extends DataSupport {
   private String mid;

    public MianBuID(String mid) {
        this.mid = mid;
    }
    public MianBuID() {

    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "MianBuID{" +
                "mid='" + mid + '\'' +
                '}';
    }
}
