package com.example.administrator.irrigationworks.Ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.administrator.irrigationworks.Ui.adapter.HidSaveAdapter;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeLinearLayout;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.PicBean;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.TotalDemo;
import com.example.administrator.irrigationworks.Ui.litepal.ConstructionDataLitepal;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.Ui.takePhone.adapter.IssuesGridViewAdapter;
import com.example.administrator.irrigationworks.UtilsTozals.FileUtilcll;
import com.example.administrator.irrigationworks.UtilsTozals.ImageUpUtils;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.squareup.okhttp.ResponseBody;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.utils.ImageUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 隐患上报
 * Created by Administrator on 2017/8/22.
 */
public class HiddentrouSavebleActvity extends BaseAppCompatActivity implements View.OnClickListener, IssuesGridViewAdapter.DeleteImageClickListener {

    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_contionnma)
    TextView tvContionnma;
    @Bind(R.id.bga_rsite_management)
    BGABadgeLinearLayout bgaRsiteManagement;
    @Bind(R.id.lv_listview)
    ListView lvListview;
    @Bind(R.id.online_camera)
    ImageView onlineCamera;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.online_img)
    ImageView onlineImg;
    @Bind(R.id.online_authid)
    EditText onlineAuthid;
    @Bind(R.id.online_reg)
    Button onlineReg;
    private String id;
    //个人信息
    private Bitmap bitmapcalep;
    private List<RoleBeanLitepal> persondates;
    //上传图片
    private static final int TAKE_PHOTO = 5;
    public static File[] files = new File[7];
    public int UPDATE_LISTVIEW = 2;
    private ArrayList<String> mImagePathTasks;//提交任务使用
    // 进度对话框
    private ProgressDialog mProDialog;
    //初始化图片预览
    private IssuesGridViewAdapter mGridViewAdapter;
    private ArrayList<String> mImagePathDatas;
    private ArrayList<String> NetmImagePathDatas;
    private String spectorid;
    private String pmid;
    private String json;
    private String contionname;
    private HidSaveAdapter hidSaveAdapter;
    private List<TaskChechItembean> taskChechItembean;
    private Map<Object, Object> taolmapTasks;
    private static final int REQUEST_SDCARD = 1;//动态申请权限要大于0或1
    public  Map<Integer,Boolean> isCheck = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        toolbarTitle.setText("隐患处理");
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = getIntent().getStringExtra("id");
        json = getIntent().getStringExtra("json");
        taolmapTasks = new HashMap<Object, Object>();
        spectorid = getIntent().getStringExtra("spectorid");//巡查员id
        pmid = getIntent().getStringExtra("pmid");//巡查员id
        contionname = getIntent().getStringExtra("contionname");
        tvContionnma.setText("工地名字" + contionname);
        Log.d("qqq", "点击了feerb22" + id);
        mImagePathDatas = new ArrayList<>();
        mImagePathTasks = new ArrayList<>();
        getCrame();
        persondates = DataSupport.findAll(RoleBeanLitepal.class);
        if (persondates.size() == 0) {
            return;
        }
        setLinter();
        request();
        Boolean net = NetUtil.checkNetWork(HiddentrouSavebleActvity.this);
        if (net) {
            if (!TextUtils.isEmpty(json)) {
                taskChechItembean = new ArrayList<>();
                getTotalData(json);
//

            } else {
            }
        }else {

        }

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
    private void getTotalData(String json) {
        TotalDemo totalDemo = NimUIKit.getGson().fromJson(json, TotalDemo.class);
        Log.d("oop", "key为TaskListBean：" + totalDemo);
        Map mapTypes1 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName1()));
        Map mapTypes2 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName2()));
        Map mapTypes3 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName3()));
        Map mapTypes4 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName4()));
        Map mapTypes5 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName6()));
        Map mapTypes6 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getValue5()));
        Map mapTypes7 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName7()));
        Map mapTypes8 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName8()));
        Map mapTypes9 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName9()));
        Map mapTypes10 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName10()));

