package com.example.administrator.irrigationworks.Ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 工地坐标采集
 * Created by Administrator on 2017/5/18.
 */
public class ThreeproofingsActivity extends BaseAppCompatActivity implements AMap.OnMarkerDragListener {

    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.info_text)
    TextView infoText;
    @Bind(R.id.map)
    MapView mapView;
    private Activity ctx;
    private View layout;
    private AMap aMap;
    private LatLng latlngA;
    private Marker makerA;
    private Marker makerB;
    private TextView Text;
    private float distance;
    private String lng;
    private String lat;
    private double doublelng;
    private double doublelat;
    private LatLng latlngB = new LatLng(39.924870, 116.403270);
//    private LatLng latlngA = new LatLng(39.926516, 116.389366);

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
        getLocation(savedInstanceState);
        infoText.setText("长按Marker可拖动\n两点间距离为：" + distance + "m");
    }


    private void getLocation(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        lng = Preference.getlng();
        lat = Preference.getLat();
        BigDecimal bg = new BigDecimal(lng);
        //只保存后六位小数,保留太多请求接口时，返回不了数据
        doublelng = bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal bglat = new BigDecimal(lat);
        //只保存后六位小数,保留太多请求接口时，返回不了数据
        doublelat = bglat.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        ;
        latlngA = new LatLng(doublelat, doublelng);// 116.472995,39.993743
        init();

    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);
        makerA = aMap.addMarker(new MarkerOptions().position(latlngA)
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        makerB = aMap.addMarker(new MarkerOptions().position(latlngB)
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(doublelat,
                doublelng), 15));
    }



    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d("qc", "onMarkerDrag此时的maker" + marker.getPosition().latitude);
        Log.d("qc", "onMarkerDrag此时的maker" + marker.getPosition().longitude);
        infoText.setText("point X：" + marker.getPosition().latitude + "\n" + "point Y：" + marker.getPosition().longitude + "\n");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three_proofing;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
