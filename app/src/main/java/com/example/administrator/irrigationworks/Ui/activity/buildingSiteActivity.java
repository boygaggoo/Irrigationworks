package com.example.administrator.irrigationworks.Ui.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.ApiUtil;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.Retrofit.RequestServes;
import com.example.administrator.irrigationworks.Ui.Retrofit.ResultCode;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.adapter.BuildingListAdapter;
import com.example.administrator.irrigationworks.Ui.adapter.ConstrutionListAdapter;
import com.example.administrator.irrigationworks.Ui.bean.BuildingSiteBean;
import com.example.administrator.irrigationworks.Ui.bean.BuildinglistBean;
import com.example.administrator.irrigationworks.Ui.bean.HoistoricaDataLitepal;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.TaskDbbean;
import com.example.administrator.irrigationworks.Ui.litepal.ConstructionDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.TotalDataLitepal;
import com.example.administrator.irrigationworks.Ui.uiview.RecyclerViewExt;
import com.example.administrator.irrigationworks.Ui.widget.RefreshableView;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import app.dinus.com.loadingdrawable.LoadingView;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 工地项目列表
 * Created by Administrator on 201R7/8/15.
 */
public class buildingSiteActivity extends BaseAppCompatActivity implements
        PoiSearch.OnPoiSearchListener, AMap.OnMyLocationChangeListener,
        GeocodeSearch.OnGeocodeSearchListener,LocationSource {
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
    @Bind(R.id.network_error)
    ImageView networkError;
    @Bind(R.id.map)
    MapView mapView;
    private AMap aMap;
    private BuildingSiteBean site;
    private List<BuildinglistBean> moreItemList;
    private boolean hasMore = true;
    public static int limitCounts = 10;
    private BuildingListAdapter findasklistadapter;
    private ConstrutionListAdapter construtionListAdapter;
    //临时设置限时操作
    private Timer timer = new Timer();
    private int recLen = 0;

    // 进度对话框
    //点击item展开
    private int oldPostion = -1;
    private String type;
    private String are;
    private MyLocationStyle myLocationStyle;
    private Circle circle;
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
        Boolean net = NetUtil.checkNetWork(buildingSiteActivity.this);
        try{
            try{
                if (net) {
//            initRefresh();
                    type = getIntent().getStringExtra(Constants.IDENTICAL);
                    are = getIntent().getStringExtra(Constants.Patroller);
//            adapterInit();
                    getData();
                    inMapUi();
                } else {
                    networkError.setVisibility(View.VISIBLE);
                    electricFanView.setVisibility(View.GONE);
                    UtilsTool.showShortToast(buildingSiteActivity.this, "没有网络");
                }
            }catch (Exception e){

            }
        }catch (Exception e){

        }



    }

    private void inMapUi() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        String lng = Preference.getlng();
        String lat = Preference.getLat();
        BigDecimal bg = new BigDecimal(lng);
//只保存后六位小数,保留太多请求接口时，返回不了数据
        double doublelng = bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal bglat = new BigDecimal(lat);
