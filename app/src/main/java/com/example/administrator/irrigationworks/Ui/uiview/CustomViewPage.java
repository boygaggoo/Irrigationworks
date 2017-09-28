package com.example.administrator.irrigationworks.Ui.uiview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class CustomViewPage extends ViewPager {
    private boolean isCanScroll = true;
    public CustomViewPage(Context context) {
        super(context);
    }
    public CustomViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        if (isCanScroll) {
            super.scrollTo(x, y);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (isCanScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}
