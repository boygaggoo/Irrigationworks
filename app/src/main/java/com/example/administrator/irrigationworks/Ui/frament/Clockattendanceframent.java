package com.example.administrator.irrigationworks.Ui.frament;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.Retrofit.ApiUtil;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.Retrofit.RequestServes;
import com.example.administrator.irrigationworks.Ui.Retrofit.ResultCode;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.activity.RegOnlineFaceDemo;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeImageView;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeLinearLayout;
import com.example.administrator.irrigationworks.Ui.bean.AuditStatusBean;
import com.example.administrator.irrigationworks.Ui.bean.Coordinate;
import com.example.administrator.irrigationworks.Ui.bean.InfoBean;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.TotalDataLitepal;
import com.example.administrator.irrigationworks.UtilsTozals.FaceUtil;
import com.example.administrator.irrigationworks.UtilsTozals.ResourceUtils;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 考勤打卡
 * Created by Administrator on 2017/8/10.
 */
public class Clockattendanceframent extends TFragment implements View.OnClickListener, LocationSource,
        AMapLocationListener, RadioGroup.OnCheckedChangeListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener, AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener {

    @Bind(R.id.tv_hour)
    TextView tvHour;
    @Bind(R.id.rl_signed_hour)
    RelativeLayout rlSignedHour;
    @Bind(R.id.tv_min)
    TextView tvMin;
    @Bind(R.id.rl_signed_min)
    RelativeLayout rlSignedMin;
    @Bind(R.id.tv_second)
    TextView tvSecond;
    @Bind(R.id.rl_signed_second)
    RelativeLayout rlSignedSecond;
    @Bind(R.id.online_camera)
    Button onlineCamera;
    @Bind(R.id.online_detect)
    RadioButton onlineDetect;
    @Bind(R.id.online_align)
    RadioButton onlineAlign;
    @Bind(R.id.gps_radio_group)
    RadioGroup gpsRadioGroup;
    @Bind(R.id.online_img)
    ImageView onlineImg;
    @Bind(R.id.online_authid)
    EditText onlineAuthid;
    @Bind(R.id.et_edittext)
    EditText etEdittext;
    @Bind(R.id.online_pick)
    Button onlinePick;
    @Bind(R.id.online_verify)
    Button onlineVerify;
    @Bind(R.id.online_reg)
    Button onlineReg;
    @Bind(R.id.tv_attendance_summit)
    TextView tvAttendanceSummit;
    @Bind(R.id.rl_attendance)
    RelativeLayout rlAttendance;
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.gps_locate_button)
    RadioButton gpsLocateButton;
    @Bind(R.id.gps_follow_button)
    RadioButton gpsFollowButton;
    @Bind(R.id.gps_rotate_button)
    RadioButton gpsRotateButton;
    @Bind(R.id.gps_radio_location)
    RadioGroup gpsRadioLocation;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.iv_record_management)
    BGABadgeImageView ivRecordManagement;
    @Bind(R.id.bga_record_management)
    BGABadgeLinearLayout bgaRecordManagement;


    private Activity ctx;
    private View layout;
    private final int REQUEST_PICTURE_CHOOSE = 1;
    private final int REQUEST_CAMERA_IMAGE = 2;
    private Bitmap mImage = null;
    private byte[] mImageData = null;
    // authid为6-18个字符长度，用于唯一标识用户
    private String mAuthid = null;
    private Toast mToast;
    // 进度对话框
    private ProgressDialog mProDialog;
    // 拍照得到的照片文件
    private File mPictureFile;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;
    private RadioGroup gps_radio_group;
    private AMap aMap;
    private Polygon polygon;
    private TextView Text;
    private Marker marker;
    private Circle circle;
    //    private OnLocationChangedListener mListener;
    private MyLocationStyle myLocationStyle;
    //获取工地的坐标
    private Double lngs;
    private Double lats;
    //获取当前用户的token
    private String token;
    private List<RoleBeanLitepal> schooldates;
    private List<Coordinate> coordinate;
    private int retSUCCESS;
    private PolygonOptions pOption;
    //地图
    private com.amap.api.maps2d.LocationSource.OnLocationChangedListener mListeners;
    //是否可以打卡
    private boolean b1;

    private OnLocationChangedListener mListener;//右上角图标


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
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

    public void getCoustreunctionData() {
        try {
            List<TotalDataLitepal> secuGetDtas = DataSupport.findAll(TotalDataLitepal.class);

            lngs = secuGetDtas.get(0).getLnt();
            lats = secuGetDtas.get(0).getLat();
            String[][] re = NimUIKit.getRe();
            Log.d("自登录的标示为", "re" + NimUIKit.getGson().toJson(re));
            for (int j = 0; j < re.length; j++) {
                Coordinate oordinate = new Coordinate();
                for (int i = 0; i < NimUIKit.getGson().toJson(re[j]).split(",").length; i++) {

                    if (NimUIKit.getGson().toJson(re[j]).split(",")[i].contains("[")) {
                        String lnt = NimUIKit.getGson().toJson(re[j]).split(",")[i].replace("[", "").replace("\"", "");
                        BigDecimal bg = new BigDecimal(lnt);
//只保存后六位小数,保留太多请求接口时，返回不了数据
                        double doublelng = bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                        lats = doublelng;
                        Log.d("自登录的标示为", "lnt" + lnt);
                        oordinate.setLnt(doublelng);
                        Log.d("自登录的标示为", "lnt" + lnt);
                    } else if (NimUIKit.getGson().toJson(re[j]).split(",")[i].contains("]")) {
                        String lng = NimUIKit.getGson().toJson(re[j]).split(",")[i].replace("]", "").replace("\"", "");
                        BigDecimal bg = new BigDecimal(lng);
//只保存后六位小数,保留太多请求接口时，返回不了数据
                        double doublelng = bg.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                        lngs = doublelng;
                        Log.d("自登录的标示为", "lgt" + lng);
                        oordinate.setLng(doublelng);
                    }

                }
                coordinate.add(oordinate);

//
            }
            Log.d("2自登录的标示为", "lgt" + NimUIKit.getGson().toJson(coordinate));
        } catch (NullPointerException e) {

        }


        List<RoleBeanLitepal> roleBeanLitepal = DataSupport.findAll(RoleBeanLitepal.class);
        token = roleBeanLitepal.get(0).getToken();
    }

    //时间显示
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
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
                    //监控现在坐标是否在目标范围内
