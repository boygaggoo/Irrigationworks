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

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.activity.HistoricalinspectionsActivity;
import com.example.administrator.irrigationworks.Ui.activity.RideRouteActivity;
import com.example.administrator.irrigationworks.Ui.activity.buildingSiteActivity;
import com.example.administrator.irrigationworks.Ui.bean.HoistoricaDataLitepal;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.SignViewDataLitepal;
import com.example.administrator.irrigationworks.UtilsTozals.DataUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 工地管理,施工方
 */
public class ConstrutionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    public List<HoistoricaDataLitepal> mAskList;
    private ArrayList<String> tasks;
    //点击item展开
    private int oldPostion = -1;
    private buildingSiteActivity.AsyncStructionlistCallback callback;

    public ConstrutionListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ConstrutionListAdapter(Context mContext, List<HoistoricaDataLitepal> mAskList, buildingSiteActivity.AsyncStructionlistCallback callback) {
        this.mContext = mContext;
        this.mAskList = mAskList;
        this.callback = callback;
        this.tasks = new ArrayList<>();
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
            final HoistoricaDataLitepal moreItem=mAskList.get(position);

            itemViewHoleder.show_note.setText(position+"."+moreItem.getName());
            itemViewHoleder.show_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("qqq","点击了");
                    Intent intentnotes=new Intent(mContext, RideRouteActivity.class);
                    intentnotes.putExtra("lag",moreItem.getLat()+"");
                    intentnotes.putExtra("Lng",moreItem.getLnt()+"");
                    mContext.startActivity(intentnotes);
//                    if (oldPostion == position) {
//                        if (moreItem.isExpand())  {
//                            oldPostion = -1;
//                        }
//                        moreItem.setExpand(!moreItem.isExpand());
//                    }else{
//                        oldPostion = position;
//                        moreItem.setExpand(true);
//                    }
//                    notifyDataSetChanged();
                }
            });

            itemViewHoleder.iv_spread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("qqq","点击了iv_spread");
                    Intent intentnotes=new Intent(mContext, RideRouteActivity.class);
                    intentnotes.putExtra("lag",moreItem.getLat()+"");
                    intentnotes.putExtra("Lng",moreItem.getLnt()+"");
                    mContext.startActivity(intentnotes);
//                    if (oldPostion == position) {
//                        if (moreItem.isExpand())  {
//                            oldPostion = -1;
//                        }
//                        moreItem.setExpand(!moreItem.isExpand());
//                    }else{
//                        oldPostion = position;
//                        moreItem.setExpand(true);
//                    }
//                    notifyDataSetChanged();

                }
            });
            itemViewHoleder.feerb5.setVisibility(View.GONE);
            itemViewHoleder.feerb4.setVisibility(View.GONE);
            itemViewHoleder.feerb1.setVisibility(View.GONE);
            //地图导航
            itemViewHoleder.feerb2.setVisibility(View.GONE);
            itemViewHoleder.feerb3.setVisibility(View.GONE);
            itemViewHoleder.feerb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentnotes=new Intent(mContext, RideRouteActivity.class);
                    intentnotes.putExtra("lag",moreItem.getLat()+"");
                    intentnotes.putExtra("Lng",moreItem.getLnt()+"");
                    mContext.startActivity(intentnotes);



                }
            });
            itemViewHoleder.show_note.setText(position+"."+moreItem.getName());
//
//            boolean setvisble=moreItem.isExpand();
//            itemViewHoleder.container_list_content.setVisibility(setvisble ? View.VISIBLE : View.GONE);



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

    private Date getWebsiteDatetime(String webUrl2) {
        try {
            URL url = new URL(webUrl2);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            return date;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        public Button feerb4;
        public Button feerb5;
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
            feerb4= (Button) view.findViewById(R.id.feerb4);
            feerb5= (Button) view.findViewById(R.id.feerb5);

        }

    }

    /**
     * 获取频道列表
     */
    public List<HoistoricaDataLitepal> getChannnelLst() {
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
