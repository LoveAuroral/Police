package com.dark_yx.policemaincommon.Util;


import android.content.Context;

import com.dark_yx.policemaincommon.Models.NoticeBean;
import com.dark_yx.policemaincommon.Models.User;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ligh on 2018/1/9 09:58
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class DataUtil {
    /*黔东*/
//    private final static String DEFAULT_BASS_IP = "111.121.251.81:1156";
//    private final static String DEFAULT_VIDEO_IP = "111.121.251.81:8095";

//    private final static String DEFAULT_BASS_IP = "222.86.157.26:1156";
//    private final static String DEFAULT_VIDEO_IP = "222.86.157.26:8095";

    // 兴义地址
    private final static String DEFAULT_BASS_IP = "222.87.184.250:1156";
    private final static String DEFAULT_VIDEO_IP = "222.87.184.250:8095";

//    private final static String DEFAULT_BASS_IP = "47.104.178.84:8025";
//    private final static String DEFAULT_VIDEO_IP = "47.104.178.84:8095";


    private final static String IP_1 = "http://";

    public final static List<String> phoneNub1 = new ArrayList<>();
    public final static List<String> phoneNub2 = new ArrayList<>();

    public static void setAccount(User user) {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.AccountSettings.ACCOUNT_SETTINGS);
        spUtil.putString(SPUtil.AccountSettings.USER_NAME, user.getUserName());
        spUtil.putString(SPUtil.AccountSettings.PASSWORD, user.getUserPwd());
        spUtil.putString(SPUtil.AccountSettings.TOKEN, user.getToken());
    }

    public static User getAccount() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.AccountSettings.ACCOUNT_SETTINGS);
        String name = spUtil.getString(SPUtil.AccountSettings.USER_NAME, "");
        String pwd = spUtil.getString(SPUtil.AccountSettings.PASSWORD, "");
        User user = new User(name, pwd);
        LogUtil.d(user.toString());
        return user;
    }

    public static void setLogin(Context context, boolean isLogin) {
        SPUtil spUtil = new SPUtil(context, SPUtil.AppSetting.APP_SETTING);
        spUtil.putBoolean(SPUtil.AppSetting.IS_LOGIN, isLogin);
    }

    public static boolean isLogin(Context c) {
        SPUtil spUtil = new SPUtil(c, SPUtil.AppSetting.APP_SETTING);
        return spUtil.getBoolean(SPUtil.AppSetting.IS_LOGIN, false);
    }

    public static void setEnter(Context context, boolean isEnter) {
        SPUtil spUtil = new SPUtil(context, SPUtil.AccountSettings.ACCOUNT_SETTINGS);
        spUtil.remove(SPUtil.AccountSettings.IS_ENTER);
        spUtil.putBoolean(SPUtil.AccountSettings.IS_ENTER, isEnter);
    }

    public static boolean isEnter(Context c) {
        SPUtil spUtil = new SPUtil(c, SPUtil.AccountSettings.ACCOUNT_SETTINGS);
        return spUtil.getBoolean(SPUtil.AccountSettings.IS_ENTER, false);
    }


    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.AccountSettings.ACCOUNT_SETTINGS);
        String token = spUtil.getString(SPUtil.AccountSettings.TOKEN, "");
        LogUtil.d("getToken: " + token);
        return token;
    }

    public static void setToken(String token) {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.AccountSettings.ACCOUNT_SETTINGS);
        spUtil.putString(SPUtil.AccountSettings.TOKEN, token);
    }

    /**
     * 存入基站
     *
     * @param context
     * @param cid
     */
    public static void setBaseSation(Context context, int cid) {
        SPUtil spUtil = new SPUtil(context, SPUtil.DeviceSettings.DEVICE_SETTINGS);
        spUtil.remove(SPUtil.DeviceSettings.CID);
        spUtil.putInt(SPUtil.DeviceSettings.CID, cid);
    }

    /**
     * 获取基站
     *
     * @return
     */
    public static int getBaseSation(Context context) {
        SPUtil spUtil = new SPUtil(context, SPUtil.DeviceSettings.DEVICE_SETTINGS);
        return spUtil.getInt(SPUtil.DeviceSettings.CID, 0);
    }

    /**
     * 存入关机前所处界面标识
     *
     * @param context
     * @param activtiy
     */
    public static void setActivity(Context context, String activtiy) {
        SPUtil spUtil = new SPUtil(context, SPUtil.DeviceSettings.DEVICE_SETTINGS);
        spUtil.remove(SPUtil.DeviceSettings.APP_ACTIVITY);
        spUtil.putString(SPUtil.DeviceSettings.APP_ACTIVITY, activtiy);
    }

    /**
     * 获取关机前所处界面标识
     *
     * @return
     */
    public static String getActivity() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.DeviceSettings.DEVICE_SETTINGS);
        String activity = spUtil.getString(SPUtil.DeviceSettings.APP_ACTIVITY, "");
        LogUtil.d("getActivity: " + activity);
        return activity;
    }

    /**
     * 存入相机可用状态
     *
     * @param context
     * @param disable
     */
    public static void setIsCameraDisable(Context context, boolean disable) {
        SPUtil spUtil = new SPUtil(context, SPUtil.DeviceSettings.DEVICE_SETTINGS);
        spUtil.remove(SPUtil.DeviceSettings.IS_CAMERADISABLE);
        spUtil.putBoolean(SPUtil.DeviceSettings.IS_CAMERADISABLE, disable);
    }

    /**
     * 获取相机可用状态
     *
     * @param context
     * @return
     */
    public static boolean getIsCameraDisable(Context context) {
        SPUtil spUtil = new SPUtil(context, SPUtil.DeviceSettings.DEVICE_SETTINGS);
        return spUtil.getBoolean(SPUtil.DeviceSettings.IS_CAMERADISABLE, false);
    }

    /**
     * 存入录音可用状态
     *
     * @param context
     * @param disable
     */
    public static void setIsAudioDisable(Context context, boolean disable) {
        SPUtil spUtil = new SPUtil(context, SPUtil.DeviceSettings.DEVICE_SETTINGS);
        spUtil.remove(SPUtil.DeviceSettings.IS_AUDIODISABLE);
        spUtil.putBoolean(SPUtil.DeviceSettings.IS_AUDIODISABLE, disable);
    }

    /**
     * 获取录音可用状态
     *
     * @param context
     * @return
     */
    public static boolean getIsAudioDisable(Context context) {
        SPUtil spUtil = new SPUtil(context, SPUtil.DeviceSettings.DEVICE_SETTINGS);
        return spUtil.getBoolean(SPUtil.DeviceSettings.IS_AUDIODISABLE, false);
    }

    /**
     * 获取ip
     *
     * @return
     */
    public static String getBassIp() {
//        SPUtil spUtil = new SPUtil(x.app(), SPUtil.IpSetting.IP_SETTING);
//        String ip = spUtil.getString(SPUtil.IpSetting.BASS_IP, DEFAULT_BASS_IP);
//        LogUtil.d("ip: " + ip);
        return IP_1 + DEFAULT_BASS_IP;
    }

    /**
     * 获取视频ip
     *
     * @return
     */
    public static String getVideoIp() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.IpSetting.IP_SETTING);
        String ip = spUtil.getString(SPUtil.IpSetting.VIDEO_IP, DEFAULT_VIDEO_IP);
        return IP_1 + ip;
    }

    /**
     * 获取ip
     *
     * @return
     */
    public static String getBassIpOnly() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.IpSetting.IP_SETTING);
        String ip = spUtil.getString(SPUtil.IpSetting.BASS_IP, DEFAULT_BASS_IP);
        return ip;
    }

    /**
     * 获取视频ip
     *
     * @return
     */
    public static String getVideoIpOnly() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.IpSetting.IP_SETTING);
        String ip = spUtil.getString(SPUtil.IpSetting.VIDEO_IP, DEFAULT_VIDEO_IP);

        return ip;
    }

    /**
     * 修改ip
     *
     * @param bassIp
     * @param videoIp
     */
    public static void setIp(String bassIp, String videoIp) {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.IpSetting.IP_SETTING);
        spUtil.putString(SPUtil.IpSetting.BASS_IP, bassIp);
        spUtil.putString(SPUtil.IpSetting.VIDEO_IP, videoIp);
    }

    public static void setNotice(String title, long time) {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.NoticeSetting.NOTICE_SETTING);
        spUtil.putString(SPUtil.NoticeSetting.NOTICE_TITLE, title);
        spUtil.putLong(SPUtil.NoticeSetting.NOTICE_TIME, time);
    }

    public static NoticeBean getNotice() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.NoticeSetting.NOTICE_SETTING);
        String title = spUtil.getString(SPUtil.NoticeSetting.NOTICE_TITLE, "标题");
        long aLong = spUtil.getLong(SPUtil.NoticeSetting.NOTICE_TIME, 0);
        return new NoticeBean(title, aLong == 0 ? "时间" : TimeUtil.getChatTime(aLong));
    }

    public static long getDefaultGroup() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.AccountSettings.ACCOUNT_SETTINGS);
        long aLong = spUtil.getLong(SPUtil.AccountSettings.DEFAULT_GROUP, 0);
        return aLong;
    }

    public static void setDefaultGroup(long defaultGroup) {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.AccountSettings.ACCOUNT_SETTINGS);
        spUtil.putLong(SPUtil.AccountSettings.DEFAULT_GROUP, defaultGroup);
    }

    public static void setNoticeRead(boolean isRead) {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.NoticeSetting.NOTICE_SETTING);
        spUtil.putBoolean(SPUtil.NoticeSetting.IS_READ, isRead);
    }

    public static boolean isNoticeRead() {
        SPUtil spUtil = new SPUtil(x.app(), SPUtil.NoticeSetting.NOTICE_SETTING);
        return spUtil.getBoolean(SPUtil.NoticeSetting.IS_READ, true);
    }
}