//                   LatLng nowlng = new LatLng(doublelng, doublelat);// 北京市经纬度
                    //设置当前工地坐标

                    LatLng nowlng = new LatLng(doublelat, doublelng);

//                    b1 = circle.contains(nowlng);//监控此时的点
                    String result = "";
                    b1 = polygon.contains(nowlng);
                    if (b1) {

                        marker = aMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory
                                        .fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.poi_marker_pressed)))
                                .position(nowlng));
                        marker.setTitle("当前位置");
                        result = "已进入考勤范围内";

                    } else {
                        marker = aMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory
                                        .fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_marka)))
                                .position(nowlng));
                        marker.setTitle("当前位置");
                        result = "不在考勤范围内";
                    }
                    Log.d("qc", "目标是否在围栏里面" + b1);
                    String webUrl2 = "http://www.baidu.com";//百度
                    Date date = getWebsiteDatetime(webUrl2);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
                    String time = sdf.format(date);
                    Bundle bundle = new Bundle();
                    bundle.putString("hour", date.getHours() + "");  //往Bundle中存放数据
                    bundle.putString("min", date.getMinutes() + "");  //往Bundle中put数据
                    bundle.putString("seconds", date.getSeconds() + "");  //往Bundle中put数据
                    bundle.putString("seconds", date.getSeconds() + "");  //往Bundle中put数据
                    bundle.putString("time", time + "");  //往Bundle中put数据
                    bundle.putString("checkon", result + "");  //往Bundle中put数据
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    msg.setData(bundle);  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private static Date getWebsiteDatetime(String webUrl) {
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            return date;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    long sysTime = System.currentTimeMillis();//获取系统时间
//                    CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);//时间显示格式
//                    time.setText(sysTimeStr); //更新时间
//                    tv_hour
                    if (NimUIKit.isHandlestop()) {
                        String hour12 = msg.getData().getString("hour");//接受msg传递过来的参
                        String minute = msg.getData().getString("min");//接受msg传递过来的参
                        String second = msg.getData().getString("seconds");//接受msg传递过来的参
                        String result = msg.getData().getString("checkon");//接受msg传递过来的参
//                    Calendar calendar = Calendar.getInstance();
//                    int hour12 = calendar.get(Calendar.HOUR);
//                    int minute = calendar.get(Calendar.MINUTE);
//                    int second = calendar.get(Calendar.SECOND);
//                        tvHour.setText(hour12 + "");
//                        tvMin.setText(minute + "");
//                        tvSecond.setText(second + "");
                        if (result.equals("已进入考勤范围内")) {
//                            tvLocation.setText(result + "");
                            //设置验证验证可以
//                            onlineVerify.setVisibility(View.VISIBLE);
                        } else {
//                            tvLocation.setText(result + "");
//                            onlineVerify.setVisibility(View.GONE);
//                            tvLocation.setTextColor(Color.RED);

                        }
                    }

                    break;
                default:
                    break;
            }
        }
    };
    private static final int REQUEST_SDCARD = 1;//动态申请权限要大于0或1
    private LocalReceiver localReceiver;

    //接受广播打卡
    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Enumerate.SEND_INFORM.equals(intent.getAction())) {//接收进行网络请求
                Log.d("接受了", "接受了");
                schooldates = new ArrayList<>();
                schooldates = DataSupport.findAll(RoleBeanLitepal.class);
                if (schooldates != null) {
                    if (schooldates.get(0).getRg().length() > 10) {
                        mAuthid = "";
                        mAuthid = "a" + schooldates.get(0).getUserid().substring(0, 6);
                        onlineAuthid.setText(mAuthid);
                    }
                }
                //进行打卡
                if (TextUtils.isEmpty(NimUIKit.getAuditState())) {
                    showTip("未完成注册");
                    return;
                }
                if ("通过".equals(NimUIKit.getAuditState())) {


                    retSUCCESS = ErrorCode.SUCCESS;


//                mAuthid = schooldates.get(0).getUserid()+"";
//                List<MianBuID> secuGetDta=DataSupport.findAll(MianBuID.class);
//                mAuthid = secuGetDta.get(0).getMid();

                    Log.d("qqqqq", "此时的面部ID" + mAuthid);
                    if (TextUtils.isEmpty(mAuthid)) {
                        showTip("authid不能为空");
                        return;
                    }
                    //获取IrrigationWordsActitivysa里面的参数


                    Log.d("qqqqq", "此时的面部ID" + mImageData);
                    mImageData = NimUIKit.getmImageData();
                    if (null != mImageData) {
                        mProDialog = new ProgressDialog(getActivity());
                        mProDialog.setCancelable(true);
                        mProDialog.setMessage("验证中...");
                        mProDialog.show();
                        // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                        // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                        mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                        mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                        retSUCCESS = mFaceRequest.sendRequest(mImageData, mRequestListener);
                    } else {
                        showTip("请拍照后再验证");
                    }

                } else if ("不通过".equals(NimUIKit.getAuditState())) {

                    showTip("您提交的资料审核不通过，请重新注册!");
                } else if ("未注册".equals(NimUIKit.getAuditState())) {

                    AlertDialog.Builder builderlogin = new AlertDialog.Builder(getActivity());
                    builderlogin.setMessage("未注册不可考勤");
                    builderlogin.setTitle("立刻去注册");
                    builderlogin.setPositiveButton("确认去注册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //清理数据库
                            Intent intentFace = new Intent(getActivity(), RegOnlineFaceDemo.class);
                            startActivity(intentFace);

                        }
                    });
                    builderlogin.setNegativeButton("取消注册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builderlogin.create().show();
                } else if ("未审核".equals(NimUIKit.getAuditState())) {

                    showTip("您提交的资料还在审核中，请耐心等待!");
                }


            }
        }
    }

    private void localReceiver() {
        localReceiver = new LocalReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Enumerate.SEND_INFORM);//接收通知
        getActivity().registerReceiver(localReceiver, filter1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (layout == null) {
            ctx = this.getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.activity_check_on_word,
                    null);
            ButterKnife.bind(this, layout);
            try{
                //获取当前工地的数据库online_camera
                coordinate = new ArrayList<>();
                request();
                getCoustreunctionData();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_SDCARD);
                } else {
                    /**
                     * 初始化用户位置
                     */
                    setMyLoctionPicture();
                }
                mapView.onCreate(savedInstanceState);// 此方法必须重写
                //设置开启hanlde
                NimUIKit.setHandlestop(true);
                init();
                tvHour = (TextView) layout.findViewById(R.id.tv_hour);
                tvMin = (TextView) layout.findViewById(R.id.tv_min);
                tvSecond = (TextView) layout.findViewById(R.id.tv_second);
                setData();
