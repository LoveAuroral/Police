package com.dark_yx.policemaincommon.Util;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;


/**
 * Created by Ligh on 2016/10/27 10:14
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class PowerUtil {
    public static PowerManager.WakeLock PowerOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);// init powerManager
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.SCREEN_DIM_WAKE_LOCK, "policeMain");
        Log.d("PowerUtil","亮屏");
        return wakeLock;
    }
}
