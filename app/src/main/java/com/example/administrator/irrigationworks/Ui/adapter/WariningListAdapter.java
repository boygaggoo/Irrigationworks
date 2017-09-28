package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.View.CalendarView;
import com.example.administrator.irrigationworks.Ui.activity.TeanActivity;
import com.example.administrator.irrigationworks.Ui.activity.buildingSiteActivity;
import com.example.administrator.irrigationworks.Ui.bean.GeDangerlistBean;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.View.IReadGetPresenter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WariningListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    private String type;
    public List<GeDangerlistBean> mAskList;
    public List<TaskChechItembean> taskChechItembean;
    public IReadGetPresenter preses;
    //点击item展开
    private int oldPostion = -1;
    private TeanActivity.AsyncCallback callback;

    public WariningListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public WariningListAdapter(Context mContext, List<GeDangerlistBean> mAskList, String type, IReadGetPresenter prese, TeanActivity.AsyncCallback callback) {
        this.mContext = mContext;
        this.mAskList = mAskList;
        this.preses = prese;
        this.callback = callback;
        this.type = type;
        taskChechItembean = new ArrayList<>();
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
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_warining_list, parent, false);
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
            Log.d("qqc", "onBindViewHolder点击onItemClick了");
            final ItemViewHolder itemViewHoleder = (ItemViewHolder) holder;
            final View view = itemViewHoleder.mView;
            itemViewHoleder.position = position;
            final GeDangerlistBean moreItem = mAskList.get(position);
//            itemViewHoleder.show_note.setText("上报标题:" + moreItem.getReport_content());
            int number=position+1;
            if(type.equals(Constants.dangerList)){
                //暂时封住
                itemViewHoleder.show_note.setText(number+":"+moreItem.getConstruction().getName());
                itemViewHoleder.tv_warining_time.setText("时间:" + moreItem.getDate_time());
                itemViewHoleder.tv_auditing.setText("周期:" + moreItem.getTimeout() +  "天");

            }else {//隐患警告
                itemViewHoleder.show_note.setText(number+":" + moreItem.getConstruction().getName());
                itemViewHoleder.tv_warining_time.setText("时间:" + moreItem.getDate_time());
                itemViewHoleder.tv_auditing.setText("周期:" + moreItem.getTimeout() +  "天");
            }
            Date date=new Date();
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time=format.format(date);
//            String time="2017-09-26";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            Log.d("xiagnchashijian","moreItem.getDate_time("+moreItem.getDate_time());
            try {
                Date dt1 = sdf.parse(time);
                Date dt2 = sdf.parse( moreItem.getDate_time()+"");//
                int calen=CalendarView.differentDays(dt2,dt1);
                if(calen>0&&calen!=0){
                    if(calen>moreItem.getTimeout()){
                        Log.d("xiagnchashijian","calen大于moreItem");
                        itemViewHoleder.tv_chaoshi.setVisibility(View.VISIBLE);
                        itemViewHoleder.tv_chaoshi.setTextColor(Color.RED);//系统自带的颜色类
                        itemViewHoleder.tv_chaoshi.setText("(超时)");
                    }else {
                        Log.d("xiagnchashijian","calen下雨于moreItem");
                        itemViewHoleder.tv_chaoshi.setVisibility(View.GONE);
                    }

                }else {
                    itemViewHoleder.tv_chaoshi.setVisibility(View.GONE);
                }
                Log.d("xiagnchashijian","xiagnchashijian"+calen);
            } catch (Exception exception) {
                exception.printStackTrace();
                Log.d("xiagnchashijian","exception"+exception);
            }




            itemViewHoleder.ll_total.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("qqq", "点击了");
