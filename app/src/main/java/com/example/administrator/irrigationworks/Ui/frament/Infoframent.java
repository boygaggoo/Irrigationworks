package com.example.administrator.irrigationworks.Ui.frament;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.Retrofit.ApiUtil;
import com.example.administrator.irrigationworks.Ui.Retrofit.HttpUtils;
import com.example.administrator.irrigationworks.Ui.Retrofit.RequestServes;
import com.example.administrator.irrigationworks.Ui.Retrofit.ResultCode;
import com.example.administrator.irrigationworks.Ui.UserPreferences.MyCache;
import com.example.administrator.irrigationworks.Ui.UserPreferences.Preference;
import com.example.administrator.irrigationworks.Ui.activity.LoginActivity;
import com.example.administrator.irrigationworks.Ui.activity.MyWorkcheckActivity;
import com.example.administrator.irrigationworks.Ui.activity.RegOnlineFaceDemo;
import com.example.administrator.irrigationworks.Ui.activity.RideRouteActivity;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeImageView;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeLinearLayout;
import com.example.administrator.irrigationworks.Ui.bean.ChechWordBean;
import com.example.administrator.irrigationworks.Ui.bean.HoistoricaDataLitepal;
import com.example.administrator.irrigationworks.Ui.bean.LoginOnSuccssBean;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.UtilsTozals.BitmapUtils;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.example.administrator.irrigationworks.UtilsTozals.VolleyLoadPicture;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 综合管理
 * Created by Administrator on 2017/8/10.
 */
public class Infoframent extends TFragment implements View.OnClickListener {
    @Bind(R.id.iv_fragment_mine_head)
    ImageView ivFragmentMineHead;
    @Bind(R.id.person_name)
    TextView personName;
    @Bind(R.id.tv_tele)
    TextView tvTele;
    @Bind(R.id.tv_works)
    TextView tvWorks;


    @Bind(R.id.bga_record_management)
    LinearLayout bgaRecordManagement;
    @Bind(R.id.iv_Leave_management)
    LinearLayout ivLeaveManagement;


    @Bind(R.id.iv_attendance_reminde)
    BGABadgeImageView ivAttendanceReminde;
    @Bind(R.id.tv_attendance_reminde)
    TextView tvAttendanceReminde;
    @Bind(R.id.bga_attendance_reminde)
    BGABadgeLinearLayout bgaAttendanceReminde;
    @Bind(R.id.iv_help_notes)
    BGABadgeImageView ivHelpNotes;
    @Bind(R.id.tv_help_notes)
    TextView tvHelpNotes;
    @Bind(R.id.bga_help_notes)
    BGABadgeLinearLayout bgaHelpNotes;


    @Bind(R.id.bga_log_out)
    LinearLayout bgaLogOut;

    @Bind(R.id.bga_cache_cleaner)
    LinearLayout bgaCacheCleaner;
    @Bind(R.id.hasCheck)
    TextView hasCheck;


    @Bind(R.id.bga_modify_password)
    LinearLayout bgaModifyPassword;
    @Bind(R.id.iv_modify_info)
    BGABadgeImageView ivModifyInfo;

