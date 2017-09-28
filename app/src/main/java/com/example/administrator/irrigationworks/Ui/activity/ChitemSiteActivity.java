package com.example.administrator.irrigationworks.Ui.activity;

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
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.adapter.CheckItemListAdapter;
import com.example.administrator.irrigationworks.Ui.bean.BuildingSiteBean;
import com.example.administrator.irrigationworks.Ui.bean.BuildinglistBean;
import com.example.administrator.irrigationworks.Ui.bean.HistorBean;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.uiview.RecyclerViewExt;
import com.example.administrator.irrigationworks.Ui.widget.RefreshableView;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import app.dinus.com.loadingdrawable.LoadingView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 历史巡查项目列表
 * Created by Administrator on 2017/8/15.
 */
public class ChitemSiteActivity extends BaseAppCompatActivity {


    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.sendBt)
    TextView sendBt;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.network_error)
    ImageView networkError;
    @Bind(R.id.electric_fan_view)
    LoadingView electricFanView;
    @Bind(R.id.recycler_work_more)
    RecyclerViewExt recyclerWorkMore;
    @Bind(R.id.SwipeRefreshLayout)
    RefreshableView swipeRefreshLayout;
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
    private List<HistorBean> historBeans;
    private List<TaskChechItembean> taskChechItembean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //临时解析检查项目
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("巡查记录");
        Boolean net = NetUtil.checkNetWork(ChitemSiteActivity.this);
        if (net) {
            initRefresh();
            historBeans = new ArrayList<>();
            json = getIntent().getStringExtra("chitems");
            if (!TextUtils.isEmpty(json)) {
                taskChechItembean = new ArrayList<>();
                taskChechItembean = JSON.parseArray(json, TaskChechItembean.class);
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
                adapterInit(taskChechItembean);
            } else {
                electricFanView.setVisibility(View.VISIBLE);
            }

        } else {
            networkError.setVisibility(View.VISIBLE);
            electricFanView.setVisibility(View.GONE);
            UtilsTool.showShortToast(ChitemSiteActivity.this, "没有网络");
        }

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

}
