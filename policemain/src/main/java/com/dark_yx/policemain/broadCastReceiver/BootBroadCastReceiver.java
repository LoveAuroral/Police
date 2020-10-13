package com.dark_yx.policemain.broadCastReceiver;

/*
 *
 * 开机自动启动广播
 * 并且打开登录页面
 *
 */

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dark_yx.policemain.activity.MainActivity;
import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.FileUtil;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.widget.Toast.LENGTH_LONG;

public class BootBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadCastReceiver";

    private ComponentName admin = null;
    private String mode;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d("开机广播" + "开机咯开机咯" + intent.getAction());
//        Toast.makeText(context, "开机咯开机咯:" + intent.getAction(), LENGTH_LONG).show();
        admin = new ComponentName(context, DeviceReceiver.class);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d(TAG, "打开登录页面");
//            Intent bootIntent = new Intent(context, LoginActivity.class);
//            bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(bootIntent);
            mode = FileUtil.readMode();
////            Toast.makeText(context, "MODE=" + mode, Toast.LENGTH_SHORT).show();
            if (!mode.equals("2")) {
//                /* 启动当前应用程序 */
                DataUtil.setEnter(context, true);
                CommonMethod.sendStatus(true, context);
                initAdmin(context, admin);
                User account = DataUtil.getAccount();
                if (DataUtil.isLogin(context) && !TextUtils.isEmpty(account.getUserName())) {
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            }
//            openLogin(context);
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
            PhoneInterfaceUtil.openInit(mAdminName, context.getPackageName(), LoginActivity.class.getCanonicalName(), context.getApplicationContext());
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

    /**
     * 正常退出警务通后关机,开机之后打开页面
     *
     * @param context
     */
    private void openLogin(Context context) {
        String activity = null;
        if (DataUtil.isEnter(context)) {
            if (DataUtil.getActivity().equals("PhoneMainActivity")) {
                activity = "PhoneMainActivity";
                Intent bootIntent = new Intent(context, MainActivity.class);
                bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(bootIntent);
            } else if (DataUtil.getActivity().equals("LoginActivity")) {
                activity = "LoginActivity";
                Intent bootIntent = new Intent(context, LoginActivity.class);
                bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(bootIntent);
            }
        } else {
            activity = "Launcher";
            CommonMethod.sendStatus(false, context);
            PhoneInterfaceUtil.exitApp(context, admin);
        }
        LogUtil.d("ThisActivity>>>" + activity);
    }
//    private void openLogin(Context context) {
//        Intent bootIntent = new Intent(context, LoginActivity.class);
//        bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        bootIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(bootIntent);
//    }

}