    @Bind(R.id.bga_modify_info)
    BGABadgeLinearLayout bgaModifyInfo;
    @Bind(R.id.iv_site_management)
    BGABadgeImageView ivSiteManagement;
    @Bind(R.id.ll_one)
    LinearLayout llOne;
    @Bind(R.id.ll_two)
    LinearLayout llTwo;
    private Activity ctx;
    private View layout;
    private List<RoleBeanLitepal> schooldates;
    private ProgressDialog mProDialog;
    private String useid;
    public int UPDATE_LISTVIEW = 2;
    private RequestServes apiService;
    private Map<String, Object> hashMap;
    private String pas1, pas2, pas3, pas4, pas5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == null) {
            ctx = this.getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.mine_info_activity,
                    null);
            ButterKnife.bind(this, layout);
            try {
                Boolean net = NetUtil.checkNetWork(getActivity());
                if (net) {
                    if (NimUIKit.getRole().equals(Constants.Patroller)) {

                        llOne.setVisibility(View.GONE);
                        llTwo.setVisibility(View.GONE);
                        bgaRecordManagement.setVisibility(View.GONE);
//                bgaHistoricalInspections.setVisibility(View.GONE);//暂时屏蔽
//                view5.setVisibility(View.GONE);
                        ivLeaveManagement.setVisibility(View.GONE);
                        bgaLogOut.setVisibility(View.GONE);
//            } else if (role.equals(Constants.Construction) || role.equals(Constants.Supervisor)) {
                    }
                    //获取数据库的个人信息
                    getPersonData();
                    Log.d("巡查员", "巡查员" + NimUIKit.getRole());

                    if (schooldates != null) {
                        personName.setText("姓名:" + schooldates.get(0).getRealname());
                        tvTele.setText("电话:" + schooldates.get(0).getPhone());
                        tvWorks.setText("职位:" + schooldates.get(0).getJob());
                        //设置头像
                        topic(schooldates.get(0).getFacepic());
                    }
                    if (NimUIKit.getRole().equals("巡查员")) {
                        //如果是巡查员的话就隐藏考勤
                        getcheckItem();
                    } else {
                        getcheckWord();
                    }


                    bgaRecordManagement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Constants.Patroller.equals(NimUIKit.getRole())) {
                                UtilsTool.showShortToast(getActivity(), "此角色暂不开放此功能");
                                return;
                            }
                            Intent workchech = new Intent(getActivity(), MyWorkcheckActivity.class);
                            startActivity(workchech);
                            Log.d("qqc", "点击了");

//                    ThreadManager.getThreadPool().execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d("qqc","点击了");
//                            HashMap<String, Object> params = new HashMap<String, Object>();
//                            params.put("监理人员到位", "111");
//                            params.put("2","");
//                            params.put("3","");
//                            AJpushUtils.jSend_notification("cec772827bd1c2225d7cafd3","690be88e0857376d165ea625",NimUIKit.getContext()+"","你好吗",params,"59c0c9aecea925374853d651","59c0c9aecea925374853d651");
//                        }
//                    });
                        }
                    });
                    setlister();
                    File mPictureFile = new File(BitmapUtils.PICPATH);
                    try {
                        long size = BitmapUtils.getFileSize(mPictureFile);
//                tvCacheCleaner.setText("清理缓存" + size + "M");
                        Log.d("qq", "此时的图片" + size);
                        Log.d("qq", "此时的File图片" + mPictureFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("qq", "此时的图片故障为" + e.toString());
                    }

                } else {
                    UtilsTool.showShortToast(getActivity(), "没有网络");
                }
            } catch (Exception e) {

            }


        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        ButterKnife.bind(this, layout);
        return layout;

    }

    private void topic(String facepic) {
        VolleyLoadPicture vlp = new VolleyLoadPicture(getActivity(), ivFragmentMineHead);
        vlp.getmImageLoader().get(FinalTozal.host + facepic, vlp.getOne_listener());
    }

    private void getcheckItem() {
        final Map<String, Object> hashMap = new HashMap<String, Object>();
        Map<String, Object> hashMaps = new HashMap<String, Object>();
        hashMap.put("inspector", schooldates.get(0).getRg());
//
        /**
         * 巡查次数
         */
        hashMap.put("token", NimUIKit.getToken());
        hashMap.put("serchValue", hashMaps);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.patrolStatistics, NimUIKit.getGson().toJson(hashMap), new GetPatrolStatistics());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setlister() {
        bgaLogOut.setOnClickListener(this);
        bgaHelpNotes.setOnClickListener(this);
        bgaCacheCleaner.setOnClickListener(this);
        bgaModifyPassword.setOnClickListener(this);
        bgaModifyInfo.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bga_modify_password://修改密码
                final View view = View.inflate(getActivity(), R.layout.myinfo_change_pass, null);


                new AlertDialog.Builder(getActivity()).setTitle("修改密码").setIcon(
                        android.R.drawable.ic_dialog_info).setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        final EditText oldpass = (EditText) view.findViewById(R.id.pass1);
                        final EditText newpass = (EditText) view.findViewById(R.id.pass2);

                        final String oldpass1 = oldpass.getText().toString().trim();
                        final String newpass2 = newpass.getText().toString().trim();
                        if (!TextUtils.isEmpty(oldpass1) && !TextUtils.isEmpty(newpass2)) {
                            final Map<String, Object> hashMap = new HashMap<String, Object>();
                            String username = schooldates.get(0).getUsername();
                            hashMap.put("token", NimUIKit.getToken());
                            hashMap.put("password", oldpass1);
                            hashMap.put("username", username);
                            hashMap.put("newPassword", newpass2);


                            mProDialog = new ProgressDialog(getActivity());
                            mProDialog.setCancelable(true);
                            mProDialog.setTitle("请稍后");
                            mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    // cancel进度框时,取消正在进行的操作

                                }
                            });
                            mProDialog.setMessage("提交中...");
                            mProDialog.show();
                            apiService = ApiUtil.getApiRetrofit().create(RequestServes.class);
                            Call<ResultCode> call = apiService.getChangePassword(hashMap);
                            call.enqueue(new Callback<ResultCode>() {
                                @Override
                                public void onResponse(Response<ResultCode> response, Retrofit retrofit) {
                                    ResultCode body = response.body();

                                    if (body == null) {
                                        UtilsTool.showShortToast(getActivity(), "修改失败");
                                        mProDialog.dismiss();
                                        return;
                                    }
                                    if (body.getCode().equals(Enumerate.LOGINSUCESS)) {
                                        UtilsTool.showShortToast(getActivity(), "修改成功,请重新登录");
                                        mProDialog.dismiss();
                                        //清理数据库
                                        MyCache.clear();
                                        //取消自动登录
                                        Preference.saveAutoLogin(false);
                                        //设置关闭hanlde
                                        NimUIKit.setHandlestop(false);
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        //这句必须要加
                                        Process.killProcess(Process.myPid());
                                        //关闭整个程序
                                        System.exit(0);
                                        getActivity().finish();

                                    } else {
                                        UtilsTool.showShortToast(getActivity(), "修改失败");
                                        mProDialog.dismiss();
                                    }
                                    Log.d("此时的个人信息", "此时的个人信息" + body);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    UtilsTool.showShortToast(getActivity(), "修改失败");
                                    mProDialog.dismiss();
                                }
                            });
