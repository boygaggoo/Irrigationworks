package com.example.administrator.irrigationworks.Ui.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by GYKJ on 2017/9/25.
 */
public class MapContainer  extends FrameLayout {
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);//拦截父类事件

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);
    }

    public MapContainer(Context context) {
        super(context);
    }

    public MapContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
