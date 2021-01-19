package com.dark_yx.policemaincommon.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import net.grandcentrix.tray.TrayPreferences;

import java.lang.reflect.Method;
import java.util.UUID;

public class SPUtil {

    private TrayPreferences trayPreferences;
    private static TrayPreferences trayPreferences2;
    private static final int VERSION = 1;

    /**
     * @param context  上下文
     * @param fileName 文件名
     */
    public SPUtil(Context context, String fileName) {
        trayPreferences = new TrayPreferences(context, fileName, VERSION);
    }

    public static TrayPreferences getInstance(Context context) {
        if (trayPreferences2 == null) {
            trayPreferences2 = new TrayPreferences(context, AccountSettings.ACCOUNT_SETTINGS, VERSION);
        }
        return trayPreferences2;
    }


    @SuppressLint("PrivateApi")
    public static void setDeviceId(Context context) {
        String id;
        Method systemProperties;
        String ret;
        try {
            systemProperties = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            ret = (String) systemProperties.invoke(null, "ro.boot.serialno");
            if (TextUtils.isEmpty(ret)) {
                ret = "";
            }
        } catch (Exception e) {
            ret = null;
        }
        String deviceId = ret;
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            deviceId = new UUID(deviceId.hashCode(), ((long) deviceId.hashCode()) << 32).toString().replace("-", "");
        }
        if (deviceId == null) {
            id = "";
        } else {
            id = deviceId.toUpperCase();
        }
        trayPreferences2.put(AccountSettings.DEVICE_ID, id);
    }

    public static String getDeviceId(Context context) {
        getInstance(context);
        String id = trayPreferences2.getString(AccountSettings.DEVICE_ID, null);
        if (id == null) {
            setDeviceId(context);
            id = trayPreferences2.getString(AccountSettings.DEVICE_ID, "");
        }
        return id;
    }

    /**
     * 向SP存入指定key对应的数据
     * 其中value可以是String、boolean、float、int、long等各种基本类型的值
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        trayPreferences.put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        trayPreferences.put(key, value);
    }

    public void putFloat(String key, float value) {
        trayPreferences.put(key, value);
    }

    public void putInt(String key, int value) {
        trayPreferences.put(key, value);
    }

    public void putLong(String key, long value) {
        trayPreferences.put(key, value);
    }

    /**
     * 清空SP里所以数据
     */
    public void clear() {
        trayPreferences.clear();
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key
     */
    public void remove(String key) {
        trayPreferences.remove(key);
    }

    /**
     * 获取SP数据里指定key对应的value。如果key不存在，则返回默认值defValue。
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        return trayPreferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return trayPreferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return trayPreferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return trayPreferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return trayPreferences.getLong(key, defValue);
    }

    /**
     * 判断SP是否包含特定key的数据
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return trayPreferences.contains(key);
    }


    /**
     * 账户信息
     */
    public class AccountSettings {
        public static final String ACCOUNT_SETTINGS = "account_settings";

        public static final String IS_ENTER = "is_enter";
        public static final String USER_NAME = "user_name";
        public static final String PASSWORD = "password";
        public static final String TOKEN = "token";
        public static final String DEFAULT_GROUP = "group";
        public static final String DEVICE_ID = "device_id";
    }

    public class IpSetting {
        public static final String IP_SETTING = "ip_setting";
        public static final String BASS_IP = "bass_ip";
        public static final String VIDEO_IP = "video_ip";
    }

    public class NoticeSetting {
        public static final String NOTICE_SETTING = "notice_setting";
        public static final String NOTICE_TITLE = "notice_title";
        public static final String NOTICE_TIME = "notice_time";
        public static final String IS_READ = "is_read";
    }

    public class AppSetting {
        public static final String APP_SETTING = "app_setting_3.1.6";
        public static final String IS_LOGIN = "is_login";
    }

    public class DeviceSettings {
        public static final String DEVICE_SETTINGS = "device_settings";
        public static final String APP_ACTIVITY = "app_activity";
        public static final String IS_CAMERADISABLE = "is_camera_disable";
        public static final String IS_AUDIODISABLE = "is_audio_disable";
        public static final String APP_VERSION = "app_version";
        public static final String CID = "cid";

    }
}  