//                            Message msg = new Message();
//                            msg.what = UPDATE_LISTVIEW;
//                            Bundle data = new Bundle();
//                            data.putString("value", NimUIKit.getGson().toJson(hashMap));
//                            msg.setData(data);
//                            handler.sendMessage(msg);

                        } else {
                            Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).setNegativeButton("取消", null).show();

                break;
            case R.id.bga_modify_info://修改个人信息

//                Intent intentinfo = new Intent(getActivity(), SetpasswordActivity.class);
//                startActivity(intentinfo);

                final View views = View.inflate(getActivity(), R.layout.job_info, null);
                final EditText p1 = (EditText) views.findViewById(R.id.pass1);
                final EditText p2 = (EditText) views.findViewById(R.id.pass2);
                final EditText p3 = (EditText) views.findViewById(R.id.pass3);
                final EditText p4 = (EditText) views.findViewById(R.id.pass4);
                final EditText p5 = (EditText) views.findViewById(R.id.pass5);
                useid = "";

                if (schooldates != null) {
                    p1.setText(schooldates.get(0).getUsername());
                    p2.setText(schooldates.get(0).getInstitution());
                    p3.setText(schooldates.get(0).getRole());
                    p4.setText(schooldates.get(0).getIdentification());
                    p5.setText(schooldates.get(0).getRealname());
                    useid = schooldates.get(0).getUserid();
                }
                pas1 = p1.getText().toString().trim();
                pas2 = p2.getText().toString().trim();
                pas3 = p3.getText().toString().trim();
                pas4 = p4.getText().toString().trim();
                pas5 = p5.getText().toString().trim();
                p1.setEnabled(false);
                p2.setEnabled(false);
                p3.setEnabled(false);
                p4.setEnabled(false);

                new AlertDialog.Builder(getActivity()).setTitle("修改个人信息").setIcon(
                        android.R.drawable.ic_dialog_info).setView(views).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


                        if (!TextUtils.isEmpty(pas1) && !TextUtils.isEmpty(pas2) && !TextUtils.isEmpty(pas3) &&
                                !TextUtils.isEmpty(pas4) && !TextUtils.isEmpty(pas5)) {
                            mProDialog = new ProgressDialog(getActivity());
                            mProDialog.setCancelable(true);
                            mProDialog.setTitle("请稍后");
                            mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    // cancel进度框时,取消正在进行的操作

                                }
                            });
                            mProDialog.setMessage("提交中...");
                            mProDialog.show();
                            pas1 = p1.getText().toString().trim();
                            pas2 = p2.getText().toString().trim();
                            pas3 = p3.getText().toString().trim();
                            pas4 = p4.getText().toString().trim();
                            pas5 = p5.getText().toString().trim();
                            hashMap = new HashMap<String, Object>();
//
                            hashMap.put("token", NimUIKit.getToken());
                            hashMap.put("userid", useid);
                            hashMap.put("username", pas1);
                            hashMap.put("institution", pas2);
                            hashMap.put("role", pas3);
                            hashMap.put("identification", pas4);
                            hashMap.put("realname", pas5);

                            try {
                                Log.d("HttpUtils", "HttpUtils" + hashMap);

                                HttpUtils.doPostAsyn(FinalTozal.useredit, NimUIKit.getGson().toJson(hashMap), new GetUseredit());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


//
                        } else {
                            Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).setNegativeButton("取消", null).show();
                break;
            //这个是点击人脸注册的
            case R.id.bga_log_out:
                if (Constants.Patroller.equals(NimUIKit.getRole())) {
                    UtilsTool.showShortToast(getActivity(), "此角色暂不开放此功能");
                    return;
                }

                Intent intentFace = new Intent(getActivity(), RegOnlineFaceDemo.class);
                startActivity(intentFace);
