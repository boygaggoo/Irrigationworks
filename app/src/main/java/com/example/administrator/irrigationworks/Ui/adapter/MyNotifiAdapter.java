package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.bean.NotifiBean;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.WorkChechDetailListBean;
import com.example.administrator.irrigationworks.Ui.bean.WorkCheckListBean;
import com.example.administrator.irrigationworks.Ui.frament.Integratedframent;
import com.example.administrator.irrigationworks.Ui.uiview.SmartImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公告通知
 * Created by YZBbanban on 2017/9/1.
 */

public class MyNotifiAdapter extends BaseAdapter {
    private Context mcontext;
    private Integratedframent.AsynFragmentCallback callback;
    private List<NotifiBean> notifiBeans;
    public MyNotifiAdapter(Context context, List<NotifiBean> beans, Integratedframent.AsynFragmentCallback callback) {
        this.mcontext = context;
        this.notifiBeans = beans;
        this.callback = callback;
    }

    public void setDatas(Context context, List<NotifiBean> beans) {
        this.mcontext = context;
        this.notifiBeans = beans;
    }

    @Override
    public int getCount() {
        return notifiBeans != null ? notifiBeans.size() : 0;
    }

    @Override
    public WorkChechDetailListBean getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        public TextView tv_title;
        public TextView tv_time;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 初始化布局
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_notifi, null);
            // 初始化子控件
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NotifiBean bean = notifiBeans.get(position);
        holder.tv_title.setText(bean.getNotice_title());//标题名称
        holder.tv_time.setText(bean.getUpdate_time());//工地
        Log.d("通知公告", "已审核" + bean);
//        holder.iv_spread.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Map<String, Object> hashMap = new HashMap<String, Object>();
////
//                hashMap.put("noticeid", bean.getNoticeid());
//                hashMap.put("token", NimUIKit.getToken());
//                Log.d("点击了","bean.getNoticeid()"+bean.getNoticeid());
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            HttpUtils.doPostAsyn(FinalTozal.notice_rea, NimUIKit.getGson().toJson(hashMap), new GetTakList());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            }
//        });




        return convertView;
    }


    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("通知公告","通知公告"+result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                callback.onSuccess(onSuccssBea.getResult());
            }else {
                callback.onSuccess("没有数据");
            }
        }
    }
}
