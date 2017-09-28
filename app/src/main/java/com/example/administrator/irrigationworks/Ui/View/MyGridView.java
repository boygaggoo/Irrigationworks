package com.example.administrator.irrigationworks.Ui.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/9/9.
 */
public class MyGridView extends GridView{
    public MyGridView(Context context) {
        super(context);
    }
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyGridView(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override//ScrollView嵌套GridView
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