//打卡广播
                localReceiver();
                //地图
            }catch (Exception e){

            }


        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        ButterKnife.bind(this, layout);
        return layout;
    }

    private void setMyLoctionPicture() {
        init();
    }

    private void request() {
        /**
         * 1. 动态申请权限（）
         */
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CHANGE_CONFIGURATION}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_SDCARD);
            return;
        }
    }
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        gpsRadioLocation.setOnCheckedChangeListener(this);
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
        //监控现在坐标是否在目标范围内
//                   LatLng nowlng = new LatLng(doublelng, doublelat);// 北京市经纬度
        //设置当前工地坐标

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
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);

        aMap.setMyLocationRotateAngle(180);
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//        double lng = Double.valueOf(Preference.getlng());
//        double lat = Double.valueOf(Preference.getLat());
//        LatLng BEIJING = new LatLng(lat, lng);// 北京市经纬度
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lats, lngs), 14));
//        circle = aMap.addCircle(new CircleOptions().center(Constants.BEIJING)
        //设置当前工地的位置
//        circle = aMap.addCircle(new CircleOptions().center(new LatLng(lats, lngs))
//                .radius(200).strokeColor(Color.argb(50, 1, 1, 1))
//                .fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(2));
        // 绘制一个长方形
//        Polygon polygon = aMap.addPolygon(new PolygonOptions()
//                .addAll(createRectangle(Constants.SHANGHAI, 1, 1))
//                .fillColor(Color.LTGRAY).strokeColor(Color.RED).strokeWidth(1));
        // 定义多边形的5个点点坐标

        // 绘制一个长方形
        PolygonOptions pOption = new PolygonOptions();


        if(coordinate.size()>0){
            for(int i=0;i<coordinate.size();i++){
                Log.d("polygon", "(coordinate.get(i).getLng()" + coordinate.get(i).getLng());
                Log.d("polygon", "coordinate.get(i).getLnt()" +coordinate.get(i).getLnt());
                Log.d("polygon", "lats" +lats);
                Log.d("polygon", "lngs" +lngs);
                double a=coordinate.get(i).getLng();
                double b=coordinate.get(i).getLnt();
                pOption.add(new LatLng(a,b));
                if(i==coordinate.size()-1){
                    polygon = aMap.addPolygon(pOption.strokeWidth(4)
                            .strokeColor(Color.argb(50, 1, 1, 1))
                            .fillColor(Color.argb(50, 1, 1, 1)));
                    Log.d("polygon", "polygon" + polygon);
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a,b), 16));
                }

            }

        }
