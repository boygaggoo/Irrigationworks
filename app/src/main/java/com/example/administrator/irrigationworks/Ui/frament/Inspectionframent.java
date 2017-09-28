package com.example.administrator.irrigationworks.Ui.frament;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.irrigationworks.Factory.Constants;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.activity.BuildatlasActivity;
import com.example.administrator.irrigationworks.Ui.activity.HistoricalinspectionsActivity;
import com.example.administrator.irrigationworks.Ui.activity.TeanActivity;
import com.example.administrator.irrigationworks.Ui.activity.buildingPicSiteActivity;
import com.example.administrator.irrigationworks.Ui.activity.buildingSiteActivity;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeImageView;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeLinearLayout;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 巡查模块
 * Created by Administrator on 2017/8/10.
 */
public class Inspectionframent extends TFragment implements View.OnClickListener {

    @Bind(R.id.city)
    TextView city;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.humidity)
    TextView humidity;
    @Bind(R.id.currentTemperature)
    TextView currentTemperature;
    @Bind(R.id.pm2_5_quality)
    TextView pm25Quality;
    @Bind(R.id.pm2_5_content)
    LinearLayout pm25Content;
    @Bind(R.id.info)
    RelativeLayout info;
    @Bind(R.id.weather_today)
    RelativeLayout weatherToday;
    @Bind(R.id.iv_record_management)
    BGABadgeImageView ivRecordManagement;
    @Bind(R.id.bga_record_management)
    BGABadgeLinearLayout bgaRecordManagement;
    @Bind(R.id.iv_Leave_management)
    BGABadgeImageView ivLeaveManagement;
    @Bind(R.id.bga_Leave_management)
    BGABadgeLinearLayout bgaLeaveManagement;
    @Bind(R.id.iv_attendance_reminde)
    BGABadgeImageView ivAttendanceReminde;
    @Bind(R.id.bga_attendance_reminde)
    BGABadgeLinearLayout bgaAttendanceReminde;
    @Bind(R.id.ivHiddenDangerWarning)
    BGABadgeImageView ivHiddenDangerWarning;
    @Bind(R.id.bga_hidden_danger_warning)
    BGABadgeLinearLayout bgaHiddenDangerWarning;
    @Bind(R.id.iv_historical_inspections)
    BGABadgeImageView ivHistoricalInspections;
    @Bind(R.id.bga_historical_inspections)
    BGABadgeLinearLayout bgaHistoricalInspections;
    @Bind(R.id.iv_hazard_list)
    BGABadgeImageView ivHazardList;
    @Bind(R.id.bga_hazard_list)
    BGABadgeLinearLayout bgaHazardList;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.view2)
    View view2;
    @Bind(R.id.view3)
    View view3;
    @Bind(R.id.view4)
    View view4;
    @Bind(R.id.view5)
    View view5;
    @Bind(R.id.view6)
    View view6;
    @Bind(R.id.iv_construction_site)
    BGABadgeImageView ivConstructionSite;
    @Bind(R.id.bga_construction_site)
    BGABadgeLinearLayout bgaConstructionSite;
    @Bind(R.id.view7)
    View view7;
    @Bind(R.id.iv_view_album)
    BGABadgeImageView ivViewAlbum;
    @Bind(R.id.bga_view_album)
    BGABadgeLinearLayout bgaViewAlbum;
    @Bind(R.id.view8)
    View view8;
    @Bind(R.id.iv_site_management)
    BGABadgeImageView ivSiteManagement;
    @Bind(R.id.bga_rsite_management)
    BGABadgeLinearLayout bgaRsiteManagement;
    @Bind(R.id.view0)
    View view0;
    private Activity ctx;
    private View layout;
    private String role;
    private List<RoleBeanLitepal> personDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (layout == null) {
            ctx = this.getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.frament_inspection,
                    null);
            ButterKnife.bind(this, layout);
            try {
                personDate = DataSupport.findAll(RoleBeanLitepal.class);
                role = personDate.get(0).getRole();
                if (role.equals(Constants.Patroller)) {
                    bgaHiddenDangerWarning.setVisibility(View.GONE);
                    bgaRsiteManagement.setVisibility(View.GONE);
//                bgaHistoricalInspections.setVisibility(View.GONE);//暂时屏蔽
//                view5.setVisibility(View.GONE);
                    view4.setVisibility(View.GONE);
                    view0.setVisibility(View.GONE);
//            } else if (role.equals(Constants.Construction) || role.equals(Constants.Supervisor)) {
                } else  {
                    bgaRecordManagement.setVisibility(View.GONE);

                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    view6.setVisibility(View.GONE);
                    bgaLeaveManagement.setVisibility(View.GONE);
                    bgaAttendanceReminde.setVisibility(View.GONE);
                    bgaHazardList.setVisibility(View.GONE);
                    bgaHiddenDangerWarning.setVisibility(View.VISIBLE);
                }

                setlisten();
                //临时
            }catch (Exception e){

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


    private void setlisten() {
        bgaRecordManagement.setOnClickListener(this);
        bgaHiddenDangerWarning.setOnClickListener(this);
        bgaHistoricalInspections.setOnClickListener(this);
        bgaHazardList.setOnClickListener(this);
        bgaLeaveManagement.setOnClickListener(this);
        bgaAttendanceReminde.setOnClickListener(this);
        bgaConstructionSite.setOnClickListener(this);
        bgaViewAlbum.setOnClickListener(this);
        bgaRsiteManagement.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bga_rsite_management://工地管理
                Intent intent_rsite_management= new Intent(getActivity(), buildingSiteActivity.class);
                intent_rsite_management.putExtra(Constants.IDENTICAL, "");
                intent_rsite_management.putExtra(Constants.Patroller, Constants.Construction);
                startActivity(intent_rsite_management);
                break;
            case R.id.bga_record_management://东片区
                Intent intent = new Intent(getActivity(), buildingSiteActivity.class);
                intent.putExtra(Constants.IDENTICAL, Constants.EAST);
                intent.putExtra(Constants.Patroller, Constants.Patroller);
                startActivity(intent);
                break;
            case R.id.bga_Leave_management://西片区
                Intent intentLeave = new Intent(getActivity(), buildingSiteActivity.class);
                intentLeave.putExtra(Constants.IDENTICAL, Constants.WEST);
                intentLeave.putExtra(Constants.Patroller, Constants.Patroller);
                startActivity(intentLeave);
                break;
            case R.id.bga_attendance_reminde://中片区
                Intent intentattendance = new Intent(getActivity(), buildingSiteActivity.class);
                intentattendance.putExtra(Constants.IDENTICAL, Constants.IN);
                intentattendance.putExtra(Constants.Patroller, Constants.Patroller);
                startActivity(intentattendance);
                break;
            case R.id.bga_hidden_danger_warning://隐患警告
                Intent intenthidden = new Intent(getActivity(), TeanActivity.class);
                intenthidden.putExtra(Constants.danger, Constants.dangerWarn);
                startActivity(intenthidden);
                break;
            case R.id.bga_historical_inspections://历史巡查
                Intent intenthistorical = new Intent(getActivity(), HistoricalinspectionsActivity.class);
                startActivity(intenthistorical);
                break;
            case R.id.bga_hazard_list://隐患列表
                Intent intenthazard = new Intent(getActivity(), TeanActivity.class);
                intenthazard.putExtra(Constants.danger, Constants.dangerList);
                startActivity(intenthazard);
                break;
            case R.id.bga_construction_site://建设工地上传图片
                Intent intentconstruction_site = new Intent(getActivity(), buildingPicSiteActivity.class);
                startActivity(intentconstruction_site);
                break;

            case R.id.bga_view_album://查看建设工地图片
                Intent intentalbum = new Intent(getActivity(), BuildatlasActivity.class);
                startActivity(intentalbum);
                break;
        }
    }

}
