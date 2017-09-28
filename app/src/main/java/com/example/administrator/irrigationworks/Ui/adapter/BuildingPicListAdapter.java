package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.activity.EventsActivity;
import com.example.administrator.irrigationworks.Ui.activity.HiddentroubleActvity;
import com.example.administrator.irrigationworks.Ui.activity.PicSiteActivity;
import com.example.administrator.irrigationworks.Ui.activity.RideRouteActivity;
import com.example.administrator.irrigationworks.Ui.activity.buildingSiteActivity;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.TaskDbbean;

import java.util.ArrayList;
import java.util.List;

public class BuildingPicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    public List<TaskDbbean> mAskList;
    //点击item展开
    private int oldPostion = -1;
    private Circle circle;
    private AMap aMap;
    public List<TaskChechItembean> taskChechItembean;
    public BuildingPicListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public BuildingPicListAdapter(Context mContext, List<TaskDbbean> mAskList, AMap aMap, Circle circle) {
        this.mContext = mContext;
        this.mAskList = mAskList;
        this.circle = circle;
        this.aMap = aMap;
        taskChechItembean=new ArrayList<>();
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
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buliding_list, parent, false);
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
            Log.d("qqc","onBindViewHolder点击onItemClick了");
            ItemViewHolder itemViewHoleder = (ItemViewHolder) holder;
            final View view = itemViewHoleder.mView;
            itemViewHoleder.position = position;
            final TaskDbbean moreItem=mAskList.get(position);

            itemViewHoleder.show_note.setText(moreItem.getConstruction().getName());
            itemViewHoleder.show_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到上传图片首页
                    Log.d("上传图片首页",moreItem.getConstruction().getConstructionid()+"");
                    Intent intentnotes=new Intent(mContext, PicSiteActivity.class);


                    intentnotes.putExtra("constructionid",moreItem.getConstruction().getConstructionid()+"");


                    mContext.startActivity(intentnotes);

                }
            });


            itemViewHoleder.feerb1.setVisibility(View.GONE);

            //隐患上报
            itemViewHoleder.feerb2.setVisibility(View.GONE);
            //坐标采集
            itemViewHoleder.feerb3.setVisibility(View.GONE);


            boolean setvisble=moreItem.isExpand();
            itemViewHoleder.container_list_content.setVisibility(setvisble ? View.VISIBLE : View.GONE);


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

            if (mAskList.size() < buildingSiteActivity.limitCounts) {
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
        return mAskList == null ? 0 : mAskList.size() <= 0 ? 0 : mAskList.size() + 1;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView show_note;
        public ImageView iv_spread;
        public LinearLayout container_list_content;
        public Button feerb1;
        public Button feerb2;
        public Button feerb3;
        public int position;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            show_note= (TextView) view.findViewById(R.id.workplan_show_note);
            iv_spread= (ImageView) view.findViewById(R.id.iv_spread);
            container_list_content= (LinearLayout) view.findViewById(R.id.container_list_content);
            feerb1= (Button) view.findViewById(R.id.feerb1);
            feerb2= (Button) view.findViewById(R.id.feerb2);
            feerb3= (Button) view.findViewById(R.id.feerb3);
        }

    }

    /**
     * 获取频道列表
     */
    public List<TaskDbbean> getChannnelLst() {
        return mAskList;
    }

    /**
     * 删除频道列表
     */
    public void removeAll() {
        mAskList.clear();
        notifyDataSetChanged();
    }

    //删除
    public void remove(int position) {
        mAskList.remove(position);
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
