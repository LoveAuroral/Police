package com.dark_yx.policemain.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import static com.dark_yx.policemain.chat.view.chatui.util.NetUtil.NETWORK_NONE;

public class NetUtil {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 4G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    /**
     * 判断有无网络
     *
     * @return true 有网, false 没有网络.
     */
    public static boolean isNetConnect(Context context) {
        return getNetworkState(context) != NETWORK_NONE;
    }
}
