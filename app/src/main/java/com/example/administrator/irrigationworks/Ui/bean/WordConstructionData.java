package com.example.administrator.irrigationworks.Ui.bean;

import org.litepal.crud.DataSupport;

/**
 * 获取考勤信息
 * Created by Administrator on 2017/9/5.
 */
public class WordConstructionData  {
    private String start_time;
    private String name;
    private String result;

    public WordConstructionData() {
    }

    public WordConstructionData(String start_time, String name, String result) {
        this.start_time = start_time;
        this.name = name;
        this.result = result;
    }

    @Override
    public String toString() {
        return "WordConstructionData{" +
                "start_time='" + start_time + '\'' +
                ", name='" + name + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
