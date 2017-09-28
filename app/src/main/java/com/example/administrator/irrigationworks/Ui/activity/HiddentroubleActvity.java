package com.example.administrator.irrigationworks.Ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.ApiUtil;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.Retrofit.RequestServes;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.UtilsTozals.ImageUtil;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.squareup.okhttp.ResponseBody;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
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
public class HiddentroubleActvity extends BaseAppCompatActivity implements View.OnClickListener {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.text_category)
    TextView textCategory;
    @Bind(R.id.tableRow2)
    TableRow tableRow2;
    @Bind(R.id.text_project)
    TextView textProject;
    @Bind(R.id.tableRow3)
    TableRow tableRow3;
    @Bind(R.id.text_lane)
    TextView textLane;
    @Bind(R.id.tableRow7)
    TableRow tableRow7;
    @Bind(R.id.edit_feedback)
    EditText editFeedback;
    @Bind(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @Bind(R.id.online_camera)
    ImageView onlineCamera;
    @Bind(R.id.online_img)
    ImageView onlineImg;
    @Bind(R.id.online_authid)
    EditText onlineAuthid;
    @Bind(R.id.online_pick)
    Button onlinePick;
    @Bind(R.id.online_reg)
    Button onlineReg;
    private String id;
    //个人信息
    private Bitmap bitmapcalep;
    private List<RoleBeanLitepal> persondates;
    //上传图片
    private static final int TAKE_PHOTO = 5;
    public static File[] files = new File[7];

    // 进度对话框
    private ProgressDialog mProDialog;
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
        id=getIntent().getStringExtra("id");
        Log.d("qqq","点击了feerb22"+id);
        persondates= DataSupport.findAll(RoleBeanLitepal.class);
        setLinter();
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
        return R.layout.activity_hiden_trouble;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.online_reg:
                //提交隐患上报
                //隐患内容
                //隐患时间
                Boolean net = NetUtil.checkNetWork(HiddentroubleActvity.this);
                if (net) {
                    try{
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
                        String time= textProject.getText().toString(); //隐患时间
                        String category= textCategory.getText().toString();//隐患标题

                        String editFeedbac=editFeedback.getText().toString();//隐患备注
                        String name=persondates.get(0).getRealname();//隐患备注
                        Map<String, String> hashMap= new HashMap<String, String>();
                        Map<String, String> maps= new HashMap<String, String>();

                        hashMap.put("report_content", category+"");
                        hashMap.put("remark", editFeedbac+"");
                        hashMap.put("report_time", time+"");
                        hashMap.put("token", NimUIKit.getToken()+"");
                        hashMap.put("reportor", name+"");
                        hashMap.put("pics", "");//上传图片
                        hashMap.put("constructionid", id);//工地id

                        try {
                            HttpUtils.doPostAsyn(FinalTozal.Hidden_trouble_report, NimUIKit.getGson().toJson(hashMap), new GetRegInfo());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }catch (Exception e){

                    }
                }else {
                    UtilsTool.showShortToast(HiddentroubleActvity.this,"没有网络");
                }


                break;
            case R.id.online_camera:
                // 动态申请照相权限和读写权限
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HiddentroubleActvity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                } else {
                    try{
                        takeToPhoto(TAKE_PHOTO);
                    }catch (SecurityException e){

                        UtilsTool.showLongToast(HiddentroubleActvity.this,"你尚未打开权限,请打开手机拍照权限");
                        return;

                    }
                }

                break;
        }
    }

    private void takeToPhoto(int takePhoto) {
        try{
            Date time=new Date();

/**
 * 最后一个参数是文件夹的名称，可以随便起
 */
            File file=new File(Environment.getExternalStorageDirectory(),"拍照");
            if(!file.exists()){
                file.mkdir();
            }
            /**
             * 这里将时间作为不同照片的名称
             */
            File output=new File(file,"拍照"+".jpg");
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
            files[takePhoto]=output;
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
        if (requestCode == TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                if(bitmapcalep!=null){
                    bitmapcalep.recycle();
                }
                bitmapcalep= ImageUtil.getSmallBitmap(files[requestCode].getPath());
                try {
                    onlineImg.setImageBitmap(bitmapcalep);//想图像显示在ImageView视图上，private ImageView img;
                } catch (OutOfMemoryError e) {

                } catch (Exception e) {

                }
                upLoadImg(files[requestCode].getPath());
                File file = files[requestCode];


            }
        }
    }

    private void upLoadImg(String path) {
        final File file = new File(path);
        // create RequestBody instance from file
        //一定要加("AttachmentKey\"; filename=\"" +，不然失败
        RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);

        ////////////////
        String token =NimUIKit.getToken();
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

    private class GetRegInfo implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("op", "提交隐患" + result);
            OnSuccssBean onSuccssBea= JSON.parseObject(result.toString(),OnSuccssBean.class);
            if(onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(HiddentroubleActvity.this,"提交成功");
                        finish();
                    }
                });

            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(HiddentroubleActvity.this,"提交失败");
                    }
                });

            }
        }
    }
}
