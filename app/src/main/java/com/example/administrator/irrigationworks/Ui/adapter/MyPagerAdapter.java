package com.example.administrator.irrigationworks.Ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

	private List<ImageView> imageViews;

	public MyPagerAdapter(List<ImageView> list) {
		super();
		imageViews = list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		// ��Ϊʵ��ֻ�м���ҳ�浫������Ҫ����ѭ��������ȡģ�������ǰ���ǵڼ���ҳ��
		int i = position % imageViews.size();
		// Ԥ����ֵ
		position = Math.abs(i);
		ImageView imageView = imageViews.get(position);
		ViewParent parent = imageView.getParent();
		// remove��View֮ǰ�Ѿ��ӵ�һ�����ؼ��У������쳣
		if (parent != null) {
			ViewGroup group = (ViewGroup) parent;
			group.removeView(imageView);
		}
		container.addView(imageView);
		return imageView;
	}
}
