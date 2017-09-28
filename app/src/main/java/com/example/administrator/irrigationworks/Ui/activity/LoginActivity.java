package com.example.administrator.irrigationworks.Ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.Jpush.ExampleUtil;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.LocationService.LocationService;
import com.example.administrator.irrigationworks.Ui.Retrofit.ApiUtil;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.Retrofit.RequestServes;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.autoLogin.UserInfo;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.litepal.ConstructionDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.TotalDataLitepal;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import org.litepal.crud.DataSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import app.dinus.com.loadingdrawable.LoadingView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 登录页面
 * Created by Administrator on 2017/8/10.
 */
public class LoginActivity extends BaseAppCompatActivity implements View.OnClickListener {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.login_user_input)
    TextView loginUserInput;
    @Bind(R.id.username_edit)
    EditText usernameEdit;
    @Bind(R.id.login_password_input)
    TextView loginPasswordInput;
    @Bind(R.id.password_edit)
    EditText passwordEdit;
    @Bind(R.id.login_div)
    RelativeLayout loginDiv;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.register_link)
    TextView registerLink;
    @Bind(R.id.day_night_view)
    LoadingView dayNightView;

    //临时设置限时操作
    private int recLen = 0;
    private TimerTask task;
    private RequestServes apiService;
    static Logger logger = LoggerFactory.getLogger(LoginActivity.class);
    private String role;
    private String constructionid;
    private static final int REQUEST_SDCARD = 1;//动态申请权限要大于0或1
    //自动登录
    private UserInfo userInfo;

    private void getLoogin() {
        // 实例化我们的mApi对象,请求登录
        dayNightView = (LoadingView) findViewById(R.id.day_night_view);
        apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
        String name = usernameEdit.getText().toString();
        String pass = passwordEdit.getText().toString();
        final Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("password", pass);
        hashMap.put("username", name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.GotoLogin, NimUIKit.getGson().toJson(hashMap), new GetLogin());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        Call<ResultCode> call = apiService.getTaskData(hashMap);
//        call.enqueue(new Callback<ResultCode>() {
//            @Override
//            public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
//                ResultCode body = response.body();
//
//                if(body==null){
//                    UtilsTool.showShortToast(LoginActivity.this,"登录失败");
//                    dayNightView= (LoadingView) findViewById(R.id.day_night_view);
//                    dayNightView.setVisibility(View.GONE);
//                    return;
//                }
//
//                if(body!=null&&body.getCode().equals(Enumerate.LOGINSUCESS)){
//
//
//                }else {
//
//
//                    if(body!=null){
//                        dayNightView.setVisibility(View.GONE);
//                        UtilsTool.showShortToast(LoginActivity.this,body.getMessage());
//
//                    }else {
//                        dayNightView= (LoadingView) findViewById(R.id.day_night_view);
//                        dayNightView.setVisibility(View.GONE);
//                        UtilsTool.showShortToast(LoginActivity.this,"登录失败");
//                    }
//                }
//
//
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                dayNightView= (LoadingView) findViewById(R.id.day_night_view);
//                dayNightView.setVisibility(View.GONE);
//                Preference.saveAutoLogin(false);//设置自动登录标识
//                UtilsTool.showShortToast(LoginActivity.this,"账号密码错误,请重新输入");
//                Log.d("qc", "失败了返回的参数retrofit" + t.getMessage());
//            }
//        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //启动实时获取坐标

        toolbar.setVisibility(View.GONE);
        login.setOnClickListener(this);
        registerLink.setOnClickListener(this);
        try{
            Drawable drawable=getResources().getDrawable(R.mipmap.user);
            drawable.setBounds(0,0,40,30);
            usernameEdit.setCompoundDrawables(drawable,null,null,null);

            Drawable drawables=getResources().getDrawable(R.mipmap.lock);
            drawables.setBounds(0,0,40,30);
            passwordEdit.setCompoundDrawables(drawables,null,null,null);
            request();
            getToLocationDataService();
            //设置权限



            initPermission();
            //时间比较
            timeOUt();
            //自动登录
            userInfo = new UserInfo(this);

            Log.d("123", "自动登录的标示为" + Preference.getsaveAutoLogin() + "");
            Boolean net = NetUtil.checkNetWork(LoginActivity.this);
            if (net) {
                if (Preference.getsaveAutoLogin()) {
                    final String user = Preference.getUserAccount();
                    final String pass = Preference.getUserToken();
                    usernameEdit.setText(user);
                    passwordEdit.setText(pass);
                    autoData();

                } else {
                    final String user = Preference.getUserAccount();
                    final String pass = Preference.getUserToken();
                    usernameEdit.setText(user);

                }
            } else {
                UtilsTool.showShortToast(LoginActivity.this, "没有网络");
            }
        }catch (Exception e){

        }



//设置极光推送的别名

    }

    private void timeOUt() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse("2017-09-13");
            Date d2 = df.parse("2017-09-20");
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            Log.d("现在的时间", "现在的时间" + days);
            Log.d("现在的时间", "现在的时间" + diff);
        } catch (Exception e) {
        }
    }

    private void autoData() {
        dayNightView = (LoadingView) findViewById(R.id.day_night_view);
        dayNightView.setVisibility(View.VISIBLE);
        apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
        String name = usernameEdit.getText().toString();
        String pass = passwordEdit.getText().toString();

//        try {
//            HttpUtils.doPostAsyn(FinalTozal.getUserAuditStatus, NimUIKit.getGson().toJson(hashMap), new GotoLogin());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        final Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("password", pass);
        hashMap.put("username", name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.GotoLogin, NimUIKit.getGson().toJson(hashMap), new GetLogin());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        Call<ResultCode> call = apiService.getTaskData(hashMap);
//        call.enqueue(new Callback<ResultCode>() {
//            @Override
//            public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
//                ResultCode body = response.body();
//
//                if(body==null){
//                    UtilsTool.showShortToast(LoginActivity.this,"登录失败");
//                    dayNightView= (LoadingView) findViewById(R.id.day_night_view);
//                    dayNightView.setVisibility(View.GONE);
//                    return;
//                }
//
//                if(body!=null&&body.getCode().equals(Enumerate.LOGINSUCESS)){
//                    Log.d("角色","角色"+NimUIKit.getGson().toJson(body.getResult()));
//                    dayNightView.setVisibility(View.GONE);
//                    String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
//                    final RoleBeanLitepal rolebean= JSON.parseObject(reu,RoleBeanLitepal.class);
//                    role=rolebean.getRole();
//                    NimUIKit.setRole(rolebean.getJob());
//                    //先删除数据库
//                    DataSupport.deleteAll(RoleBeanLitepal.class);
//                    List<RoleBeanLitepal> secuGetDta=new ArrayList<RoleBeanLitepal>();
//
//                    secuGetDta.add(rolebean);
//                    DataSupport.saveAll(secuGetDta);//保存到数据库
//                    NimUIKit.setName(rolebean.getRealname());
//                    //获取工地的数据
//                    apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
//                    String token=rolebean.getToken();
//                    NimUIKit.setToken(token);
//                    Log.d("qc","说台词是的"+token);
//
//                    //设置激光推送
//                    setAalis(rolebean.getRg());
//                    Call<ResultCode> call = apiService.getConstructionData(token);
//                    call.enqueue(new Callback<ResultCode>() {
//                        @Override
//                        public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
//                            ResultCode body = response.body();
//                            Log.d("qc","说台词是的body"+body);
//                            if(body.getResult()!=null){//是施工方
//                                //保存工地信息在数据库
//                                //先删除数据库
//                                try{
//                                    String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
//
//                                    ConstructionDataLitepal rolebean= JSON.parseObject(reu,ConstructionDataLitepal.class);
//                                    Log.d("secuGetDta","reu此时的数据"+reu);
//                                    if(rolebean.getResult().equals("[]")){
//                                        UtilsTool.showShortToast(LoginActivity.this,"此账号尚没有添加工地信息,请先在后台添加");
//                                        Preference.saveAutoLogin(false);//设置自动登录标识
//                                        return;
//                                    }
//                                    //获取对象
//                                    List<TotalDataLitepal> rolebeans= JSON.parseArray(rolebean.getResult(),TotalDataLitepal.class);
//                                    Log.d("secuGetDta","reu此时的数据"+reu);
//                                    DataSupport.deleteAll(TotalDataLitepal.class);
////                                    setAalis(rolebeans.get(0).getRg());
//                                    rolebeans.add(rolebeans.get(0));
//                                    constructionid=rolebeans.get(0).getConstructionid();
//                                    NimUIKit.setConstructionid(constructionid);
//
//                                    DataSupport.saveAll(rolebeans);//保存到数据库
//                                    Intent iten = new Intent(LoginActivity.this, com.example.administrator.irrigationworks.Ui.adapter.IrrigationWordsActivity.class);
//                                    iten.putExtra("role",role);
//                                    iten.putExtra("constructionid",constructionid);
//                                    startActivity(iten);
//                                    Preference.saveAutoLogin(true);//设置自动登录标识
//                                    Preference.saveUserToken(passwordEdit.getText().toString());//保存用户密码
//                                    Preference.saveUserAccount(usernameEdit.getText().toString());//保存用户账号
//                                    finish();
//                                }catch (NullPointerException e){
//
//                                }
//
//
////
//                            }else {//不是施工方
//
//                                Intent iten = new Intent(LoginActivity.this, IrrigationRoleWordsActivity.class);
//                                iten.putExtra("role",role);
//
//
//                                startActivity(iten);
//                                Preference.saveAutoLogin(true);//设置自动登录标识
//                                Preference.saveUserToken(passwordEdit.getText().toString());//保存用户密码
//                                Preference.saveUserAccount(usernameEdit.getText().toString());//保存用户账号
//                                finish();
//                            }
////                            if(role.equals(Constants.Patroller)||role.equals(Constants.Supervisor)){
////                                Intent iten = new Intent(LoginActivity.this, IrrigationRoleWordsActivity.class);
////                                iten.putExtra("role",role);
////
////
////                                startActivity(iten);
////                            }else if(role.equals(Constants.Construction)){
////                                Intent iten = new Intent(LoginActivity.this, com.example.administrator.irrigationworks.Ui.adapter.IrrigationWordsActivity.class);
////                                iten.putExtra("role",role);
////                                iten.putExtra("constructionid",constructionid);
////                                startActivity(iten);
////                            }
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//                            Log.d("qc","获取了工地的信息失败"+t.getMessage());
//                        }
//                    });
//
//                }else {
//
//
//                    if(body!=null){
//                        dayNightView.setVisibility(View.GONE);
//
//                        UtilsTool.showShortToast(LoginActivity.this,body.getMessage());
//                    }else {
//                        dayNightView= (LoadingView) findViewById(R.id.day_night_view);
//                        dayNightView.setVisibility(View.GONE);
//                        UtilsTool.showShortToast(LoginActivity.this,"登录失败");
//                    }
//                }
//
//
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                dayNightView= (LoadingView) findViewById(R.id.day_night_view);
//                dayNightView.setVisibility(View.GONE);
//                Preference.saveAutoLogin(false);//设置自动登录标识
//
//                UtilsTool.showShortToast(LoginActivity.this,"账号密码错误,请重新输入");
//                Log.d("qc", "失败了返回的参数retrofit" + t.getMessage());
//            }
//        });
    }

    private void request() {
        /**
         * 1. 动态申请权限（）
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_CONFIGURATION}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_SDCARD);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_SDCARD);
            return;
        }
    }

    private void setAalis(String rgid) {
        Log.d("注册", "设置注册的id为" + rgid);

        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        if (!rid.isEmpty()) {

        } else {
            Log.d("注册", "注册失败的id为" + rid);

//            Toast.makeText(this, "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
        }
        Log.d("注册", "注册rgid失败的id为" + rgid);
        JPushInterface.resumePush(LoginActivity.this);
        String tag = "教师";
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {

                return;
            }
            tagSet.add(sTagItme);
        }
        JPushInterface.setAliasAndTags(LoginActivity.this, rgid, tagSet, null);
//        JPushInterface.setAliasAndTags(FriendActivity.this,Preference.getTeacherId()+"",tagSet,null);
        JPushInterface.resumePush(LoginActivity.this);
    }

    private void initPermission() {
        int permission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //需不需要解释的dialog
            if (shouldRequest()) return;
            //请求权限

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.NFC, Manifest.permission.WAKE_LOCK}, 1);
        }
//        如果在Activity中申请权限可以的调用：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissions = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        }
    }

    private boolean shouldRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.RECORD_AUDIO)) {
            //显示一个对话框，给用户解释
            explainDialog();
            return true;
        }
        return false;
    }

    private void explainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("应用需要访问你的权限,是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    }
                }).setNegativeButton("取消", null)
                .create().show();
    }

    private void getToLocationDataService() {
        Log.i("qc", "开始定位成功");
//        boolean Flag = ServiceUtil.isServiceRunning(LoginActivity.this, "zph.zhjx.com.chat.service.LocationService");
        Intent intet1 = new Intent(LoginActivity.this, LocationService.class);
        startService(intet1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);


    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //进行登陆
            case R.id.login:
                //创建retrofit对象
                Boolean net = NetUtil.checkNetWork(LoginActivity.this);
                if (net) {
                    dayNightView.setVisibility(View.VISIBLE);
                    getLoogin();
                } else {
                    UtilsTool.showShortToast(LoginActivity.this, "没有网络");
                }


                break;
            case R.id.register_link://修改密码
                Intent intent = new Intent(LoginActivity.this, SetpasswordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }//end of switch


    }

    private class GetLogin implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("角色", "角色" + result);
            final LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {

                String reu = loginOnSuccssBean.getResult().replace("_id", "rg");
                final RoleBeanLitepal rolebean = JSON.parseObject(reu, RoleBeanLitepal.class);
                role = rolebean.getRole();
                NimUIKit.setRole(rolebean.getJob());
                //先删除数据库
                DataSupport.deleteAll(RoleBeanLitepal.class);
                List<RoleBeanLitepal> secuGetDta = new ArrayList<RoleBeanLitepal>();

                secuGetDta.add(rolebean);
                DataSupport.saveAll(secuGetDta);//保存到数据库
                NimUIKit.setAuditState(rolebean.getAudit_status());//保存审核状态
                Log.d("审核状态", "审核状态" + NimUIKit.getAuditState());
                //获取工地的数据
                apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
                String token = rolebean.getToken();
                NimUIKit.setToken(token);
                Log.d("qc", "说台词是的" + token);

                //设置激光推送
                setAalis(rolebean.getRg());

                final Map<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("token", token);

                try {
                    HttpUtils.doPostAsyn(FinalTozal.findConstructionByUser, NimUIKit.getGson().toJson(hashMap), new GetSoertor());
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                Call<ResultCode> call = apiService.getConstructionData(token);
//                call.enqueue(new Callback<ResultCode>() {
//                    @Override
//                    public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
//                        ResultCode body = response.body();
//                        if(body.getResult()!=null){
//                            //是施工方
//                            //保存工地信息在数据库
//                            //先删除数据库
//                            try{
//                                String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
//
//                                ConstructionDataLitepal rolebean= JSON.parseObject(reu,ConstructionDataLitepal.class);
//                                Log.d("secuGetDta","reu此时的数据"+reu);
//                                if(rolebean.getResult().equals("[]")){
//                                    UtilsTool.showShortToast(LoginActivity.this,"此账号尚没有添加工地信息,请先在后台添加");
//                                    Preference.saveAutoLogin(false);//设置自动登录标识
//                                    return;
//                                }
//                                //获取对象
//                                List<TotalDataLitepal> rolebeans= JSON.parseArray(rolebean.getResult(),TotalDataLitepal.class);
//                                DataSupport.deleteAll(TotalDataLitepal.class);
////                                    setAalis(rolebeans.get(0).getRg());
//                                rolebeans.add(rolebeans.get(0));
//                                constructionid=rolebeans.get(0).getConstructionid();
//                                NimUIKit.setConstructionid(constructionid);
//                                DataSupport.saveAll(rolebeans);//保存到数据库
//                                Intent iten = new Intent(LoginActivity.this, com.example.administrator.irrigationworks.Ui.adapter.IrrigationWordsActivity.class);
//                                iten.putExtra("role",role);
//                                iten.putExtra("constructionid",constructionid);
//                                startActivity(iten);
//                                Preference.saveAutoLogin(true);//设置自动登录标识
//                                Preference.saveUserToken(passwordEdit.getText().toString());//保存用户密码
//                                Preference.saveUserAccount(usernameEdit.getText().toString());//保存用户账号
//                                finish();
//
//                            }catch (NullPointerException e){
//
//                            }
//
//
////
//                        }else {//不是施工方
//                            Intent iten = new Intent(LoginActivity.this, IrrigationRoleWordsActivity.class);
//                            iten.putExtra("role",role);
//
//
//                            startActivity(iten);
//                            Preference.saveAutoLogin(true);//设置自动登录标识
//                            Preference.saveUserToken(passwordEdit.getText().toString());//保存用户密码
//                            Preference.saveUserAccount(usernameEdit.getText().toString());//保存用户账号
//                            finish();
//                        }
////
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        Log.d("qc","获取了工地的信息失败"+t.getMessage());
//                    }
//                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dayNightView = (LoadingView) findViewById(R.id.day_night_view);
                        dayNightView.setVisibility(View.GONE);
                        UtilsTool.showShortToast(LoginActivity.this, loginOnSuccssBean.getMessage() + "");

                    }
                });
            }
        }
    }

    private class GetSoertor implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("角色1", "result" + result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                if (loginOnSuccssBean.getResult() != null) {
                    //是施工方
                    //保存工地信息在数据库
                    //先删除数据库
                    try {
                        String reu = loginOnSuccssBean.getResult().replace("_id", "rg");
                        Log.d("角色1", "reuresult" + reu);
                        ConstructionDataLitepal rolebean = JSON.parseObject(reu, ConstructionDataLitepal.class);
                        if (rolebean.getResult().equals("[]")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsTool.showShortToast(LoginActivity.this, "此账号尚没有添加工地信息,请先在后台添加");
                                    Preference.saveAutoLogin(false);//设置自动登录标识
                                    dayNightView = (LoadingView) findViewById(R.id.day_night_view);
                                    dayNightView.setVisibility(View.GONE);
                                }
                            });
                            return;
                        }
                        //获取对象
                         List<TotalDataLitepal> rolebeans = JSON.parseArray(rolebean.getResult(), TotalDataLitepal.class);
                        DataSupport.deleteAll(TotalDataLitepal.class);
//                                    setAalis(rolebeans.get(0).getRg());
                        rolebeans.add(rolebeans.get(0));
                        constructionid = rolebeans.get(0).getConstructionid();
                        if(rolebeans.get(0).getCoordinate().length==0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsTool.showShortToast(LoginActivity.this,"请在后台绘制此角色工地所属工地坐标");
                                }
                            });
                            return;
                        }
                            NimUIKit.setRe(rolebeans.get(0).getCoordinate());
                        NimUIKit.setConstructionid(constructionid);
                        DataSupport.saveAll(rolebeans);//保存到数据库

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent iten = new Intent(LoginActivity.this, com.example.administrator.irrigationworks.Ui.adapter.IrrigationWordsActivity.class);
                                iten.putExtra("role", role);
                                iten.putExtra("constructionid", constructionid);
                                startActivity(iten);
                                Preference.saveAutoLogin(true);//设置自动登录标识
                                Preference.saveUserToken(passwordEdit.getText().toString());//保存用户密码
                                Preference.saveUserAccount(usernameEdit.getText().toString());//保存用户账号
                                finish();
                            }
                        });


                    } catch (NullPointerException e) {

                    }


//
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("角色", "runOnUiThread");
                            //不是施工方
                            Intent iten = new Intent(LoginActivity.this, IrrigationRoleWordsActivity.class);
                            iten.putExtra("role", role);


                            startActivity(iten);
                            Preference.saveAutoLogin(true);//设置自动登录标识
                            Preference.saveUserToken(passwordEdit.getText().toString());//保存用户密码
                            Preference.saveUserAccount(usernameEdit.getText().toString());//保存用户账号
                            finish();
                        }
                    });

                }
            } else {
                Log.d("角色", "Runnable" + result);
                //不是施工方
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //不是施工方
                        Intent iten = new Intent(LoginActivity.this, IrrigationRoleWordsActivity.class);
                        iten.putExtra("role", role);


                        startActivity(iten);
                        Preference.saveAutoLogin(true);//设置自动登录标识
                        Preference.saveUserToken(passwordEdit.getText().toString());//保存用户密码
                        Preference.saveUserAccount(usernameEdit.getText().toString());//保存用户账号
                        finish();
                    }
                });
            }

        }
    }

    /**
     * 进行登录
     */
//    private class GotoLogin implements HttpUtils.CallBack {
//        @Override
//        public void onRequestComplete(String result) {
//            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
//            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
//
//            }
//        }
//    }
}
