package com.example.administrator.irrigationworks.Ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.overlay.RideRouteOverlay;
import com.example.administrator.irrigationworks.UtilsTozals.AMapUtil;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.ToastUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 骑行路径规划功能的实现
 */
public class RideRouteActivity extends BaseAppCompatActivity implements OnMapClickListener,
        OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.route_drive)
    ImageView routeDrive;
    @Bind(R.id.route_bus)
    ImageView routeBus;
    @Bind(R.id.route_walk)
    ImageView routeWalk;
    @Bind(R.id.route_CrosstownBus)
    TextView routeCrosstownBus;
    @Bind(R.id.routemap_choose)
    LinearLayout routemapChoose;
    @Bind(R.id.routemap_header)
    RelativeLayout routemapHeader;
    @Bind(R.id.firstline)
    TextView firstline;
    @Bind(R.id.secondline)
    TextView secondline;
    @Bind(R.id.detail)
    LinearLayout detail;
    @Bind(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @Bind(R.id.route_map)
    MapView routeMap;
    @Bind(R.id.bus_result_list)
    ListView busResultList;
    @Bind(R.id.bus_result)
    LinearLayout busResult;
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private RideRouteResult mRideRouteResult;
    //	private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);//起点，116.335891,39.942295
    private LatLonPoint mStartPoint;
    //	private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576
    private LatLonPoint mEndPoint;
    private final int ROUTE_TYPE_RIDE = 4;

    private RelativeLayout mBottomLayout, mHeadLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private ProgressDialog progDialog = null;// 搜索时进度条


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            ButterKnife.bind(this);
        mContext = this.getApplicationContext();
        toolbar.setVisibility(View.VISIBLE);
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapView = (MapView) findViewById(R.id.route_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        Boolean net = NetUtil.checkNetWork(RideRouteActivity.this);
        if (net) {
            init();
            getLoationg();
            setfromandtoMarker();
            searchRouteResult(ROUTE_TYPE_RIDE, RouteSearch.RidingDefault);
        } else {
            UtilsTool.showShortToast(RideRouteActivity.this, "没有网络");
        }

    }

    private void getLoationg() {
        //起点坐标
        String lng = Preference.getlng();
        String lat = Preference.getLat();
        double lngmEndPoint = Double.valueOf(getIntent().getStringExtra("lag"));
        double latmEndPoint = Double.valueOf(getIntent().getStringExtra("Lng"));
        BigDecimal bg = new BigDecimal(lng);
//只保存后六位小数,保留太多请求接口时，返回不了数据
        Log.d("坐标", "此时的坐标为" + "lng" + lng + "lat" + lat);
        Log.d("坐标", "此时的坐标为" + "lngmEndPoint" + lngmEndPoint + "latmEndPoint" + latmEndPoint);
        double doublelng = bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal bglat = new BigDecimal(lat);
//只保存后六位小数,保留太多请求接口时，返回不了数据
        //
        double doublelat = bglat.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        mStartPoint = new LatLonPoint(doublelat, doublelng);
        //起点暂时固定
//		 mStartPoint = new LatLonPoint(39.924870, 116.403270);//起点
        //终点工地坐标
        mEndPoint = new LatLonPoint(lngmEndPoint, latmEndPoint);//终点
    }

    @Override
    protected int getLayoutId() {
        return R.layout.route_activity;
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mHeadLayout = (RelativeLayout) findViewById(R.id.routemap_header);
        mHeadLayout.setVisibility(View.GONE);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);

    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(RideRouteActivity.this);
        aMap.setOnMarkerClickListener(RideRouteActivity.this);
        aMap.setOnInfoWindowClickListener(RideRouteActivity.this);
        aMap.setInfoWindowAdapter(RideRouteActivity.this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }


    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {

            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_RIDE) {// 骑行路径规划
            RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateRideRouteAsyn(query);// 异步路径规划骑行模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {

    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mapView.onDestroy();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mRideRouteResult = result;
                    final RidePath ridePath = mRideRouteResult.getPaths()
                            .get(0);
                    RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
                            this, aMap, ridePath,
                            mRideRouteResult.getStartPos(),
                            mRideRouteResult.getTargetPos());
                    rideRouteOverlay.removeFromMap();
                    rideRouteOverlay.addToMap();
                    rideRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) ridePath.getDistance();
                    int dur = (int) ridePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    RideRouteDetailActivity.class);
                            intent.putExtra("ride_path", ridePath);
                            intent.putExtra("ride_result",
                                    mRideRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }


}

