package com.example.administrator.irrigationworks.Ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMapTouchListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.VisibleRegion;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.ApiUtil;
import com.example.administrator.irrigationworks.Ui.Retrofit.RequestServes;
import com.example.administrator.irrigationworks.Ui.Retrofit.ResultCode;
import com.example.administrator.irrigationworks.Ui.UserPreferences.MyCache;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * 坐标采集
 * OnCameraChangeListener三种监听器用法
 */

public class EventsActivity extends BaseAppCompatActivity implements OnMapClickListener,
        OnMapLongClickListener, OnCameraChangeListener, OnMapTouchListener, AMap.OnMyLocationChangeListener
        , LocationSource, AMap.OnMarkerDragListener, AMap.OnInfoWindowClickListener,View.OnClickListener {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_lat)
    TextView tvLat;
    @Bind(R.id.tv_lng)
    TextView tvLng;
    @Bind(R.id.tap_text)
    TextView tapText;
    @Bind(R.id.camera_text)
    TextView cameraText;
    @Bind(R.id.touch_text)
    TextView touchText;
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.sendBt)
    TextView sendBt;
    private AMap aMap;
    private TextView mTapTextView;
    private TextView mCameraTextView;
    private TextView mTouchTextView;
    private Marker makerB;
    private Marker detailMarker;
    private LatLng latlngB;
    private Marker marker;
    private MyLocationStyle myLocationStyle;
    private String id;
    private String lnts;
    private String lngs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
        // Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        // MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        Boolean net = NetUtil.checkNetWork(EventsActivity.this);
        if (net) {
            init();
            sendBt.setText("采集坐标");
            sendBt.setOnClickListener(this);
            mProDialog = new ProgressDialog(EventsActivity.this);
            mProDialog.setCancelable(true);
            mProDialog.setTitle("请稍后");
            id=getIntent().getStringExtra("id");
        }else {
            UtilsTool.showShortToast(EventsActivity.this,"没有网络");
        }

    }


    @Override
    protected int getLayoutId() {
        return R.layout.events_activity;
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mTapTextView = (TextView) findViewById(R.id.tap_text);
        mCameraTextView = (TextView) findViewById(R.id.camera_text);
        mTouchTextView = (TextView) findViewById(R.id.touch_text);
    }

    /**
     * amap添加一些事件监听器
     */
    private void setUpMap() {
        double lng = Double.valueOf(Preference.getlng());
        double lat = Double.valueOf(Preference.getLat());

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
        LatLng BEIJING = new LatLng(lat, lng);// 北京市经纬度
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14));
        latlngB = new LatLng(lat, lng);
        makerB = aMap.addMarker(new MarkerOptions().position(latlngB)
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        marker = aMap.addMarker(new MarkerOptions()
                .position(latlngB)
                .title("目标位置")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .perspective(true).draggable(true));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
        marker.setPositionByPixels(400, 400);

        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
        aMap.setOnMapTouchListener(this);// 对amap添加触摸地图事件监听器


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
        mapView.onDestroy();
    }

    /**
     * 对单击地图事件回调
     */
    @Override
    public void onMapClick(LatLng point) {
        mTapTextView.setText("tapped, point=" + point);
    }

    /**
     * 对长按地图事件回调
     */
    @Override
    public void onMapLongClick(LatLng point) {
        mTapTextView.setText("long pressed, point=" + point);
    }

    /**
     * 对正在移动地图事件回调
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        mCameraTextView.setText("onCameraChange:" + cameraPosition.toString());
        lnts=cameraPosition.target.latitude+"";
        lngs=cameraPosition.target.longitude+"";
        latlngB = new LatLng(cameraPosition.target.latitude, cameraPosition.target.latitude);

        makerB = aMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.poi_marker_pressed)))
                .position(latlngB));
//        marker.setPosition(latlngB);

    }

    /**
     * 对移动地图结束事件回调
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mCameraTextView.setText("onCameraChangeFinish:"
                + cameraPosition.toString());
        tvLat.setText("当前lat坐标" + cameraPosition.target.latitude);
        tvLng.setText("当前lng坐标" + cameraPosition.target.longitude);
        latlngB = new LatLng(cameraPosition.target.latitude, cameraPosition.target.latitude);
        makerB = aMap.addMarker(new MarkerOptions().position(latlngB)
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        makerB.setIcon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(
                        getResources(),
                        R.mipmap.poi_marker_pressed)));
        VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion(); // 获取可视区域、

        LatLngBounds latLngBounds = visibleRegion.latLngBounds;// 获取可视区域的Bounds
//        boolean isContain = latLngBounds.contains(Constants.SHANGHAI);// 判断上海经纬度是否包括在当前地图可见区域
//        if (isContain) {
//            ToastUtil.show(EventsActivity.this, "上海市在地图当前可见区域内");
//        } else {
//            ToastUtil.show(EventsActivity.this, "上海市超出地图当前可见区域");
//        }
    }

    /**
     * 对触摸地图事件回调
     */
    @Override
    public void onTouch(MotionEvent event) {

        mTouchTextView.setText("触摸事件：屏幕位置" + event.getX() + " " + event.getY());
    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.d("cqq", "此时的高德地图位置getLatitude" + location.getLatitude() + "时的高德地图位置getLongitude" + location.getLongitude());

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    /**
     * 监听开始拖动marker事件回调
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d("cqq", "onMarkerDragStart此时的高德地图位置getLatitude" + marker.getPosition().latitude + "时的高德地图位置getLongitude" + marker.getPosition().latitude);

    }

    /**
     * 监听拖动marker时事件回调
     */
    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d("cqq", "onMarkerDrag此时的高德地图位置getLatitude" + marker.getPosition().latitude + "时的高德地图位置getLongitude" + marker.getPosition().latitude);

    }

    /**
     * 监听拖动marker结束事件回调
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d("cqq", "onMarkerDragEnd此时的高德地图位置getLatitude" + marker.getPosition().latitude + "时的高德地图位置getLongitude" + marker.getPosition().latitude);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("cqq", "此时的高德地图位置getLatitude" + marker.getPosition().latitude + "时的高德地图位置getLongitude" + marker.getPosition().latitude);

    }
    // 进度对话框
    private ProgressDialog mProDialog;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendBt:
                    //采集坐标
                    AlertDialog.Builder builderlogin = new AlertDialog.Builder(EventsActivity.this);
                builderlogin.setMessage("确认采集坐标吗？");
                builderlogin.setTitle("提示");
                builderlogin.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProDialog = new ProgressDialog(EventsActivity.this);
                        mProDialog.setCancelable(true);
                        mProDialog.setMessage("采集中...");
                        mProDialog.show();
                        getData();

                    }
                });
                builderlogin.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderlogin.create().show();
                break;
        }
    }

    public void getData() {
        String lntpositon="";
        String lngpositon="";
        if(!TextUtils.isEmpty(lnts)&&!TextUtils.isEmpty(lngs)){
            lntpositon=lnts;
            lngpositon=lngs;
        }else {
            lngpositon = Double.valueOf(Preference.getlng())+"";
            lntpositon = Double.valueOf(Preference.getLat())+"";
        }
        //暂时传手机本地的坐标
        String lng = Preference.getlng();
        String lat = Preference.getLat();
        RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
        Map<String, Object> hashMap = new HashMap<String, Object>();
        /**
         * 参数为手机当前定位位置
         */
        hashMap.put("lat", lat);
        hashMap.put("lnt", lng);
        hashMap.put("token", NimUIKit.getToken());
        hashMap.put("constructionid", id);
        Call<ResultCode> call = apiService.getLcation(hashMap);
        call.enqueue(new Callback<ResultCode>() {
            @Override
            public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
                ResultCode body = response.body();
                if(body!=null){
                    if(body.getCode().equals(Enumerate.LOGINSUCESS)){
                        UtilsTool.showShortToast(EventsActivity.this,"采集成功");
                        mProDialog.dismiss();
                        finish();
                    }else {
                        UtilsTool.showShortToast(EventsActivity.this,"采集失败");
                        mProDialog.dismiss();
                    }
                }else {
                    UtilsTool.showShortToast(EventsActivity.this,"采集失败");
                    mProDialog.dismiss();
                }


//
//
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("qc", "坐标失败" + t.getMessage());
                UtilsTool.showShortToast(EventsActivity.this,"采集失败");
                mProDialog.dismiss();
            }
        });
    }
}
