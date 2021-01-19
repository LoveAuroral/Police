package com.dark_yx.policemain.util;

import android.content.Context;

import net.grandcentrix.tray.TrayPreferences;

public class SPUtil {

    private static TrayPreferences trayPreferences;
    private static final int VERSION = 1;

    /**
     * @param context  上下文
     * @param fileName 文件名
     */
    public SPUtil(Context context, String fileName) {
        trayPreferences = new TrayPreferences(context, fileName, VERSION);
    }

    public static TrayPreferences getInstance(Context context) {
        if (trayPreferences == null) {
            trayPreferences = new TrayPreferences(context, Phone.PHONE, VERSION);
        }
        return trayPreferences;
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
     * 手机信息
     */
    public class Phone {
        public static final String PHONE = "phone";

        public static final String MODE = "phone_mode";
    }
}  