//                        if (obj.has("checkitem")) {
//                            JSONObject transitListArray = obj.getJSONObject("checkitem");
//                            for (int i = 0; i < transitListArray.length(); i++) {
//                                Log.d("oop","历史数据"+transitListArray);
//                            }
//                        }
        for (Object obj : mapTypes1.keySet()) {
            if (mapTypes1.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes1.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes2.keySet()) {
            if (mapTypes2.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes2.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes3.keySet()) {
            if (mapTypes3.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes3.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes4.keySet()) {
            if (mapTypes4.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes4.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes5.keySet()) {
            if (mapTypes5.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes5.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes6.keySet()) {
            if (mapTypes6.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes6.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes7.keySet()) {
            if (mapTypes7.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes7.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes8.keySet()) {
            if (mapTypes8.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes8.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes9.keySet()) {
            if (mapTypes9.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes9.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        for (Object obj : mapTypes10.keySet()) {
            if (mapTypes10.get(obj).equals("不通过")) {
                TaskChechItembean taskChechItembean1 = new TaskChechItembean();
                taskChechItembean1.setKeys(obj + "");
                taskChechItembean1.setObjs(mapTypes10.get(obj) + "");
                taskChechItembean.add(taskChechItembean1);//获取当前的值
            }

        }
        adapterInit(taskChechItembean);
    }

    private void adapterInit(List<TaskChechItembean> historBeans) {
        if (historBeans != null) {
            hidSaveAdapter=new HidSaveAdapter(HiddentrouSavebleActvity.this, historBeans, new AsyncCallback() {
                @Override
                public void onSuccess(Map<Object, Object> mapTasks) {
                    taolmapTasks=null;
                    taolmapTasks=mapTasks;
                    Log.d("taolmapTasks", "taolmapTasks" + taolmapTasks);
                }
            },taolmapTasks);
            lvListview.setAdapter(hidSaveAdapter);
        } else {
        }
    }

    private void setLinter() {
        onlineReg.setOnClickListener(this);
        onlineCamera.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hiden_trouble_save;
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
    }

    private void gettoData(ArrayList<String> netmImagePathDatas) {
        try {


            String name = persondates.get(0).getRealname();//隐患备注
            Map<Object, Object> hash = new HashMap<Object, Object>();
            hash.put("contect", NimUIKit.getGson().toJson(taolmapTasks));//内容
            hash.put("pics", netmImagePathDatas);//上传图片
            final Map<Object, Object> hashMap = new HashMap<Object, Object>();
            hashMap.put("rectificationInfo", hash);
            hashMap.put("token", NimUIKit.getToken() + "");
            hashMap.put("_id", id);
            Log.d("qc", "地址" + FinalTozal.pm_confirm + "参数rectificationInfo=" + hash + "&token=" + NimUIKit.getToken() + "_id" + id);

            try {
                HttpUtils.doPostAsyn(FinalTozal.patrolpmsubmit, NimUIKit.getGson().toJson(hashMap), new GetRegInfo());
            } catch (Exception e) {
                Log.d("op", "提交失败" + e);
                e.printStackTrace();
            }


        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.online_reg:
                if(taolmapTasks.size()==0||taolmapTasks==null){
                    UtilsTool.showShortToast(HiddentrouSavebleActvity.this, "请先勾选需要处理的选项");
                    return;
                }
                Boolean net = NetUtil.checkNetWork(HiddentrouSavebleActvity.this);
                if (net) {
//          //提交隐患上报
                    //隐患内容
                    //隐患时间
                    //首先上传图片
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
                    if (mImagePathDatas.isEmpty() || mImagePathDatas == null) {
                        UtilsTool.showShortToast(HiddentrouSavebleActvity.this, "图片不能为空");
                        mProDialog.dismiss();
                        return;
                    }
                    for (int i = 0; i < mImagePathDatas.size(); i++) {
                        Bitmap map = ImageUtils.getSmallBitmap(mImagePathDatas.get(i));
                        String urlpath = FileUtilcll.saveFile(HiddentrouSavebleActvity.this, (System.currentTimeMillis() + 1) + ".jpg", map);
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
                                                UtilsTool.showShortToast(HiddentrouSavebleActvity.this, "图片上传失败");
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
                } else {

                    UtilsTool.showShortToast(HiddentrouSavebleActvity.this, "没有网络");
                }


                break;
            case R.id.online_camera:
                // 动态申请照相权限和读写权限
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HiddentrouSavebleActvity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                } else {
                    try {
                        takeToPhoto(TAKE_PHOTO);
                    } catch (SecurityException e) {

                        UtilsTool.showLongToast(HiddentrouSavebleActvity.this, "你尚未打开权限,请打开手机拍照权限");
                        return;

                    }
                }

                break;
        }
    }

    private void takeToPhoto(int takePhoto) {
        try {
            Date time = new Date();

/**
 * 最后一个参数是文件夹的名称，可以随便起
 */
            File file = new File(Environment.getExternalStorageDirectory(), "拍照");
            if (!file.exists()) {
                file.mkdir();
            }
            /**
             * 这里将时间作为不同照片的名称
             */
            File output = new File(file, "拍照" + ".jpg");
            /**
             * 如果该文件夹已经存在，则删除它，否则创建一个
             */
            try {
                if (output.exists()) {
                    output.delete();
                }
                output.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /**
             * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
             * 将文件转化为uri
             */
            files[takePhoto] = output;
            Uri imageUri = Uri.fromFile(output);
            // 调用系统相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);


        } catch (NullPointerException E) {

        }
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

    private void upLoadImg(String path) {
        final File file = new File(path);
        // create RequestBody instance from file
        //一定要加("AttachmentKey\"; filename=\"" +，不然失败
        RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);

        ////////////////
        String token = NimUIKit.getToken();
        RequestBody requestFiles = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFiles);

        // create a map of data to pass along
        RequestBody tokenBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), token);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", tokenBody);

        Call<ResponseBody> call = apiService.uploadFile(tokenBody);
        // 执行
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void getCrame() {
        mGridViewAdapter = new IssuesGridViewAdapter(this);
        mGridViewAdapter.setDeleteImageClickListenner(this);
        gridView.setAdapter(mGridViewAdapter);
    }


    @Override
    public void DeleteImageClickI(int position) {
        mImagePathDatas.remove(position);
        mGridViewAdapter.setDatas(mImagePathDatas);
        mGridViewAdapter.notifyDataSetChanged();
    }

    private class GetRegInfo implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("op", "提交隐患" + result);
            if (result == null || TextUtils.isEmpty(result)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(HiddentrouSavebleActvity.this, "提交失败");
                    }
                });
                mProDialog.dismiss();
                return;
            }
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                ThreadManager.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("qqc", "点击了");
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("监理人员到位", "111");
                        params.put("2", "");
                        params.put("3", "");
                        //发送信息给巡查员和监理
                        AJpushUtils.jSend_notification("cec772827bd1c2225d7cafd3", "690be88e0857376d165ea625", NimUIKit.getContext() + "", "项目经理处理了隐患", params, spectorid, pmid);
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(HiddentrouSavebleActvity.this, "提交成功");
                        finish();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(HiddentrouSavebleActvity.this, "提交失败");
                    }
                });

            }
        }
    }
    public interface AsyncCallback {

        void onSuccess(Map<Object, Object> TaolmapTasks);


    }
}
