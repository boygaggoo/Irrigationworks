package com.example.administrator.irrigationworks.Ui.litepal;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */
public class ConstructionListDataLitepal extends DataSupport {

    private String  result;

    public ConstructionListDataLitepal() {

    }

    public ConstructionListDataLitepal(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ConstructionListDataLitepal{" +
                "result='" + result + '\'' +
                '}';
    }
}
