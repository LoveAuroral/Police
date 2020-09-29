package com.dark_yx.policemain.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dark_yx.policemain.activity.NoticeActivity;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.chat.beans.ModeInput;
import com.dark_yx.policemaincommon.Models.PrivatePhoneBean;
import com.dark_yx.policemaincommon.Models.UserTreeResult;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by lmp on 2016/5/28.
 */
public class CommonMethod {

    private static InputMethodManager imm;
    private static ArrayList<String> numbers;
    private static ArrayList<UsersBean> users;

    public static void addFriend(final Activity activity) {
        final EditText text = new EditText(activity);
        openKeyboard(text, activity);
        text.setTextColor(Color.BLACK);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("添加好友")
                .setView(text)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(text.getText())) {
                            Intent intent = new Intent("com.dark_yx.addFriends");
                            intent.putExtra("friendsId", text.getText().toString());
                            activity.sendBroadcast(intent);
                            Toast.makeText(activity, "添加好友消息已发送!", Toast.LENGTH_SHORT).show();

                            Intent intent1 = new Intent("com.dark_yx.updateContacts");
                            activity.sendBroadcast(intent1);
                            closeKeyboard(activity);
                        } else {
                            Toast.makeText(activity, "输入错误,添加好友失败!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeKeyboard(activity);
                    }
                })
                .show();
    }

    public static void openKeyboard(EditText text, Context context) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(text, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void closeKeyboard(Activity activity) {
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取当前分钟
     */
    public static int getMinute() {
        Calendar c = Calendar.getInstance();
        return c.getTime().getMinutes();
    }

    /**
     * 获取当前小时
     */
    public static int getHours() {
        Calendar c = Calendar.getInstance();
        return c.getTime().getHours();
    }

    /**
     * 获取当前月
     */
    public static int getMouth() {
        Calendar c = Calendar.getInstance();
        return c.getTime().getMonth();
    }

    /**
     * 获取当前月
     */
    public static int getMouth(Long s) {
        Date date = new Date(s);
        return date.getMonth();
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本失败";
        }
    }

    /**
     * 打开公告
     */
    public static void openNotice(Context context) {
        Intent intent1 = new Intent(context, NoticeActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent1);
    }

    /**
     * @return Whether fm is being played right now.
     */
    public static boolean isFmActive(Context context) {
        final AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (am == null) {
            return false;
        }
        return am.isMusicActive();
    }

    public static String getCurrentActivityName(Activity activity) {
        ActivityManager am = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = null;
        if (taskInfo.size() > 0) {
            componentInfo = taskInfo.get(0).topActivity;
        }
        return componentInfo.getClassName();
    }

    /**
     * 检测服务是否运行
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRun = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Short.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
            if (info.service.getClassName().equals(className)) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

    /**
     * 判断自己是否为默认桌面
     */
    public static boolean isDefaultLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);//Intent.ACTION_VIEW
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String de = info.activityInfo.packageName;
        LogUtil.d("default launcher: " + de);
        boolean isDefault = context.getPackageName().equals(de);
        return isDefault;
    }

    public static ArrayList<String> getPrivateNumbers(List<PrivatePhoneBean> result) {
        ArrayList<String> strings = new ArrayList<>();
        for (PrivatePhoneBean bean : result) {
            if (strings.size() < 5)
                strings.add(bean.getPhoneNumber().trim());
        }
        return strings;
    }

    public static ArrayList<String> getPublicNumbers(UserTreeResult result) {
        numbers = new ArrayList<>();
        for (UserTreeResult.ResultBean bean : result.getResult()) {
            getNumber(bean);
        }
        return numbers;
    }

    private static void getNumber(UserTreeResult.ResultBean bean) {
        for (UserTreeResult.ResultBean child : bean.getChildren()) {
            getNumber(child);
        }
        for (UsersBean user : bean.getUsers()) {
            if (user != null) {
                if (user.getLandline() != null && numbers.size() < 195)
                    numbers.add(user.getLandline().trim());
            }
        }
    }

    public static ArrayList<UsersBean> getPublicUsers(UserTreeResult result) {
        users = new ArrayList<>();
        for (UserTreeResult.ResultBean bean : result.getResult()) {
            getUser(bean);
        }
        return users;
    }

    private static void getUser(UserTreeResult.ResultBean bean) {
        for (UserTreeResult.ResultBean child : bean.getChildren()) {
            getUser(child);
        }
        for (UsersBean user : bean.getUsers()) {
            if (user != null)
                users.add(user);
        }
    }

    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context, DeviceReceiver.class);
    }

    public static ArrayList<String> getAllPhone(List<UsersBean> groupUser, List<PrivatePhoneBean> privateNumber) {
        ArrayList<String> strings = new ArrayList<>();
        for (UsersBean s : groupUser) {
            if (strings.size() < 195)
                strings.add(s.getLandline());
        }
        for (PrivatePhoneBean p : privateNumber) {
            if (strings.size() < 200)
                strings.add(p.getPhoneNumber());
        }
        return strings;
    }

    public static void sendStatus(boolean isIn, Context context) {
        DataUtil.setEnter(context, isIn);
        String mode = "生活模式";
        if (isIn) {
            mode = "安全模式";
        }
        LogUtil.e("sendStatus: now mode" + mode);
        ApiFactory.getService().updateStatus(DataUtil.getToken(), new ModeInput(mode)).enqueue(new MyCallBack<ResponseBody>() {
            @Override
            public void onSuc(Response<ResponseBody> response) {

            }

            @Override
            public void onFail(String message) {
                LogUtil.e(message);
            }
        });
    }

    public static void makeAsRead() {
        ApiFactory.getService().makeAsRead(DataUtil.getToken()).enqueue(new MyCallBack<ResponseBody>() {
            @Override
            public void onSuc(Response<ResponseBody> response) {
                LogUtil.d("makeAsRead success");
            }

            @Override
            public void onFail(String message) {
                LogUtil.e(message);
            }
        });
    }
}
