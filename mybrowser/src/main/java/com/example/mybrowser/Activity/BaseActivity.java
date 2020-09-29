package com.example.mybrowser.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.mybrowser.MyBrowserApplication;

import org.xutils.x;

/**
 * Created by dark_yx on 2016-03-08.
 * 自定义Activity基类
 */
public class BaseActivity extends FragmentActivity {
    public MyBrowserApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mApplication = (MyBrowserApplication) this.getApplication();
        mApplication.addActivity(this);
    }
}
