package com.dark_yx.policemain.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemaincommon.Models.MissedPhones;
import com.dark_yx.policemaincommon.Models.TeleTimeAndNubBean;
import com.dark_yx.policemaincommon.Models.TeleTimeAndNubBeanResult;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.FileUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class ListenCallService extends Service {
    List<MissedPhones> list = new ArrayList<>();
    private int lastState = TelephonyManager.CALL_STATE_IDLE;
    long time1, time2;

    public ListenCallService() {

    }

    private TelephonyManager tm;
    private PhoneStateListener listener = new PhoneStateListener() {

        // 当通话状态发生改变时调用
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:// 空闲（挂断电话/未来电之前）
                    Log.e("能不能监听到", "空闲（挂断电话/未来电之前）");
                    time1 = System.currentTimeMillis();
                    long time = time1 - time2;
                    TeleTimeAndNubBean bean = new TeleTimeAndNubBean();
                    bean.setAnswerUserPhone(incomingNumber);
                    bean.setTalkTime(time);
                    LogUtil.d("TeleTimeAndNubBean" + "\n" + incomingNumber + "\n" + time);
                    ApiFactory.getService().setTeleTimeAndNub(DataUtil.getToken(), bean).enqueue(new MyCallBack<TeleTimeAndNubBeanResult>() {
                        @Override
                        public void onSuc(Response<TeleTimeAndNubBeanResult> response) {
                            LogUtil.d("TeleTimeAndNubBeanResult" + response.body());
                        }

                        @Override
                        public void onFail(String message) {
                        }
                    });
                    break;
                case TelephonyManager.CALL_STATE_RINGING:// 响铃Util.getData();
                    PhoneInterfaceUtil.addWhitePhone(getApplicationContext());
                    Log.e("能不能监听到", "响铃" + incomingNumber);
                    // 如果来电是黑名单号（110），就挂断电话
                    Log.d("phoneList1: ", DataUtil.phoneNub1.size() + " "
                            + DataUtil.phoneNub1.toString());
                    Log.d("phoneList11: ", DataUtil.phoneNub2.size() + " "
                            + DataUtil.phoneNub2.toString());
                    if (!DataUtil.phoneNub1.contains(incomingNumber) &&
                            !DataUtil.phoneNub2.contains(incomingNumber)) {
                        try {
                            endCall();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:// 接通
                    PhoneInterfaceUtil.addWhitePhone(getApplicationContext());
                    time2 = System.currentTimeMillis();
                    Log.e("能不能监听到", "接通" + incomingNumber);
                    Log.d("phoneList2: ", DataUtil.phoneNub1.size() + " "
                            + DataUtil.phoneNub1.toString());
                    Log.d("phoneList22: ", DataUtil.phoneNub2.size() + " "
                            + DataUtil.phoneNub2.toString());
                    if (!DataUtil.phoneNub1.contains(incomingNumber) &&
                            !DataUtil.phoneNub2.contains(incomingNumber)) {
                        try {
                            endCall();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
            if (lastState == TelephonyManager.CALL_STATE_RINGING
                    && state == TelephonyManager.CALL_STATE_IDLE) {
                Log.d("未接来电号码是：", incomingNumber);
                MissedPhones missedPhones = new MissedPhones();
                missedPhones.setPhone(incomingNumber);
                missedPhones.setName("测试");
                list.add(missedPhones);
                LogUtil.d("mlistIs:" + list.toString());
                FileUtil.writeMissedPhone(list.toString());
            }
            // 最后的时候改变当前值
            lastState = state;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * 挂断电话
     */
    private void endCall() throws Exception {
        // 通过反射调用隐藏的API
        // 得到隐藏类的Class对象
        Class c = Class.forName("android.os.ServiceManager");
        // 得到方法所对应的Method对象
        Method method = c.getMethod("getService", String.class);
        // 调用方法
        IBinder iBinder = (IBinder) method.invoke(null,
                Context.TELEPHONY_SERVICE);
        // 得到接口对象
        ITelephony telephony = ITelephony.Stub.asInterface(iBinder);
        // 结束通话
        Log.d("能不能监听到", "为什么不挂断电话");
        telephony.endCall();

    }
    private String CHANNEL_ID_STRING = "01";
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        createNotificationChannel();
        // 得到电话管理器
        tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        // 监听电话状态
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "my_channel_01";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = getString(R.string.app_name);
//         用户可以看到的通知渠道的描述
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
//         配置通知渠道的属性
            mChannel.setDescription(description);
//         设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true); mChannel.setLightColor(Color.RED);
//         设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//         最后在notificationmanager中创建该通知渠道 //
            mNotificationManager.createNotificationChannel(mChannel);
            // 为该通知设置一个id
            int notifyID = 1;
            // 通知渠道的id
            String CHANNEL_ID = "my_channel_01";
            // Create a notification and set the notification channel.
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("New Message")
                    .setContentText("You've received new messages.")
                    .setSmallIcon(R.drawable.folderhouse)
                    .setChannelId(CHANNEL_ID)
                    .build();
            startForeground(1,notification);
        }
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 停止电话监听
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }
}
