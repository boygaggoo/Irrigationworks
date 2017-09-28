package com.example.administrator.irrigationworks.Ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.View.MyGridView;
import com.example.administrator.irrigationworks.Ui.takePhone.adapter.IssuesGridViewAdapter;
import com.example.administrator.irrigationworks.UtilsTozals.FaceUtil;
import com.example.administrator.irrigationworks.UtilsTozals.FileUtilcll;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.utils.ImageUtils;

/**
 * 巡查图册
 * Created by GYKJ on 2017/9/10.DeleteImageClickI
 */
public class GridViewActivity extends BaseAppCompatActivity implements
        IssuesGridViewAdapter.DeleteImageClickListener, View.OnClickListener {
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.sendBt)
    TextView sendBt;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gridView)
    GridView mGridView;

    //初始化图片预览
    private IssuesGridViewAdapter mGridViewAdapter;
    private ArrayList<String> mImagePathDatas;
    private ArrayList<String> comprespic;
    private Bitmap mImage = null;
    private byte[] mImageData = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        ivBackLeft.setOnClickListener(this);
        sendBt.setOnClickListener(this);
        sendBt.setText("确定");
        mImagePathDatas=new ArrayList<>();
        comprespic=new ArrayList<>();

        inUi();
    }

    private void inUi() {
        //初始化图片预览
        mGridViewAdapter = new IssuesGridViewAdapter(this);
        mGridViewAdapter.setDeleteImageClickListenner(this);
        mGridView.setAdapter(mGridViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gridview_activity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片预览
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                mImagePathDatas = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                Log.d("qc", "此时的ic" + mImagePathDatas);

                mGridViewAdapter.setDatas(mImagePathDatas);
                mGridViewAdapter.notifyDataSetChanged();
                for(int i=0;i<mImagePathDatas.size();i++){
                   Bitmap map= ImageUtils.getSmallBitmap(mImagePathDatas.get(i));
                    String  urlpath = FileUtilcll.saveFile(GridViewActivity.this, (System.currentTimeMillis()+1)+".jpg", map);
                    Log.d("qc", "comprespic" + urlpath);
                    if(comprespic!=null){
                        comprespic.clear();
                        comprespic.add(urlpath);
                    }
//                    comprespic.add(ImageUtils.bitmapToString(mImagePathDatas.get(i)));


                }

            }
        }


    }

    @Override
    public void DeleteImageClickI(int position) {
        mImagePathDatas.remove(position);
        mGridViewAdapter.setDatas(mImagePathDatas);
        mGridViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_left:

                finish(); //结束当前的activity的生命周期
                break;
            case R.id.sendBt:
                Intent intents = new Intent();
                intents.putStringArrayListExtra("select_image", comprespic);
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(2, intents);

                finish(); //结束当前的activity的生命周期
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {



        finish(); //结束当前的activity的生命周期
        return super.onKeyDown(keyCode, event);
    }
}
