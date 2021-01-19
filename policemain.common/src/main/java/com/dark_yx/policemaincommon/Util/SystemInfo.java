package com.dark_yx.policemaincommon.Util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dark_yx on 2016-02-29.
 */
public class SystemInfo {
    private static String TAG = "SystemInfo";
    private static String powerUpTime = null;
    private static String IMEI = null;
    private static TelephonyManager tm;
    public static String BatteryScale = null;
    public static String UserName = null;

    // 获取开机时间
    public static String getPowerUpTime(Context context) {

        if (powerUpTime == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date d1 = new Date();
            powerUpTime = format.format(d1);
        }
        Log.e(TAG, powerUpTime);
        return powerUpTime;
    }

    // 获取IEMI
    public static String GetIMEI(Context context) {
        if (IMEI == null) {
            IMEI = SPUtil.getDeviceId(context);
        }
        return IMEI;
    }

    /*
     *获取电量信息
     */
    public static String GetBatteryScale(Context context) {
        if (BatteryScale == null) {
            return "UnKnown";
        }
        String result = BatteryScale;

        Log.e("Battery", BatteryScale);
        return result;
    }

    /**
     * 获取IMSI,sim卡唯一标识
     */
    public static String GetSimImsi(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi;
    }

    /**
     * 获取当前系统
     */
    public static String GetContainer(Context context) {
        String s = "生活模式";
        return s;
    }

    public static String getMyUUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        LogUtil.d("UUID: " + uniqueId);
        return uniqueId;
    }

    /**
     * 获取IMSI,sim卡唯一标识
     */
    public static String getSimImsi(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi != null ? imsi : "未知";
    }

    /**
     * 获取电话号码
     */
    public static String getNativePhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = mTelephonyMgr.getLine1Number();
//        return phoneNumber != null ? phoneNumber.substring(3) : "未知";
        return phoneNumber != null ? phoneNumber : "未知";
    }

}
