package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Jpush.EnmuType;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.activity.buildingSiteActivity;
import com.example.administrator.irrigationworks.Ui.bean.HistorBean;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.View.ICommonGetPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEAD = 2;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //加载完成已经没有更多数据了
    public static final int NO_MORE_DATA = 2;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private Context mContext;
    private List<EnmuType> typeList = new ArrayList<EnmuType>();
    private HashMap<String, Object> params;//检查参数上传
    private  String state;
    private List<TaskChechItembean> historBeans;
    public CheckItemListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public CheckItemListAdapter(Context mContext, List<TaskChechItembean> historBeans) {
        this.mContext = mContext;
        this.historBeans = historBeans;
        params = new HashMap<String, Object>();
    }



    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_site_list, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent,
                    false);
            return new FootViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHoleder = (ItemViewHolder) holder;
            final View view = itemViewHoleder.mView;
            itemViewHoleder.position = position;
            final TaskChechItembean moreItem=historBeans.get(position);
            itemViewHoleder.mAdd.setText(moreItem.getKeys());
            if(moreItem.getObjs().equals("不通过")){
                itemViewHoleder.tv_resutl.setText(moreItem.getObjs());
                itemViewHoleder.tv_resutl.setTextColor(android.graphics.Color.RED);//系统自带的颜色类

            }else if(moreItem.getObjs().equals("通过")){
                itemViewHoleder.tv_resutl.setText(moreItem.getObjs());
                itemViewHoleder.tv_resutl.setTextColor(Color.BLUE);//系统自带的颜色类

            }

            //默认是通过的


        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_pb.setVisibility(View.GONE);
                    footViewHolder.foot_view_item_tv.setText("加载完成，已到底部");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_pb.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
                case NO_MORE_DATA:
                    footViewHolder.foot_view_item_pb.setVisibility(View.GONE);
                    footViewHolder.foot_view_item_tv.setText("加载完成已经没有更多数据");
                    break;
            }

            if (historBeans.size() < buildingSiteActivity.limitCounts) {
                footViewHolder.foot_view_item_pb.setVisibility(View.GONE);
                footViewHolder.foot_view_item_tv.setText("加载完成已经没有更多数据");
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return historBeans == null ? 0 : historBeans.size() <= 0 ? 0 : historBeans.size() + 1;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView mAdd;
        public TextView tv_resutl;
        public int position;


        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            mAdd= (TextView) view.findViewById(R.id.workplan_show_note);
            tv_resutl= (TextView) view.findViewById(R.id.tv_resutl);
        }
    }

    /**
     * 获取频道列表
     */
    public List<TaskChechItembean> getChannnelLst() {
        return historBeans;
    }

    /**
     * 删除频道列表
     */
    public void removeAll() {
        historBeans.clear();
        notifyDataSetChanged();
    }

    //删除
    public void remove(int position) {
        historBeans.remove(position);
        notifyDataSetChanged();
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;
        private ProgressBar foot_view_item_pb;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_pb = (ProgressBar) view.findViewById(R.id.progressBar);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
        }
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
