package com.example.administrator.irrigationworks.Ui.adapter;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.activity.DetailImageActivity;
import com.example.administrator.irrigationworks.Ui.uiview.SmartImageView;
import com.example.administrator.irrigationworks.Ui.uiview.TouchImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by YZBbanban on 16/6/14.
 */
public class ViewPagerAdapter extends PagerAdapter  {
    private Context context;
    private List<String> images;
    private ImageView ivSingleimg;
//    private TouchImageView ivSingleimg;
    private int currentPosition;
    private static final String TAG = "ViewPagerAdapter";

    public ViewPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
//        mTask.cancel(true);
    }

    @Override
    public int getCount() {
        if (images != null) {
            return images.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.image_detial_item, null);
        ivSingleimg = (ImageView) view.findViewById(R.id.iv_single_img);

        currentPosition = position;
        String detialImage = images.get(position);
        Log.i(TAG, "instantiateItem: "+detialImage);
        //glide 显示
        Glide.with(context).load(detialImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivSingleimg);
//        ivSingleimg.setImageResource(R.drawable.amap_car);
//        ivSingleimg.setImageUrl(detialImage);



        container.addView(view);

        ivSingleimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetailImageActivity) context).finish();
                ((DetailImageActivity) context).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            }
        });

        return view;
    }


}
