package com.dark_yx.policemain.broadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by lmp on 2016/5/4.
 */
public class FingerReceiver extends BroadcastReceiver {
    private Handler mHandler;

    public FingerReceiver(Handler mhandler) {
        this.mHandler = mhandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            mHandler.sendEmptyMessage(1);
        }
    }
}