//        if(i==coordinate.size()-1){
//            Log.d("polygonpOption", NimUIKit.getGson().toJson(pOption));
//            polygon = aMap.addPolygon(pOption.strokeWidth(4)
//                    .strokeColor(Color.argb(50, 1, 1, 1))
//                    .fillColor(Color.argb(50, 1, 1, 1)));
//            Log.d("polygon", "polygon" + polygon);
//            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lats,lngs), 16));
//        }
            Log.d("polygonpOption", NimUIKit.getGson().toJson(pOption));

//        pOption.add(new LatLng(23.026922, 113.175172));
//        pOption.add(new LatLng(23.026942, 113.176867));
//        pOption.add(new LatLng(23.025619, 113.177125));
//        pOption.add(new LatLng(23.024927, 113.175966));
//        pOption.add(new LatLng(23.025046, 113.174829));



        // 声明 多边形参数对象
        PolygonOptions polygonOptions = new PolygonOptions();
// 添加 多边形的每个顶点（顺序添加）
//        polygonOptions.add(latLng1, latLng2, latLng3, latLng4, latLng5);
//        polygonOptions.strokeWidth(15) // 多边形的边框
//                .strokeColor(Color.argb(50, 1, 1, 1)) // 边框颜色
//                .fillColor(Color.argb(1, 1, 1, 1));   // 多边形的填充色
//        polygon= aMap.addPolygon(polygonOptions);
    }

    List<LatLng> createRectangle(LatLng center, double halfWidth,
                                 double halfHeight) {
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
        latLngs.add(new LatLng(center.latitude - halfHeight, center.longitude + halfWidth));
        latLngs.add(new LatLng(center.latitude + halfHeight, center.longitude + halfWidth));
        latLngs.add(new LatLng(center.latitude + halfHeight, center.longitude - halfWidth));
        return latLngs;
    }

    private void setData() {
        onlinePick.setOnClickListener(this);
        onlineReg.setOnClickListener(this);
        onlineCamera.setOnClickListener(this);
        onlineDetect.setOnClickListener(this);
        onlineAlign.setOnClickListener(this);
        onlineVerify.setOnClickListener(this);
        rlAttendance.setOnClickListener(this);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mProDialog = new ProgressDialog(getActivity());
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");
        //开启线程线程显示时间
        new TimeThread().start(); //启动新的线程
        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作
                if (null != mFaceRequest) {
                    mFaceRequest.cancel();
                }
            }
        });
        mFaceRequest = new FaceRequest(getActivity());
        gpsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int ret = ErrorCode.SUCCESS;
                switch (checkedId) {
                    case R.id.online_detect:

                        Log.d("qc", "点击了检测中");
                        //点击执行逻辑
                        mImageData = null;
                        mImageData = NimUIKit.getmImageData();
                        if (null != mImageData) {
                            mProDialog.setMessage("检测中...");
                            mProDialog.show();
                            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "detect");
                            ret = mFaceRequest.sendRequest(mImageData, mRequestListener);
                        } else {
                            showTip("请选择图片后再检测");
                        }
                        break;
                    case R.id.online_align:
                        mImageData = null;
                        mImageData = NimUIKit.getmImageData();
                        Log.d("qc", "点击了聚焦中");
                        if (null != mImageData) {
                            mProDialog.setMessage("聚焦中...");
                            mProDialog.show();
                            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "align");
                            ret = mFaceRequest.sendRequest(mImageData, mRequestListener);
                        } else {
                            showTip("请选择图片后再聚集");
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeMessages(1);
            mHandler.removeCallbacksAndMessages(null);
        }
        getActivity().unregisterReceiver(localReceiver);//销毁广播

        TimeThread.currentThread().interrupt();
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
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
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        Log.d("qq", "目标是否在围栏里面" + b1);
        if (mListener != null && aLocation != null) {

        }

        Log.d("qq", "此时的坐标为" + aLocation.getLatitude() + "唯独" + aLocation.getLongitude());
    }


    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {


            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
                } else if ("verify".equals(type)) {
                    verify(object);
                } else if ("detect".equals(type)) {
                    detect(object);
                } else if ("align".equals(type)) {
                    align(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }
            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("authid已经被注册，请更换后再试");
                        break;

                    default:
                        Log.d("qc", "未检测到");
                        try {
                            onlineCamera.setVisibility(View.VISIBLE);
                            onlineImg.setImageBitmap(null);
                        } catch (Exception e) {

                        }

                        showTip(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };

    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("注册失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            showTip("注册成功");
        } else {
            showTip("注册失败");
        }
    }

    private void verify(JSONObject obj) throws JSONException {
        Log.d("qc", "需要验证的信息" + obj);
        int ret = obj.getInt("ret");
        if (ret != 0) {
            try {
                onlineCamera.setVisibility(View.VISIBLE);
                onlineImg.setImageBitmap(null);
            } catch (Exception e) {

            }
            showTip("验证失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            Log.d("qc", "需要验证成功的信息" + obj);
            if (obj.getBoolean("verf")) {
                Log.d("qc", "需要验证成功的信息" + obj);
                //通过验证过，考勤打开按钮才出现
                //开始打卡
                rlAttendance.setVisibility(View.GONE);
                //提交考勤记录
                gotoworkcheck();

            } else {
                try {
                    onlineCamera.setVisibility(View.VISIBLE);
                    onlineImg.setImageBitmap(null);
                } catch (Exception e) {

                }


                showTip("验证不通过");
            }
        } else {
            try {
                onlineCamera.setVisibility(View.VISIBLE);
                onlineImg.setImageBitmap(null);
            } catch (Exception e) {

            }

            showTip("验证失败");
        }
    }

    //进行打开功能
    private void gotoworkcheck() {
        RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
        Map<String, Object> hashMap = new HashMap<String, Object>();
        /**
         * 参数为手机当前定位位置
         */
        hashMap.put("lat", lats);
        hashMap.put("lnt", lngs);
        hashMap.put("token", token);
        hashMap.put("constructionid", NimUIKit.getConstructionid());
        Call<ResultCode> call = apiService.getCheckWord(hashMap);
        call.enqueue(new Callback<ResultCode>() {
            @Override
            public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
                ResultCode body = response.body();
                Log.d("qc", "打卡成功" + body.getResult());
                if (body != null) {
                    if (body.getMessage().equals("添加成功")) {

                        rlAttendance.setVisibility(View.GONE);
                        try {
                            onlineCamera.setVisibility(View.VISIBLE);
                            onlineImg.setImageBitmap(null);
                        } catch (Exception e) {

                        }
                        UtilsTool.showLongToast(getActivity(), "打卡成功");

                    }
                } else {
                    try {
                        onlineCamera.setVisibility(View.VISIBLE);
                        onlineImg.setImageBitmap(null);
                    } catch (Exception e) {

                    }
                    rlAttendance.setVisibility(View.GONE);
                    UtilsTool.showLongToast(getActivity(), "打卡失败");
                }
                UtilsTool.showLongToast(getActivity(), body.getMessage());
                Log.d("qc", "打卡成功" + body.getMessage());
                onlineCamera.setVisibility(View.VISIBLE);
                onlineImg.setImageBitmap(null);
            }

            @Override
            public void onFailure(Throwable t) {
                rlAttendance.setVisibility(View.GONE);
                try {
                    onlineCamera.setVisibility(View.VISIBLE);
                    onlineImg.setImageBitmap(null);
                } catch (Exception e) {

                }
                UtilsTool.showLongToast(getActivity(), "打卡失败" + t.getMessage());
            }
        });
    }

    private void detect(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("检测失败");
            return;
        }

        if ("success".equals(obj.get("rst"))) {
            JSONArray faceArray = obj.getJSONArray("face");
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);
            Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
                    mImage.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(mImage, new Matrix(), null);
            for (int i = 0; i < faceArray.length(); i++) {
                float x1 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("left");
                float y1 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("top");
                float x2 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("right");
                float y2 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("bottom");
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(new Rect((int) x1, (int) y1, (int) x2, (int) y2),
                        paint);
            }
            mImage = bitmap;
