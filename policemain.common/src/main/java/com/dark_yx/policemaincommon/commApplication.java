package com.dark_yx.policemaincommon;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Administrator on 2018/5/10.
 */

public class commApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
