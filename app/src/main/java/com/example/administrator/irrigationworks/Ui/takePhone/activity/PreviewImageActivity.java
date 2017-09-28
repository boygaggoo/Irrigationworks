package com.example.administrator.irrigationworks.Ui.takePhone.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.takePhone.adapter.PreviewImageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片预览
 */
public class PreviewImageActivity extends FragmentActivity implements PreviewImageAdapter.OnFinishCallBackListener {

    private int mCurPageIndex;
    private int mPageCount;
    private ViewPager mViewPager;
    private LinearLayout mPageDotLl;
    private boolean isNetworkLoading;
    private String mCurrentImageUrl;
    private Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_previewimage);
        initUI();
        initData();
    }

    private void initData() {
        mIntent = getIntent();
        isNetworkLoading = mIntent.getBooleanExtra("isNetworkLoading", false);
        if (isNetworkLoading){
            loadNetWork();
        }else{
            loadPath();
        }

    }

    private void loadPath() {
        List<String> photoUrlBeanList = mIntent.getStringArrayListExtra("imagesUrlBean");
        int index = mIntent.getIntExtra("index", -1);
        mCurrentImageUrl= photoUrlBeanList.get(index);
        mPageCount=photoUrlBeanList.size();
        mCurPageIndex=getCurrentIndex(photoUrlBeanList);
        PreviewImageAdapter adapter= new PreviewImageAdapter(this);
        adapter.setOnFinishCallBackListener(this);
        adapter.setDatas(photoUrlBeanList);
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mCurPageIndex);
        setOvalLayout();
    }

    private void loadNetWork() {
        List<String> photoUrlBeanList = mIntent.getStringArrayListExtra("imagesUrlBean");
        List<String> newPhotoUrlList=new ArrayList<>();

        for (String imageUtlBean:photoUrlBeanList){
            //加载网络图片
            String imageUrl = FinalTozal.SITE_INSPECTION_PIC + imageUtlBean;
            newPhotoUrlList.add(imageUrl);
        }
        int index = mIntent.getIntExtra("index", -1);
        mCurrentImageUrl= newPhotoUrlList.get(index);
        mPageCount=newPhotoUrlList.size();
        mCurPageIndex=getCurrentIndex(newPhotoUrlList);

        PreviewImageAdapter adapter= new PreviewImageAdapter(this);
        adapter.setOnFinishCallBackListener(this);
        adapter.setDatas(newPhotoUrlList);
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mCurPageIndex);
        setOvalLayout();
    }

    private void setOvalLayout() {
        if (mPageCount<=0){
            return;
        }
        mPageDotLl.removeAllViews();
        for (int i=0;i<mPageCount;i++){
            mPageDotLl.addView(LayoutInflater.from(this).inflate(R.layout.vp_dot, mPageDotLl, false));
        }
        mPageDotLl.getChildAt(mCurPageIndex).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.shape_circle_white);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 取消圆点选中
                mPageDotLl.getChildAt(mCurPageIndex).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.shape_circle_gray);
                // 圆点选中
                mPageDotLl.getChildAt(position).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.shape_circle_white);

                mCurPageIndex = position;

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private int getCurrentIndex(List<String> list) {
        return list.indexOf(mCurrentImageUrl);
    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.show_origin_pic_vp);
        mPageDotLl = (LinearLayout) findViewById(R.id.show_origin_pic_dot);
    }

    @Override
    public void finishPreview() {
        finish();
        overridePendingTransition(0, R.anim.activity_zoom_close);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finishPreview();
        return super.onKeyDown(keyCode, event);
    }
}


