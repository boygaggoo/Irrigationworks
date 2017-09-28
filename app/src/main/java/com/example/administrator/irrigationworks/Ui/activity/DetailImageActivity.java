package com.example.administrator.irrigationworks.Ui.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailImageActivity extends AppCompatActivity {
    private static final String TAG = "DetailImageActivity";
    @Bind(R.id.pager)
    ViewPager viewPager;
    private int currentPosition;
    private int position;
    private ArrayList<String> images;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        ButterKnife.bind(this);
        initData();
        setListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
         images= NimUIKit.urlList;
        //TODO
        Log.i(TAG, "initData: "+images);
        adapter = new ViewPagerAdapter(this,images);
        viewPager.setAdapter(adapter);
        position = getIntent().getIntExtra("image_position", 0);
        Log.i(TAG, "initData: "+position);
        //viewPager.setCurrentItem(currentPosition);
        viewPager.setCurrentItem(position);
        // adapter.setPositon(position);


    }
    private void setListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