//只保存后六位小数,保留太多请求接口时，返回不了数据
        double doublelat = bglat.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        Preference.savaLat(doublelat + "");
        Preference.savalng(doublelng + "");
        LatLng nowlng = new LatLng(doublelat, doublelng);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.getMyLocationIcon();
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
//

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

    private void adapterInit() {
        //获取任务巡查数据
        RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
        String token = NimUIKit.getToken();
        Map<String, Object> hashMap = new HashMap<String, Object>();
        /**
         * 参数为手机当前定位位置
         */
        hashMap.put("token", token);
        hashMap.put("construction.area", type);//过滤片区
        Call<ResultCode> call = apiService.getConstructionList(hashMap);
        call.enqueue(new Callback<ResultCode>() {
            @Override
            public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
                ResultCode body = response.body();

                String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
                if (body != null) {
                    if (body.getCode().equals(Enumerate.LOGINSUCESS)) {
                        ConstructionDataLitepal rolebean = JSON.parseObject(reu, ConstructionDataLitepal.class);
                        List<RoleBeanLitepal> secuGetDta = new ArrayList<RoleBeanLitepal>();
                        DataSupport.saveAll(secuGetDta);//保存到数据库
                        Log.d("qccq", "打卡成功" + rolebean.getResult());
                        //获取对象
                        List<TotalDataLitepal> rolebeans = JSON.parseArray(rolebean.getResult(), TotalDataLitepal.class);
                        if (rolebeans == null || rolebeans.size() == 0) {
                            UtilsTool.showShortToast(buildingSiteActivity.this, "没有数据");
                            return;
                        }
//                        showAdapter(rolebeans);
                    }
                } else {
                    UtilsTool.showShortToast(buildingSiteActivity.this, "没有数据");
                }


            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

//        String jsonData = ResourceUtils.geFileFromAssets(this, "buildingData.json");
//        Log.d("qc", "此时解析的集合为" + jsonData);
//        moreItemList = new ArrayList<>();
//        OnSuccssBean moreItemLists = JSON.parseObject(jsonData, OnSuccssBean.class);
//        if (moreItemLists != null) {
//            moreItemList = JSON.parseArray(moreItemLists.getResult(), BuildinglistBean.class);
//            findasklistadapter = new BuildingListAdapter(this, moreItemList);
//            recyclerWorkMore.setLayoutManager(new LinearLayoutManager(this));
//            recyclerWorkMore.setAdapter(findasklistadapter);
//            electricFanView.setVisibility(View.GONE);
//            swipeRefreshLayout.setVisibility(View.VISIBLE);
////            recyclerWorkMore.setOnItemClickListener(new RecyclerViewExt.OnItemClickListener() {
////                @Override
////                public void onItemClick(RecyclerView.ViewHolder vh, int position) {
////                    Log.d("qqc","点击onItemClick了");
////                    BuildinglistBean data = moreItemList.get(position);
////                    if (oldPostion == position) {
////                        if (data.isExpand())  {
////                            oldPostion = -1;
////                        }
////                        data.setExpand(!data.isExpand());
////                    }else{
////                        oldPostion = position;
////                        data.setExpand(true);
////                    }
////                    findasklistadapter.notifyDataSetChanged();
////                }
////
////                @Override
////                public void onItemLongClick(RecyclerView.ViewHolder vh, int position) {
////
////                }
////            });
//        } else {
//            electricFanView.setVisibility(View.VISIBLE);
//        }
    }

    private void showAdapter(final List<TaskDbbean> rolebeans) {
        Log.d("qc", "showAdapter打卡成功" + rolebeans.size());
        Log.i("qc", "showAdapter: "+rolebeans);
        Log.d("showAdapter打卡成功", "showAdapter打卡成功" + rolebeans.get(0).getConstruction().getCoordinate());
        if (rolebeans != null) {
            findasklistadapter = new BuildingListAdapter(this, rolebeans, aMap, circle, new AsyncCallback() {
                @Override
                public void onSuccess() {
                    finish();
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
        return R.layout.activity_building_site;
    }

    public void getData() {
        final Map<String, Object> hashMap = new HashMap<String, Object>();

        final Map<String, Object> hashMaps = new HashMap<String, Object>();
        hashMaps.put("area", type);//过滤片区
        /**
         * 参数为手机当前定位位置
         */
        hashMap.put("token", NimUIKit.getToken());
        Map<String, Object> hashMapsort = new HashMap<String, Object>();
        hashMapsort.put("_id", 1);//升序
        hashMap.put("order", hashMapsort);

        Log.d("qc","此时的参数"+NimUIKit.getGson().toJson(hashMap));


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(Constants.Construction.equals(are)){
                        //如果是施工方的
                        HttpUtils.doPostAsyn(FinalTozal.getConstructionList, NimUIKit.getGson().toJson(hashMap), new GetStructionlist());
                    }else {
                        //如果是巡查员的话
                        hashMap.put("searchValue", hashMaps);//过滤片区
                        HttpUtils.doPostAsyn(FinalTozal.getCheckTask, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
    //倒序 add by bb
    List<TaskDbbean> rbs=new ArrayList<>();
    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("此时的解析", "此时的解析" + result);
            final LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);

            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                if(loginOnSuccssBean.getResult().equals("[]")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            electricFanView= (LoadingView) findViewById(R.id.electric_fan_view);
                            electricFanView.setVisibility(View.VISIBLE);
                            UtilsTool.showShortToast(buildingSiteActivity.this,"没有数据");
                        }
                    });
                    return;
                }
                String reus = loginOnSuccssBean.getResult().replace("_id", "rg");
//                ConstructionDataLitepal rolebean = JSON.parseObject(reus, ConstructionDataLitepal.class);
//            ConstructionDataLitepal rolebean = JSON.parseObject(reus, ConstructionDataLitepal.class);
//            //获取对象


                final List<TaskDbbean> rolebeans = JSON.parseArray(reus, TaskDbbean.class);
                if (rolebeans == null || rolebeans.size() == 0) {
                    UtilsTool.showShortToast(buildingSiteActivity.this, "没有数据");
                    return;
                }

                //倒序排列 add by bb
                for (int i = rolebeans.size()-1; i >=0; i--) {
                    rbs.add(rolebeans.get(i));
                }

                Log.d("qc", "jsonData" + rolebeans);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAdapter(rbs);
                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        electricFanView= (LoadingView) findViewById(R.id.electric_fan_view);
                        electricFanView.setVisibility(View.VISIBLE);
                        UtilsTool.showShortToast(buildingSiteActivity.this,loginOnSuccssBean.getMessage());
                    }
                });

            }
//            Log.d("qc", "此时的解析" + result);
//            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
//            String reus = loginOnSuccssBean.getResult().replace("_id", "rg");
//            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
//                ConstructionDataLitepal rolebean = JSON.parseObject(reus, ConstructionDataLitepal.class);
////            ConstructionDataLitepal rolebean = JSON.parseObject(reus, ConstructionDataLitepal.class);
////            //获取对象
//                if (rolebean.getResult().equals("[]")) {
//                    UtilsTool.showShortToast(buildingSiteActivity.this, "没有数据");
//                    return;
//                }
//
//                final List<TaskDbbean> rolebeans = JSON.parseArray(rolebean.getResult(), TaskDbbean.class);
//                if (rolebeans == null || rolebeans.size() == 0) {
//                    UtilsTool.showShortToast(buildingSiteActivity.this, "没有任务");
//                    return;
//                }
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showAdapter(rolebeans);
//                    }
//                });
//
//            } else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        UtilsTool.showShortToast(buildingSiteActivity.this, "没有数据");
//                        electricFanView.setVisibility(View.VISIBLE);
//                    }
//                });
//
//            }
        }
    }
    public interface AsyncCallback {

        void onSuccess();


    }

    public interface AsyncStructionlistCallback {

        void oncallbask();


    }

    private class GetStructionlist implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("历史巡查","历史巡查"+result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            String reus = loginOnSuccssBean.getResult().replace("_id", "rg");
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                Log.d("历史巡查","历史巡查reus"+reus);
                LoginOnSuccssBean loginOnSuccssBeans = JSON.parseObject(reus, LoginOnSuccssBean.class);
                final List<HoistoricaDataLitepal> poistoricaDataLitepal=JSON.parseArray(loginOnSuccssBeans.getResult(),HoistoricaDataLitepal.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAdapterStructionlist(poistoricaDataLitepal);

                    }
                });


            }
        }
    }

    private void showAdapterStructionlist(List<HoistoricaDataLitepal> rolebeans) {
        Log.d("历史巡查1","历史巡查1"+rolebeans);
        Log.d("历史巡查1","历史巡查1"+rolebeans.size());
        if (rolebeans != null) {
            construtionListAdapter = new ConstrutionListAdapter(this, rolebeans, new AsyncStructionlistCallback() {
                @Override
                public void oncallbask() {

                }
            });
            recyclerWorkMore= (RecyclerViewExt) findViewById(R.id.recycler_work_more);
            electricFanView= (LoadingView) findViewById(R.id.electric_fan_view);
            swipeRefreshLayout= (RefreshableView) findViewById(R.id.SwipeRefreshLayout);

            recyclerWorkMore.setLayoutManager(new LinearLayoutManager(this));
            recyclerWorkMore.setAdapter(construtionListAdapter);
            electricFanView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            electricFanView.setVisibility(View.VISIBLE);
        }
    }
}
