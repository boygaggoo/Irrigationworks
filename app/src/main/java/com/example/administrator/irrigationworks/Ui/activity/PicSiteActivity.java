package com.example.administrator.irrigationworks.Ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.bean.OnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.PicBean;
import com.example.administrator.irrigationworks.Ui.litepal.ConstructionDataLitepal;
import com.example.administrator.irrigationworks.Ui.takePhone.adapter.IssuesGridViewAdapter;
import com.example.administrator.irrigationworks.UtilsTozals.FileUtilcll;
import com.example.administrator.irrigationworks.UtilsTozals.ImageUpUtils;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.example.administrator.irrigationworks.UtilsTozals.WaterMaskUtil;
import com.example.administrator.irrigationworks.UtilsTozals.WaterMaskView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.utils.ImageUtils;

/**
 * 建设工地图片上传
 * Created by Administrator on 2017/9/18.
 */
public class PicSiteActivity extends BaseAppCompatActivity implements IssuesGridViewAdapter.DeleteImageClickListener, View.OnClickListener {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.sendBt)
    TextView sendBt;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.network_error)
    ImageView networkError;
    @Bind(R.id.et_edittext)
    EditText etEdittext;
    //初始化图片预览
    private IssuesGridViewAdapter mGridViewAdapter;
    private ArrayList<String> mImagePathDatas;
    private ArrayList<String> mImagePathTasks;//提交任务使用

    private ArrayList<String> NetmImagePathDatas;
    // 进度对话框
    private ProgressDialog mProDialog;
    public int UPDATE_LISTVIEW = 2;
    private String constructionid;
    //水印
    private WaterMaskView waterMaskView;
    private Bitmap waterBitmap;
    private Bitmap watermarkBitmap;
    private  String time ;

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
        toolbarTitle.setText("上传建设图片");
        constructionid = getIntent().getStringExtra("constructionid");
        sendBt.setOnClickListener(this);
        Boolean net = NetUtil.checkNetWork(PicSiteActivity.this);
        if (net) {

            inUi();
//            adapterInitDta();
        } else {
            networkError.setVisibility(View.VISIBLE);
            UtilsTool.showShortToast(PicSiteActivity.this, "没有网络");
        }
    }

    private void inUi() {
        //初始化图片预览
        mImagePathTasks = new ArrayList<>();
        mImagePathDatas = new ArrayList<>();
        //初始化图片预览
        mGridViewAdapter = new IssuesGridViewAdapter(this);
        mGridViewAdapter.setDeleteImageClickListenner(this);
        gridView.setAdapter(mGridViewAdapter);
        //打印
        waterMaskView = new WaterMaskView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        waterMaskView.setLayoutParams(params);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String webUrl2 = "http://www.baidu.com";//百度

                Date date = getWebsiteDatetime(webUrl2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
                 time = sdf.format(date);
            }
        }).start();


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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pic_site;
    }

    @Override
    public void DeleteImageClickI(int position) {
        mImagePathDatas.remove(position);
        mGridViewAdapter.setDatas(mImagePathDatas);
        mGridViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //上传图片
            case R.id.sendBt:
                //提交参数
                if(TextUtils.isEmpty(etEdittext.getText().toString().trim())){
                    UtilsTool.showShortToast(PicSiteActivity.this,"描述不能为空");
                    return;
                }
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
                    UtilsTool.showShortToast(PicSiteActivity.this, "图片不能为空");
                    mProDialog.dismiss();
                    return;
                }
                waterMaskView.setCompanyText(etEdittext.getText().toString().trim()+"");
                waterMaskView.setInfoText("拍照时间:" + time);
                for (int i = 0; i < mImagePathDatas.size(); i++) {
                    Bitmap map = ImageUtils.getSmallBitmap(mImagePathDatas.get(i));
                    //进行水印
                    Log.d("此时的水印", "此时的水印" + map);
                    saveWaterMask(i, map);


                }
                break;
        }
    }

    private void saveWaterMask(int po, Bitmap sourBitmap) {
        waterBitmap = WaterMaskUtil.convertViewToBitmap(waterMaskView);

        //根据原图处理要生成的水印的宽高
        float width = sourBitmap.getWidth();
        float height = sourBitmap.getHeight();
        float be = width / height;
        Log.d("此时的水印", "此时的水印be =" + be);
        if ((float) 16 / 9 >= be && be >= (float) 4 / 3) {
            //在图片比例区间内16;9~4：3内，将生成的水印bitmap设置为原图宽高各自的1/5
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) width / 5, (int) height / 5);
        } else if (be > (float) 16 / 9) {
            //生成4：3的水印
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) width / 5, (int) width * 3 / 20);
        } else if (be < (float) 4 / 3) {
            //生成4：3的水印
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) height * 4 / 15, (int) height / 5);
        }
        Log.d("此时的水印", "watermarkBitmap此时的水印be =" + be);
        watermarkBitmap = WaterMaskUtil.createWaterMaskLeftBottom(this, sourBitmap, waterBitmap, 0, 0);
        String urlpath = FileUtilcll.saveFile(PicSiteActivity.this, (System.currentTimeMillis() + 1) + ".jpg", watermarkBitmap);
        Log.d("此时的水印", "urlpath =" + urlpath);
        mImagePathTasks.add(urlpath);
        Log.d("此时的水印", "mImagePathTasks =" + mImagePathTasks.size());
        if (po == mImagePathDatas.size() - 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 拍照的图片保存
                    String[] toBeStored = mImagePathTasks.toArray(new String[mImagePathTasks.size()]);
                    Log.d("此时的水印", "toBeStored" + toBeStored);
                    String path = ImageUpUtils.uploadFile(NimUIKit.getToken(), FinalTozal.SHOWPIC, toBeStored);
                    if (path.equals("图片上传失败") || TextUtils.isEmpty(path)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilsTool.showShortToast(PicSiteActivity.this, "图片上传失败");
                                mProDialog.dismiss();
                            }
                        });
                        return;
                    }
                    Log.d("此时的水印", "图片上传成功" + path);
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
        Log.d("上传图片首页", "ConstructionDataLitepal" + NimUIKit.getGson().toJson(rolebean));
        Log.d("此时的水印", "图片上传成功" + NimUIKit.getGson().toJson(rolebean));
        if (rolebean.getCode().equals(Enumerate.LOGINSUCESS)) {
            //获取对象
            List<PicBean> rolebeans = JSON.parseArray(rolebean.getResult(), PicBean.class);

            Log.d("此时的水印", "PicBean" + NimUIKit.getGson().toJson(rolebeans));
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
        Log.d("此时的水印", "gettoData");
        if (constructionid != null) {
            Map<String, Object> hashMap = new HashMap<String, Object>();

            hashMap.put("constructionid", constructionid);//工地constructionid
            hashMap.put("token", NimUIKit.getToken());//token
            hashMap.put("pics", netmImagePathDatas);//图片地址
            try {
                HttpUtils.doPostAsyn(FinalTozal.construction_buildinfo, NimUIKit.getGson().toJson(hashMap), new GetRegInfo());
            } catch (Exception e) {
                Log.d("此时的水印", "提交失败" + e.toString());
                e.printStackTrace();
            }
        } else {
            UtilsTool.showShortToast(PicSiteActivity.this, "请选择检查项目");
        }
    }

    private class GetRegInfo implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("上传图片首页", result);
            OnSuccssBean onSuccssBea = JSON.parseObject(result.toString(), OnSuccssBean.class);
            if (onSuccssBea.getCode().equals(Enumerate.LOGINSUCESS)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(PicSiteActivity.this, "提交成功");
                        finish();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProDialog.dismiss();
                        UtilsTool.showShortToast(PicSiteActivity.this, "提交失败");
                        finish();
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (waterBitmap != null) {
            waterBitmap.recycle();
            waterBitmap = null;
        }
    }
}
