package com.example.administrator.irrigationworks.Ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.example.administrator.irrigationworks.UtilsTozals.WaterMaskUtil;
import com.example.administrator.irrigationworks.UtilsTozals.WaterMaskView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 图片水印测试类
 * Created by Administrator on 2017/9/20.
 */
public class TextViewActivity extends BaseAppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.wartermark_pic)
    ImageView ivWaterMark;
    @Bind(R.id.rb_wartermark_lefttop)
    RadioButton rbWartermarkLefttop;
    @Bind(R.id.rb_wartermark_righttop)
    RadioButton rbWartermarkRighttop;
    @Bind(R.id.rb_wartermark_rightbottom)
    RadioButton rbWartermarkRightbottom;
    @Bind(R.id.rb_wartermark_leftbottom)
    RadioButton rbWartermarkLeftbottom;
    @Bind(R.id.rg_wartermark)
    RadioGroup rgWaterMark;


    private WaterMaskView waterMaskView;
    private Bitmap sourBitmap;
    private Bitmap waterBitmap;
    private Bitmap watermarkBitmap;

    private final static int LEFT_TOP=0;
    private final static int RIGHT_TOP=1;
    private final static int RIGHT_BOTTOM=2;
    private final static int LEFT_BOTTOM=3;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (sourBitmap != null) {
            sourBitmap.recycle();
            sourBitmap = null;
        }
        if (waterBitmap != null) {
            waterBitmap.recycle();
            waterBitmap = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        rgWaterMark.setOnCheckedChangeListener(this);

        sourBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_03);
        Boolean net = NetUtil.checkNetWork(TextViewActivity.this);
        if (net) {
//          waterMaskView = new WaterMaskView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            waterMaskView.setLayoutParams(params);
            waterMaskView.setCompanyText("XXXXXX公司");
            waterMaskView.setInfoText("这是相关信息1这是相关信息2这是相关信息3这是相关信息4这是相关信息5");

            //默认设置水印位置在左下
            saveWaterMask(LEFT_BOTTOM);


        } else {
            UtilsTool.showShortToast(TextViewActivity.this, "没有网络");
        }

    }

    private void saveWaterMask(int leftBottom) {
        waterBitmap = WaterMaskUtil.convertViewToBitmap(waterMaskView);

        //根据原图处理要生成的水印的宽高
        float width = sourBitmap.getWidth();
        float height = sourBitmap.getHeight();
        float be = width / height;

        if ((float) 16 / 9 >= be && be >= (float) 4 / 3) {
            //在图片比例区间内16;9~4：3内，将生成的水印bitmap设置为原图宽高各自的1/5
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) width / 5, (int) height / 5);
        } else if (be > (float) 16 / 9) {
            //生成4：3的水印
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) width / 5, (int) width*3 / 20);
        } else if (be < (float) 4 / 3) {
            //生成4：3的水印
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) height*4 / 15, (int) height / 5);
        }

        switch (leftBottom) {
            case LEFT_TOP:
                watermarkBitmap = WaterMaskUtil.createWaterMaskLeftTop(this, sourBitmap, waterBitmap, 0, 0);
                break;
            case RIGHT_TOP:
                watermarkBitmap = WaterMaskUtil.createWaterMaskRightTop(this, sourBitmap, waterBitmap, 0, 0);
                break;
            case RIGHT_BOTTOM:
                watermarkBitmap = WaterMaskUtil.createWaterMaskRightBottom(this, sourBitmap, waterBitmap, 0, 0);
                break;
            case LEFT_BOTTOM:
                watermarkBitmap = WaterMaskUtil.createWaterMaskLeftBottom(this, sourBitmap, waterBitmap, 0, 0);
                break;
        }
        ivWaterMark.setImageBitmap(watermarkBitmap);
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i){
            case R.id.rb_wartermark_lefttop:
                saveWaterMask(LEFT_TOP);
                break;
            case R.id.rb_wartermark_righttop:
                saveWaterMask(RIGHT_TOP);
                break;
            case R.id.rb_wartermark_rightbottom:
                saveWaterMask(RIGHT_BOTTOM);
                break;
            case R.id.rb_wartermark_leftbottom:
                saveWaterMask(LEFT_BOTTOM);
                break;
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_text_shuiyin;
    }
}
