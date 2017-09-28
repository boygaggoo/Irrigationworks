package com.example.administrator.irrigationworks.View;


import java.util.Map;


/**联网请求
 * Created by Administrator on 2016/9/9 0009.
 */
public class CommonPresenter implements ICommonGetPresenter {
    private IbuildingDataListener view;

    public CommonPresenter(IbuildingDataListener view) {
        this.view = view;
    }


    @Override
    public void ICommonOkhttp(Map<Object, Object> map,String states,Map<String, Object> maps) {
        if(map!=null){//接收数据
            view.reclaim(map,states,maps);
        }

    }
}
