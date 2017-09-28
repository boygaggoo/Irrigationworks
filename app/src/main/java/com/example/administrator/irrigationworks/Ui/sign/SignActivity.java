package com.example.administrator.irrigationworks.Ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.activity.ChitemSiteActivity;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.bean.SignViewDataLitepal;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.TotalDemo;
import com.example.administrator.irrigationworks.UtilsTozals.DataUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignActivity extends BaseAppCompatActivity {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.activity_main_tv_main_day)
    TextView activityMainTvMainDay;
    @Bind(R.id.activity_main_tv_score)
    TextView activityMainTvScore;
    @Bind(R.id.activity_main_tv_year)
    TextView activityMainTvYear;
    @Bind(R.id.activity_main_tv_month)
    TextView activityMainTvMonth;
    @Bind(R.id.activity_main_ll_date)
    LinearLayout activityMainLlDate;
    public List<TaskChechItembean> taskChechItembean;


    private TextView tvSignDay;
    private TextView tvScore;
    private TextView tvYear;
    private TextView tvMonth;
    private SignView signView;
    private List<SignEntity> data;
    private String constructtionid = "";
    private Date date;
    private Calendar calendarToday;
    private List<Integer> daylist;
    private List<SignViewDataLitepal> poistoricaDataLitepal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        taskChechItembean = new ArrayList<>();
        constructtionid = getIntent().getStringExtra("constructtionid");
        initView();
        onReady();
        getData();
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

    private void getData() {
        final String webUrl2 = "http://www.baidu.com";//百度时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                date = getWebsiteDatetime(webUrl2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
                String time = sdf.format(date);
                final Map<String, Object> hashMap = new HashMap<String, Object>();
//
                hashMap.put("token", NimUIKit.getToken());
                hashMap.put("constructionid", constructtionid);
                hashMap.put("ym", time);
                Log.d("获取当前的数据", "constructionid" + constructtionid + "ym" + time + "token" + NimUIKit.getToken());
                try {
                    HttpUtils.doPostAsyn(FinalTozal.patrollist, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    protected int getLayoutId() {
        return R.layout.actvity_sign;
    }

    private void initView() {
        daylist = new ArrayList<>();
        tvSignDay = (TextView) findViewById(R.id.activity_main_tv_main_day);
        tvScore = (TextView) findViewById(R.id.activity_main_tv_score);
        tvYear = (TextView) findViewById(R.id.activity_main_tv_year);
        tvMonth = (TextView) findViewById(R.id.activity_main_tv_month);
        signView = (SignView) findViewById(R.id.activity_main_cv);
        if (signView != null) {
            signView.setOnTodayClickListener(onTodayClickListener);
        }

        //---------------------------------分辨率适配----------------------------------
        ResolutionUtil resolutionUtil = ResolutionUtil.getInstance();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = resolutionUtil.formatVertical(40);
        tvSignDay.setLayoutParams(layoutParams);
        tvSignDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(42));

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = resolutionUtil.formatVertical(40);
        tvScore.setLayoutParams(layoutParams);
        tvScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(95));

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(130));
        layoutParams.topMargin = resolutionUtil.formatVertical(54);
        View llDate = findViewById(R.id.activity_main_ll_date);
        if (llDate != null) {
            llDate.setLayoutParams(layoutParams);
        }

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = resolutionUtil.formatHorizontal(43);
        tvYear.setLayoutParams(layoutParams);
        tvYear.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(43));

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = resolutionUtil.formatHorizontal(44);
        tvMonth.setLayoutParams(layoutParams);
        tvMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX, resolutionUtil.formatVertical(43));

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(818));
        signView.setLayoutParams(layoutParams);

        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resolutionUtil.formatVertical(142));
        layoutParams.topMargin = resolutionUtil.formatVertical(111);
        layoutParams.leftMargin = layoutParams.rightMargin = resolutionUtil.formatHorizontal(42);

    }

    private void onReady() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        tvSignDay.setText(R.string.you_have_sign);
        tvScore.setText(String.valueOf(3015));
        tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        calendarToday = Calendar.getInstance();



    }

    private void onSign() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SignDialogFragment signDialogFragment = SignDialogFragment.newInstance(15);
        signDialogFragment.setOnConfirmListener(onConfirmListener);
        signDialogFragment.show(fragmentManager, SignDialogFragment.TAG);
    }

    private void signToday() {
        data.get(signView.getDayOfMonthToday() - 1).setDayType(SignView.DayType.SIGNED.getValue());
        signView.notifyDataSetChanged();


        int score = Integer.valueOf((String) tvScore.getText());
        tvScore.setText(String.valueOf(score + 15));
    }

    private SignView.OnTodayClickListener onTodayClickListener = new SignView.OnTodayClickListener() {
        @Override
        public void onTodayClick() {
//            onSign();

        }
    };

    private SignDialogFragment.OnConfirmListener onConfirmListener = new SignDialogFragment.OnConfirmListener() {
        @Override
        public void onConfirm() {
            signToday();
        }
    };

    /**
     * 获取当前时间
     */
    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("获取当前的数据", "获取当前的数据" + result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            String reus = loginOnSuccssBean.getResult().replace("_id", "rg");
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                //用来判断
                LoginOnSuccssBean loginOnSuccssBeans = JSON.parseObject(reus, LoginOnSuccssBean.class);
                poistoricaDataLitepal = JSON.parseArray(loginOnSuccssBeans.getResult(), SignViewDataLitepal.class);


                Log.d("历史巡查", "当前日" + DataUtils.dataToCalendar(DataUtils.dataToDate(poistoricaDataLitepal.get(0).getYmd())).get(Calendar.DAY_OF_MONTH));
                //设置当前的巡查日历
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < poistoricaDataLitepal.size(); i++) {
//                            SignEntity signEntity = new SignEntity();
                            int dayOfMonthToday = DataUtils.dataToCalendar(DataUtils.dataToDate(poistoricaDataLitepal.get(i).getYmd())).get(Calendar.DAY_OF_MONTH);
                            //加入集合
                            //暂时屏蔽
//                            int MonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
//                            for (int s = 0; s < 30; i++) {
//                                SignEntity signEntitys = new SignEntity();
//                                if (MonthToday == i + 1) {
//                                    signEntitys.setDayType(2);
//                                } else if (dayOfMonthToday == s) {
//                                    signEntitys.setDayType(0);
//                                } else {
//                                    signEntitys.setDayType(1);
//                                }
//                                data.add(signEntitys);
//                            }
//                            daylist.add(dayOfMonthToday);
//                            SignAdapter signAdapter = new SignAdapter(data);
//                            signView.setAdapter(signAdapter, daylist);
                            int MonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
                            //已经签到
                            data = new ArrayList<>();
                            Random ran = new Random();
                            //暂时屏蔽
                            for (int s = 0; i < 30; i++) {
                                SignEntity signEntity = new SignEntity();
                                if (MonthToday == s + 1)
                                    signEntity.setDayType(2);
                                else
                                    signEntity.setDayType((ran.nextInt(1000) % 2 == 0) ? 0 : 1);

                                data.add(signEntity);
                            }
                            SignAdapter signAdapter = new SignAdapter(data);
                            signView.setAdapter(SignActivity.this,signAdapter, daylist, poistoricaDataLitepal, taskChechItembean);
                        }

                    }
                });

            }
        }
    }

}
