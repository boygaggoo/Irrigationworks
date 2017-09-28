package com.example.administrator.irrigationworks.Ui.takePhone.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.administrator.irrigationworks.Ui.takePhone.tackphonView.PinchImageView;

import java.util.List;

public class PreviewImageAdapter extends PagerAdapter {
    private List<String> mDatas;
    private Context mContext;
    private OnFinishCallBackListener mOnFinishCallBackListener;
    private LayoutInflater mInflater;

    public PreviewImageAdapter(Context c) {
        mContext=c;
        mInflater=LayoutInflater.from(mContext);
    }

    public void setOnFinishCallBackListener(OnFinishCallBackListener listener){
        mOnFinishCallBackListener=listener;
    }

    public void setDatas(List<String> datas){
        this.mDatas=datas;
    }

    @Override
    public int getCount() {
        return mDatas!=null?mDatas.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PinchImageView imageView= new PinchImageView(mContext);
        LinearLayout.LayoutParams imageParams= new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT,
                ViewPager.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageParams);
        //imageView.setImageResource(R.mipmap.ic_launcher);
        Glide.with(mContext).load(mDatas.get(position)).into(imageView);
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnFinishCallBackListener.finishPreview();
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface  OnFinishCallBackListener{
        void finishPreview();
    }
}
