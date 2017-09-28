package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by GYKJ on 2017/9/9.
 */
public class HistorBean {
    private String name;
    private String resule;

    public HistorBean() {
    }

    public HistorBean(String name, String resule) {
        this.name = name;
        this.resule = resule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResule() {
        return resule;
    }

    public void setResule(String resule) {
        this.resule = resule;
    }

    @Override
    public String toString() {
        return "HistorBean{" +
                "name='" + name + '\'' +
                ", resule='" + resule + '\'' +
                '}';
    }
}
