package com.dark_yx.policemain.nfc;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.dark_yx.policemain.activity.MainActivity;
import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemain.util.WhiteListUtil;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Util.DataUtil;

import org.xutils.common.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;


/**
 * Created by lmp on 2016/4/13.
 */
public class NFCHandle {
    private static final String TAG = "NFCHandle";
    public static final String EXIT = "com.dark_yx.policemain.exit";
    public static final String ENTER = "com.dark_yx.policemain.enter";
    public static final String WORK = "com.dark_yx.policemain.qd.work";


    /**
     * 检测手机是否有NFC设备或者设备是否打开
     */
    public void check(final Context context, NfcAdapter nfcAdapter, ComponentName admin) {
        if (nfcAdapter == null) {
            Toast.makeText(context, "设备不支持NFC！", Toast.LENGTH_SHORT).show();
        } else if (!nfcAdapter.isEnabled()) {
            PhoneInterfaceUtil.setNFCStatus(admin, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示")
                    .setMessage("NFC功能未打开,是否前往设置页面?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    /**
     * 读NFC上面的数据
     *
     * @param intent
     * @return
     */
    public String readMessage(Intent intent) {
        String s = null;
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            try {
                if (mNdefRecord != null) {
                    s = new String(mNdefRecord.getPayload(), "UTF-8");
                    LogUtil.d("paName:" + s);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return s;
    }


    /**
     * 刷卡退出app
     *
     * @param context
     * @param message
     */
    public void switchs(Context context, String message, ComponentName admin) {
        if (message != null && message.length() > 0) {
            if (message.contains(EXIT)) {
                LogUtil.d("enter" + DataUtil.isEnter(context));
                LogUtil.d("退出app" + message);
                DataUtil.setEnter(context, false);
                CommonMethod.sendStatus(false, context);
                PhoneInterfaceUtil.exitApp(context, admin);
            }
        }
    }

    /**
     * 刷卡打开app
     *
     * @param context
     * @param message
     */
    public void openApp(final Context context, String message, ComponentName admin) {
        LogUtil.d("进入app" + message);
        WhiteListUtil whiteListUtil = new WhiteListUtil(context, true);
        whiteListUtil.getData();
        DataUtil.setEnter(context, true);
        CommonMethod.sendStatus(true, context);
        initAdmin(context, admin);
        User account = DataUtil.getAccount();
        if (DataUtil.isLogin(context) && !TextUtils.isEmpty(account.getUserName())) {
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }

    public void initAdmin(Context context, ComponentName mAdminName) {
        startTheNetwork(context);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (devicePolicyManager != null && !devicePolicyManager.isAdminActive(mAdminName)) {
            Intent intent = new Intent();
            intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
            context.startActivity(intent);
        } else {
            PhoneInterfaceUtil.openInit(mAdminName, context.getPackageName(), LoginActivity.class.getCanonicalName(), context.getApplicationContext());
        }
    }

    public void startTheNetwork(Context context) {
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        Class[] getArgArray = null;
        Class[] setArgArray = new Class[]{boolean.class};
        Object[] getArgInvoke = null;
        try {
            Method mGetMethod = teleManager.getClass().getMethod("getDataEnabled", getArgArray);
            Method mSetMethod = teleManager.getClass().getMethod("setDataEnabled", setArgArray);
            boolean isOpen = (Boolean) mGetMethod.invoke(teleManager, getArgInvoke);
            LogUtil.d("isOpen" + isOpen);
            if (!isOpen) {
                mSetMethod.invoke(teleManager, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
