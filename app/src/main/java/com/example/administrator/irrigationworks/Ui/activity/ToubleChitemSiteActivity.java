package com.example.administrator.irrigationworks.Ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.adapter.CheckItemListAdapter;
import com.example.administrator.irrigationworks.Ui.bean.BuildingSiteBean;
import com.example.administrator.irrigationworks.Ui.bean.BuildinglistBean;
import com.example.administrator.irrigationworks.Ui.bean.HistorBean;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.TotalDemo;
import com.example.administrator.irrigationworks.Ui.uiview.RecyclerViewExt;
import com.example.administrator.irrigationworks.Ui.widget.RefreshableView;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import app.dinus.com.loadingdrawable.LoadingView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 历史巡查项目列表
 * Created by Administrator on 2017/8/15.
 */
public class ToubleChitemSiteActivity extends BaseAppCompatActivity {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_work_more)
    RecyclerViewExt recyclerWorkMore;
    @Bind(R.id.SwipeRefreshLayout)
    RefreshableView swipeRefreshLayout;
    @Bind(R.id.electric_fan_view)
    LoadingView electricFanView;
    @Bind(R.id.network_error)
    ImageView networkError;
    @Bind(R.id.sendBt)
    TextView sendBt;

    private BuildingSiteBean site;
    private List<BuildinglistBean> moreItemList;
    private boolean hasMore = true;
    public static int limitCounts = 10;
    private CheckItemListAdapter findasklistadapter;
    //临时设置限时操作
    private Timer timer = new Timer();
    private int recLen = 0;


    //点击item展开
    private int oldPostion = -1;
    private String type;
    //历史巡查项目
    private String json = "";
    private String id = "";//传递id
    private String spectorid = "";//巡查员id
    private String pmid = "";//总监理id
    private String Pm_confirm = "";//总监理id
    private String role = "";//总监理
    private String taskid = "";//未检查的事项
    private String contionname = "";//工地名字
    private List<HistorBean> historBeans;
    private List<TaskChechItembean> taskChechItembean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //临时解析检查项目
        try{
            ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
            ivBackLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            toolbarTitle.setText("巡查记录");
            if(Enumerate.General_supervision.equals(NimUIKit.getRole())){
                //隐患处理按钮
                sendBt.setVisibility(View.GONE);
            }
            Boolean net = NetUtil.checkNetWork(ToubleChitemSiteActivity.this);
            id=getIntent().getStringExtra("id");
            spectorid=getIntent().getStringExtra("spectorid");
            pmid=getIntent().getStringExtra("pmid");
            taskid=getIntent().getStringExtra("taskid");
            contionname=getIntent().getStringExtra("name");
            Pm_confirm=getIntent().getStringExtra("confirm");
            if (net) {
                initRefresh();
                if(Enumerate.project_manager.equals(NimUIKit.getRole())){
                    sendBt.setText("去处理");
                }else {
                    sendBt.setVisibility(View.GONE);
                }

                gotoyidu();
                historBeans = new ArrayList<>();
                json = getIntent().getStringExtra("moreItem");
                //去处理
                sendBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Pm_confirm.equals("1")){
                            UtilsTool.showShortToast(ToubleChitemSiteActivity.this,"已处理隐患");
                            return;
                        }
                        Intent intentnotes = new Intent(ToubleChitemSiteActivity.this, HiddentrouSavebleActvity.class);
                        intentnotes.putExtra("id", id);//传递id
                        intentnotes.putExtra("spectorid",spectorid);//巡查员id
                        intentnotes.putExtra("json",json);//巡查员id
                        intentnotes.putExtra("contionname",contionname);//巡查员id
                        intentnotes.putExtra("pmid", pmid);//总监理id
                        startActivity(intentnotes);
                        finish();
                    }
                });

                if (!TextUtils.isEmpty(json)) {
                    taskChechItembean = new ArrayList<>();
                    getTotalData(json);
//                taskChechItembean = JSON.parseArray(json, TaskChechItembean.class);
//                net.sf.json.JSONObject jsimple = new net.sf.json.JSONObject(json);
//                Iterator it_color = jsimple.keys();
//                while (it_color.hasNext()) {
//                    HistorBean HistorBean=new HistorBean();
//
//                    String key = it_color.next().toString();
//                    String value = jsimple.getString(key);
//                    HistorBean.setName(key);
//                    HistorBean.setResule(value);
//                    historBeans.add(HistorBean);
//                    Log.d("oop","历史数据moreItem.getCheckitem()"+key+"value"+value);
//                }
//                Log.d("oop","历史数据moreItem.getCheckitem()"+historBeans);

                } else {
                    electricFanView.setVisibility(View.VISIBLE);
                }

            } else {
                networkError.setVisibility(View.VISIBLE);
                electricFanView.setVisibility(View.GONE);
                UtilsTool.showShortToast(ToubleChitemSiteActivity.this, "没有网络");
            }

        }catch (Exception e){

        }

    }

    private void gotoyidu() {
        //项目经理已读去读
        final Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", NimUIKit.getToken());
        hashMap.put("_id", id);
new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            HttpUtils.doPostAsyn(FinalTozal.patrolreaded, NimUIKit.getGson().toJson(hashMap), new Getpatrolreaded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}).start();

    }

    private void getTotalData(String  json) {
        TotalDemo totalDemo = NimUIKit.getGson().fromJson(json, TotalDemo.class);
        Log.d("oop", "key为TaskListBean：" + totalDemo);
        Map mapTypes1 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName1()));
        Map mapTypes2 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName2()));
        Map mapTypes3 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName3()));
        Map mapTypes4 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName4()));
        Map mapTypes5 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName6()));
        Map mapTypes6 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getValue5()));
        Map mapTypes7 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName7()));
        Map mapTypes8 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName8()));
        Map mapTypes9 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName9()));
        Map mapTypes10 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName10()));