//                ThreadManager.getThreadPool().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("qqc","点击了");
//                        HashMap<String, Object> params = new HashMap<String, Object>();
//                        params.put("监理人员到位", "111");
//                        params.put("2","");
//                        params.put("3","");
//                        AJpushUtils.jSend_notification("cec772827bd1c2225d7cafd3","690be88e0857376d165ea625",NimUIKit.getContext()+"","巡查员巡查了你的工地"+"结果:",params,"59c61de83ceeb578e6a011cb","59c5f3383ceeb578e6a011b7");
//                    }
//                });

                break;

            case R.id.bga_cache_cleaner:
                //清理缓存
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认清理缓存吗？");
                builder.setTitle("清理缓存");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //清理注册时拍照的图片
                        BitmapUtils.delePicteDir();
                        BitmapUtils.deleCutDir();
                        BitmapUtils.deleCutOneDir();
                        UtilsTool.showShortToast(getActivity(), "已处理");
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.bga_help_notes://位置列表导航
                Intent intentnotes = new Intent(getActivity(), RideRouteActivity.class);
                startActivity(intentnotes);
                break;
            case R.id.iv_Leave_management://请假
                UtilsTool.showShortToast(getActivity(), "此功能暂不开放");
                break;
            default:
                break;
        }
    }

    /**
     * 接受图片返回的结果
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 2:
                    Bundle data = msg.getData();
                    getData(data);
                    break;


            }

        }
    };

    private void getData(Bundle data) {
        final String val = data.getString("value");
        if (!TextUtils.isEmpty(val)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpUtils.doPostAsyn(FinalTozal.user_edit, val, new getInfo());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        //进行解析
    }

    public void getPersonData() {
        ivLeaveManagement.setOnClickListener(this);
        schooldates = DataSupport.findAll(RoleBeanLitepal.class);
    }

    public void getcheckWord() {
        final Map<String, Object> hashMap = new HashMap<String, Object>();
//
        /**
         * 参数为手机当前定位位置
         */
        hashMap.put("token", NimUIKit.getToken());


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.doPostAsyn(FinalTozal.checkworkStatistic, NimUIKit.getGson().toJson(hashMap), new GetTakList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class GetTakList implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                final ChechWordBean chechWordBean = NimUIKit.getGson().fromJson(loginOnSuccssBean.getResult(), ChechWordBean.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String str = String.valueOf(chechWordBean.getHasCheck());//浮点变量a转换为字符串str
                        int idx = str.lastIndexOf(".");//查找小数点的位置
                        String strNum = str.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
                        int num = Integer.valueOf(strNum);//把整数部分通过Integer.valueof方法转换为数字

                        String noCheck = String.valueOf(chechWordBean.getNoCheck());//浮点变量a转换为字符串str
                        int idxnoCheck = noCheck.lastIndexOf(".");//查找小数点的位置
                        String strNumnoCheck = noCheck.substring(0, idxnoCheck);//截取从字符串开始到小数点位置的字符串，就是整数部分
                        int numnoCheck = Integer.valueOf(strNumnoCheck);//把整数部分通过Integer.valueof方法转换为数字
                        hasCheck.setText("当月考勤次数:" + num + "次");
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hasCheck.setText("暂无考勤");
                    }
                });
            }
        }
    }

    private class getInfo implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("此时的个人信息", "此时的个人信息" + result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(getActivity(), "修改成功");
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(getActivity(), "修改失败");
                    }
                });
            }
        }
    }

    private class GetUseredit implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("qc", "修改成功了" + result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(getActivity(), "修改成功");
                        mProDialog.dismiss();
                    }
                });


            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UtilsTool.showShortToast(getActivity(), "修改成功");
                        mProDialog.dismiss();
                    }
                });

                UtilsTool.showShortToast(getActivity(), "修改失败");
                mProDialog.dismiss();

            }
        }
    }

    private class GetPatrolStatistics implements HttpUtils.CallBack {
        @Override
        public void onRequestComplete(String result) {
            Log.d("巡查", "巡查" + result);
            LoginOnSuccssBean loginOnSuccssBean = JSON.parseObject(result, LoginOnSuccssBean.class);
            if (loginOnSuccssBean.getCode().equals(Enumerate.LOGINSUCESS)) {
                if (loginOnSuccssBean.getResult().equals("[]")) {

                    return;
                }
                String reus = loginOnSuccssBean.getResult().replace("_id", "rg");
                LoginOnSuccssBean loginOnSuccssBeans = JSON.parseObject(reus, LoginOnSuccssBean.class);
                final List<HoistoricaDataLitepal> poistoricaDataLitepal = JSON.parseArray(loginOnSuccssBeans.getResult(), HoistoricaDataLitepal.class);
                Log.d("历史巡查1", "历史巡查1" + poistoricaDataLitepal.size());
//


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hasCheck.setText("当月巡查次数:" + poistoricaDataLitepal.size() + "次");
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hasCheck.setText("当月暂无巡查");
                    }
                });
            }
        }
    }
}
