package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.activity.HiddentrouSavebleActvity;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.WorkChechDetailListBean;
import com.example.administrator.irrigationworks.Ui.bean.WorkCheckListBean;
import com.example.administrator.irrigationworks.Ui.uiview.SmartImageView;
import com.example.administrator.irrigationworks.UtilsTozals.MapUtilsManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YZBbanban on 2017/9/1.
 */

public class HidSaveAdapter extends BaseAdapter {
    private List<TaskChechItembean> mDatas;
    private Context mcontext;
    private HiddentrouSavebleActvity.AsyncCallback callbackt;
    private Map<Object, Object> taolmapTasks;
    public  Map<Integer,Boolean> isCheck = null;
    public HidSaveAdapter(Context context, List<TaskChechItembean> mDatas, HiddentrouSavebleActvity.AsyncCallback callback,Map<Object, Object> taolmapTasks) {
        this.mDatas = mDatas;
        this.mcontext = context;
        isCheck = new HashMap<Integer, Boolean>();
        this.taolmapTasks  =taolmapTasks;
        this.callbackt  = callback;
        initMap();
    }
    private void initMap(){
        for (int i = 0; i < mDatas.size(); i++) {
            isCheck.put(i, false);
        }
    }
    public void setDatas(Context context, List<TaskChechItembean> beans) {
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
        public TextView workplan_show_note;
        public CheckBox tv_resutl;

    }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                // 初始化布局
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_check_site_list, null);
                // 初始化子控件
                holder = new ViewHolder();
                holder.tv_resutl = (CheckBox) convertView.findViewById(R.id.tv_resutl);
                holder.workplan_show_note = (TextView) convertView.findViewById(R.id.workplan_show_note);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final TaskChechItembean moreItem=mDatas.get(position);
            holder.workplan_show_note.setText(moreItem.getKeys());
            taolmapTasks.put(moreItem.getKeys(),"未处理");
            callbackt.onSuccess(taolmapTasks);
            final ViewHolder finalHolder = holder;

            holder.tv_resutl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isCheck.put(position, true);


                        finalHolder.tv_resutl.setText("已处理");
                        finalHolder.tv_resutl.setTextColor(Color.BLUE);//系统自带的颜色类

                        taolmapTasks.put(moreItem.getKeys(),"已处理");
                        callbackt.onSuccess(taolmapTasks);

                    } else {
                        isCheck.put(position, true);


                        finalHolder.tv_resutl.setText("未处理");
                        finalHolder.tv_resutl.setTextColor(Color.BLUE);//系统自带的颜色类
                        taolmapTasks.put(moreItem.getKeys(),"未处理");
                        callbackt.onSuccess(taolmapTasks);


                    }
                }
            });

            holder.tv_resutl.setChecked(isCheck.get(position));
            return convertView;
        }


}
