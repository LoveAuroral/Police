package com.dark_yx.policemain.broadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dark_yx.policemaincommon.Util.SystemInfo;

//电池广播
public class BatteryReceiver extends BroadcastReceiver {
    public BatteryReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //判断它是否是为电量变化的Broadcast Action
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            //获取当前电量
            int level = intent.getIntExtra("level", 0);
            //电量的总刻度
            int scale = intent.getIntExtra("scale", 100);
            //把它转成百分比
            SystemInfo.BatteryScale = ((level * 100) / scale) + "%";
//            Log.d("Battery", SystemInfo.BatteryScale);
            Toast.makeText(context, ((level * 100) / scale) + "%", Toast.LENGTH_SHORT);
//            Log.d("Battery", ((level * 100) / scale) + "%");
        }
    }
}
