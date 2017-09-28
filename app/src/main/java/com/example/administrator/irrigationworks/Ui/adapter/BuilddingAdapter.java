package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.bean.NewHistorBean;
import com.example.administrator.irrigationworks.Ui.bean.WorkChechDetailListBean;
import com.example.administrator.irrigationworks.Ui.bean.WorkCheckListBean;
import com.example.administrator.irrigationworks.Ui.uiview.SmartImageView;
import com.example.administrator.irrigationworks.UtilsTozals.MapUtilsManager;
import com.example.administrator.irrigationworks.View.ICommonGetPresenter;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YZBbanban on 2017/9/1.
 */

public class BuilddingAdapter extends BaseAdapter {
    private List<NewHistorBean> mDatas;
    private Context mcontext;
    private HashMap<Object, Object> params;//检查参数上传
    private ICommonGetPresenter presenter;
    private String state;
    private Map<String, Object> TaolmapTasks;
    public  Map<Integer,Boolean> isCheck = null;

    public BuilddingAdapter(Context context, List<NewHistorBean> beans, ICommonGetPresenter presenter, Map<String, Object> TaolmapTasks) {
        this.mcontext = context;
        this.mDatas = beans;
        this.presenter = presenter;
        this.TaolmapTasks = TaolmapTasks;
        params = new HashMap<Object, Object>();
        isCheck = new HashMap<Integer, Boolean>();
        state = "已完成";//必填  有一项不通过为“有隐患”,都通过为“已完成”
        initMap();
    }
    private void initMap(){
        for (int i = 0; i < mDatas.size(); i++) {
            isCheck.put(i, true);
        }
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
        public TextView mAdd;
        public RadioGroup radioGroup_select;
        public CheckBox bold_cb ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 初始化布局
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_buliding_site_list, null);
            // 初始化子控件
            holder = new ViewHolder();
            holder.mAdd = (TextView) convertView.findViewById(R.id.workplan_show_note);
            holder.radioGroup_select = (RadioGroup) convertView.findViewById(R.id.radioGroup_select);
            holder.bold_cb = (CheckBox) convertView.findViewById(R.id.bold_cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RadioGroup radioGroup_select = (RadioGroup) convertView.findViewById(R.id.radioGroup_select);
        final RadioButton radio_pass = (RadioButton) convertView.findViewById(R.id.radio_pass);
        final RadioButton radio_not_pass = (RadioButton) convertView.findViewById(R.id.radio_not_pass);
        final ViewHolder finalHolder = holder;

        final ViewHolder finalHolder1 = holder;
        holder.bold_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCheck.put(position, true);
                    finalHolder.bold_cb.setText("通过");
                    params = null;
                    params = MapUtilsManager.getThreadPool(mDatas.get(position).getSutupe(), mDatas.get(position).getName(), Enumerate.PASS);
                    Log.d("此时map", "radio_pass" + mDatas.get(position).getSutupe());
                    state = "已完成";
                    presenter.ICommonOkhttp(params, state, TaolmapTasks);//传入参数
                } else {
                    finalHolder.bold_cb.setText("不通过");
                    isCheck.put(position, false);
                    params = null;
                    params = MapUtilsManager.getThreadPool(mDatas.get(position).getSutupe(), mDatas.get(position).getName(), Enumerate.UNQUALIFIED);
                    Log.d("此时map", "radio_pass" + mDatas.get(position).getSutupe());
                    Log.d("此时map", "radio_not_pass" + mDatas.get(position).getName());
                    state = "有隐患";
                    presenter.ICommonOkhttp(params, state, TaolmapTasks);//传入参数


                }
            }
        });
        holder.bold_cb.setChecked(isCheck.get(position));

        final NewHistorBean moreItem = mDatas.get(position);

        holder.mAdd.setText(mDatas.get(position).getName());








        return convertView;
    }


}
