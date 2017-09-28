package com.example.administrator.irrigationworks.Ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.Jpush.AJpushUtils;
import com.example.administrator.irrigationworks.Jpush.ThreadManager;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.ApiUtil;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.Retrofit.RequestServes;
import com.example.administrator.irrigationworks.Ui.Retrofit.ResultCode;
import com.example.administrator.irrigationworks.Ui.adapter.BuilddingAdapter;
import com.example.administrator.irrigationworks.Ui.bean.BuildingSiteBean;
import com.example.administrator.irrigationworks.Ui.bean.HistorBean;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.NewHistorBean;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.PicBean;
import com.example.administrator.irrigationworks.Ui.bean.ShigongfangistBean;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.TaskDbbean;
import com.example.administrator.irrigationworks.Ui.litepal.ConstructionDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;
import com.example.administrator.irrigationworks.Ui.takePhone.adapter.IssuesGridViewAdapter;
import com.example.administrator.irrigationworks.UtilsTozals.CheckUtils;
import com.example.administrator.irrigationworks.UtilsTozals.FileUtilcll;
import com.example.administrator.irrigationworks.UtilsTozals.ImageUpUtils;
import com.example.administrator.irrigationworks.UtilsTozals.MapUtilsManager;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.ResourceUtils;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.example.administrator.irrigationworks.View.CommonPresenter;
import com.example.administrator.irrigationworks.View.ICommonGetPresenter;
import com.example.administrator.irrigationworks.View.IbuildingDataListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.utils.ImageUtils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 提交任务
 * Created by GYKJ on 2017/9/10.
 */
public class BuildingCheckItemActivity extends BaseAppCompatActivity implements View.OnClickListener, IbuildingDataListener,
        IssuesGridViewAdapter.DeleteImageClickListener {
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
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.recycler_work_more)
    ListView recyclerWorkMore;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.ll_data)
    LinearLayout llData;
    private BuildingSiteBean site;
    private List<BuildingSiteBean> moreItemList;
    private boolean hasMore = true;
    public static int limitCounts = 10;
    public int UPDATE_LISTVIEW = 2;
    public BuilddingAdapter findasklistadapter;
    //初始化图片预览
    //临时设置限时操作
    private Timer timer = new Timer();
    private int recLen = 0;
    private String state = "";
    private String inspectorId = "";//施工方id
    private String supervisorId = "";//监理id
    private String id = "";
    private String checkitem = "";
    private Map<Object, Object> mapTasks;
    private Map<String, Object> TaolmapTasks;
    // 进度对话框
    private ProgressDialog mProDialog;
    private ShigongfangistBean rolebeans;


    //任务列表
    private ArrayList<String> tasks;
    private ArrayList<String> NetmImagePathDatas;
    private List<HistorBean> historBeens = new ArrayList<>();
    private static final int Mars = 0;
    private static final int REQUEST_SDCARD = 1;//动态申请权限要大于0或1
    //初始化图片预览
    private IssuesGridViewAdapter mGridViewAdapter;
    private ArrayList<String> mImagePathDatas;
    private ArrayList<String> mImagePathTasks;//提交任务使用
    private ICommonGetPresenter presenter;
    private String construction;
    private String inspector;
    private String timeout="";
    private String constructionid;
    private InspectorDataLitepal inspectorDataLitepal;
    public BuildingCheckItemActivity() {
        this.presenter = new CommonPresenter(this);
    }
    public List<TaskChechItembean> taskChechItembean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        taskChechItembean=new ArrayList<>();
        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapTasks = new HashMap<Object, Object>();
        TaolmapTasks = new HashMap<String, Object>();
        mImagePathTasks = new ArrayList<>();
        tasks = new ArrayList<>();
        checkitem = getIntent().getStringExtra("chechitem");
        request();
        showdata();
        //临时使用
        Boolean net = NetUtil.checkNetWork(BuildingCheckItemActivity.this);
        if (net) {
            inUi();
            getConstruction();
//            adapterInitDta();
        } else {
            networkError.setVisibility(View.VISIBLE);
            UtilsTool.showShortToast(BuildingCheckItemActivity.this, "没有网络");
        }
