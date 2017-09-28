package com.example.administrator.irrigationworks.Ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
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
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.adapter.GriviewSaveAdapter;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 审核处理问题
 * Created by GYKJ on 2017/9/26.
 */
public class VerifyActivity extends BaseAppCompatActivity {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.text_category)
    EditText textCategory;
    @Bind(R.id.tableRow2)
    TableRow tableRow2;
    @Bind(R.id.text_project)
    EditText textProject;
    @Bind(R.id.tableRow3)
    TableRow tableRow3;
    @Bind(R.id.text_lane)
    EditText textLane;
    @Bind(R.id.tableRow7)
    TableRow tableRow7;
    @Bind(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @Bind(R.id.online_camera)
    ImageView onlineCamera;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.online_img)
    ImageView onlineImg;
    @Bind(R.id.online_authid)
    EditText onlineAuthid;
    @Bind(R.id.online_pick)
    Button onlinePick;
    @Bind(R.id.sendBt)
    TextView sendBt;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.gallery)
    Gallery gallery;
    private ArrayList<String> path;
    private String contect;
    private String id;
    private GriviewSaveAdapter griviewSaveAdapter;
    private ProgressDialog mProDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

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
        toolbarTitle.setText("审核");
        sendBt.setText("确认审核");
        sendBt.setVisibility(View.GONE);
        Boolean net = NetUtil.checkNetWork(VerifyActivity.this);
        if (net) {
//            initRefresh();
            id = getIntent().getStringExtra("id");
            contect = getIntent().getStringExtra("contect");
            path = getIntent().getStringArrayListExtra("pics");
            Log.d("审核", "审核" + "id" + id + " contect " + contect + "path:" + path);

            //进行已读
            getToyidu();
            String hint = contect.replace("{", "").replace("\"", "").replace("}", "");
            textCategory.setText(hint);
            griviewSaveAdapter = new GriviewSaveAdapter(path);
            gallery.setAdapter(griviewSaveAdapter);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean net = NetUtil.checkNetWork(VerifyActivity.this);
                    if (net) {
                        mProDialog = new ProgressDialog(VerifyActivity.this);
                        mProDialog.setCancelable(true);
                        mProDialog.setTitle("请稍后");
                        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // cancel进度框时,取消正在进行的操作

                            }
                        });
                        mProDialog.setMessage("查看中...");
                        mProDialog.show();
                        //确认审核
                        final Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("token", NimUIKit.getToken());
                        hashMap.put("_id", id);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HttpUtils.doPostAsyn(FinalTozal.chiefsupervisorsubmit, NimUIKit.getGson().toJson(hashMap), new GetSorsubmit());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        UtilsTool.showShortToast(VerifyActivity.this, "没有网络");
                    }


                }
            });

        } else {

            UtilsTool.showShortToast(VerifyActivity.this, "没有网络");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_trouble_save;
    }

    public void getToyidu() {
        final Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", NimUIKit.getToken());
        hashMap.put("_id", id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.patrolreaded, NimUIKit.getGson().toJson(hashMap), new Getpatrolreaded());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private class Getpatrolreaded implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("已读", "已读" + result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                Log.d("qc", "已读" + result);

            } else {
            }
        }
    }

    private class GetSorsubmit implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("已审核", "已审核" + result);
            final OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {

                Log.d("qc", "已审核" + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(VerifyActivity.this, "已审核");
                        finish();
                    }
                });


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(VerifyActivity.this, onSuccssBea.getMessage());
                    }
                });

            }
        }
    }
}
