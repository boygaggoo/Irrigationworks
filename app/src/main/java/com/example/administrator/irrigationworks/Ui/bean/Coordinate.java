package com.example.administrator.irrigationworks.Ui.bean;

/**
 * 工地坐标
 * Created by Administrator on 2017/9/25.
 */
public class Coordinate {
    private double lnt;
    private double lng;

    public Coordinate() {

    }

    public Coordinate(double lnt, double lng) {
        this.lnt = lnt;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "lnt=" + lnt +
                ", lng=" + lng +
                '}';
    }

    public double getLnt() {
        return lnt;
    }

    public void setLnt(double lnt) {
        this.lnt = lnt;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
