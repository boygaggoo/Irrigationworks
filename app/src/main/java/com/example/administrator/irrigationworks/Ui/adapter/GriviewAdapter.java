package com.example.administrator.irrigationworks.Ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.bean.WorkChechDetailListBean;
import com.example.administrator.irrigationworks.Ui.bean.WorkCheckListBean;
import com.example.administrator.irrigationworks.Ui.uiview.SmartImageView;
import com.example.administrator.irrigationworks.UtilsTozals.VolleyLoadPicture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片画册
 * Created by YZBbanban on 2017/9/1.
 */

public class GriviewAdapter extends BaseAdapter {
    private List<String>  mDatas;
    private Context mcontext;

    public GriviewAdapter(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    public void setDatas(Context context, List<String> beans) {
        this.mcontext = context;
        mDatas = beans;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public WorkChechDetailListBean getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        public SmartImageView iv_order_img;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 初始化布局
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_photo_grivie, null);
            // 初始化子控件
            holder = new ViewHolder();
            holder.iv_order_img = (SmartImageView) convertView.findViewById(R.id.issuesIv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.d("工地图册", "mDatas.get(position)" + mDatas.get(position));
//        holder.iv_order_img.setImageUrl(mDatas.get(position));
/**
 * InnerTask task; // 判断Map中是否存在匹配的任务 task = tasks.get(convertView);
 * if(task != null) { task.cancel(true); task = null; } // 创建当前所需要执行的任务
 * task = new InnerTask(pic, holder.ivImage); tasks.put(convertView,
 * task); task.execute();
 *
 */
        // task = new InnerTask(pic, holder.ivImage);
        // task.execute();
        // return convertView;
        // 判断Map中是否存在匹配的任务
        //用Volley加载图片
        VolleyLoadPicture vlp = new VolleyLoadPicture(mcontext, holder.iv_order_img);
        vlp.getmImageLoader().get(mDatas.get(position), vlp.getOne_listener());
//        InnerTask task = tasks.get(convertView);
//        if (task != null) {
//            task.cancel(true);
//            task = null;
//        }
//        // 创建当前所需要执行的任务
//        task = new InnerTask(mDatas.get(position),holder.iv_order_img);
//        tasks.put(convertView, task);
//        task.execute();

        return convertView;
    }

    private Map<View, InnerTask> tasks = new HashMap<View, InnerTask>();

    private class InnerTask extends AsyncTask<Void, Void, Bitmap> {
        private String pic;
        private ImageView imageview;

        public InnerTask(String pic, ImageView imageview) {
            super();
            this.pic = pic;
            this.imageview = imageview;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // 计算缩放比例


                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 1;
                Bitmap bm = BitmapFactory.decodeFile(pic, opts);




            return bm;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // 该方法是运行在主线程的方法，在doInBackground()方法执行完成之后被调用
            imageview.setImageBitmap(result);
        }

    }
}
