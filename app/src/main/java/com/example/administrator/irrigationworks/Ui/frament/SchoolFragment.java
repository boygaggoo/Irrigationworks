package com.example.administrator.irrigationworks.Ui.frament;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.badgeview.BGABadgeImageView;
import com.example.administrator.irrigationworks.Ui.frament.Infoframent;
import com.example.administrator.irrigationworks.Ui.frament.Inspectionframent;
import com.example.administrator.irrigationworks.Ui.frament.TFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 测试类，可以删除
 * Created by Administrator on 2016/8/16 0016.
 */
public class SchoolFragment extends TFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final double DOUBLE = 1.;
    @Bind(R.id.tv_approve_show_title)
    TextView tvApproveShowTitle;
    @Bind(R.id.btn_schoolfragment_conversation)
    Button mbtnConversation;
    @Bind(R.id.btn_schoolfragment_contacts)
    Button mbtnContacts;
    @Bind(R.id.dialog_generic_layout_bottom)
    LinearLayout dialogGenericLayoutBottom;
    @Bind(R.id.school_title)
    LinearLayout schoolTitle;
    @Bind(R.id.iv_schoolfragment_contacts)
    ImageView ivSchoolfragmentContacts;
    @Bind(R.id.rl_charoom_title)
    RelativeLayout rlCharoomTitle;
    @Bind(R.id.tv_contact_sreach_pic)
    EditText tvContactSreachPic;
    @Bind(R.id.iv_delete)
    ImageView ivDelete;
    @Bind(R.id.menu_s_title)
    LinearLayout menuSTitle;
    @Bind(R.id.contacts_item_head)
    ImageView contactsItemHead;
    @Bind(R.id.head_layout)
    RelativeLayout headLayout;
    @Bind(R.id.contacts_item_name)
    TextView contactsItemName;
    @Bind(R.id.contacts_item_desc)
    TextView contactsItemDesc;
    @Bind(R.id.contacts_item_name_layout)
    LinearLayout contactsItemNameLayout;
    @Bind(R.id.friendShape)
    LinearLayout friendShape;
    @Bind(R.id.friendShapes)
    LinearLayout friendShapes;

    @Bind(R.id.searchResultList)
    ListView searchResultList;
    private Activity ctx;
    private View layout;
    private List<Fragment> mListFragment;
    private FragmentPagerAdapter adapter;
    FrameLayout mlayout;
    //校友圈
    private ImageView ivDele;
    private ImageView ivPic, mIvAdd;
    private BGABadgeImageView ivContact;
    private TextView tvTitle;

    //全局搜索页面,支持通讯录搜索、消息全文检索
    private ListView lvContacts;
    private boolean addFriend = false;
    private boolean addGroup = false;

    //创建群
    public static final String EXTRA_DATA = "EXTRA_DATA"; // 请求数据：Option
    public static final String RESULT_DATA = "RESULT_DATA"; // 返回结果

    private static final int REQUEST_CODE_NORMAL = 3;
    private static final int REQUEST_CODE_ADVANCED = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (layout == null) {
            ctx = this.getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.fragment_school,
                    null);
            ButterKnife.bind(this, layout);

            indata();
            //全局搜索页面
            //校友圈
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        ButterKnife.bind(this, layout);
        return layout;
    }


    //设置监听
    private void indata() {

        InnerOnClickListener listener = new InnerOnClickListener();
        mbtnConversation.setOnClickListener(listener);
        mbtnContacts.setOnClickListener(listener);
        mListFragment = new ArrayList<>();
        mListFragment.add(new Infoframent());
        mListFragment.add(new Inspectionframent());
        adapter = new InnerFragmentPagerAdapter(getChildFragmentManager());
        mlayout = (FrameLayout) layout.findViewById(R.id.viewPager_school_charoom);
        // 默认选中第1个按钮
        selectButton(mbtnConversation);

    }

    /**
     * 选中某个按钮
     *
     * @param button 被选择的Button对象
     */
    private void selectButton(Button button) {
        mbtnConversation.setEnabled(true);
        mbtnContacts.setEnabled(true);
        button.setEnabled(false);
        // 获取需要显示的Fragment的position
        int position = 0;
        if (button == mbtnConversation) {
            position = 0;
        } else if (button == mbtnContacts) {
            position = 1;
        }
        // 判断是否需要销毁
        if (lastFragment != null) {
            adapter.destroyItem(mlayout, lastPosition, lastFragment);
            lastFragment = null;
        }
        // 初始化需要显示的Fragment
        Object fragment = adapter.instantiateItem(mlayout, position);
        // 设置显示
        adapter.setPrimaryItem(mlayout, 0, fragment);
        // 更新显示
        adapter.finishUpdate(mlayout);
        // 将本次新显示的Fragment记录为lastFragment，以便于下次显示其它Fragment可以销毁当前这个Fragment
        lastFragment = fragment;
        lastPosition = position;
    }

    private Object lastFragment;
    private int lastPosition;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                //暂时封住
                //TODO

                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class InnerFragmentPagerAdapter extends FragmentPagerAdapter {
        public InnerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = null;
            switch (position) {
                case 0:
                    frag = new Infoframent();
                    break;
                case 1:
                    frag = new Inspectionframent();
                    break;

            }
            return frag;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private class InnerOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_schoolfragment_conversation:
                    // 如果点击第1个按钮
                    selectButton(mbtnConversation);
//                    HttpGPutils.hideSoftKeyboard(getActivity());
//                    mEdsreach.setCursorVisible(false);//失去光标
                    break;

                case R.id.btn_schoolfragment_contacts:
                    // 如果点击第2个按钮
                    selectButton(mbtnContacts);
//                    HttpGPutils.hideSoftKeyboard(getActivity());
//                    mEdsreach.setCursorVisible(false);//失去光标


                    break;


            }
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    //


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }


}
