package com.example.administrator.irrigationworks.Ui.frament;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.adapter.MyNotifiAdapter;
import com.example.administrator.irrigationworks.Ui.bean.NotifiBean;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 公告模块
 * Created by Administrator on 2017/8/10.
 */
public class Integratedframent extends TFragment {

    @Bind(R.id.lv_list)
    ListView lvList;
    private Activity ctx;
    private View layout;
    private MyNotifiAdapter myNotifiAdapter;
    private List<NotifiBean> notifiBeans;
    //倒序 add by bb


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == null) {

            ctx = this.getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.fragment_notification,
                    null);
            ButterKnife.bind(this, layout);
            notifiBeans = new ArrayList<>();

            getData();

            lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("点击了", "点击了");
                    Boolean net = NetUtil.checkNetWork(getActivity());
                    if (net) {
                        final Map<String, Object> hashMap = new HashMap<String, Object>();
                        final Map<String, Object> hashMaps = new HashMap<String, Object>();
                        hashMaps.put("create_time", 1);//升序
                        hashMap.put("noticeid", notifiBeans.get(position).getNoticeid());
                        hashMap.put("token", NimUIKit.getToken());
                        hashMap.put("order", hashMaps);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HttpUtils.doPostAsyn(FinalTozal.notice_rea, NimUIKit.getGson().toJson(hashMap), new GetNotices());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        UtilsTool.showShortToast(getActivity(), "没有网络");
                    }

                }
            });


        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        ButterKnife.bind(this, layout);
        return layout;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void getData() {

        final Map<String, Object> hashMap = new HashMap<String, Object>();
//
        hashMap.put("token", NimUIKit.getToken());


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.noticeList, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<NotifiBean> nBs = new ArrayList<>();

    /**
     * 通知公告
     */
    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {

                OnSuccssBean onSuccssBean = JSON.parseObject(onSuccssBea.getResult(), OnSuccssBean.class);
                Log.d("通知公告", "通知公告" + onSuccssBean.getResult());
                notifiBeans = JSON.parseArray(onSuccssBean.getResult(), NotifiBean.class);
                Log.d("通知公告", "通知公告" + notifiBeans);
                if (notifiBeans != null && notifiBeans.size() > 0) {
                    //倒序排列 add by bb
                    for (int i = notifiBeans.size() - 1; i >= 0; i--) {
                        nBs.add(notifiBeans.get(i));
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAdapter(nBs);
                    }
                });
            } else {

            }
        }
    }

    private void showAdapter(final List<NotifiBean> notifiBeans) {
        myNotifiAdapter = new MyNotifiAdapter(getActivity(), notifiBeans, new AsynFragmentCallback() {
            @Override
            public void onSuccess(String re) {
                if (re.equals("没有数据")) {
                    UtilsTool.showShortToast(getActivity(), "没有数据");
                } else {
                    if (!TextUtils.isEmpty(re)) {
                        Intent iten = new Intent(getActivity(), NotificationActivity.class);
                        iten.putExtra("path", re);
                        startActivity(iten);
                    } else {
                        UtilsTool.showShortToast(getActivity(), "没有数据");
                    }

                }
            }
        });
        lvList.setAdapter(myNotifiAdapter);


    }

    public interface AsynFragmentCallback {

        void onSuccess(String re);


    }

    private class GetNotices implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("通知公告", "通知公告" + result);
            final OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent iten = new Intent(getActivity(), NotificationActivity.class);
                        iten.putExtra("path", onSuccssBea.getResult());
                        startActivity(iten);
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(getActivity(), "没有内容");
                    }
                });
            }
        }
    }
}
