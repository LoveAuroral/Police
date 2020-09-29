package com.dark_yx.policemain.broadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;

import org.xutils.common.util.LogUtil;

/**
 * 关机广播
 * Created by Lmp on 2016/8/8.
 */
public class ShutDownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d("ShutDownReceiver: " + "关机");
        ComponentName admin = new ComponentName(context, DeviceReceiver.class);
//        PhoneInterfaceUtil.setAppAsLauncher(admin, context.getPackageName(), LoginActivity.class.getCanonicalName());
    }
}
