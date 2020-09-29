package com.dark_yx.policemain.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeMap;

/**
 *
 * Created by Administrator on 2018/1/26.
 */

public class ApkTool {
    /**
     * 获取最近运行，需要用户手工授予权限，查看最近应用
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getForegroundApp(Context context) {
        String topPackageName = null;
        Field mLastEventField = null;
        int lastEvent;
        @SuppressLint("WrongConstant") UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext()
                .getSystemService(Context.USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);

        if (stats != null) {
            TreeMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                NavigableSet<Long> keySet = mySortedMap.navigableKeySet();
                Iterator iterator = keySet.descendingIterator();
                while (iterator.hasNext()) {
                    UsageStats usageStats = mySortedMap.get(iterator.next());
                    if (mLastEventField == null) {
                        try {
                            mLastEventField = UsageStats.class.getField("mLastEvent");
                        } catch (NoSuchFieldException e) {
                            break;
                        }
                    }
                    if (mLastEventField != null) {
                        try {
                            lastEvent = mLastEventField.getInt(usageStats);
                        } catch (IllegalAccessException e) {
                            break;
                        }
                        if (lastEvent == 1) {
                            topPackageName = usageStats.getPackageName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (topPackageName == null) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        }
        return topPackageName;
    }
}
