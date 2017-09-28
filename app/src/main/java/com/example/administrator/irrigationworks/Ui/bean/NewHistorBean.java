package com.example.administrator.irrigationworks.Ui.bean;

/**
 * Created by GYKJ on 2017/9/9.
 */
public class NewHistorBean {
    private String sutupe  ;
    private String name;

    public NewHistorBean() {
    }

    public NewHistorBean(String sutupe, String name) {
        this.sutupe = sutupe;
        this.name = name;
    }

    public String getSutupe() {
        return sutupe;
    }

    public void setSutupe(String sutupe) {
        this.sutupe = sutupe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NewHistorBean{" +
                "sutupe='" + sutupe + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
