package com.example.administrator.irrigationworks.Ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import net.sf.json.JSONObject;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 巡查图册
 * Created by GYKJ on 2017/9/10.DeleteImageClickI
 */
public class SetpasswordActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.sendBt)
    TextView sendBt;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.textView)
    ImageView textView;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_surepassword)
    EditText etSurepassword;
    @Bind(R.id.tv_sure)
    TextView tvSure;

    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.pass1)
    EditText pass1;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.pass2)
    EditText pass2;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.pass3)
    EditText pass3;
    @Bind(R.id.textView4)
    TextView textView4;
    @Bind(R.id.pass4)
    EditText pass4;
    @Bind(R.id.textView6)
    TextView textView6;
    @Bind(R.id.pass5)
    EditText pass5;
    private String useid;
    // 进度对话框
    private ProgressDialog mProDialog;
    private Map<String, Object> hashMap;
    private String pas1,pas2,pas3,pas4,pas5;
    private List<RoleBeanLitepal> schooldates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        toolbarTitle.setText("修改密码");
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendBt.setVisibility(View.GONE);
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        ivBackLeft.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        sendBt.setVisibility(View.GONE);
        schooldates = DataSupport.findAll(RoleBeanLitepal.class);
        Log.d("修改成功了","修改成功了"+schooldates);
        if(schooldates!=null){
            pass1.setText(schooldates.get(0).getUsername());
            pass2.setText(schooldates.get(0).getInstitution());
            pass3.setText(schooldates.get(0).getRole());
            pass4.setText(schooldates.get(0).getIdentification());
            pass5.setText(schooldates.get(0).getRealname());
            useid=schooldates.get(0).getUserid();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_password;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back_left:

                finish(); //结束当前的activity的生命周期
                break;
            case R.id.tv_sure://确定修改
//                mProDialog = new ProgressDialog(SetpasswordActivity.this);
//                mProDialog.setCancelable(true);
//                mProDialog.setTitle("请稍后");
//                mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        // cancel进度框时,取消正在进行的操作
//
//                    }
//                });
//                mProDialog.setMessage("提交中...");
//                mProDialog.show();

                pas1 = pass1.getText().toString().trim();
                pas2 = pass2.getText().toString().trim();
                pas3 = pass3.getText().toString().trim();
                pas4 = pass4.getText().toString().trim();
                pas5 = pass5.getText().toString().trim();
                hashMap= new HashMap<String, Object>();
//
                hashMap.put("token", NimUIKit.getToken());
                hashMap.put("userid", useid);
                hashMap.put("username", pas1);
                hashMap.put("institution", pas2);
                hashMap.put("role",pas3 );
                hashMap.put("identification", pas4);
                hashMap.put("realname", pas5);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("HttpUtils","HttpUtils"+hashMap);

                            HttpUtils.doPostAsyn(FinalTozal.useredit, NimUIKit.getGson().toJson(hashMap), new GetUseredit());
//                            HttpUtils.doPostAsyn(FinalTozal.useredit, JSON.toJSONString(hashMap), new GetUseredit());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        finish(); //结束当前的activity的生命周期
        return super.onKeyDown(keyCode, event);
    }

    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("qc", "修改密码成功" + result);
        }
    }

    private class GetUseredit implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("qc","修改成功了"+result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(SetpasswordActivity.this,"修改成功");
//                        mProDialog.dismiss();
                    }
                });


            }else {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(SetpasswordActivity.this,"修改失败");
//                        mProDialog.dismiss();
                    }
                });



            }
        }
    }
}
