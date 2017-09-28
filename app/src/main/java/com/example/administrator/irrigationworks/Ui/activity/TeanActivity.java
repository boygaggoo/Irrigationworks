package com.example.administrator.irrigationworks.Ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.adapter.WariningListAdapter;
import com.example.administrator.irrigationworks.Ui.bean.BuildingSiteBean;
import com.example.administrator.irrigationworks.Ui.bean.GeDangerlistBean;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.WarininglistBean;
import com.example.administrator.irrigationworks.Ui.litepal.ConstructionDataLitepal;
import com.example.administrator.irrigationworks.Ui.uiview.RecyclerViewExt;
import com.example.administrator.irrigationworks.Ui.widget.RefreshableView;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.example.administrator.irrigationworks.View.IReadDataListener;
import com.example.administrator.irrigationworks.View.IReadGetPresenter;
import com.example.administrator.irrigationworks.View.ReadnPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import app.dinus.com.loadingdrawable.LoadingView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/13.
 */
public class TeanActivity extends BaseAppCompatActivity implements IReadDataListener {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
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
    private List<WarininglistBean> moreItemList;
    private boolean hasMore = true;
    public static int limitCounts = 10;
    private WariningListAdapter findasklistadapter;
    //临时设置限时操作
    private Timer timer = new Timer();
    private int recLen = 0;

    //点击item展开
    private int oldPostion = -1;
    private String type = "";
    private IReadGetPresenter prese;
    // 进度对话框
    private ProgressDialog mProDialog;

    public TeanActivity() {
        this.prese = new ReadnPresenter(this);
    }

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
        try {
            Boolean net = NetUtil.checkNetWork(TeanActivity.this);
            if (net) {
//            initRefresh();
                type = getIntent().getStringExtra(Constants.danger);
                adapterInit();


            } else {
                networkError.setVisibility(View.VISIBLE);
                electricFanView.setVisibility(View.GONE);
                UtilsTool.showShortToast(TeanActivity.this, "没有网络");
            }
        }catch (Exception e){

        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_warining;
    }

