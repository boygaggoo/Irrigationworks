package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.activity.HistoricalinspectionsActivity;
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

public class HistoricalinspectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    private HistoricalinspectionsActivity.AsyncCallback callback;

    public HistoricalinspectionListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public HistoricalinspectionListAdapter(Context mContext, List<HoistoricaDataLitepal> mAskList, ArrayList<String> tasks, HistoricalinspectionsActivity.AsyncCallback callback) {
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
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist, parent, false);
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
            //OBH获取当前的key值
//            Map mapTypes = JSON.parseObject(NimUIKit.getGson().toJson(taskListBean.getName1()));
//            for (Object obj : mapTypes.keySet()){
//                Log.d("li","key为："+obj+"值为："+mapTypes.get(obj));
//                tasks.add(obj+"");//获取当前的值
//                Log.d("li","mapTypes："+mapTypes.size()+"tasks值为："+tasks.size());
//                if(mapTypes.size()==tasks.size()){
//                    //进行适配
//
//                }
//
//            }
            //将Json字符串转为java对象

//            JSONObject obj = JSONObject.fromObject(moreItem.getCheckitem());
//            //获取Object中的UserName
////循环并得到key列表
//            //正式提取未知的key值
//            String name = obj.getString("checkitem");
//
//            Iterator it = obj.keys();
//            List<String> keyListstr = new ArrayList<String>();
//            while(it.hasNext()){
//                keyListstr.add(it.next().toString());
//            }
//            Log.d("oop","历史数据"+keyListstr);
            //获取ArrayObject
//            if (obj.has("checkitem")) {
//                JSONObject transitListArray = obj.getJSONObject("checkitem");
//                for (int i = 0; i < transitListArray.length(); i++) {
//                    Log.d("oop","历史数据"+transitListArray);
//                }
//            }
            itemViewHoleder.show_note.setText(position+"."+moreItem.getName());
            itemViewHoleder.show_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String webUrl2 = "http://www.baidu.com";//百度时间
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            java.util.Date date = getWebsiteDatetime(webUrl2);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);// 输出北京时间
                            String time = sdf.format(date);
                            final Map<String, Object> hashMap = new HashMap<String, Object>();
                            final Map<String, Object> hashMaps = new HashMap<String, Object>();
                            hashMaps.put("constructionid", moreItem.getConstructionid());
                            hashMaps.put("ym",time);
                            hashMap.put("token", NimUIKit.getToken());
                            hashMap.put("searchValue", hashMaps);//过滤片区


                            try {
                                Log.d("getConstructionid","getConstructionid"+ NimUIKit.getGson().toJson(hashMap));
                                HttpUtils.doPostAsyn(FinalTozal.patrollist, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
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

            //跳转到巡查项目
            itemViewHoleder.iv_spread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("qqq","点击了"+moreItem.getConstructionid());

                    final String webUrl2 = "http://www.baidu.com";//百度时间
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            java.util.Date date = getWebsiteDatetime(webUrl2);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);// 输出北京时间
                            String time = sdf.format(date);
                            final Map<String, Object> hashMap = new HashMap<String, Object>();
//
                            hashMap.put("token", NimUIKit.getToken());
                            hashMap.put("constructionid", moreItem.getConstructionid());
                            hashMap.put("ym",time);
                            try {
                                HttpUtils.doPostAsyn(FinalTozal.patrollist, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            });

            itemViewHoleder.tv_auditing.setText(moreItem.getState()+"");
            itemViewHoleder.text_name.setText(moreItem.getInspector().getRealname()+"");
            itemViewHoleder.text_time.setText(moreItem.getStart_time()+"");
            itemViewHoleder.tv_warining.setText("隐患:"+tasks.toString()+"");
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
        public TextView tv_auditing;
        public TextView tv_warining;
        public TextView text_name;
        public TextView text_time;
        public ImageView iv_spread;
        public LinearLayout container_list_content;

        public int position;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            show_note= (TextView) view.findViewById(R.id.workplan_show_note);
            text_name= (TextView) view.findViewById(R.id.text_name);
            text_time= (TextView) view.findViewById(R.id.text_time);
            tv_auditing= (TextView) view.findViewById(R.id.tv_auditing);
            tv_warining= (TextView) view.findViewById(R.id.tv_warining);
            iv_spread= (ImageView) view.findViewById(R.id.iv_spread);
            container_list_content= (LinearLayout) view.findViewById(R.id.container_list_content);

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

    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("获取当前的数据", "获取当前的数据" + result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);

            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                String reus = loginOnSuccssBean.getResult().replace("_id", "rg");
                //用来判断
                LoginOnSuccssBean loginOnSuccssBeans = JSON.parseObject(reus, LoginOnSuccssBean.class);
                if(loginOnSuccssBeans.getResult().equals("[]")){
                    callback.onSuccess(null);
                    return;
                }
                List<SignViewDataLitepal> poistoricaDataLitepal = JSON.parseArray(loginOnSuccssBeans.getResult(), SignViewDataLitepal.class);


                Log.d("历史巡查", "当前日" + DataUtils.dataToCalendar(DataUtils.dataToDate(poistoricaDataLitepal.get(0).getYmd())).get(Calendar.DAY_OF_MONTH));
                //设置当前的巡查日历
              callback.onSuccess(poistoricaDataLitepal);

            }else {
                Log.d("获取当前的数据", "else获取当前的数据" + result);
                List<SignViewDataLitepal> poistoricaDataLitepal=null;
                callback.onSuccess(poistoricaDataLitepal);
            }
        }
    }
}
