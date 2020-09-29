package com.dark_yx.policemain.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dark_yx.policemain.activity.WriteGroupInfoActivity;
import com.dark_yx.policemain.R;

/**
 * 对讲联系人
 * Created by lmp on 2016/4/29.
 */
public class PTTFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private LinearLayout llAddGroup, llMyContacts, llMyGroups;
    private TextView tvContacts, tvGroup;
    private Fragment myContactsFragment, myGroupFragment;
    private FragmentTransaction fragmentTransaction;
    private static final String TAG = "PTTFragment";

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_ptt, null);
        return view;
    }

    @Override
    public void initData() {
        init();
    }

    private void init() {
        llAddGroup = (LinearLayout) view.findViewById(R.id.ll_add_group);
        llMyContacts = (LinearLayout) view.findViewById(R.id.ll_contacts);
        llMyGroups = (LinearLayout) view.findViewById(R.id.ll_group);
        tvContacts = (TextView) view.findViewById(R.id.tv_contacts);
        tvGroup = (TextView) view.findViewById(R.id.tv_group);

        llAddGroup.setOnClickListener(this);
        llMyGroups.setOnClickListener(this);
        llMyContacts.setOnClickListener(this);

        switchPage(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_group://创建群
                Intent intent1 = new Intent(getContext(), WriteGroupInfoActivity.class);
                intent1.putExtra("type", "createGroup");
                startActivity(intent1);
                break;
            case R.id.ll_contacts://好友列表
                switchPage(0);
                setContactsColor();
                break;
            case R.id.ll_group://群组
                switchPage(1);
                setGroupColor();
                break;
        }
    }


    public void setGroupColor() {
        tvGroup.setTextColor(getResources().getColor(R.color.blue));
        tvContacts.setTextColor(getResources().getColor(R.color.black));
    }

    public void setContactsColor() {
        tvContacts.setTextColor(getResources().getColor(R.color.blue));
        tvGroup.setTextColor(getResources().getColor(R.color.black));
    }

    private void switchPage(int flag) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        hideFragment();
        switch (flag) {
            case 0:
                if (myContactsFragment != null) {
                    fragmentTransaction.show(myContactsFragment);
                } else {
                    myContactsFragment = new ItemContactsFragment();
                    fragmentTransaction.add(R.id.framelayout, myContactsFragment);
                }
                break;
            case 1:
                if (myGroupFragment != null) {
                    fragmentTransaction.show(myGroupFragment);
                } else {
                    myGroupFragment = new ItemGroupsFragment();
                    fragmentTransaction.add(R.id.framelayout, myGroupFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideFragment() {
        if (myContactsFragment != null) {
            fragmentTransaction.hide(myContactsFragment);
        }
        if (myGroupFragment != null) {
            fragmentTransaction.hide(myGroupFragment);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //阻止activity保存fragment的状态
        //super.onSaveInstanceState(outState);
    }
}