    @Override
    public void reclaim(String states) {
        Log.d("qc","reclaim已审核"+states);

    }
    /**
     * 隐患列表
     */
    private class GetHazardlist implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("隐患列表", "隐患列表" + result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                ConstructionDataLitepal rolebean = JSON.parseObject(onSuccssBea.getResult(), ConstructionDataLitepal.class);
                Log.d("隐患警告", "隐患列表" + rolebean.getResult());
                //获取对象
                final List<GeDangerlistBean> personlistBean = JSON.parseArray(rolebean.getResult(), GeDangerlistBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAdapter(personlistBean);
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(TeanActivity.this, "没有数据");
                    }
                });

            }
        }


    }

    private class GeDangerWarn implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("隐患警告", "隐患警告" + result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);

            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                if("[]".equals(onSuccssBea.getResult())){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilsTool.showShortToast(TeanActivity.this, "没有数据");
                            if(mProDialog!=null){
                                mProDialog.dismiss();
                            }
                        }
                    });
                    return;
                }
                String reus = onSuccssBea.getResult().replace("_id", "rg");
                ConstructionDataLitepal rolebean = JSON.parseObject(reus, ConstructionDataLitepal.class);
                if("[]".equals(rolebean.getResult())){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mProDialog!=null){
                                mProDialog.dismiss();
                            }
                            UtilsTool.showShortToast(TeanActivity.this, "没有数据");
                        }
                    });
                    return;
                }
                //获取对象
                final List<GeDangerlistBean> personlistBean = JSON.parseArray(rolebean.getResult(), GeDangerlistBean.class);
                Log.d("隐患警告", "隐患警告" + personlistBean);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAdapter(personlistBean);
                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mProDialog!=null){
                            mProDialog.dismiss();
                        }
                        UtilsTool.showShortToast(TeanActivity.this, "没有数据");
                    }
                });


            }
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
        //获取任务巡查数据
        if (type.equals(Constants.dangerList)) {//隐患列表
            final Map<String, Object> hashMaps = new HashMap<String, Object>();
            hashMaps.put("_id", 1);//升序
            Map<String, Object> hashMap = new HashMap<String, Object>();

            hashMap.put("token", NimUIKit.getToken());
            hashMap.put("order", hashMaps);
            try {
                HttpUtils.doPostAsyn(FinalTozal.getHazardlist, NimUIKit.getGson().toJson(hashMap), new GeDangerWarn());
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (type.equals(Constants.dangerWarn)) {
            Map<String, Object> hashMap = new HashMap<String, Object>();
            final Map<String, Object> hashMaps = new HashMap<String, Object>();
            hashMaps.put("_id", 1);//升序
//                    /**
//                     * 参数为手机当前定位位置
//                     */
//
            hashMap.put("token", NimUIKit.getToken());
            hashMap.put("order", hashMaps);
            try {
                HttpUtils.doPostAsyn(FinalTozal.getDangerWarn, NimUIKit.getGson().toJson(hashMap), new GeDangerWarn());
            } catch (Exception e) {
                e.printStackTrace();
            }
//            RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
//            String token = NimUIKit.getToken();
//
//            Call<ResultCode> call = apiService.getDangerWarn(token);
//            call.enqueue(new Callback<ResultCode>() {
//                @Override
//                public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
//                    ResultCode body = response.body();
//                    Log.d("隐患警告", "隐患警告" + body.getResult());
//                String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
//                if (body != null) {
//                    if (body.getCode().equals(Enumerate.LOGINSUCESS)) {
//                        ConstructionDataLitepal rolebean = JSON.parseObject(reu, ConstructionDataLitepal.class);
//                        Log.d("隐患警告", "隐患列表" + rolebean.getResult());
////获取对象
//                        List<HazardPersonlistBean> personlistBean= JSON.parseArray(rolebean.getResult(), HazardPersonlistBean.class);
//                        showAdapter(personlistBean);
//                    }
//                } else {
//                    UtilsTool.showShortToast(WariningActivity.this,"没有数据");
//                }
//
//
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    Log.d("隐患警告", "隐患警告" + t.getMessage());
//                }
//            });
        }//警告

//

    }

    private void showAdapter(final List<GeDangerlistBean> personlistBean) {
        if(mProDialog!=null){
            mProDialog.dismiss();
        }
        if (personlistBean != null) {
            findasklistadapter = new WariningListAdapter(this, personlistBean, type, prese, new AsyncCallback() {
                @Override
                public void onSuccess(String states) {
                    if (!TextUtils.isEmpty(states)) {
                        if (states.equals("未按确认读取,不可操作")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsTool.showShortToast(TeanActivity.this, "未按确认读取,不可操作");
                                }
                            });
                        }else {

                        }
                        if (states.equals("项目经理尚未处理")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsTool.showShortToast(TeanActivity.this, "项目经理尚未处理");
                                }
                            });
                        }else {

                        }
                        if (states.equals("项目经理未处理隐患,不可操作")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsTool.showShortToast(TeanActivity.this, "项目经理未处理隐患,不可操作");
                                }
                            });
                        }else {

                        }
                        if (states.equals("已读")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsTool.showShortToast(TeanActivity.this, "已读");
                                    mProDialog = new ProgressDialog(TeanActivity.this);
                                    mProDialog.setCancelable(true);
                                    mProDialog.setMessage("验证中...");
                                    mProDialog.show();
                                    if (type.equals(Constants.dangerList)) {//隐患列表
                                        Map<String, Object> hashMap = new HashMap<String, Object>();
//                    /**
//                     * 参数为手机当前定位位置
//                     */
//
                                        hashMap.put("token", NimUIKit.getToken());

                                        try {
                                            HttpUtils.doPostAsyn(FinalTozal.getHazardlist, NimUIKit.getGson().toJson(hashMap), new GeDangerWarn());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else if (type.equals(Constants.dangerWarn)) {
                                        Map<String, Object> hashMap = new HashMap<String, Object>();
//                    /**
//                     * 参数为手机当前定位位置
//                     */
//
                                        hashMap.put("token", NimUIKit.getToken());
                                        try {
                                            HttpUtils.doPostAsyn(FinalTozal.getDangerWarn, NimUIKit.getGson().toJson(hashMap), new GeDangerWarn());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
//            RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
//            String token = NimUIKit.getToken();
//
//            Call<ResultCode> call = apiService.getDangerWarn(token);
//            call.enqueue(new Callback<ResultCode>() {
//                @Override
//                public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
//                    ResultCode body = response.body();
//                    Log.d("隐患警告", "隐患警告" + body.getResult());
//                String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
//                if (body != null) {
//                    if (body.getCode().equals(Enumerate.LOGINSUCESS)) {
//                        ConstructionDataLitepal rolebean = JSON.parseObject(reu, ConstructionDataLitepal.class);
//                        Log.d("隐患警告", "隐患列表" + rolebean.getResult());
////获取对象
//                        List<HazardPersonlistBean> personlistBean= JSON.parseArray(rolebean.getResult(), HazardPersonlistBean.class);
//                        showAdapter(personlistBean);
//                    }
//                } else {
//                    UtilsTool.showShortToast(WariningActivity.this,"没有数据");
//                }
//
//
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    Log.d("隐患警告", "隐患警告" + t.getMessage());
//                }
//            });
                                    }//警告

                                }
                            });
                        }
                        if (states.equals("已审核")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsTool.showShortToast(TeanActivity.this, "已审核");
                                    mProDialog = new ProgressDialog(TeanActivity.this);
                                    mProDialog.setCancelable(true);
                                    mProDialog.setMessage("验证中...");
                                    mProDialog.show();
                                    if (type.equals(Constants.dangerList)) {//隐患列表
                                        Map<String, Object> hashMap = new HashMap<String, Object>();
//                    /**
//                     * 参数为手机当前定位位置
//                     */
//
                                        hashMap.put("token", NimUIKit.getToken());

                                        try {
                                            HttpUtils.doPostAsyn(FinalTozal.getHazardlist, NimUIKit.getGson().toJson(hashMap), new GeDangerWarn());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else if (type.equals(Constants.dangerWarn)) {
                                        Map<String, Object> hashMap = new HashMap<String, Object>();
//                    /**
//                     * 参数为手机当前定位位置
//                     */
//
                                        hashMap.put("token", NimUIKit.getToken());
                                        try {
                                            HttpUtils.doPostAsyn(FinalTozal.getDangerWarn, NimUIKit.getGson().toJson(hashMap), new GeDangerWarn());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
//            RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
//            String token = NimUIKit.getToken();
//
//            Call<ResultCode> call = apiService.getDangerWarn(token);
//            call.enqueue(new Callback<ResultCode>() {
//                @Override
//                public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
//                    ResultCode body = response.body();
//                    Log.d("隐患警告", "隐患警告" + body.getResult());
//                String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
//                if (body != null) {
//                    if (body.getCode().equals(Enumerate.LOGINSUCESS)) {
//                        ConstructionDataLitepal rolebean = JSON.parseObject(reu, ConstructionDataLitepal.class);
//                        Log.d("隐患警告", "隐患列表" + rolebean.getResult());
////获取对象
//                        List<HazardPersonlistBean> personlistBean= JSON.parseArray(rolebean.getResult(), HazardPersonlistBean.class);
//                        showAdapter(personlistBean);
//                    }
//                } else {
//                    UtilsTool.showShortToast(WariningActivity.this,"没有数据");
//                }
//
//
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    Log.d("隐患警告", "隐患警告" + t.getMessage());
//                }
//            });
                                    }//警告

                                }
                            });
                        }
                        if (states.equals("请求失败")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsTool.showShortToast(TeanActivity.this, "请求失败");
                                }
                            });
                        }
                    }
                }
            });
            recyclerWorkMore= (RecyclerViewExt) findViewById(R.id.recycler_work_more);
            electricFanView= (LoadingView) findViewById(R.id.electric_fan_view);
            swipeRefreshLayout= (RefreshableView) findViewById(R.id.SwipeRefreshLayout);
            recyclerWorkMore.setLayoutManager(new LinearLayoutManager(this));
            recyclerWorkMore.setAdapter(findasklistadapter);
            final List<TaskChechItembean> taskChechItembean=new ArrayList<>();
            recyclerWorkMore.setOnItemClickListener(new RecyclerViewExt.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                    Log.d("qcv",""+"隐患警告"+NimUIKit.getRole());
                    if (type.equals(Constants.dangerWarn)) {
                        //隐患列表
                        if(Enumerate.General_supervision.equals(NimUIKit.getRole())){
                            //如果是总监理
                            if (personlistBean.get(position).getPm_confirm() == 0) {//项目经理 1已经整改了 0没有整改

                                Intent intentnotess = new Intent(TeanActivity.this, ToubleChitemSiteActivity.class);
                                intentnotess.putExtra("chitems", NimUIKit.getGson().toJson(taskChechItembean));

                                intentnotess.putExtra("id", personlistBean.get(position).getRg());//传递id
                                intentnotess.putExtra("spectorid", personlistBean.get(position).getInspector().getRg());//巡查员id
                                intentnotess.putExtra("pmid", personlistBean.get(position).getConstruction().getChiefsupervisor().getRg());//总监理id
                                intentnotess.putExtra("moreItem", personlistBean.get(position).getCheckitem());//巡查项目
                                intentnotess.putExtra("name", personlistBean.get(position).getConstruction().getName());//工地名字
                                intentnotess.putExtra("confirm",personlistBean.get(position).getPm_confirm()+"");//项目经理是否已经处理mContext.startActivity(intentnotess);
                                startActivity(intentnotess);
                                return;
                            }
                            if(null==personlistBean.get(position).getRectificationInfo()){
                                UtilsTool.showShortToast(TeanActivity.this,"没有数据");
                                return;
                            }
                            Intent intentnotess = new Intent(TeanActivity.this, VerifyActivity.class);
                            intentnotess.putExtra("contect",personlistBean.get(position).getRectificationInfo().getContect());
                            intentnotess.putExtra("id",personlistBean.get(position).getConstruction().getChiefsupervisor().getRg());//总监理id
                            intentnotess.putStringArrayListExtra("pics", personlistBean.get(position).getRectificationInfo().getPics());
                           startActivity(intentnotess);
                            return;
                        }else if(Enumerate.project_manager.equals(NimUIKit.getRole())) {
                            //如果是项目经理

                            Intent intentnotess = new Intent(TeanActivity.this, ToubleChitemSiteActivity.class);
                            intentnotess.putExtra("chitems", NimUIKit.getGson().toJson(taskChechItembean));

                            intentnotess.putExtra("id", personlistBean.get(position).getRg());//传递id
                            intentnotess.putExtra("spectorid", personlistBean.get(position).getInspector().getRg());//巡查员id
                            intentnotess.putExtra("pmid", personlistBean.get(position).getConstruction().getChiefsupervisor().getRg());//总监理id
                            intentnotess.putExtra("taskid", personlistBean.get(position).getTaskid());//隐患列表的不通过的检查项目
                            intentnotess.putExtra("moreItem", personlistBean.get(position).getCheckitem());//巡查项目
                            intentnotess.putExtra("name", personlistBean.get(position).getConstruction().getName());//工地名字
                            intentnotess.putExtra("confirm",personlistBean.get(position).getPm_confirm()+"");//项目经理是否已经处理mContext.startActivity(intentnotess);
                            startActivity(intentnotess);
                            return;

                        }else {
                            Intent intentnotess = new Intent(TeanActivity.this, ToubleChitemSiteActivity.class);
                            intentnotess.putExtra("moreItem", personlistBean.get(position).getCheckitem());//巡查项目
                            intentnotess.putExtra("name", personlistBean.get(position).getConstruction().getName());//工地名字
                            intentnotess.putExtra("chitems", NimUIKit.getGson().toJson(taskChechItembean));
                           startActivity(intentnotess);
                        }

                    } else {

                        Intent intentnotess = new Intent(TeanActivity.this, ToubleChitemSiteActivity.class);
                        intentnotess.putExtra("moreItem", personlistBean.get(position).getCheckitem());//巡查项目
                        intentnotess.putExtra("chitems", NimUIKit.getGson().toJson(taskChechItembean));
                        intentnotess.putExtra("name", personlistBean.get(position).getConstruction().getName());//工地名字
                        startActivity(intentnotess);
                    }

                }

                @Override
                public void onItemLongClick(RecyclerView.ViewHolder vh, int position) {

                }
            });
            electricFanView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            UtilsTool.showShortToast(TeanActivity.this, "没有数据");
            electricFanView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    public interface AsyncCallback {

        void onSuccess(String re);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerWorkMore!=null){

        }

    }
}
