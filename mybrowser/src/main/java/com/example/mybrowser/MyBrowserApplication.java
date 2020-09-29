package com.example.mybrowser;

import android.app.Application;
import com.example.mybrowser.Activity.BaseActivity;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lmp on 2016/8/4.
 */
public class MyBrowserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }

    private List<BaseActivity> activities = new ArrayList<BaseActivity>();

    public void addActivity(BaseActivity activity) {
        activities.add(activity);
    }
}