//        adapterInit();
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
    private void showdata() {
        taskChechItembean =JSON.parseArray(checkitem,TaskChechItembean.class);
     Log.d("qc","此时的职位"+taskChechItembean);
    }

    private void adapterInitDta() {
        final Map<String, Object> hashMap = new HashMap<String, Object>();
//
        hashMap.put("token", NimUIKit.getToken());

        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");
        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作

            }
        });
        mProDialog.setMessage("提交中...");
        mProDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.getCheckTask, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void inUi() {
        //初始化图片预览
        mImagePathDatas = new ArrayList<>();
        sendBt.setOnClickListener(this);
        //初始化图片预览
        mGridViewAdapter = new IssuesGridViewAdapter(this);
        mGridViewAdapter.setDeleteImageClickListenner(this);
        gridView.setAdapter(mGridViewAdapter);
        mapTasks= MapUtilsManager.getThreadPool("","","");
        state="已完成";
        Log.d("mapTasks此时map", "mapTasks此时map" + NimUIKit.getGson().toJson(mapTasks));
    }


    private void adapterInit() {
        Map<String, Object> hashMap = new HashMap<String, Object>();
//
        hashMap.put("token", NimUIKit.getToken());
//        try {
//            HttpUtils.doPostAsyn(FinalTozal.getCheckTask, NimUIKit.getGson().toJson(hashMap), new GetTakList());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);

        Call<ResultCode> call = apiService.getCheckTask(hashMap);
        call.enqueue(new Callback<ResultCode>() {
            @Override
            public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
                ResultCode body = response.body();
                Log.d("li", "获取工地巡查任务" + body.getResult());
                String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
                if (body != null) {
                    Log.d("li", "获取工地巡查任务" + body.getResult());
                    if (body.getCode().equals(Enumerate.LOGINSUCESS)) {
                        ConstructionDataLitepal rolebean = JSON.parseObject(reu, ConstructionDataLitepal.class);
                        //获取对象
                        List<TaskDbbean> rolebeans = JSON.parseArray(rolebean.getResult(), TaskDbbean.class);
                        Log.d("li", "rolebeans获取工地巡查任务" + rolebeans);
                        if (rolebeans == null || rolebeans.size() == 0) {

                            UtilsTool.showShortToast(BuildingCheckItemActivity.this, "没有任务");
                            return;
                        }
                        Log.d("li", "rolebeans获取工地巡查任务" + rolebeans);
                        String jsonData = ResourceUtils.geFileFromAssets(BuildingCheckItemActivity.this, "buildingData.json");
                        ConstructionDataLitepal rl = JSON.parseObject(jsonData, ConstructionDataLitepal.class);
                        List<NewHistorBean> newTask = new ArrayList<>();
                        newTask = JSON.parseArray(rl.getResult(), NewHistorBean.class);
                        showAdapte(newTask);
                        //正式版暂时封全，以下不可删除
//                        StringBuffer marketXml = new StringBuffer();
//
//
//                        JSONObject jsonArray = new JSONObject(rolebeans.get(0).getCheckitem());

//                        String showMessage = jsonArray.getString("土方");
//                        JSONObject jsimple = new JSONObject(showMessage);
//                        Iterator it_color = jsimple.keys();
//                        while (it_color.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color.next().toString();
////                                String value = jsimple.getString(key);//因为 value为空的
//                            HistorBean.setName(key);
////                                HistorBean.setResule(value);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String json2 = jsonArray.getString("安全防护设施");
//                        JSONObject jsimple1 = new JSONObject(json2);
//                        Iterator it_color1 = jsimple1.keys();
//                        while (it_color1.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color1.next().toString();
////                                String value = jsimple.getString(key);//因为 value为空的
//                            HistorBean.setName(key);
////                                HistorBean.setResule(value);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons3 = jsonArray.getString("常规项目");
//                        JSONObject jsimple2 = new JSONObject(jsons3);
//                        Iterator it_color2 = jsimple2.keys();
//                        while (it_color2.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color2.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons4 = jsonArray.getString("常规项目");
//                        JSONObject jsimple3 = new JSONObject(jsons4);
//                        Iterator it_color3 = jsimple2.keys();
//                        while (it_color3.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color3.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons5 = jsonArray.getString("施工现场");
//                        JSONObject jsimple4 = new JSONObject(jsons5);
//                        Iterator it_color4 = jsimple2.keys();
//                        while (it_color4.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color4.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons6 = jsonArray.getString("施工风、水、电及通讯");
//                        JSONObject jsimple5 = new JSONObject(jsons6);
//                        Iterator it_color5 = jsimple2.keys();
//                        while (it_color5.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color5.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons7 = jsonArray.getString("焊接与气割、危险品管理");
//                        JSONObject jsimple6 = new JSONObject(jsons7);
//                        Iterator it_color6 = jsimple2.keys();
//                        while (it_color6.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color6.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons8 = jsonArray.getString("砌体");
//                        JSONObject jsimple7 = new JSONObject(jsons8);
//                        Iterator it_color7 = jsimple2.keys();
//                        while (it_color7.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color7.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons9 = jsonArray.getString("进度检查");
//                        JSONObject jsimple8 = new JSONObject(jsons9);
//                        Iterator it_color8 = jsimple2.keys();
//                        while (it_color8.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color8.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons10 = jsonArray.getString("金属结构、机电设备及安装工程");
//                        JSONObject jsimple9 = new JSONObject(jsons10);
//                        Iterator it_color9 = jsimple2.keys();
//                        while (it_color9.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color9.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//                        String jsons11 = jsonArray.getString("钢砼");
//                        JSONObject jsimple10 = new JSONObject(jsons11);
//                        Iterator it_color10 = jsimple2.keys();
//                        while (it_color9.hasNext()) {
//                            HistorBean HistorBean = new HistorBean();
//
//                            String key = it_color10.next().toString();
//                            HistorBean.setName(key);
//                            historBeens.add(HistorBean);
//                            Log.d("oop", "历史数据moreItem.getCheckitem()" + key + "value");
//                        }
//
////                        roleid=rolebeans.get(0).getConstruction().getConstructor().getRg();//获取发送给施工方和监理的ID
//                        Log.d("qccq", "打卡成功" + rolebeans.get(0).getCheckitem());

//                        String json= marks.get("土方");
//                        String json2= (String) jsimples.get("安全防护设施");
//                        String jsons3= (String) jsimples.get("常规项目");
//                        String jsons4= (String) jsimples.get("常规项目");
//                        String jsons5= (String) jsimples.get("施工现场");
//                        String jsons6= (String) jsimples.get("施工风、水、电及通讯");
//                        String jsons7= (String) jsimples.get("焊接与气割、危险品管理");
//                        String jsons8= (String) jsimples.get("砌体");
//                        String jsons9= (String) jsimples.get("进度检查");
//                        String jsons10= (String) jsimples.get("金属结构、机电设备及安装工程");
//                        String jsons11= (String) jsimples.get("钢砼");


//                        TaskListBean taskListBean = NimUIKit.getGson().fromJson(rolebeans.get(0).getCheckitem().toString(), TaskListBean.class);
//
//                        //OBH获取当前的key值
//
//                        Map mapTypes = JSON.parseObject(NimUIKit.getGson().toJson(taskListBean.getName1()));
//                        for (Object obj : mapTypes.keySet()) {
//                            Log.d("li", "key为：" + obj + "值为：" + mapTypes.get(obj));
//                            tasks.add(obj + "");//获取当前的值
//                            Log.d("li", "mapTypes：" + mapTypes.size() + "tasks值为：" + tasks.size());
//                            if (mapTypes.size() == tasks.size()) {
//                                //进行适配
//                                showAdapte();
//                            }
//
//                        }


                    }
                } else {
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("qc", "获取工地巡查任务" + t.getMessage());
            }
        });

//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                mImagePathDatas = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                Log.d("qc", "此时的ic" + mImagePathDatas);

                mGridViewAdapter.setDatas(mImagePathDatas);
                mGridViewAdapter.notifyDataSetChanged();


            }
        }

    }

    private void showAdapte(List<NewHistorBean> newTask) {

        if (newTask != null) {
            Log.d("params", "此时map" + mapTasks);
            llData.setVisibility(View.VISIBLE);
            findasklistadapter = new BuilddingAdapter(this, newTask, presenter,TaolmapTasks);
            recyclerWorkMore= (ListView) findViewById(R.id.recycler_work_more);
            recyclerWorkMore.setAdapter(findasklistadapter);
            recyclerWorkMore.setVisibility(View.VISIBLE);
            Log.d("qc", "showAdapte的解析" + newTask);
            mProDialog.dismiss();
        } else {
            mProDialog.dismiss();
            UtilsTool.showShortToast(BuildingCheckItemActivity.this, "没有数据");

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        MapUtilsManager.getClean();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_checkitem_site;
    }

    /**
     * 接受图片返回的结果
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 2:
                    Bundle data = msg.getData();
                    getData(data);
                    break;


            }

        }
    };

    private void getData(Bundle data) {
        String val = data.getString("value");
        ConstructionDataLitepal rolebean = JSON.parseObject(val, ConstructionDataLitepal.class);
        if (rolebean.getCode().equals(Enumerate.LOGINSUCESS)) {
            //获取对象
            List<PicBean> rolebeans = JSON.parseArray(rolebean.getResult(), PicBean.class);


            NetmImagePathDatas = new ArrayList<>();
            for (int i = 0; i < rolebeans.size(); i++) {
                NetmImagePathDatas.add(rolebeans.get(i).getPath());
                if (NetmImagePathDatas.size() == rolebeans.size()) {
                    //开始上传图片
                    gettoData(NetmImagePathDatas);
                }
            }
        }

        //进行解析
    }

    private void gettoData(ArrayList<String> netmImagePathDatas) {
        Log.d("qc","请选择检查项目"+mapTasks);
        Log.d("qc","请选择检查项目"+state);
        Log.d("请选择检查项目","请选择检查项目"+timeout);
        if (mapTasks != null & !TextUtils.isEmpty(state)) {
            Map<String, Object> hashMap = new HashMap<String, Object>();

            hashMap.put("state", state);
            hashMap.put("checkitem", mapTasks);
            hashMap.put("token", NimUIKit.getToken());
            hashMap.put("pics", netmImagePathDatas);//网络图片地址
            hashMap.put("timeout", timeout);//网络图片地址
            hashMap.put("constructionid", constructionid);//上传参数
            hashMap.put("inspector", inspectorDataLitepal);//巡查员参数
            try {
                HttpUtils.doPostAsyn(FinalTozal.getNearbyData, NimUIKit.getGson().toJson(hashMap), new GetRegInfo());
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            UtilsTool.showShortToast(BuildingCheckItemActivity.this, "请选择检查项目");
        }
    }

    @Override
    public void reclaim(Map<Object, Object> map, String states,Map<String, Object> maps) {
        //赋值
        //先设置等于null;
        mapTasks = null;
        mapTasks = map;

//        state = states;
        if(NimUIKit.getGson().toJson(mapTasks).contains("不通过")){
            state="有隐患";
        }else if(NimUIKit.getGson().toJson(mapTasks).contains("通过")){
            state="已完成";
        }
        Log.d("mapTasks此时maps", "有隐患" +state);
    }

    @Override
    public void DeleteImageClickI(int position) {
        mImagePathDatas.remove(position);
        mGridViewAdapter.setDatas(mImagePathDatas);
        mGridViewAdapter.notifyDataSetChanged();
    }

    public void getConstruction() {
        //提交参数
                mProDialog = new ProgressDialog(this);
                mProDialog.setCancelable(true);
                mProDialog.setTitle("请稍后");
                mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // cancel进度框时,取消正在进行的操作

                    }
                });
                mProDialog.setMessage("提交中...");
                mProDialog.show();
        construction = getIntent().getStringExtra("construction");
        inspector = getIntent().getStringExtra("inspector");
        constructionid = getIntent().getStringExtra("constructionid");

        rolebeans = NimUIKit.getGson().fromJson(construction, ShigongfangistBean.class);
        inspectorDataLitepal = NimUIKit.getGson().fromJson(inspector, InspectorDataLitepal.class);
        Log.d("角色11","construction"+NimUIKit.getGson().toJson(construction));
        if (rolebeans == null) {
            UtilsTool.showShortToast(BuildingCheckItemActivity.this, "没有任务");
            return;
        }
        Log.d("角色","rolebeans"+NimUIKit.getGson().toJson(rolebeans));
        Log.d("角色","rolebeans.getConstructor()"+rolebeans.getConstructor());
        //获取施工方id
        inspectorId =rolebeans.getPm().getRg();
        //获取监理id
        supervisorId = rolebeans.getChiefsupervisor().getRg();

        String jsonData = ResourceUtils.geFileFromAssets(BuildingCheckItemActivity.this, "buildingData.json");
        ConstructionDataLitepal rl = JSON.parseObject(jsonData, ConstructionDataLitepal.class);
        List<NewHistorBean> newTask = new ArrayList<>();

        newTask = JSON.parseArray(rl.getResult(), NewHistorBean.class);
        Log.d("未完成", "NewHistorBean此时的解析" + newTask);
        List<NewHistorBean> finalNewTask = newTask;

        showAdapte(finalNewTask);

    }

    private class GetRegInfo implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("qc", "提交成功" + result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {

                ThreadManager.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("qqc","点击了");
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("监理人员到位", "111");
                        params.put("2","");
                        params.put("3","");
                        AJpushUtils.jSend_notification("cec772827bd1c2225d7cafd3","690be88e0857376d165ea625",NimUIKit.getContext()+"","巡查员巡查了你的工地"+"结果:"+state,params,inspectorId,supervisorId);
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("qqc", "点击了");

                        mProDialog.dismiss();
                        UtilsTool.showShortToast(BuildingCheckItemActivity.this, "提交成功");
                        finish();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(BuildingCheckItemActivity.this, "提交失败");
                        finish();
                    }
                });

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBt:
                Log.d("qc","点击饿了");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HashMap<String, Object> params = new HashMap<String, Object>();
//                        params.put("监理人员到位", "111");
//                        params.put("2", "");
//                        params.put("3", "");
//                        AJpushUtils.jSend_notification("cec772827bd1c2225d7cafd3", "690be88e0857376d165ea625", NimUIKit.getContext() + "", "巡查员巡查了你的工地", params, inspectorId , supervisorId );//Iid为可发送给多人
//
//
//                    }
//                }).start();
                //首先判断有无隐患，提交整改期限天数

                if(!TextUtils.isEmpty(state)&&"有隐患".equals(state)){

                    final View view = View.inflate(BuildingCheckItemActivity.this, R.layout.mytime_out, null);
                    final EditText p1 = (EditText) view.findViewById(R.id.pass1);
                    p1.setText("3");
                    timeout = p1.getText().toString().trim();
                            new AlertDialog.Builder(BuildingCheckItemActivity.this).setTitle("发现隐患,请输入整改期限天数").setIcon(
                                    android.R.drawable.ic_dialog_info).setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub



                                    timeout = p1.getText().toString().trim();
                                    Log.d("此时的timeout","此时的timeout"+timeout);
                                    if(!TextUtils.isEmpty(timeout)){
                                        if( Integer.parseInt(timeout)>10){//大于期限数的话就返回
                                            //大于期限数的话就返回
                                            UtilsTool.showShortToast(BuildingCheckItemActivity.this,"不可大于10天");
                                            return;
                                        }
                                    }else {
                                        return;
                                    }
                                    if(CheckUtils.judgeIsDecimal(timeout)){
                                        //如果为true的话就提示
                                        UtilsTool.showShortToast(BuildingCheckItemActivity.this,"不可输入小数");
                                        return;
                                    }
                                    mProDialog = new ProgressDialog(BuildingCheckItemActivity.this);
                                    mProDialog.setCancelable(true);
                                    mProDialog.setTitle("请稍后");
                                    mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            // cancel进度框时,取消正在进行的操作

                                        }
                                    });
                                    mProDialog.setMessage("提交中...");
                                    mProDialog.show();
                                    //首先上传图片
                                    if (mImagePathDatas.isEmpty() || mImagePathDatas == null) {
                                        UtilsTool.showShortToast(BuildingCheckItemActivity.this, "图片不能为空");
                                        mProDialog.dismiss();
                                        return;
                                    }
                                    for (int i = 0; i < mImagePathDatas.size(); i++) {
                                        Bitmap map = ImageUtils.getSmallBitmap(mImagePathDatas.get(i));
                                        String urlpath = FileUtilcll.saveFile(BuildingCheckItemActivity.this, (System.currentTimeMillis() + 1) + ".jpg", map);
                                        mImagePathTasks.add(urlpath);
                                        if (i == mImagePathDatas.size() - 1) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // 拍照的图片保存
                                                    String[] toBeStored = mImagePathTasks.toArray(new String[mImagePathTasks.size()]);
                                                    Log.d("li", "toBeStored" + toBeStored);
                                                    String path = ImageUpUtils.uploadFile(NimUIKit.getToken(), FinalTozal.SHOWPIC, toBeStored);
                                                    if (path.equals("图片上传失败") || TextUtils.isEmpty(path)) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                UtilsTool.showShortToast(BuildingCheckItemActivity.this, "图片上传失败");
                                                                mProDialog.dismiss();
                                                            }
                                                        });
                                                        return;
                                                    }
                                                    Log.d("li", "图片上传成功" + path);
                                                    Message msg = new Message();
                                                    msg.what = UPDATE_LISTVIEW;
                                                    Bundle data = new Bundle();
                                                    data.putString("value", path);
                                                    msg.setData(data);
                                                    handler.sendMessage(msg);
//                        gettoData(NetmImagePathDatas);
                                                }
                                            }).start();
                                        }

                                    }


                                }
                            }).setNegativeButton("取消", null).show();
                }else {
                    mProDialog = new ProgressDialog(this);
                    mProDialog.setCancelable(true);
                    mProDialog.setTitle("请稍后");
                    mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // cancel进度框时,取消正在进行的操作

                        }
                    });
                    mProDialog.setMessage("提交中...");
                    mProDialog.show();
                    //首先上传图片
                    if (mImagePathDatas.isEmpty() || mImagePathDatas == null) {
                        UtilsTool.showShortToast(BuildingCheckItemActivity.this, "图片不能为空");
                        mProDialog.dismiss();
                        return;
                    }
                    for (int i = 0; i < mImagePathDatas.size(); i++) {
                        Bitmap map = ImageUtils.getSmallBitmap(mImagePathDatas.get(i));
                        String urlpath = FileUtilcll.saveFile(BuildingCheckItemActivity.this, (System.currentTimeMillis() + 1) + ".jpg", map);
                        mImagePathTasks.add(urlpath);
                        if (i == mImagePathDatas.size() - 1) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // 拍照的图片保存
                                    String[] toBeStored = mImagePathTasks.toArray(new String[mImagePathTasks.size()]);
                                    Log.d("li", "toBeStored" + toBeStored);
                                    String path = ImageUpUtils.uploadFile(NimUIKit.getToken(), FinalTozal.SHOWPIC, toBeStored);
                                    if (path.equals("图片上传失败") || TextUtils.isEmpty(path)) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                UtilsTool.showShortToast(BuildingCheckItemActivity.this, "图片上传失败");
                                                mProDialog.dismiss();
                                            }
                                        });
                                        return;
                                    }
                                    Log.d("li", "图片上传成功" + path);
                                    Message msg = new Message();
                                    msg.what = UPDATE_LISTVIEW;
                                    Bundle data = new Bundle();
                                    data.putString("value", path);
                                    msg.setData(data);
                                    handler.sendMessage(msg);
//                        gettoData(NetmImagePathDatas);
                                }
                            }).start();
                        }

                    }

                }
                //提交参数

                break;
            case R.id.gridView:
                //进行拍照

                break;
//

        }

    }

    //巡查任务
    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("qc", "此时的解析" + result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            String reus = loginOnSuccssBean.getResult().replace("_id", "rg");
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                ConstructionDataLitepal rolebean = JSON.parseObject(reus, ConstructionDataLitepal.class);
//            ConstructionDataLitepal rolebean = JSON.parseObject(reus, ConstructionDataLitepal.class);
//            //获取对象
                if (rolebean.getResult().equals("[]")) {
                    UtilsTool.showShortToast(BuildingCheckItemActivity.this, "没有数据");
                    return;
                }

                List<TaskDbbean> rolebeans = JSON.parseArray(rolebean.getResult(), TaskDbbean.class);
                if (rolebeans == null || rolebeans.size() == 0) {
                    UtilsTool.showShortToast(BuildingCheckItemActivity.this, "没有任务");
                    return;
                }

                Log.d("qc", "jsonData" + rolebeans);
                String jsonData = ResourceUtils.geFileFromAssets(BuildingCheckItemActivity.this, "buildingData.json");
                Log.d("qc", "jsonData" + jsonData);
                ConstructionDataLitepal rl = JSON.parseObject(jsonData, ConstructionDataLitepal.class);
                List<NewHistorBean> newTask = new ArrayList<>();

                newTask = JSON.parseArray(rl.getResult(), NewHistorBean.class);
                Log.d("qc", "NewHistorBean此时的解析" + newTask);
                final List<NewHistorBean> finalNewTask = newTask;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAdapte(finalNewTask);
                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(BuildingCheckItemActivity.this, "没有任务");
                    }
                });

            }
        }
    }
}
