package com.example.administrator.irrigationworks.Ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.adapter.HistoricalinspectionListAdapter;
import com.example.administrator.irrigationworks.Ui.bean.BuildingSiteBean;
import com.example.administrator.irrigationworks.Ui.bean.HoistoricaDataLitepal;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.SignViewDataLitepal;
import com.example.administrator.irrigationworks.Ui.bean.WarininglistBean;
import com.example.administrator.irrigationworks.Ui.newsign.NewSignActivity;
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
 * 有隐患警告列表
 * * Created by Administrator on 2017/8/15.
 */
public class HistoricalinspectionsActivity extends BaseAppCompatActivity {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_work_more)
    RecyclerViewExt recyclerWorkMore;
    @Bind(R.id.SwipeRefreshLayout)
    RefreshableView swipeRefreshLayout;
    @Bind(R.id.electric_fan_view)
    LoadingView electricFanView;
    @Bind(R.id.police_station)
    Spinner policeStation;
    private BuildingSiteBean site;
    private List<WarininglistBean> moreItemList;
    private boolean hasMore = true;
    public static int limitCounts = 10;
    private HistoricalinspectionListAdapter findasklistadapter;
    //临时设置限时操作
    private Timer timer = new Timer();
    private int recLen = 0;

    //点击item展开
    private int oldPostion = -1;
    private String id="";
    private String time="";
    private ArrayList<String> tasks;
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
//        initRefresh();
        try{
            Boolean net = NetUtil.checkNetWork(HistoricalinspectionsActivity.this);
            if (net) {
//            initRefresh();
                adapterInit();


            } else {
                electricFanView.setVisibility(View.GONE);
                UtilsTool.showShortToast(HistoricalinspectionsActivity.this, "没有网络");
            }

        }catch (Exception e){

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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && hasMore) {
//                if (hasMore) {

                    hasMore = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {


                                swipeRefreshLayout.finishRefreshing();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1500);
                } else if (newState == 0) {
                }

            }

        });
    }

    private void adapterInit() {
        final Map<String, Object> hashMap = new HashMap<String, Object>();
//
        hashMap.put("token", NimUIKit.getToken());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.constructionlist, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //获取任务巡查数据
//        RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
//        String token= NimUIKit.getToken();
//        Map<String, Object> hashMap = new HashMap<String, Object>();
//        /**
//         * 参数为手机当前定位位置
//         */
//        hashMap.put("token", token);
//        Call<ResultCode> call = apiService.getTaskHistory(token);
//        call.enqueue(new Callback<ResultCode>() {
//            @Override
//            public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
//                ResultCode body = response.body();
//                Log.d("op","历史数据"+body.getResult());
//                if(body.getResult().equals("[]")){
//                    UtilsTool.showShortToast(HistoricalinspectionsActivity.this,"没有数据");
//                    return;
//                }
//                String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
//                if(body!=null||body.getResult().equals("[]")){
//                    if(body.getCode().equals(Enumerate.LOGINSUCESS)){
//                        ConstructionDataLitepal rolebean= JSON.parseObject(reu,ConstructionDataLitepal.class);
//                        Log.d("op","历史数据"+rolebean.getResult());
////                        //获取对象
////                        List<HoistoricaDataLitepal> rolebeans= JSON.parseArray(rolebean.getResult(),HoistoricaDataLitepal.class);
////                        Log.d("op","历史数据"+rolebeans);
////                        showAdapter(rolebeans);
//                        List<TaskDbbean> rolebeans= JSON.parseArray(rolebean.getResult(),TaskDbbean.class);
//
//                        if(rolebeans==null||rolebeans.size()==0){
//                            UtilsTool.showShortToast(HistoricalinspectionsActivity.this,"没有任务");
//                            return;
//                        }
//
////            JSONObject obj = JSONObject.fromObject(moreItem);
//                        // -----------------------------------------------------------------------------------------
//                        // 取得名字字段
////            JSONObject jsonObject_AA = jsimple.getJSONObject("checkitem");
//
//
//                        showAdapter(rolebeans);
//
//                    }
//                }else  {
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });



    }

    private void showAdapter(List<HoistoricaDataLitepal> rolebeans) {
        if (rolebeans != null) {
            findasklistadapter = new HistoricalinspectionListAdapter(this, rolebeans, tasks, new AsyncCallback() {
                @Override
                public void onSuccess(List<SignViewDataLitepal> signViewDataLitepal) {
                    Log.d("获取当前的数据", "获取当前的数据signViewDataLitepal" + signViewDataLitepal);
                    if(signViewDataLitepal!=null){
                        Intent intentnotes=new Intent(HistoricalinspectionsActivity.this, NewSignActivity.class);

                        intentnotes.putExtra("constructtionid",NimUIKit.getGson().toJson(signViewDataLitepal));
                        startActivity(intentnotes);
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilsTool.showShortToast(HistoricalinspectionsActivity.this,"没有数据");
                            }
                        });

                    }

                }
            });
            recyclerWorkMore= (RecyclerViewExt) findViewById(R.id.recycler_work_more);
            electricFanView= (LoadingView) findViewById(R.id.electric_fan_view);
            swipeRefreshLayout= (RefreshableView) findViewById(R.id.SwipeRefreshLayout);




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
        return R.layout.activity_historical;
    }
//用户所属工地
    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("历史巡查","历史巡查"+result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            String reus = loginOnSuccssBean.getResult().replace("_id", "rg");
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                Log.d("历史巡查","历史巡查reus"+reus);
                LoginOnSuccssBean loginOnSuccssBeans = JSON.parseObject(reus, LoginOnSuccssBean.class);
                final List<HoistoricaDataLitepal> poistoricaDataLitepal=JSON.parseArray(loginOnSuccssBeans.getResult(),HoistoricaDataLitepal.class);
                Log.d("历史巡查1","历史巡查1"+poistoricaDataLitepal.size());
                if(poistoricaDataLitepal.size()==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilsTool.showShortToast(HistoricalinspectionsActivity.this,"没有数据");
                        }
                    });
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAdapter(poistoricaDataLitepal);
                    }
                });

            }
        }
    }
    public interface AsyncCallback {

        void onSuccess(List<SignViewDataLitepal> signViewDataLitepal);


    }
}
