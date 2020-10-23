package com.dark_yx.policemain.service;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemaincommon.Util.DataUtil;

/**
 * @author xulin
 */
public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    private ComponentName admin;

    @Override
    public void onCreate() {
        super.onCreate();
        admin = new ComponentName(this, DeviceReceiver.class);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (DataUtil.isEnter(getApplicationContext())) {
            String packageName = event.getPackageName().toString();
            int eventType = event.getEventType();
            Log.i("xulindev", "accessibility packageName = " + packageName + " eventType = " + eventType);
            String app = "com.dark_yx.policemain";
            String system = "android";
            String lockScreen = "com.android.systemui";
            String phone = "com.android.incallui";
            String calculator = "com.android.calculator2";
            if (packageName.equals(app) || packageName.equals(system) ||
                    packageName.equals(lockScreen) || packageName.equals(phone) ||
                    packageName.equals(calculator)) {
                return;
            }
            PhoneInterfaceUtil.killApplicationProcess(admin, packageName);
//            String qq = "com.tencent.mm";
//            if (packageName.equals(qq)) {
//                PhoneInterfaceUtil.killApplicationProcess(admin, qq);
//                return;
//            }
//            String wx = "com.tencent.mobileqq";
//            if (packageName.equals(wx)) {
//                PhoneInterfaceUtil.killApplicationProcess(admin, wx);
//                return;
//            }
//            String zfb = "com.eg.android.AlipayGphone";
//            if (packageName.equals(zfb)) {
//                PhoneInterfaceUtil.killApplicationProcess(admin, zfb);
//                return;
//            }
//            String dd = "com.alibaba.android.rimet";
//            if (packageName.equals(dd)) {
//                PhoneInterfaceUtil.killApplicationProcess(admin, dd);
//                return;
//            }
//            if (packageName.equals("com.huawei.android.internal.app")) {
////                lockScreen();
//                return;
//            }
//            if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && packageName.equals("com.android.systemui")) {
////                lockScreen();
//            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void lockScreen() {
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
