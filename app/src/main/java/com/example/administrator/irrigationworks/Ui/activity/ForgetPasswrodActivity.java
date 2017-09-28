package com.example.administrator.irrigationworks.Ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 工地巡查列表
 * Created by Administrator on 2017/8/15.
 */
public class ForgetPasswrodActivity extends BaseAppCompatActivity {


    @Bind(R.id.et_newpassword)
    EditText etNewpassword;
    @Bind(R.id.et_surepassword)
    EditText etSurepassword;
    @Bind(R.id.btn_makesure)
    Button btnMakesure;
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //临时解析检查项目


    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }


}
