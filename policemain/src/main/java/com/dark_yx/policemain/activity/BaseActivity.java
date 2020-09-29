package com.dark_yx.policemain.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dark_yx.policemain.PoliceMainApplication;

import org.xutils.x;

/**
 * Created by dark_yx on 2016-03-08.
 * 自定义Activity基类
 */
public class BaseActivity extends FragmentActivity {
    public PoliceMainApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


        mApplication = (PoliceMainApplication) this.getApplication();
        mApplication.addActivity(this);
    }
}
