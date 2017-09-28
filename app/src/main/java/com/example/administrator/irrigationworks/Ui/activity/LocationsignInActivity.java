package com.example.administrator.irrigationworks.Ui.activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/9.
 */
public class LocationsignInActivity extends BaseAppCompatActivity implements LocationSource,
        AMapLocationListener,RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.tv_curdate)
    TextView tvCurdate;
    @Bind(R.id.tv_curlocal)
    TextView tvCurlocal;
    @Bind(R.id.tv_location_update)
    TextView tvLocationUpdate;
    @Bind(R.id.rl_update)
    RelativeLayout rlUpdate;
    @Bind(R.id.map)
    MapView map;
    @Bind(R.id.gps_locate_button)
    RadioButton gpsLocateButton;
    @Bind(R.id.gps_follow_button)
    RadioButton gpsFollowButton;
    @Bind(R.id.gps_rotate_button)
    RadioButton gpsRotateButton;
    @Bind(R.id.gps_radio_group)
    RadioGroup gpsRadioGroup;
    @Bind(R.id.tv_s)
    TextView tvS;
    @Bind(R.id.tv_curtime)
    TextView tvCurtime;
    @Bind(R.id.rl_signed)
    RelativeLayout rlSigned;
    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private RadioGroup mGPSModeGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
         /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mGPSModeGroup=(RadioGroup) findViewById(R.id.gps_radio_group);
        mGPSModeGroup.setOnCheckedChangeListener(this);
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_locationsin_in;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.gps_locate_button:
                //设置定位的类型为定位模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                break;
            case R.id.gps_follow_button:
                //设置定位的类型为 跟随模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                break;
            case R.id.gps_rotate_button:
                //设置定位的类型为根据地图面向方向旋转
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                break;
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
        deactivate();
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
        ButterKnife.unbind(this);
    }



    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mListener != null && aLocation != null) {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点

        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {

    }


}
