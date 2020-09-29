package com.dark_yx.policemain.broadCastReceiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dark_yx.policemain.activity.MainActivity;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemain.util.WhiteListUtil;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.beans.WorkInput;
import com.dark_yx.policemain.beans.WorkResult;
import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;

import org.xutils.common.util.LogUtil;

import java.util.List;

import retrofit2.Response;

public class OpenAppReceiverMJ extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("MyReceiver", intent.getAction());
        openApp(context, intent);
    }

    public void openApp(final Context context, Intent intent) {
        LogUtil.d("getAction is :" + intent.getAction());
        int mode = intent.getIntExtra("MODE", 0);
        int state = intent.getIntExtra("STATE", -1);
        LogUtil.d("data is " + mode + "" + state + "");
        ComponentName admin = new ComponentName(context, DeviceReceiver.class);
        if (state == 0) {
            WorkInput workInput = new WorkInput();
            workInput.setIsNFC(2);
            ApiFactory.getService().workIng(DataUtil.getToken(), workInput).enqueue(new MyCallBack<WorkResult>() {
                @Override
                public void onSuc(Response<WorkResult> response) {
                    LogUtil.d("response is:" + response.body().isB());
                    Toast.makeText(context, "刷卡成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(String message) {
                    LogUtil.d("message is:" + message);
                    Toast.makeText(context, "刷卡失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
            if (mode == 2) {
                DataUtil.setEnter(context, false);
                CommonMethod.sendStatus(false, context);
                PhoneInterfaceUtil.exitApp(context, admin);
            } else {
                WhiteListUtil whiteListUtil = new WhiteListUtil(context, true);
                whiteListUtil.getData();
                /* 启动当前应用程序 */
                DataUtil.setEnter(context, true);
                PhoneInterfaceUtil.openInit(admin, context.getPackageName(), LoginActivity.class.getCanonicalName(), context.getApplicationContext());
                Intent myIntent = new Intent(context, MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myIntent);
            }
        }
    }

    /**
     * 判断应用是否在运行
     *
     * @param context
     * @return
     */
    public boolean isRun(Context context, String packagename) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        //100表示取的最大的任务数，info.topActivity表示当前正在运行的Activity，info.baseActivity表系统后台有此进程在运行
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packagename) || info.baseActivity.getPackageName().equals(packagename)) {
                isAppRunning = true;
                Log.d("ActivityService isRun()", info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                break;
            }
        }
        Log.d("ActivityService isRun()", "com.ad 程序  ...isAppRunning......" + isAppRunning);
        return isAppRunning;
    }

}