//                        if (obj.has("checkitem")) {
//                            JSONObject transitListArray = obj.getJSONObject("checkitem");
//                            for (int i = 0; i < transitListArray.length(); i++) {
//                                Log.d("oop","历史数据"+transitListArray);
//                            }
//                        }
        for (Object obj : mapTypes1.keySet()) {
            if(mapTypes1.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes1.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes2.keySet()) {
            if(mapTypes2.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes2.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes3.keySet()) {
            if(mapTypes3.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes3.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes4.keySet()) {
            if(mapTypes4.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes4.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes5.keySet()) {
            if(mapTypes5.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes5.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes6.keySet()) {
            if(mapTypes6.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes6.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes7.keySet()) {
            if(mapTypes7.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes7.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes8.keySet()) {
            if(mapTypes8.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes8.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes9.keySet()) {
            if(mapTypes9.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes9.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes10.keySet()) {
            if(mapTypes10.get(obj).equals("不通过")){
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes10.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        adapterInit(taskChechItembean);
    }


    private void initRefresh() {
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(1500);
//                    findasklistadapter.notifyDataSetChanged();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.finishRefreshing();
            }
        }, RefreshableView.MYASKLISTID, RefreshableView.RECYCLERVIEWID);
        /////////////////////////////////////////
        recyclerWorkMore.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("qc", "StateChanged = " + newState);


//                if (newState == RecyclerView.SCROLL_STATE_IDLE && hasMore) {
////                if (hasMore) {
//
//                    hasMore = false;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//
//
//                                swipeRefreshLayout.finishRefreshing();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, 1500);
//                } else if (newState == 0) {
//                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void adapterInit(List<TaskChechItembean> historBeans) {
        if (historBeans != null) {
            findasklistadapter = new CheckItemListAdapter(this, taskChechItembean);
            recyclerWorkMore = (RecyclerViewExt) findViewById(R.id.recycler_work_more);
            electricFanView = (LoadingView) findViewById(R.id.electric_fan_view);
            swipeRefreshLayout = (RefreshableView) findViewById(R.id.SwipeRefreshLayout);


            recyclerWorkMore.setLayoutManager(new LinearLayoutManager(this));
            recyclerWorkMore.setAdapter(findasklistadapter);
            electricFanView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            electricFanView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_checkitem;
    }

    private class Getpatrolreaded implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("已读", "已读" + result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                Log.d("qc", "已读" + result);

            } else {
            }
        }
    }
}
