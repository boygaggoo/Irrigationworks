package com.example.administrator.irrigationworks.View;


import java.util.Map;


/**联网请求
 * Created by Administrator on 2016/9/9 0009.
 */
public class ReadnPresenter implements IReadGetPresenter {
    private IReadDataListener view;

    public ReadnPresenter(IReadDataListener view) {
        this.view = view;
    }


    @Override
    public void ICommonOkhttp(String states) {
        view.equals(states);
    }
}
