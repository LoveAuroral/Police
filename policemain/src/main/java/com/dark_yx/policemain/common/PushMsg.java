package com.dark_yx.policemain.common;

import android.content.Context;

import com.dark_yx.policemaincommon.Util.HttpCallBackEvent;
import com.dark_yx.policemaincommon.Util.SystemInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dark_yx on 2016-03-01.
 * 发送消息至后台
 */
public class PushMsg {

    // 首次开机时间
    public static String POWER_UP_TIME = "powerUpTime";

    // 动作
    public static String ACTION = "action";

    // IMEI
    public static String PHONE_IMEI = "IMEI";

    //电量
    public static String BATTERY_SCALE = "BatteryScale";

    public static String LONGITUDE = "longitude";

    public static String LATITUDE = "latitude";

    /**
     * 发送首次开机时间及本机信息至服务器
     */
    public static boolean SendFirstPowerMsg(Context context) {

        final String powerUpTime = SystemInfo.getPowerUpTime(context);
        final String IMEI = SystemInfo.GetIMEI(context);
        final String BatteryScale = SystemInfo.GetBatteryScale(context);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ACTION, "firstPowerMsg");
        param.put(POWER_UP_TIME, powerUpTime);
        param.put(PHONE_IMEI, IMEI);
        param.put(BATTERY_SCALE, BatteryScale);
        //HttpHelp.Get(null, HttpHelp.GetBaasIp().GetIp() + "/Polling", param);
        return true;
    }

    /*
     * 发送数据并轮询消息
      */
    public static boolean PollingMsg(String userName, Context context, HttpCallBackEvent event) {
        String IMEI = SystemInfo.GetIMEI(context);
        String BatteryScale = SystemInfo.GetBatteryScale(context);
        double longitude = 0.1;
        double latitude = 0.1;
        Map<String, Object> params = new HashMap<>();
        params.put(PHONE_IMEI, IMEI);
        params.put(ACTION, "Polling");
        params.put(BATTERY_SCALE, BatteryScale);
        params.put(LONGITUDE, longitude);
        params.put(LATITUDE, latitude);
        /*params.put("UserName", LoginLogic.USERNAME);
        HttpHelp.Get(event, HttpHelp.GetBaasIp() + "/Polling", params);*/
        return false;
    }
}
