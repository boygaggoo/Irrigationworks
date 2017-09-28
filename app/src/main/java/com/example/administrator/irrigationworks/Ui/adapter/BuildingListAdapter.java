package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.activity.BuildingCheckItemActivity;
import com.example.administrator.irrigationworks.Ui.activity.EventsActivity;
import com.example.administrator.irrigationworks.Ui.activity.HiddentroubleActvity;
import com.example.administrator.irrigationworks.Ui.activity.PicSiteActivity;
import com.example.administrator.irrigationworks.Ui.activity.RideRouteActivity;
import com.example.administrator.irrigationworks.Ui.activity.ViewalbumActivity;
import com.example.administrator.irrigationworks.Ui.activity.buildingSiteActivity;
import com.example.administrator.irrigationworks.Ui.bean.Coordinate;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.TaskDbbean;
import com.example.administrator.irrigationworks.Ui.bean.TotalDemo;
import com.example.administrator.irrigationworks.Ui.frament.Inspectionframent;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 巡查员
 */
public class BuildingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    private PolygonOptions pOption;
    private AMap aMap;
    private buildingSiteActivity.AsyncCallback callback;
    public List<TaskChechItembean> taskChechItembean;
    public BuildingListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public BuildingListAdapter(Context mContext, List<TaskDbbean> mAskList, AMap aMap, Circle circle, buildingSiteActivity.AsyncCallback callback) {
        this.mContext = mContext;
        this.mAskList = mAskList;
        this.circle = circle;
        this.aMap = aMap;
        this.callback = callback;
        taskChechItembean=new ArrayList<>();
        pOption = new PolygonOptions();
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
        int number=position+1;
            itemViewHoleder.show_note.setText(number+":"+moreItem.getConstruction().getName());
            itemViewHoleder.show_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("qqq","点击了show_note");
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
            Log.d("此时的坐标参数","此时的坐标参数"+moreItem.getConstruction().getCoordinate());
            //跳转到巡查项目
            itemViewHoleder.iv_spread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("qqq","点击了iv_spread");

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
            //地图导航
            itemViewHoleder.feerb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentnotes=new Intent(mContext, RideRouteActivity.class);
                    intentnotes.putExtra("lag",moreItem.getConstruction().getLat()+"");
                    intentnotes.putExtra("Lng",moreItem.getConstruction().getLnt()+"");
                    mContext.startActivity(intentnotes);



                }
            });
            //去巡查
            itemViewHoleder.feerb2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Enumerate.patroller.equals(NimUIKit.getRole())){
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(moreItem.getConstruction().getLat(),moreItem.getConstruction().getLnt()),14));
                        //设置当前工地的位置
                        List<Coordinate> coordinate=new ArrayList<Coordinate>();
                        String[][] re=moreItem.getConstruction().getCoordinate();
                        for(int j=0;j<re.length;j++) {
                            Coordinate oordinate=new Coordinate();
                            for (int i = 0 ; i <NimUIKit.getGson().toJson(re[j]).split(",").length ; i++ ) {

                                if(NimUIKit.getGson().toJson(re[j]).split(",")[i].contains("[")){
                                    String lnt=NimUIKit.getGson().toJson(re[j]).split(",")[i].replace("[","").replace("\"","");
                                    BigDecimal bg = new BigDecimal(lnt);
//只保存后六位小数,保留太多请求接口时，返回不了数据
                                    double doublelng = bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    oordinate.setLnt(doublelng);
                                    Log.d("自登录的标示为", "lnt" + lnt);
                                }else if(NimUIKit.getGson().toJson(re[j]).split(",")[i].contains("]")){
                                    String lng=NimUIKit.getGson().toJson(re[j]).split(",")[i].replace("]","").replace("\"","");
                                    BigDecimal bg = new BigDecimal(lng);
//只保存后六位小数,保留太多请求接口时，返回不了数据
                                    double doublelng = bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    oordinate.setLng(doublelng);
                                }

                            }
                            coordinate.add(oordinate);

//
                        }

                        if(coordinate.size()>0){
                            for(int i=0;i<coordinate.size();i++){
                                pOption.add(new LatLng(coordinate.get(i).getLng(),coordinate.get(i).getLnt()));
                                Log.d("coordinate", "coordinate" + NimUIKit.getGson().toJson(pOption));
                                if(i==coordinate.size()-1){
                                    Polygon polygon= aMap.addPolygon(pOption.strokeWidth(4)
                                            .strokeColor(Color.argb(50, 1, 1, 1))
                                            .fillColor(Color.argb(50, 1, 1, 1)));
                                    Log.d("qqq","点击了feerb1");
                                    String lng = Preference.getlng();
                                    String lat = Preference.getLat();
                                    Log.d("coordinate", "lng" + lng);
                                    Log.d("coordinate", "lat" + lat);
                                    BigDecimal bg = new BigDecimal(lng);
//只保存后六位小数,保留太多请求接口时，返回不了数据
                                    double doublelng = bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    BigDecimal bglat = new BigDecimal(lat);
//只保存后六位小数,保留太多请求接口时，返回不了数据
                                    double doublelat = bglat.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    Preference.savaLat(doublelat + "");
                                    Preference.savalng(doublelng + "");
                                    //监控现在坐标是否在目标范围内
//                   LatLng nowlng = new LatLng(doublelng, doublelat);// 北京市经纬度
                                    //设置当前工地坐标

                                    LatLng nowlng = new LatLng(doublelat, doublelng);

                                    boolean b1 = polygon.contains(nowlng);//监控此时的点
                                    Log.d("b1","b1"+b1);
                                    if (b1) {
                                        TotalDemo totalDemo=NimUIKit.getGson().fromJson(moreItem.getCheckitem(), TotalDemo.class);
                                        Log.d("oop","key为TaskListBean："+totalDemo);
                                        Map mapTypes1 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName1()));
                                        Map mapTypes2 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName2()));
                                        Map mapTypes3 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName3()));
                                        Map mapTypes4= JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName4()));
                                        Map mapTypes5= JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName6()));
                                        Map mapTypes6= JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getValue5()));
                                        Map mapTypes7= JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName7()));
                                        Map mapTypes8= JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName8()));
                                        Map mapTypes9= JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName9()));
                                        Map mapTypes10= JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName10()));
                                        Log.d("oop","key为mapTypes1："+mapTypes1);

