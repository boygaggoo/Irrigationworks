package com.example.administrator.irrigationworks.Ui.newsign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.administrator.irrigationworks.Ui.sign.ResolutionUtil;
import com.example.administrator.irrigationworks.Ui.sign.SignEntity;
import com.example.administrator.irrigationworks.UtilsTozals.DataUtils;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewSignActivity extends BaseAppCompatActivity implements CalendarViewGAC.OnDateSelectedListener {
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
    @Bind(R.id.activity_main_cv)
    CalendarViewGAC calendar;


    private TextView tvSignDay;
    private TextView tvScore;
    private TextView tvYear;
    private TextView tvMonth;
    private List<SignEntity> data;
    private String constructtionid = "";
    private Date date;
    private Calendar calendarToday;
    private List<Integer> daylist;
    private List<SignViewDataLitepal> poistoricaDataLitepal;
    //日历
    private HashMap<String, Integer> map = TestData.getMap();
    private RelativeLayout view;
    private ViewPager vp;
    private CalendarPagerAdapter adapter;
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
        toolbarTitle.setText("巡查日历");
        taskChechItembean = new ArrayList<>();
        constructtionid = getIntent().getStringExtra("constructtionid");
        poistoricaDataLitepal = JSON.parseArray(constructtionid, SignViewDataLitepal.class);
        for (int i = 0; i < poistoricaDataLitepal.size(); i++) {
//                            SignEntity signEntity = new SignEntity();
            int dayOfMonthToday = DataUtils.dataToCalendar(DataUtils.dataToDate(poistoricaDataLitepal.get(i).getYmd())).get(Calendar.DAY_OF_MONTH);
            if (poistoricaDataLitepal.get(i).getCheckitem().contains("不通过")) {
                map.put(poistoricaDataLitepal.get(i).getYmd(), 1);
            } else {
                map.put(poistoricaDataLitepal.get(i).getYmd(), 0);
            }
            Log.d("此时的map", "此时的map" + poistoricaDataLitepal.get(i).getYmd());
            if (i == poistoricaDataLitepal.size() - 1) {
                calendar.setOnDateSelectedLintener(this);
                calendar.setDecorator(new TextDecorator(map));
            }
        }


        initView();
        setDayMay();
        onReady();
//        getData();
    }

    private void setDayMay() {
        calendar.setOnDateSelectedLintener(this);

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
    protected int getLayoutId() {
        return R.layout.actvity_new_sign;
    }

    private void initView() {
        daylist = new ArrayList<>();
        tvSignDay = (TextView) findViewById(R.id.activity_main_tv_main_day);
        tvScore = (TextView) findViewById(R.id.activity_main_tv_score);
        tvYear = (TextView) findViewById(R.id.activity_main_tv_year);
        tvMonth = (TextView) findViewById(R.id.activity_main_tv_month);


    }

    private void onReady() {
        tvSignDay.setVisibility(View.GONE);
        tvSignDay.setText(R.string.you_have_sign);


    }

    /**
     * 点击当前的时间
     *
     * @param view
     */
    @Override
    public void onDateSelected(DayView view) {
        Log.d("日期", "view" + view);
        view.setSelected();
        Log.d("日期", "view.view.getDate().toString()" + view.getDate().getDay());
//        print(view.getDate().toString());
        String checkite = "";
        for (int i = 0; i < poistoricaDataLitepal.size(); i++) {
            int dayOfMonthToday = DataUtils.dataToCalendar(DataUtils.dataToDate(poistoricaDataLitepal.get(i).getYmd())).get(Calendar.DAY_OF_MONTH);
            if (view.getDate().getDay() == dayOfMonthToday) {
                checkite = poistoricaDataLitepal.get(i).getCheckitem();

            } else {
//                UtilsTool.showShortToast(NewSignActivity.this,"没有数据");
            }
        }
        if (!TextUtils.isEmpty(checkite)) {
            ShowData(checkite);
        } else {
            UtilsTool.showShortToast(NewSignActivity.this, "没有数据");
        }
    }

    private void ShowData(String checkitem) {
        TotalDemo totalDemo = NimUIKit.getGson().fromJson(checkitem, TotalDemo.class);
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
        Log.d("oop", "key为mapTypes1：" + mapTypes1);
        Log.d("oop", "key为mapTypes2：" + mapTypes2);
        Log.d("oop", "key为mapTypes3：" + mapTypes3);
        Log.d("oop", "key为mapTypes5：" + mapTypes5);
        Log.d("oop", "key为mapTypes4：" + mapTypes4);
        Log.d("oop", "key为mapTypes6：" + mapTypes6);
        Log.d("oop", "key为mapTypes7：" + mapTypes7);
        Log.d("oop", "key为mapTypes8：" + mapTypes8);
        Log.d("oop", "key为mapTypes9：" + mapTypes9);
        Log.d("oop", "key为mapTypes10：" + mapTypes10);
//                        if (obj.has("checkitem")) {
//                            JSONObject transitListArray = obj.getJSONObject("checkitem");
//                            for (int i = 0; i < transitListArray.length(); i++) {
//                                Log.d("oop","历史数据"+transitListArray);
//                            }
//                        }
        Log.d("oop", "key为：" + taskChechItembean);
        for (Object obj : mapTypes1.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes1.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes2.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes2.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes3.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes3.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes4.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes4.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes5.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes5.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes6.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes6.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes7.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes7.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes8.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes8.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes9.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes9.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes10.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes10.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        Intent intentnotess = new Intent(NewSignActivity.this, ChitemSiteActivity.class);
        intentnotess.putExtra("chitems", NimUIKit.getGson().toJson(taskChechItembean));
        startActivity(intentnotess);

    }

    public void print(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 进行刷新
     */
    public interface AsyncCallback {

        void onSuccess(List<SignViewDataLitepal> signViewDataLitepal);


    }
}
