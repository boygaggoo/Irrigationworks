package com.example.administrator.irrigationworks.Ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.UserPreferences.MyCache;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeImageView;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeLinearLayout;
import com.example.administrator.irrigationworks.Ui.frament.Clockattendanceframent;
import com.example.administrator.irrigationworks.Ui.frament.Infoframent;
import com.example.administrator.irrigationworks.Ui.frament.Inspectionframent;
import com.example.administrator.irrigationworks.Ui.frament.Integratedframent;
import com.example.administrator.irrigationworks.Ui.uiview.CustomViewPage;
import com.example.administrator.irrigationworks.UtilsTozals.FaceUtil;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.iflytek.cloud.FaceRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 施工方水利工程主界面
 * Created by Administrator on 2017/8/10.
 */
public class IrrigationWordsActivity extends BaseAppCompatActivity {


    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager_app)
    CustomViewPage viewpagerApp;
    @Bind(R.id.iv_word)
    BGABadgeImageView ivWord;
    @Bind(R.id.tv_word_list)
    TextView tvWordList;
    @Bind(R.id.mLayWord)
    BGABadgeLinearLayout mLayWord;
    @Bind(R.id.re_school)
    RelativeLayout reSchool;
    @Bind(R.id.ib_info)
    BGABadgeImageView ibInfo;
    @Bind(R.id.tv_profile_info)
    TextView tvProfileInfo;
    @Bind(R.id.mLayinfo)
    BGABadgeLinearLayout mLayinfo;
    @Bind(R.id.re_info)
    RelativeLayout reInfo;
    @Bind(R.id.ib_mine)
    BGABadgeImageView ibMine;
    @Bind(R.id.tv_profile_mine)
    TextView tvProfileMine;
    @Bind(R.id.mLayMine)
    BGABadgeLinearLayout mLayMine;
    @Bind(R.id.re_mine)
    RelativeLayout reMine;
    @Bind(R.id.main_bottom)
    LinearLayout mainBottom;
    private TextView[] textviews;
    private FragmentPagerAdapter adapter;
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private BGABadgeImageView[] imagebuttons;
    //获取图片
    private Bitmap mImage = null;
    private Toast mToast;
    private byte[] mImageData = null;
    //获取角色名字
    private String role;

    public byte[] getmImageData() {
        return mImageData;
    }

    public void setmImageData(byte[] mImageData) {
        this.mImageData = mImageData;
    }

    public byte[] getData() {
        return this.getmImageData();
    }
    // 进度对话框
    private ProgressDialog mProDialog;
    // 拍照得到的照片文件
    private File mPictureFile;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ivAdd.setImageResource(R.mipmap.add_friend3x);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderlogin = new AlertDialog.Builder(IrrigationWordsActivity.this);
                builderlogin.setMessage("确认注销登录吗？");
                builderlogin.setTitle("提示");
                builderlogin.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //清理数据库
                        MyCache.clear();
                        //取消自动登录
                        Preference.saveAutoLogin(false);
                        //设置关闭hanlde
                        NimUIKit.setHandlestop(false);
                        Intent intent =new Intent(IrrigationWordsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        //这句必须要加
                        android.os.Process.killProcess(android.os.Process.myPid());
                        //关闭整个程序
                        System.exit(0);
                        finish();

                    }
                });
                builderlogin.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderlogin.create().show();
            }
        });
        Boolean net = NetUtil.checkNetWork(IrrigationWordsActivity.this);
        if (net) {
            role = getIntent().getStringExtra("role");

            inview();
            indata();
            inLister();
            //协议绑定
            getFacr();

            //提前加载上传任务数量在onResume;
        } else {
            UtilsTool.showShortToast(IrrigationWordsActivity.this, Enumerate.NONETWORD);
        }
    }

    private void getFacr() {
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");
        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作
                if (null != mFaceRequest) {
                    mFaceRequest.cancel();
                }
            }
        });
        mFaceRequest = new FaceRequest(this);
    }

    private void inview() {
        mToast = Toast.makeText(IrrigationWordsActivity.this, "", Toast.LENGTH_SHORT);
        fragments = new ArrayList<>();
        fragments.add(new Clockattendanceframent());
        fragments.add(new Integratedframent());
        fragments.add(new Infoframent());
        viewPager = (ViewPager) findViewById(R.id.viewpager_app);
        imagebuttons = new BGABadgeImageView[3];
        imagebuttons[0] = (BGABadgeImageView) findViewById(R.id.iv_word);
        imagebuttons[1] = (BGABadgeImageView) findViewById(R.id.ib_info);
        imagebuttons[2] = (BGABadgeImageView) findViewById(R.id.ib_mine);
        imagebuttons[0].setSelected(true);
        textviews = new TextView[3];
        textviews[0] = (TextView) findViewById(R.id.tv_word_list);
        textviews[1] = (TextView) findViewById(R.id.tv_profile_info);
        textviews[2] = (TextView) findViewById(R.id.tv_profile_mine);
        textviews[0].setTextColor(0xff01c3fa);
        viewPager.setOffscreenPageLimit(3);
    }

    private void indata() {
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("qq", "进入了" + "IrrigationWordsActivity1" + data + "此时的requestCode" + requestCode);
        if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {//进行截图发给Clockattendanceframent
            // 获取返回数据
            Bitmap bmp = data.getParcelableExtra("data");
            // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if (null != bmp) {
                FaceUtil.saveBitmapToFile(IrrigationWordsActivity.this, bmp);
            }
            // 获取图片保存路径
            String fileSrc = FaceUtil.getImagePath(IrrigationWordsActivity.this);
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
            NimUIKit.setmImageData(null);
            NimUIKit.setmImageData(mImageData);

        }

    }

    private void showTip(final String str) {

        mToast.setText(str);
        mToast.show();
    }

    private void inLister() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        imagebuttons[0].setSelected(true);
                        imagebuttons[1].setSelected(false);
                        imagebuttons[2].setSelected(false);
                        textviews[0].setTextColor(0xff01c3fa);
                        textviews[1].setTextColor(0xff808080);
                        textviews[2].setTextColor(0xff808080);


                        break;
                    case 1:
                        imagebuttons[1].setSelected(true);
                        imagebuttons[0].setSelected(false);
                        imagebuttons[2].setSelected(false);

                        textviews[0].setTextColor(0xff808080);
                        textviews[1].setTextColor(0xff01c3fa);
                        textviews[2].setTextColor(0xff808080);
                        break;
                    case 2:
                        imagebuttons[2].setSelected(true);
                        imagebuttons[1].setSelected(false);
                        imagebuttons[0].setSelected(false);
                        textviews[0].setTextColor(0xff808080);
                        textviews[1].setTextColor(0xff808080);
                        textviews[2].setTextColor(0xff01c3fa);
                        break;
                    case 3:
                        imagebuttons[3].setSelected(true);
                        imagebuttons[0].setSelected(false);
                        imagebuttons[2].setSelected(true);
                        textviews[0].setTextColor(0xff808080);
                        textviews[1].setTextColor(0xff808080);
                        textviews[2].setTextColor(0xff01c3fa);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onTabClicked(View v) {
        switch (v.getId()) {
            case R.id.re_school:
                viewPager.setCurrentItem(0);
                break;

            case R.id.re_info:
                viewPager.setCurrentItem(1);

                break;
            case R.id.re_mine:
                viewPager.setCurrentItem(2);
                imagebuttons[2].setSelected(true);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime;

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {

//            String packName = "com.monsterlin.hsp_teacher";
//            ActivityKill.dosstory(LoginActivity.this, packName);
            Process.killProcess(Process.myPid());
            //关闭整个程序
            System.exit(0);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        //取消实时定位
        DestoreService();

        Process.killProcess(Process.myPid());
        //关闭整个程序
        System.exit(0);
    }

    private void DestoreService() {
//        Intent intet1 = new Intent(IrrigationWordsActivity.this, LocationService.class);
//        stopService(intet1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_irrigation_contrution;
    }
}
