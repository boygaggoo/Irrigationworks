package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.bean.WorkChechDetailListBean;
import com.example.administrator.irrigationworks.Ui.bean.WorkCheckListBean;
import com.example.administrator.irrigationworks.Ui.uiview.SmartImageView;

import java.util.List;

/**
 * Created by YZBbanban on 2017/9/1.
 */

public class MyOrderAdapter extends BaseAdapter {
    private List<WorkCheckListBean> mDatas;
    private Context mcontext;

    public MyOrderAdapter(List<WorkCheckListBean> mDatas) {
        this.mDatas = mDatas;
    }

    public void setDatas(Context context, List<WorkCheckListBean> beans) {
        this.mcontext = context;
        mDatas = beans;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
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
        public SmartImageView iv_order_img;
        public TextView tv_order_name;
        public TextView tv_order_price;
        public TextView tv_order_status;
        public TextView iv_order_create_time;
        public ImageView iv_order_status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 初始化布局
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_my_order, null);
            // 初始化子控件
            holder = new ViewHolder();
            holder.iv_order_img = (SmartImageView) convertView.findViewById(R.id.iv_order_img);
            holder.tv_order_name = (TextView) convertView.findViewById(R.id.tv_order_name);
            holder.tv_order_price = (TextView) convertView.findViewById(R.id.tv_order_price);
            holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);
            holder.iv_order_create_time = (TextView) convertView.findViewById(R.id.iv_order_create_time);
            holder.iv_order_status = (ImageView) convertView.findViewById(R.id.iv_order_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WorkCheckListBean bean = mDatas.get(position);
        holder.tv_order_name.setText(bean.getUser().getRealname());//人名称
        holder.tv_order_price.setText(bean.getConstruction().getName());//工地
        String status="";

        holder.tv_order_status.setText("已打卡");
        holder.iv_order_create_time.setText(bean.getDate_time());//打卡时间



        return convertView;
    }


}
