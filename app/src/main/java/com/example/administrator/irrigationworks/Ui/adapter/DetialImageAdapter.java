package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.uiview.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZBbanban on 16/6/5.
 */
public class DetialImageAdapter extends BaseAdapter<String> {
    private List<String> data;
    private static final String TAG = "DetialImageAdapter";

    public DetialImageAdapter(Context context, ArrayList<String> data) {
        super(context, data);
        this.data = data;
        Log.i(TAG, "DetialImageAdapter: ");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String d = data.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView=getLayoutInflater().inflate(R.layout.item_photo_grivie, null);
            // 初始化子控件
            holder.iv_order_img = (ImageView) convertView.findViewById(R.id.issuesIv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.i(TAG, "getView: "+d);
        //glide 加载
        Glide.with(getContext()).load(d).dontAnimate().into(holder.iv_order_img);
        //适应显示
//        holder.iv_order_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_order_img;

    }

}