//                        if (obj.has("checkitem")) {
//                            JSONObject transitListArray = obj.getJSONObject("checkitem");
//                            for (int i = 0; i < transitListArray.length(); i++) {
//                                Log.d("oop","历史数据"+transitListArray);
//                            }
//                        }
                                        Log.d("oop","key为："+taskChechItembean);
                                        for (Object obj : mapTypes1.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes1.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes2.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes2.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes3.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes3.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes4.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes4.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes5.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes5.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes6.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes6.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes7.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes7.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes8.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes8.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes9.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes9.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        for (Object obj : mapTypes10.keySet()){
                                            TaskChechItembean taskChechItembean1=new TaskChechItembean();
                                            taskChechItembean1.setKeys(obj+"");
                                            taskChechItembean1.setObjs(mapTypes10.get(obj)+"");
                                            taskChechItembean.add(taskChechItembean1);//获取当前的值
                                        }
                                        Intent intentnotes=new Intent(mContext, BuildingCheckItemActivity.class);
                                        intentnotes.putExtra("lag",moreItem.getConstruction().getLat()+"");
                                        intentnotes.putExtra("Lng",moreItem.getConstruction().getLnt()+"");
                                        intentnotes.putExtra("construction",NimUIKit.getGson().toJson(moreItem.getConstruction())+"");
                                        intentnotes.putExtra("constructionid",moreItem.getConstruction().getConstructionid()+"");
                                        intentnotes.putExtra("inspector",NimUIKit.getGson().toJson(moreItem.getInspector())+"");
                                        intentnotes.putExtra("chechitem",NimUIKit.getGson().toJson(taskChechItembean));

                                        mContext.startActivity(intentnotes);
                                    } else {
                                        UtilsTool.showShortToast(mContext,"不在工地范围内,不允许巡查");
                                    }
                                }
                            }


                        }
                        circle = aMap.addCircle(new CircleOptions().center(new LatLng(moreItem.getConstruction().getLat(), moreItem.getConstruction().getLnt()))
                                .radius(200).strokeColor(Color.argb(50, 1, 1, 1))
                                .fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(2));


                    }
//                    Intent intentnotes=new Intent(mContext, HiddentroubleActvity.class);
//                    intentnotes.putExtra("id",moreItem.getConstruction().getRg());
//                    mContext.startActivity(intentnotes);
                }
            });
            if(Enumerate.patroller.equals(NimUIKit.getRole())){

            }else {//如果不是巡查员就隐藏坐标采集功能
                itemViewHoleder.feerb3.setVisibility(View.GONE);
            }
            //坐标采集
            itemViewHoleder.feerb3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intentnotes=new Intent(mContext, EventsActivity.class);
                    Log.d("getConstructionid","getConstructionid"+moreItem.getConstruction().getConstructionid());
                    intentnotes.putExtra("id",moreItem.getConstruction().getConstructionid());
                    mContext.startActivity(intentnotes);
                    callback.onSuccess();

                }
            });
            //建设图册
            itemViewHoleder.feerb4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //跳转上传建设工地图片
                    Log.d("上传图片首页",moreItem.getConstruction().getConstructionid()+"");
                    Intent intentnotes=new Intent(mContext, PicSiteActivity.class);


                    intentnotes.putExtra("constructionid",moreItem.getConstruction().getConstructionid()+"");


                    mContext.startActivity(intentnotes);

                }
            });
            //建设图册
            itemViewHoleder.feerb5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //跳转巡查画册
                    Intent intentnotes=new Intent(mContext, ViewalbumActivity.class);


                    intentnotes.putExtra("constructionid",moreItem.getConstruction().getConstructionid()+"");


                    mContext.startActivity(intentnotes);

                }
            });


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