//            onlineImg.setImageBitmap(mImage);
            ((ImageView) getActivity().findViewById(R.id.online_img)).setImageBitmap(mImage);
        } else {
            showTip("检测失败");
        }
    }

    @SuppressWarnings("rawtypes")
    private void align(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("聚焦失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {

            if (mImage == null) {
                return;
            }
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

            Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
                    mImage.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(mImage, new Matrix(), null);

            JSONArray faceArray = obj.getJSONArray("result");
            for (int i = 0; i < faceArray.length(); i++) {
                JSONObject landmark = faceArray.getJSONObject(i).getJSONObject(
                        "landmark");
                Iterator it = landmark.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    JSONObject postion = landmark.getJSONObject(key);
                    canvas.drawPoint((float) postion.getDouble("x"),
                            (float) postion.getDouble("y"), paint);
                }
            }

            mImage = bitmap;
//            onlineImg.setImageBitmap(mImage);
            ((ImageView) getActivity().findViewById(R.id.online_img)).setImageBitmap(mImage);
        } else {
            showTip("聚焦失败");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (null != mProDialog) {
            mProDialog.dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        retSUCCESS = ErrorCode.SUCCESS;
        switch (v.getId()) {
            case R.id.tv_attendance_summit:
                //进行打开功能
                break;
            case R.id.online_pick:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
                break;
            //进行图片验证
            case R.id.online_verify:
                Log.d("qc", "图片验证中");
                //首先调用后台审核状态
                if (TextUtils.isEmpty(NimUIKit.getAuditState())) {
                    showTip("未完成注册");
                    return;
                }
                if ("通过".equals(NimUIKit.getAuditState())) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            schooldates = new ArrayList<>();
                            schooldates = DataSupport.findAll(RoleBeanLitepal.class);
                            if (schooldates != null) {
                                if (schooldates.get(0).getRg().length() > 10) {
                                    mAuthid = "";
                                    mAuthid = "a" + schooldates.get(0).getUserid().substring(0, 6);


                                }
                            }
//                mAuthid = schooldates.get(0).getUserid()+"";
//                List<MianBuID> secuGetDta=DataSupport.findAll(MianBuID.class);
//                mAuthid = secuGetDta.get(0).getMid();

                            Log.d("qqqqq", "此时的面部ID" + mAuthid);
                            if (TextUtils.isEmpty(mAuthid)) {
                                showTip("authid不能为空");
                                return;
                            }
                            //获取IrrigationWordsActitivysa里面的参数
                            mImageData = null;
                            mImageData = NimUIKit.getmImageData();
                            Log.d("qqqqq", "此时的面部ID" + mImageData);

                            if (null != mImageData) {
                                mProDialog = new ProgressDialog(getActivity());
                                mProDialog.setCancelable(true);
                                mProDialog.setMessage("验证中...");
                                mProDialog.show();
                                // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                                // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                                mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                                mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                                retSUCCESS = mFaceRequest.sendRequest(mImageData, mRequestListener);
                            } else {
                                showTip("请拍照后再验证");
                            }
                        }
                    });


                } else if ("不通过".equals(NimUIKit.getAuditState())) {

                    showTip("您提交的资料审核不通过，请重新注册!");
                } else if ("未注册".equals(NimUIKit.getAuditState())) {

                    AlertDialog.Builder builderlogin = new AlertDialog.Builder(getActivity());
                    builderlogin.setMessage("未注册不可考勤");
                    builderlogin.setTitle("立刻去注册");
                    builderlogin.setPositiveButton("确认去注册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //清理数据库
                            Intent intentFace = new Intent(getActivity(), RegOnlineFaceDemo.class);
                            startActivity(intentFace);

                        }
                    });
                    builderlogin.setNegativeButton("取消注册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builderlogin.create().show();
                } else if ("未审核".equals(NimUIKit.getAuditState())) {

                    showTip("您提交的资料还在审核中，请耐心等待!");
                }

                break;
            //提交考勤
            case R.id.rl_attendance:
                break;
            case R.id.online_reg:
