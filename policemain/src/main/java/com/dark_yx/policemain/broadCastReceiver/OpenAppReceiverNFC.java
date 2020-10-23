package com.dark_yx.policemain.broadCastReceiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.dark_yx.policemain.activity.MainActivity;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemain.util.WhiteListUtil;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Util.DataUtil;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;

public class OpenAppReceiverNFC extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName admin = new ComponentName(context, DeviceReceiver.class);
        int message = intent.getIntExtra("state", 0);
        Log.d("messageIs1:", message + "");
        if (message != 0) {
            Log.d("messageIs2:", message + "");
            if (message == 2) {
                LogUtil.d("进入app" + message);
                WhiteListUtil whiteListUtil = new WhiteListUtil(context, true);
                whiteListUtil.getData();
                CommonMethod.sendStatus(true, context);
                initAdmin(context, admin);
                User account = DataUtil.getAccount();
                if (DataUtil.isLogin(context) && !TextUtils.isEmpty(account.getUserName())) {
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            } else if (message == 1) {
                LogUtil.d("退出app" + message);
                CommonMethod.sendStatus(false, context);
                PhoneInterfaceUtil.exitApp(context, admin);
            }
        } else {
            Log.d("messageIs3:", message + "");
            CommonMethod.sendStatus(false, context);
            PhoneInterfaceUtil.exitApp(context, admin);
        }
    }

    public void initAdmin(Context context, ComponentName mAdminName) {
        startTheNetwork(context);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (devicePolicyManager != null && !devicePolicyManager.isAdminActive(mAdminName)) {
            Intent intent = new Intent();
            intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
            context.startActivity(intent);
        } else {
            PhoneInterfaceUtil.openInit(mAdminName, context.getPackageName(), MainActivity.class.getCanonicalName(), context.getApplicationContext());
        }
    }

    public void startTheNetwork(Context context) {
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        Class[] getArgArray = null;
        Class[] setArgArray = new Class[]{boolean.class};
        Object[] getArgInvoke = null;
        try {
            Method mGetMethod = teleManager.getClass().getMethod("getDataEnabled", getArgArray);
            Method mSetMethod = teleManager.getClass().getMethod("setDataEnabled", setArgArray);
            boolean isOpen = (Boolean) mGetMethod.invoke(teleManager, getArgInvoke);
            LogUtil.d("isOpen" + isOpen);
            if (!isOpen) {
                mSetMethod.invoke(teleManager, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
