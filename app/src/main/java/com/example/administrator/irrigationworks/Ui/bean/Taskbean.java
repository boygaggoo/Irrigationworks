package com.example.administrator.irrigationworks.Ui.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/6.
 */
public class Taskbean  {
     private String checkitem;

    public Taskbean() {
    }

    public Taskbean(String checkitem) {
        this.checkitem = checkitem;
    }

    public String getCheckitem() {
        return checkitem;
    }

    public void setCheckitem(String checkitem) {
        this.checkitem = checkitem;
    }

    @Override
    public String toString() {
        return "Taskbean{" +
                "checkitem='" + checkitem + '\'' +
                '}';
    }
}
