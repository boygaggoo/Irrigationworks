package com.example.administrator.irrigationworks.Ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/11.
 */
public class InfoDataActvity extends BaseAppCompatActivity implements View.OnClickListener{
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.sendBt)
    TextView sendBt;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_bank)
    EditText etBank;
    @Bind(R.id.ll_bank)
    LinearLayout llBank;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.ll_name)
    LinearLayout llName;
    @Bind(R.id.et_account)
    EditText etAccount;
    @Bind(R.id.et_money)
    EditText etMoney;
    @Bind(R.id.iv_del)
    ImageView ivDel;
    @Bind(R.id.tv_overstep)
    TextView tvOverstep;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.ll_balance)
    LinearLayout llBalance;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
private String date;
private String report_context;
    private InspectorDataLitepal constructor;//施工方
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        sendBt.setVisibility(View.GONE);
        date=getIntent().getStringExtra("data");
        report_context=getIntent().getStringExtra("report_content");
        Log.d("qqq","constructor"+date);
        constructor= JSON.parseObject(date,InspectorDataLitepal.class);
        setdata();
        Log.d("qqq","constructor"+constructor);
    }

    private void setdata() {
        etBank.setText(constructor.getRealname());
        etName.setText(constructor.getInstitution());
        etAccount.setText(constructor.getPhone());
        etMoney.setText(report_context+"");
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit://提交审核
                break;

        }
    }
}
