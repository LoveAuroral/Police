package com.dark_yx.policemain.broadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lmp on 2016/5/4.
 */
public class FaceReceiver extends BroadcastReceiver {
    public static List<FaceHandler> mListener = new ArrayList<FaceHandler>();

    @Override
    public void onReceive(Context context, Intent intent) {
        for (FaceHandler item : mListener) {
            item.onFaceSuccess();
        }
    }

    public interface FaceHandler {
        void onFaceSuccess();
    }
}