//                    if(type.equals(Constants.dangerList)){
//
//                    }else {
//                        if (oldPostion == position) {
//                            if (moreItem.isExpand()) {
//                                oldPostion = -1;
//                            }
//                            moreItem.setExpand(!moreItem.isExpand());
//                        } else {
//                            oldPostion = position;
//                            moreItem.setExpand(true);
//                        }
//                        notifyDataSetChanged();
//                    }

                }
            });






            //跳转到巡查项目
            itemViewHoleder.iv_spread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });



            if("有隐患".equals(moreItem.getState())){
                itemViewHoleder.rl_signed_hour.setImageResource(R.mipmap.faxianyinhuan);//改变状态
            }

            //0是未读
            String Constructor_readed = "";
            String Supervisor_readed = "";
            String Pm_confirm = "";
            String Chiefsupervisor_confirm = "";
            if (moreItem.getConstructor_readed() == 0||moreItem.getSupervisor_readed() == 0) {//施工方未读
                if(moreItem.getConstructor_readed() == 0&&moreItem.getSupervisor_readed() == 0){


                        itemViewHoleder.rl_signed_minrl_signed_min.setImageResource(R.mipmap.tongyong);//改变状态

                }else {
                    if(moreItem.getConstructor_readed() == 1){
                        itemViewHoleder.rl_signed_minrl_signed_min.setImageResource(R.mipmap.shigongfangyidu);//改变状态
                    }else if(moreItem.getSupervisor_readed() == 1){
                        itemViewHoleder.rl_signed_minrl_signed_min.setImageResource(R.mipmap.jianlifangyidu);//改变状态
                    }else if(moreItem.getConstructor_readed() == 1&&(moreItem.getSupervisor_readed() == 1)){
                        itemViewHoleder.rl_signed_minrl_signed_min.setImageResource(R.mipmap.shenhwancheng);//改变状态
                    }

                }

//                Constructor_readed = "未读";
//                itemViewHoleder.rl_signed_minrl_signed_min.setImageResource(R.mipmap.zh);//改变状态
            } else if(moreItem.getConstructor_readed() == 1&&moreItem.getSupervisor_readed() == 1) {//施工方已读监理已读
                Constructor_readed = "已读";
                itemViewHoleder.rl_signed_minrl_signed_min.setImageResource(R.mipmap.shenhwancheng);//改变状态
            }


            itemViewHoleder.tv_warining.setText("施工方:" + Constructor_readed + "  监理:" + Supervisor_readed);
            Log.d("qc", "此时的监理" + NimUIKit.getRole());
            if (!TextUtils.isEmpty(NimUIKit.getRole())) {
                if (moreItem.getPm_confirm() == 0) {//项目经理 1已经整改了 0没有整改
                } else {
                    itemViewHoleder.rl_zhenggai.setImageResource(R.mipmap.shenhwancheng);//改变状态
                }
                if (moreItem.getChiefsupervisor_confirm() == 0) {//总监理确认
                } else {
                    itemViewHoleder.rl_shenghei.setBackgroundColor(Color.parseColor("#1873BA"));
                }



            }

//            boolean setvisble = moreItem.isExpand();
//            itemViewHoleder.ll_hide.setVisibility(setvisble ? View.VISIBLE : View.GONE);

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
        public TextView tv_auditing;
        public TextView tv_warining;
        public TextView tv_chaoshi;
        public TextView tv_warining_time;
        public ImageView iv_spread;
        public LinearLayout ll_hide;
        public LinearLayout ll_total;
        public ImageView rl_signed_hour;
        public ImageView rl_signed_minrl_signed_min;
        public ImageView rl_zhenggai;
        public ImageView rl_shenghei;

        public int position;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            show_note = (TextView) view.findViewById(R.id.workplan_show_note);
            tv_auditing = (TextView) view.findViewById(R.id.tv_auditing);
            tv_warining = (TextView) view.findViewById(R.id.tv_warining);
            tv_chaoshi = (TextView) view.findViewById(R.id.tv_chaoshi);
            iv_spread = (ImageView) view.findViewById(R.id.iv_spread);
            tv_warining_time = (TextView) view.findViewById(R.id.tv_warining_time);

            ll_total = (LinearLayout) view.findViewById(R.id.ll_total);
            ll_hide = (LinearLayout) view.findViewById(R.id.ll_hide);
            rl_signed_hour = (ImageView) view.findViewById(R.id.rl_signed_hour);
            rl_zhenggai = (ImageView) view.findViewById(R.id.rl_zhenggai);
            rl_signed_minrl_signed_min = (ImageView) view.findViewById(R.id.rl_signed_minrl_signed_min);
            rl_shenghei = (ImageView) view.findViewById(R.id.rl_shenghei);

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
    /**
     * 获取频道列表
     */
    public List<GeDangerlistBean> getChannnelLst() {
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

    private class Getpatrolreaded implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("已读", "已读" + result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                Log.d("qc", "已读" + result);
                callback.onSuccess("已读");

            } else {
                callback.onSuccess("请求失败");
            }
        }
    }


}
