package com.example.administrator.irrigationworks.Ui.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.UpData.UpdateOperation;
import com.example.administrator.irrigationworks.Ui.UserPreferences.MyCache;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.activity.LoginActivity;
import com.example.administrator.irrigationworks.Ui.activity.MyappcodeActivity;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeImageView;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeLinearLayout;
import com.example.administrator.irrigationworks.Ui.dialog.ActionItem;
import com.example.administrator.irrigationworks.Ui.dialog.TitlePopup;
import com.example.administrator.irrigationworks.Ui.frament.Clockattendanceframent;
import com.example.administrator.irrigationworks.Ui.frament.Infoframent;
import com.example.administrator.irrigationworks.Ui.frament.Inspectionframent;
import com.example.administrator.irrigationworks.Ui.frament.Integratedframent;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.Ui.uiview.CustomViewPage;
import com.example.administrator.irrigationworks.UtilsTozals.FaceUtil;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 水利工程主界面
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
    @Bind(R.id.main_bottom)
    LinearLayout mainBottom;
    @Bind(R.id.ib_school_moto)
    BGABadgeImageView ibSchoolMoto;
    @Bind(R.id.tv_school_moto)
    TextView tvSchoolMoto;
    @Bind(R.id.mLaySchool)
    BGABadgeLinearLayout mLaySchool;
    @Bind(R.id.re_word)
    RelativeLayout reWord;
    @Bind(R.id.ib_mine)
    BGABadgeImageView ibMine;
    @Bind(R.id.tv_profile_mine)
    TextView tvProfileMine;
    @Bind(R.id.mLayMine)
    BGABadgeLinearLayout mLayMine;
    @Bind(R.id.re_mine)
    RelativeLayout reMine;
    @Bind(R.id.ib_info)
    BGABadgeImageView ibInfo;
    @Bind(R.id.tv_profile_info)
    TextView tvProfileInfo;
    @Bind(R.id.mLayinfo)
    BGABadgeLinearLayout mLayinfo;
    @Bind(R.id.re_info)
    RelativeLayout reInfo;

    private TextView[] textviews;
    private FragmentPagerAdapter adapter;
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private BGABadgeImageView[] imagebuttons;
    //获取图片
    private Bitmap mImage = null;
    private Toast mToast;
    private byte[] mImageData = null;

    public byte[] getmImageData() {
        return mImageData;
    }

    public void setmImageData(byte[] mImageData) {
        this.mImageData = mImageData;
    }
    // 都是static声明的变量，避免被实例化多次；因为整个app只需要一个计时任务就可以了。
    private static Timer mTimer; // 计时器，每1秒执行一次任务
    public   MyTimerTask mTimerTask; // 计时任务，判断是否未操作时间到达5s
    private static long mLastActionTime; // 上一次操作时间
    private String constructionid; // 上一次操作时间
    private String coordinate; // 上一次操作时间

    private TitlePopup titlePopup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ivAdd.setImageResource(R.mipmap.add_friend3x);

        Boolean net = NetUtil.checkNetWork(IrrigationWordsActivity.this);
        //设置弹窗

        getSubMore().setOnClickListener(new View.OnClickListener() {//获取更多，切换NFC和ORC之间的activity
            @Override
            public void onClick(View v) {
                initPopWindow();
                titlePopup.show(v);
            }
        });
        try{
            if (net) {
                inview();
                List<RoleBeanLitepal> schooldates = new ArrayList<>();
                schooldates = DataSupport.findAll(RoleBeanLitepal.class);
                if (schooldates != null) {
                    if (schooldates.get(0).getRg().length() > 15) {
                        String   mAuthid = "";
                        mAuthid = "a" + schooldates.get(0).getRg().substring(3, 16);
                        Log.d("接受了","接受了"+mAuthid);
                        Log.d("接受了","接受了"+schooldates.get(0).getRg());
                    }

                }

                indata();
                inLister();
                //协议绑定
                //设置长时间不操作，自动退出
//            startTimer();
//进行检查更新地址
                checkUpload();

                //提前加载上传任务数量在onResume;
            }else {
                UtilsTool.showShortToast(IrrigationWordsActivity.this, Enumerate.NONETWORD);
            }
        }catch (Exception e){

        }

    }
    private android.support.v7.app.AlertDialog mIsSetPwdDialog;
    private void initPopWindow() {
        // 实例化标题栏弹窗
        titlePopup = new TitlePopup(IrrigationWordsActivity.this, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(IrrigationWordsActivity.this, R.string.set_nfc,
                R.mipmap.nazhuo));
        titlePopup.addAction(new ActionItem(IrrigationWordsActivity.this, R.string.set_orc,
                R.mipmap.ios));
        titlePopup.addAction(new ActionItem(IrrigationWordsActivity.this, R.string.set_aliens,
                R.mipmap.zhuxiao));

        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0://ISO版本下载

                        Intent intent = new Intent(IrrigationWordsActivity.this, MyappcodeActivity.class);

                        startActivity(intent);
                        break;

                    case 1:// 安卓版本下载
                        UtilsTool.showShortToast(IrrigationWordsActivity.this,"暂未开放");

                        break;
                    case 2:// 注销
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(IrrigationWordsActivity.this);
                        View view = View.inflate(IrrigationWordsActivity.this, R.layout.dialog_issetpwd, null);
                        TextView tvCancal = (TextView) view.findViewById(R.id.bt_cancel);
                        TextView tvExit = (TextView) view.findViewById(R.id.bt_exit);
                        tvCancal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//取消
                                mIsSetPwdDialog.dismiss();
                            }
                        });
                        tvExit.setOnClickListener(new View.OnClickListener() {//确认
                            @Override
                            public void onClick(View v) {
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
                                mIsSetPwdDialog.dismiss();
                                finish();
                            }
                        });
                        builder.setView(view);
                        mIsSetPwdDialog = builder.show();

