package com.example.administrator.irrigationworks.Ui.takePhone.adapter;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.takePhone.activity.PreviewImageActivity;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;


public class IssuesGridViewAdapter extends BaseAdapter {
    protected ArrayList<String> mDatas;
    private Context mContext;
    protected LayoutInflater mInflater;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_IMAGE =2 ;
    private DeleteImageClickListener mDeleteImageClickListener;

    public IssuesGridViewAdapter(Context c) {
        mContext=c;
        mInflater= LayoutInflater.from(mContext);
    }

    public void setDeleteImageClickListenner(DeleteImageClickListener listener){
        mDeleteImageClickListener=listener;
    }

    public void setDatas(ArrayList<String> datas){
        mDatas=datas;
    }

    @Override
    public int getCount() {
        return mDatas!=null?mDatas.size()+1:1;
    }

    class ViewHolder{
        ImageView issuesIv;
        ImageView deleteIv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder= new ViewHolder();
            convertView=mInflater.inflate(R.layout.item_photo_issues,parent,false);
            holder.issuesIv = (ImageView) convertView.findViewById(R.id.issuesIv);
            holder.deleteIv = (ImageView) convertView.findViewById(R.id.deleteIv);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Log.d("qc","点击了"+position);
        holder.issuesIv.setClickable(false);
        holder.deleteIv.setClickable(false);
        if(position==getCount()-1){
            holder.issuesIv.setClickable(true);
            Glide.with(mContext).load(R.drawable.addition).into(holder.issuesIv);
            holder.deleteIv.setVisibility(View.GONE);
            holder.issuesIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("qc","点击了issuesIv"+position);
                    showPhotoSelect();
                }
            });
            if (getCount()>=10){

                holder.issuesIv.setVisibility(View.GONE);

            }
        }else{
            holder.deleteIv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mDatas.get(position)).into(holder.issuesIv);
            holder.issuesIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("qc","点击了issuesIv"+position);
                    Intent intent = new Intent(mContext, PreviewImageActivity.class);
                    intent.putExtra("index",position);
                    intent.putStringArrayListExtra("imagesUrlBean",mDatas);
                    intent.putExtra("isNetworkLoading",false);//(图片预览) 判断是否从网络加载还是本地加载
                    mContext.startActivity(intent);
                    ((Activity)mContext).overridePendingTransition(R.anim.activity_zoom_open, 0);
                }
            });
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeleteImageClickListener.DeleteImageClickI(position);
                }
            });
        }

        return convertView;
    }

    public void showPhotoSelect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    mContext.getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            int maxNum = 9;
            MultiImageSelector selector = MultiImageSelector.create(mContext);
            selector.showCamera(true);
            selector.count(maxNum);
            selector.multi();
            selector.origin(mDatas);
            selector.start((Activity) mContext, REQUEST_IMAGE);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext, permission)){
            new AlertDialog.Builder(mContext)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)mContext, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        }else{
            ActivityCompat.requestPermissions((Activity)mContext, new String[]{permission}, requestCode);
        }
    }



    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public interface DeleteImageClickListener {
        void DeleteImageClickI(int position);
    }

}
