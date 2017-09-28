package com.example.administrator.irrigationworks.Ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2017/3/22.
 */
public abstract  class BaseAppCompatActivity extends AppCompatActivity {
    private static final String TAG = BaseAppCompatActivity.class.getSimpleName();
    private TextView mToolbarTitle;
    private Toolbar mToolbar;
    private ImageView iv_retrun,iv_add;
    private Context contextFinsh;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//

        setContentView(getLayoutId());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_retrun= (ImageView) findViewById(R.id.iv_back_left);
        iv_add= (ImageView) findViewById(R.id.iv_add);
       /*
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub Title");
        */
        iv_retrun = (ImageView) findViewById(R.id.iv_back_left);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            //设置默认的标题不显示
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if(null != getToolbar() && isShowBacking()){
            showBack();
        }
    }
    /**
     * 获取头部标题的TextView
     * @return
     */
    public TextView getToolbarTitle(){
        return mToolbarTitle;
    }
    /**
     * 获取返回键
     * @return
     */
    public ImageView getSubImage(){
        return iv_retrun;
    }
    /**
     * 获取等多
     * @return
     */
    public ImageView getSubMore(){
        return iv_add;
    }


    /**
     * 设置头部标题
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if(mToolbarTitle != null){
            mToolbarTitle.setText(title);
        }else{
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }
    /**
     * this Activity of tool bar.
     * 获取头部.
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }
    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack(){
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
//        getToolbar().setNavigationIcon(R.mipmap.nav_menu_fanhui);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     * @return
     */
    protected boolean isShowBacking(){
        return true;
    }

    /**
     * this ui.activity layout res
     * 设置layout布局,在子类重写该方法.
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy...");


    }



}
