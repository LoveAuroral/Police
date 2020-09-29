package com.dark_yx.policemain.broadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by Ligh on 2016/10/18 17:53
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 * 监听耳机
 */

public class HeadsetPlugReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = ((AudioManager) context.getSystemService(AUDIO_SERVICE));
        if (intent.hasExtra("state")) {
            if (intent.getIntExtra("state", 0) == 0) {
//                Toast.makeText(context, "拔下耳机", Toast.LENGTH_LONG).show();
                audioManager.setSpeakerphoneOn(true);
            } else if (intent.getIntExtra("state", 0) == 1) {
//                Toast.makeText(context, "插入耳机", Toast.LENGTH_LONG).show();
                audioManager.setSpeakerphoneOn(false);
            }
        }
    }

}