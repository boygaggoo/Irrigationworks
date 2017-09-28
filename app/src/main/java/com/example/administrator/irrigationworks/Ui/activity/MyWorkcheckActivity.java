package com.example.administrator.irrigationworks.Ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.Retrofit.ApiUtil;
import com.example.administrator.irrigationworks.Ui.Retrofit.RequestServes;
import com.example.administrator.irrigationworks.Ui.Retrofit.ResultCode;
import com.example.administrator.irrigationworks.Ui.adapter.MyOrderAdapter;
import com.example.administrator.irrigationworks.Ui.bean.WorkCheckListBean;
import com.example.administrator.irrigationworks.Ui.litepal.ConstructionDataLitepal;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import java.util.HashMap;
import java.util.List;

import app.dinus.com.loadingdrawable.LoadingView;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 我的订单首页
 */
public class MyWorkcheckActivity extends BaseAppCompatActivity implements View.OnClickListener {


    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.lv_order)
    ListView lvOrder;

    @Bind(R.id.electric_fan_view)
    LoadingView electricFanView;
    private MyOrderAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //进行注解
        ButterKnife.bind(this);
        Boolean net = NetUtil.checkNetWork(MyWorkcheckActivity.this);
        if (net) {
            Log.d("qc", "考勤记录");
            ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
            ivBackLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            toolbarTitle.setText("考勤记录");
            //  Api/Order/lists 参数p页码  order_status订单状态
            getData();
        } else {
            electricFanView.setVisibility(View.VISIBLE);
            UtilsTool.showShortToast(MyWorkcheckActivity.this, Enumerate.NONETWORD);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_order;
    }


    @Override
    public void onClick(View v) {

    }

    private void getData() {
        RequestServes apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
//        if (!TextUtils.isEmpty("")) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", NimUIKit.getToken());
        Call<ResultCode> call = apiService.getAttendanceRecord(params);
        call.enqueue(new Callback<ResultCode>() {
            @Override
            public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
                ResultCode body = response.body();
                Log.d("qc", "考勤记录成功" + body);
                String reu = NimUIKit.getGson().toJson(body.getResult()).replace("_id", "rg");
                if (body != null) {
                    if (body.getCode().equals(Enumerate.LOGINSUCESS)) {
                        ConstructionDataLitepal rolebean = JSON.parseObject(reu, ConstructionDataLitepal.class);
                        //获取对象
                        List<WorkCheckListBean> rolebeans = JSON.parseArray(rolebean.getResult(), WorkCheckListBean.class);
                        if (rolebeans == null||rolebeans.size()==0) {
                            UtilsTool.showShortToast(MyWorkcheckActivity.this, "没有数据");
                            electricFanView.setVisibility(View.VISIBLE);
                            return;
                        }
                        electricFanView.setVisibility(View.GONE);
                        showAdapter(rolebeans);
                    }
                } else {
                    UtilsTool.showShortToast(MyWorkcheckActivity.this, "没有数据");
                    electricFanView.setVisibility(View.VISIBLE);
                }

            }


            @Override
            public void onFailure(Throwable t) {
                UtilsTool.showShortToast(MyWorkcheckActivity.this, "没有数据");
                electricFanView.setVisibility(View.VISIBLE);

            }
        });

    }

    private void showAdapter(List<WorkCheckListBean> rolebeans) {
        adapter = new MyOrderAdapter(rolebeans);
        lvOrder.setAdapter(adapter);
        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

}
