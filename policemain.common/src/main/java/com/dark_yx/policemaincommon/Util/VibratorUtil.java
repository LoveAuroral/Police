package com.dark_yx.policemaincommon.Util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Ligh on 2016/10/27 09:42
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 * 手机震动工具类
 */

public class VibratorUtil {
    /**
     * final Context context  ：调用该方法的上下文
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * int repeat ：只震动一次:-1
     */

    public static Vibrator Vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
        return vib;
    }

    public static Vibrator Vibrate(final Context context, int repeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        long[] pattern = {300, 400, 300, 400};
        vib.vibrate(pattern, repeat);
        return vib;
    }
}
