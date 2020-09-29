package com.dark_yx.policemain.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.activity.ApkTool;
import com.dark_yx.policemain.activity.CheckPerimissionActivity;
import com.dark_yx.policemain.activity.MainActivity;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.chat.ChatService;
import com.dark_yx.policemain.entity.UpdataInfo;
import com.dark_yx.policemain.launcher.launcher3.Launcher;
import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Util.DataUtil;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.M)
public class BaseSationService extends Service {
    public static final int NP_CELL_INFO_UPDATE = 1001;
    public volatile CellGeneralInfo serverCellInfo;
    private List<CellGeneralInfo> HistoryServerCellList;
    TelephonyManager phoneManager;
    private MyPhoneStateListener myPhoneStateListener;
    int myCid = -1;
    private UpdataInfo info;
    private String XML_PATH = DataUtil.getBassIp() + "/update/update.xml";//xml解析地址(版本号)
    ComponentName admin = null;
    private LocationManager lm;//【位置管理】
    List<Integer> allCid = null;
    List<String> packageNames1;
    List<String> packageNames2;
    private ScheduledExecutorService mService;
    private String CHANNEL_ID_STRING = "01";
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.CALL_PHONE};

    public BaseSationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
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
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
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
            startForeground(1, notification);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        packageNames1 = new ArrayList<>();
        packageNames1.add("com.android.settings");
        packageNames1.add("com.huawei.systemmanager");
        packageNames1.add("com.android.packageinstaller");
        packageNames2=new ArrayList<>();
        packageNames2.add("com.huawei.KoBackup");
        packageNames2.add("com.huawei.hidisk");
        packageNames2.add("com.google.android.gms");
        allCid = new ArrayList<>();
        allCid.add(25632139);
        admin = new ComponentName(this, DeviceReceiver.class);
        serverCellInfo = new CellGeneralInfo();
        myPhoneStateListener = new MyPhoneStateListener();
        phoneManager = (TelephonyManager) getApplication().getSystemService(TELEPHONY_SERVICE);
        if (phoneManager != null) {
            phoneManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
        HistoryServerCellList = new ArrayList<>();
        InitProcessThread();
        return START_REDELIVER_INTENT;
    }

    private void openApp(int cid) {
        if (cid != 0) {
            if (cid == 25632139) {
                if (!DataUtil.isEnter(getApplicationContext())) {
                    CommonMethod.sendStatus(true, getApplicationContext());
                    initAdmin(getApplicationContext(), admin);
                    User account = DataUtil.getAccount();
                    if (DataUtil.isLogin(getApplicationContext()) && !TextUtils.isEmpty(account.getUserName())) {
                        getApplicationContext().startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
            } else {
                if (DataUtil.isEnter(getApplicationContext())) {
                    Log.d("退出app", cid + "");
                    DataUtil.setEnter(getApplicationContext(), false);
                    CommonMethod.sendStatus(false, getApplicationContext());
                    PhoneInterfaceUtil.exitApp(getApplicationContext(), admin);
                }
            }
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

    private void InitProcessThread() {
        if (mService == null || mService.isShutdown()) {
            mService = Executors.newScheduledThreadPool(1);
            mService.scheduleWithFixedDelay(new PhoneInfoThreadRunnable(), 0, 5 * 1000, TimeUnit.MILLISECONDS);
        }
    }

    class PhoneInfoThreadRunnable implements Runnable {

        @Override
        public void run() {
            String str = ApkTool.getForegroundApp(getApplicationContext());
            LogUtil.d("GPS-is-open:" + PhoneInterfaceUtil.isGPSOn(admin));
            if (!PhoneInterfaceUtil.isGPSOn(admin)) {
                PhoneInterfaceUtil.openGPS(admin, true);
            }
            if (!DataUtil.isEnter(getApplicationContext())) {
                LogUtil.d("use app pkgName: " + str);
                if (packageNames1.contains(str)) {
                    PhoneInterfaceUtil.setMenuStatus(admin, true);//设置功能键禁用
                    PhoneInterfaceUtil.setMicrophoneDisabled(admin, true);//设置语音功能禁用
                    PhoneInterfaceUtil.setBarStatus(admin, true);//设置下拉状态栏禁用
                }
            }
            if (packageNames2.contains(str)){
                PhoneInterfaceUtil.setWifiDisable(admin, true);//禁用wifi
            }else {
                PhoneInterfaceUtil.setWifiDisable(admin, false);//启用wifi
            }
            boolean perimissionFlas = false;
            int perimission = 0;
            for (String permissionStr : permissions) {
                // 检查该权限是否已经获取
                int per = ContextCompat.checkSelfPermission(getApplicationContext(), permissionStr);
                LogUtil.d("权限申请：" + per + "  " + permissionStr);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (per != PackageManager.PERMISSION_GRANTED) {
                    perimissionFlas = true;
                }
            }
            LogUtil.d("情况权限开启--->" + isUsageAgeAccessOpen(getApplicationContext()));
            if (perimissionFlas || !isUsageAgeAccessOpen(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), CheckPerimissionActivity.class);
                if (perimissionFlas) {
                    perimission = 1;
                } else {
                    perimission = 2;
                }
                intent.putExtra("perimission", perimission);
                startActivity(intent);
            } else {
                if (!DataUtil.isEnter(getApplicationContext()) && !packageNames1.contains(str)) {
                    PhoneInterfaceUtil.setDataConnectivityDisabled(admin, false);//数据流量启用
                    PhoneInterfaceUtil.setWifiDisable(admin, false);//启用wifi
                    PhoneInterfaceUtil.setBluetoothDisable(admin, false);//启用蓝牙
                    PhoneInterfaceUtil.setWifiApDisabled(admin, false);//启用热点
                    PhoneInterfaceUtil.setHomedisable(admin, false);//启用HOME键
                    PhoneInterfaceUtil.setMenuStatus(admin, false);//启用功能鍵
                    PhoneInterfaceUtil.setBarStatus(admin, false);//启用下拉状态栏
                    PhoneInterfaceUtil.setMicrophoneDisabled(admin, false);//启用语音功能
                }
            }
            // 更新信息
            if (serverCellInfo.cid == 0) {
                serverCellInfo.cid = 0;
            }
            LogUtil.d("cidIs" + serverCellInfo.cid + "");
//            DataUtil.setBaseSation(getApplicationContext(), serverCellInfo.cid);
//            openApp(serverCellInfo.cid);
        }
    }


    class CellGeneralInfo implements Cloneable {
        public int cid;

        @Override
        public Object clone() {
            CellGeneralInfo cellinfo = null;
            try {
                cellinfo = (CellGeneralInfo) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return cellinfo;
        }
    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            try {
                getPhoneGeneralInfo();
                getServerCellInfo();
            } catch (Exception ignored) {
            }
            updateHistoryCellList(serverCellInfo);

        }

        @SuppressLint("MissingPermission")
        public void getPhoneGeneralInfo() {
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @SuppressLint({"MissingPermission", "NewApi"})
        public void getServerCellInfo() {
            try {
                List<CellInfo> allCellinfo;
                allCellinfo = phoneManager.getAllCellInfo();
                LogUtil.d("allCellinfo" + allCellinfo.size());
                if (allCellinfo.size() > 0) {
                    CellInfo cellInfo = allCellinfo.get(0);
                    if (cellInfo instanceof CellInfoGsm) {
                        CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                        serverCellInfo.cid = cellInfoGsm.getCellIdentity().getCid();
                        LogUtil.d("serverCellInfo.cid1" + serverCellInfo.cid);
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                        serverCellInfo.cid = cellInfoWcdma.getCellIdentity().getCid();
                        LogUtil.d("serverCellInfo.cid2" + serverCellInfo.cid);
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                        serverCellInfo.cid = cellInfoLte.getCellIdentity().getCi();
                        LogUtil.d("serverCellInfo.cid3" + serverCellInfo.cid);
                    }
                } else {
//                    serverCellInfo.cid = 0;
                    try {
                        getServerCellInfoOnOlderDevices();
                    } catch (Exception e) {
                        // 华为手机没有开启位置信息会导致
                    }
                }
            } catch (Exception e) {
                try {
                    getServerCellInfoOnOlderDevices();
                } catch (Exception ee) {
                    // 华为手机没有开启位置信息会导致
                }
            }
        }

        void getServerCellInfoOnOlderDevices() {
            @SuppressLint("MissingPermission") GsmCellLocation location = (GsmCellLocation) phoneManager.getCellLocation();
            LogUtil.d("getServer=====>" + location.getCid());
            serverCellInfo.cid = location.getCid();
        }

        void updateHistoryCellList(BaseSationService.CellGeneralInfo serverinfo) {
            BaseSationService.CellGeneralInfo newcellInfo = (BaseSationService.CellGeneralInfo) serverinfo.clone();
            HistoryServerCellList.add(newcellInfo);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 检测权限是否打开
     */
    private void checkPermissons() {

    }

    /**
     * 判断查看应用使用情况权限是否打开
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isUsageAgeAccessOpen(Context context) {
        long ts = System.currentTimeMillis();
        @SuppressLint("WrongConstant")
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext()
                .getSystemService("usagestats");
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }


}
