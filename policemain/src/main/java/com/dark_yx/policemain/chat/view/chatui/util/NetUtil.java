package com.dark_yx.policemain.chat.view.chatui.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.xutils.common.util.LogUtil;

/**
 * Created by Ligh on 2017/11/3 10:29
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class NetUtil {
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                LogUtil.d("NETWORK_WIFI");
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                LogUtil.d("NETWORK_MOBILE");
                return NETWORK_MOBILE;
            }
        } else {
            LogUtil.d("NETWORK_NONE");
            return NETWORK_NONE;
        }
        LogUtil.d("NETWORK_NONE");
        return NETWORK_NONE;
    }
}