//                schooldates=new ArrayList<>();
                schooldates = DataSupport.findAll(RoleBeanLitepal.class);
                if (schooldates != null) {
                    if (schooldates.get(0).getRg().length() > 10) {
                        mAuthid = "";
                        mAuthid = "a" + schooldates.get(0).getUserid().substring(0, 6);


                    }
                }
                if (TextUtils.isEmpty(mAuthid)) {
                    showTip("注册图片号码不能为空");
                    return;
                }
                //获取IrrigationWordsActitivysa里面的参数
                mImageData = NimUIKit.getmImageData();

                if (null != mImageData) {
                    mProDialog.setMessage("注册中...");
                    mProDialog.show();
                    // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                    // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                    retSUCCESS = mFaceRequest.sendRequest(mImageData, mRequestListener);
                } else {
                    showTip("请选择图片后再注册");
                }
                break;
            case R.id.online_detect:
                break;
            case R.id.online_align:
                break;
            case R.id.online_camera:
                //首先判断是否在考勤范围内

                if (b1) {
                    //已进入考勤范围内
                    Log.d("qc", "点击了相机");
                    // 设置相机拍照后照片保存路径
                    mPictureFile = new File(Environment.getExternalStorageDirectory(),
                            "picture" + System.currentTimeMillis() / 1000 + ".jpg");
                    // 启动拍照,并保存到临时文件
                    Intent mIntent = new Intent();
                    mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
                    mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    mIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    mIntent.putExtra("autofocus", true); // 自动对焦
                    mIntent.putExtra("fullScreen", false); // 全屏
                    startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);

                } else {
                    //不在考勤范围内
                    UtilsTool.showShortToast(getActivity(), "不在考勤范围内");
                    return;
                }

                break;
            default:
                break;
        }//end of switch

        if (ErrorCode.SUCCESS != retSUCCESS) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }
            showTip("面部不通过,请重新拍照");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("qq", "进入了" + "Clockattendanceframent");

        Log.d("进入了", "进入了onActivityResult");
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }

        String fileSrc = null;
        if (requestCode == REQUEST_PICTURE_CHOOSE) {
            Log.d("qq", "跳转了到裁剪" + "Clockattendanceframent");
            if ("file".equals(data.getData().getScheme())) {
                // 有些低版本机型返回的Uri模式为file
                fileSrc = data.getData().getPath();
            } else {
                // Uri模型为content
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(data.getData(), proj,
                        null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileSrc = cursor.getString(idx);
                cursor.close();
            }
            // 跳转到图片裁剪页面

            FaceUtil.cropPicture(getActivity(), Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == REQUEST_CAMERA_IMAGE) {
            Log.d("进入了", "进入了REQUEST_CAMERA_IMAGE");
            if (null == mPictureFile) {
                showTip("拍照失败，请重试");
                return;
            }

            fileSrc = mPictureFile.getAbsolutePath();
            Glide.with(this).load(fileSrc).asBitmap().centerCrop().into(new BitmapImageViewTarget(onlineImg) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    onlineImg.setImageDrawable(circularBitmapDrawable);
                    onlineCamera.setVisibility(View.GONE);
                }
            });
//            Log.d("进入了", "进入了fileSrc"+fileSrc);

            updateGallery(fileSrc);
            // 跳转到图片裁剪页面
            FaceUtil.cropPicture(getActivity(), Uri.fromFile(new File(fileSrc)));
            Log.d("进入了", "进入了REQUEST_CAMERA_IMAGE3");
        } else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
            // 获取返回数据
            Bitmap bmp = data.getParcelableExtra("data");
            // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if (null != bmp) {
                FaceUtil.saveBitmapToFile(getActivity(), bmp);
            }
            // 获取图片保存路径
            fileSrc = FaceUtil.getImagePath(getActivity());
            // 获取图片的宽和高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            options.inJustDecodeBounds = true;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 压缩图片
            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);


            // 若mImageBitmap为空则图片信息不能正常获取
            if (null == mImage) {
                showTip("图片信息无法正常获取！");
                return;
            }

            // 部分手机会对图片做旋转，这里检测旋转角度
            int degree = FaceUtil.readPictureDegree(fileSrc);
            if (degree != 0) {
                // 把图片旋转为正的方向
                mImage = FaceUtil.rotateImage(degree, mImage);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            mImageData = baos.toByteArray();
            Log.d("qq", "裁剪后图片的数据" + mImageData);
            ((ImageView) getActivity().findViewById(R.id.online_img)).setImageBitmap(mImage);
        }

    }


    private void updateGallery(String filename) {
        MediaScannerConnection.scanFile(getActivity(), new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }

    /**
     * 首先判断是否可以打卡状态
     */
    private class GetSorsubmit implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("首先判断是否可以打卡状态", "首先判断是否可以打卡状态" + result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                AuditStatusBean auditStatusBean = JSON.parseObject(loginOnSuccssBean.getResult(), AuditStatusBean.class);

            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showTip("人脸识别尚未审核,请通知负责人审核");
                    }
                });
            }
        }
    }
}
