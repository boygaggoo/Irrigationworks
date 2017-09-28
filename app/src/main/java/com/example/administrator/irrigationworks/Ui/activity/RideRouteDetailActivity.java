package com.example.administrator.irrigationworks.Ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.amap.api.services.route.RidePath;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.adapter.RideSegmentListAdapter;
import com.example.administrator.irrigationworks.UtilsTozals.AMapUtil;
import com.example.administrator.irrigationworks.UtilsTozals.NetUtil;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 骑行路线详情
 */
public class RideRouteDetailActivity extends BaseAppCompatActivity {

    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.firstline)
    TextView mTitleWalkRoute;
    @Bind(R.id.secondline)
    TextView secondline;
    @Bind(R.id.bus_segment_list)
    ListView mRideSegmentList;
    @Bind(R.id.bus_path)
    LinearLayout busPath;
    @Bind(R.id.route_map)
    MapView routeMap;
    private RidePath mRidePath;
    private RideSegmentListAdapter mRideSegmentListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getIntentData();
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        ivBackLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("骑行路线详情");
//		mTitle = (TextView) findViewById(R.id.title_center);
//		mTitle.setText("骑行路线详情");
        Boolean net = NetUtil.checkNetWork(RideRouteDetailActivity.this);
        if (net) {

            String dur = AMapUtil.getFriendlyTime((int) mRidePath.getDuration());
            String dis = AMapUtil
                    .getFriendlyLength((int) mRidePath.getDistance());
            mTitleWalkRoute.setText(dur + "(" + dis + ")");
            mRideSegmentListAdapter = new RideSegmentListAdapter(
                    this.getApplicationContext(), mRidePath.getSteps());
            mRideSegmentList.setAdapter(mRideSegmentListAdapter);
        } else {
            UtilsTool.showShortToast(RideRouteDetailActivity.this, "没有网络");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_route_detail;
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mRidePath = intent.getParcelableExtra("ride_path");
    }

    public void onBackClick(View view) {
        this.finish();
    }

}
