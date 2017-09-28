package com.example.administrator.irrigationworks.UtilsTozals;

import com.iflytek.cloud.thirdparty.T;

/**
 * Created by Administrator on 2017/9/13.
 */
public class ResultNameCode {
    private T result;

    public ResultNameCode() {
    }

    public ResultNameCode(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultNameCode{" +
                "result=" + result +
                '}';
    }
}
