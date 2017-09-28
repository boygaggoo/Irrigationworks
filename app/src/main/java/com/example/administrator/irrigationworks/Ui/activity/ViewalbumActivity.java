package com.example.administrator.irrigationworks.Ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.adapter.DetialImageAdapter;
import com.example.administrator.irrigationworks.Ui.adapter.GriviewAdapter;
import com.example.administrator.irrigationworks.Ui.adapter.MyPagerAdapter;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.uiview.CustomViewPage;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 工地画册
 * Created by Administrator on 2017/9/18.
 */
public class ViewalbumActivity extends BaseAppCompatActivity {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.network_error)
    ImageView networkError;
    @Bind(R.id.horizon_listview)
    GridView horizonListview;

    private String constructionid;
    //    private GriviewAdapter griviewAdapter;
    //适配器 add by bb
    private DetialImageAdapter griviewAdapter;
    //解析图片数据
    private ArrayList<String> urlList;
    private static final int REQUEST_SDCARD = 1;//动态申请权限要大于0或1
    private ImageView imageView;
    private DisplayImageOptions options;

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
        toolbarTitle.setText("图册");
        Boolean net = NetUtil.checkNetWork(ViewalbumActivity.this);
        if (net) {
            urlList = new ArrayList<>();

            mListImageView = new ArrayList<ImageView>();
            constructionid = getIntent().getStringExtra("constructionid");
//            adapterInit();
            request();
            getData();
            //点击图片
            horizonListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Intent intent = new Intent(ViewalbumActivity.this, DetailImageActivity.class);
                    NimUIKit.position = position;//设置图片位置
                    intent.putExtra("image_position", position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
            });

            // TODO Auto-generated method stubhorizonListview.on

        } else {
            networkError.setVisibility(View.VISIBLE);
            UtilsTool.showShortToast(ViewalbumActivity.this, "没有网络");
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

    private void getData() {
        final Map<String, Object> hashMap = new HashMap<String, Object>();
//
        /**
         * 获取工地图册列表
         */
        hashMap.put("token", NimUIKit.getToken());
        hashMap.put("constructionid", constructionid);//上传参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.construction_buildatlas, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gallery_pic;
    }

    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("工地图册", "工地图册" + result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray array = obj.getJSONArray("result");
                    Log.d("工地图册1", "array " + array.length());
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            Log.d("工地图册", "PicPathBean工地图册" + array.getString(i));
                            Log.d("工地图册", "array.length() " + array.length());
                            Log.d("工地图册", "i " + i);
                            //图片集合
                            urlList.add(array.getString(i));

//                            new Task().execute(array.getString(i));
                            if (i == array.length() - 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAdapter(urlList);
                                    }
                                });

                            }
                        }
                        NimUIKit.urlList = urlList;
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilsTool.showShortToast(ViewalbumActivity.this, "工地还没有画册,请先上传");
                                finish();
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("工地图册", "失败PicPathBean工地图册" + e.getMessage());
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(ViewalbumActivity.this, "提交失败");
                        finish();
                    }
                });
            }
        }
    }


    private void showAdapter(List<String> mLists) {
        Log.d("工地图册", "showAdapter");
//        griviewAdapter = new GriviewAdapter(mLists);
        griviewAdapter = new DetialImageAdapter(this, (ArrayList<String>) mLists);
        //显示界面
        horizonListview.setAdapter(griviewAdapter);
    }

    private void init(List<String> mListss) {
        Log.d("图片", "图片" + mListss.size());
        Log.d("图片", "图片" + mListss);
        // TODO Auto-generated method stub


    }


    // ��¼��ǰ��ҳ��
    private int mCount = 0;
    // ��ʼ
    public static final int START = -1;
    // ֹͣ
    public static final int STOP = -2;
    // ����
    public static final int UPDATE = -3;
    // ���ܴ������ĵ�ǰҳ����
    public static final int RECORD = -4;
    private List<ImageView> mListImageView;
    private MyPagerAdapter mAdapter;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case START:


                    handler.sendEmptyMessageDelayed(UPDATE, 3000);
                    break;
                case STOP:
                    handler.removeMessages(UPDATE);
                    break;
                case UPDATE:
                    mCount++;

                    break;
                case RECORD:
                    mCount = msg.arg1;
                    break;

                default:
                    break;
            }

        }
    };
}
