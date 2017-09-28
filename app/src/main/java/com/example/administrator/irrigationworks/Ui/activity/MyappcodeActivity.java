package com.example.administrator.irrigationworks.Ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 扫描二维码下载
 * Created by Administrator on 2017/9/22.
 */
public class MyappcodeActivity extends BaseAppCompatActivity {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_head_image)
    ImageView ivHeadImage;
    @Bind(R.id.tv_nike)
    TextView tvNike;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.iv_code)
    ImageView ivCode;

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
        toolbarTitle.setText("扫描二维码下载");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qrcode;
    }
}