//                        AlertDialog.Builder builderlogin = new AlertDialog.Builder(IrrigationWordsActivity.this);
//                        builderlogin.setMessage("确认注销登录吗？");
//                        builderlogin.setTitle("提示");
//                        builderlogin.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                //清理数据库
//                                MyCache.clear();
//                                //取消自动登录
//                                Preference.saveAutoLogin(false);
//                                //设置关闭hanlde
//                                NimUIKit.setHandlestop(false);
//
//                                Intent intent =new Intent(IrrigationWordsActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                //这句必须要加
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                //关闭整个程序
//                                System.exit(0);
//                                finish();
//
//                            }
//                        });
//                        builderlogin.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        builderlogin.create().show();

//                        UtilsTool.showShortToast(OperationActivityTwo.this,"已经在ORC功能页");

                        //第一版本
                        break;
                }
            }
        });
    }

    private void checkUpload() {
        UpdateOperation update = new UpdateOperation(IrrigationWordsActivity.this);
        UpdateOperation.showed=false;
        update.checkUpdate();
    }

    // 登录成功，开始计时
    protected  void startTimer() {
        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        // 初始化上次操作时间为登录成功的时间
        mLastActionTime = System.currentTimeMillis();
        // 每过1s检查一次
        mTimer.schedule(mTimerTask, 0, 1000);
        Log.e("wanghang", "start timer");
    }
    // 每当用户接触了屏幕，都会执行此方法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mLastActionTime = System.currentTimeMillis();
        Log.e("wanghang", "user action");
        return super.dispatchTouchEvent(ev);

    }
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Log.e("wanghang", "check time");
            // 60s未操作
            if (System.currentTimeMillis() - mLastActionTime > 60000) {
                // 退出登录
                Intent intentLogin=new Intent(IrrigationWordsActivity.this,LoginActivity.class);
                startActivity(intentLogin);
                finish();
                // 停止计时任务
                stopTimer();

            }
        }
    }
    // 停止计时任务
    protected static void stopTimer() {
        if(mTimer!=null){
            mTimer.cancel();
        }
        Log.e("wanghang", "cancel timer");
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
            mImageData=null;
            mImageData = baos.toByteArray();
            NimUIKit.setmImageData(null);
            NimUIKit.setmImageData(mImageData);
        //发送打卡广播
            Intent intent = new Intent();
            intent.setAction(Enumerate.SEND_INFORM);
            sendBroadcast(intent);
        }
    }
    private void showTip(final String str) {

        mToast.setText(str);
        mToast.show();
    }
    private void inview() {
        mToast = Toast.makeText(IrrigationWordsActivity.this, "", Toast.LENGTH_SHORT);
        fragments = new ArrayList<>();
        fragments.add(new Clockattendanceframent());
        fragments.add(new Inspectionframent());
        fragments.add(new Integratedframent());
        fragments.add(new Infoframent());
        viewPager = (ViewPager) findViewById(R.id.viewpager_app);
        imagebuttons = new BGABadgeImageView[4];
        imagebuttons[0] = (BGABadgeImageView) findViewById(R.id.iv_word);
        imagebuttons[1] = (BGABadgeImageView) findViewById(R.id.ib_school_moto);
        imagebuttons[2] = (BGABadgeImageView) findViewById(R.id.ib_info);
        imagebuttons[3] = (BGABadgeImageView) findViewById(R.id.ib_mine);
        imagebuttons[0].setSelected(true);
        textviews = new TextView[4];
        textviews[0] = (TextView) findViewById(R.id.tv_word_list);
        textviews[1] = (TextView) findViewById(R.id.tv_school_moto);
        textviews[2] = (TextView) findViewById(R.id.tv_profile_info);
        textviews[3] = (TextView) findViewById(R.id.tv_profile_mine);
        textviews[0].setTextColor(0xff01c3fa);
        viewPager.setOffscreenPageLimit(4);
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
                        imagebuttons[3].setSelected(false);
                        textviews[0].setTextColor(0xff01c3fa);
                        textviews[1].setTextColor(0xff808080);
                        textviews[2].setTextColor(0xff808080);
                        textviews[3].setTextColor(0xff808080);


                        break;
                    case 1:
                        imagebuttons[1].setSelected(true);
                        imagebuttons[0].setSelected(false);
                        imagebuttons[2].setSelected(false);
                        imagebuttons[3].setSelected(false);

                        textviews[0].setTextColor(0xff808080);
                        textviews[1].setTextColor(0xff01c3fa);
                        textviews[2].setTextColor(0xff808080);
                        textviews[3].setTextColor(0xff808080);
                        break;
                    case 2:
                        imagebuttons[2].setSelected(true);
                        imagebuttons[1].setSelected(false);
                        imagebuttons[0].setSelected(false);
                        imagebuttons[3].setSelected(false);
                        textviews[0].setTextColor(0xff808080);
                        textviews[1].setTextColor(0xff808080);
                        textviews[2].setTextColor(0xff01c3fa);
                        textviews[3].setTextColor(0xff808080);
                        break;
                    case 3:
                        imagebuttons[3].setSelected(true);
                        imagebuttons[0].setSelected(false);
                        imagebuttons[2].setSelected(false);
                        imagebuttons[1].setSelected(false);
                        textviews[0].setTextColor(0xff808080);
                        textviews[1].setTextColor(0xff808080);
                        textviews[2].setTextColor(0xff808080);
                        textviews[3].setTextColor(0xff01c3fa);
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
            case R.id.re_word:
                viewPager.setCurrentItem(1);
                break;
            case R.id.re_info:
                viewPager.setCurrentItem(2);
                imagebuttons[2].setSelected(true);
                break;
            case R.id.re_mine:
                viewPager.setCurrentItem(3);
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
            android.os.Process.killProcess(android.os.Process.myPid());
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
        //暂时关闭
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
        return R.layout.activity_site_words;
    }
}
