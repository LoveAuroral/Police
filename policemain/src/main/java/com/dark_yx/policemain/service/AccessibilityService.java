package com.dark_yx.policemain.service;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;

/**
 * @author xulin
 */
public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        int eventType = event.getEventType();
        Log.i("xulindev", "accessibility packageName = " + packageName + " eventType = " + eventType);
//        if (packageName.equals("com.huawei.android.internal.app")){
//            ComponentName componentName = new ComponentName(getApplicationContext(), DeviceReceiver.class);
//            DevicePolicyManager manager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
//            if (manager != null) {
//                boolean adminActive = manager.isAdminActive(componentName);
//                if (adminActive) {
//                    manager.lockNow();
//                }
//            }
//            return;
//        }
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && packageName.equals("com.android.systemui")) {
            ComponentName componentName = new ComponentName(getApplicationContext(), DeviceReceiver.class);
            DevicePolicyManager manager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
            if (manager != null) {
                boolean adminActive = manager.isAdminActive(componentName);
                if (adminActive) {
                    manager.lockNow();
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
