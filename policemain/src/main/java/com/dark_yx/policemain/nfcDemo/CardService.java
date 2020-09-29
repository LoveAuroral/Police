package com.dark_yx.policemain.nfcDemo;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dark_yx.policemain.activity.MainActivity;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.beans.WorkInput;
import com.dark_yx.policemain.beans.WorkResult;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemain.util.WhiteListUtil;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.FileUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;
import com.newabel.nfcsdk.NfcHelper;

import org.xutils.common.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

import retrofit2.Response;

public class CardService extends HostApduService implements NfcHelper.NfcCallBack {
    private final String TAG = this.getClass().getSimpleName();

    private NfcHelper mNfcHelper;
    /**
     * CardID 八位 即SN,可使用其他字符串(账号，手机号，IMEI,ANDROID_ID等)代替，请保持唯一性,为了保持唯一性。
     */
    private SpHelper mSpHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mSpHelper = new SpHelper(this);
        mNfcHelper = new NfcHelper(this);
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        String mCardID = mSpHelper.getString(mSpHelper.mCardID, "");
        Log.d("mCardID is", mCardID);
        Log.d("commandApdu is", commandApdu.toString());
        if (!TextUtils.isEmpty(mCardID)) {
            mNfcHelper.setSN(mCardID);
            return mNfcHelper.processCommandApdu(commandApdu);
        }
        return new byte[0];
    }

    @Override
    public void onDeactivated(int reason) {

    }


    @Override
    public void dealWithMode(int mode, int state) {
        Log.d("modeis", mode + "\n" + state);
        openApp(this, mode, state);
    }

    public void openApp(final Context context, int mode, int state) {
        LogUtil.d("data is " + mode + "" + state + "");
        File file = new File(Environment.getExternalStorageDirectory() + "/Police/MODE.txt");
        FileUtil.deleteFile(file);
        FileUtil.writeMode(String.valueOf(mode));
        ComponentName admin = new ComponentName(context, DeviceReceiver.class);
        if (state == 0) {
            if (mode == 2) {
                DataUtil.setEnter(context, false);
                CommonMethod.sendStatus(false, context);
                PhoneInterfaceUtil.exitApp(context, admin);
            } else {
                WhiteListUtil whiteListUtil = new WhiteListUtil(context, true);
                whiteListUtil.getData();
                /* 启动当前应用程序 */
                DataUtil.setEnter(context, true);
                CommonMethod.sendStatus(true, context);
                initAdmin(context, admin);
                User account = DataUtil.getAccount();
                if (DataUtil.isLogin(context) && !TextUtils.isEmpty(account.getUserName())) {
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            }
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

    public void writeLog(String message, String dir, String filePath) {
        File myDir = new File(Environment.getExternalStorageDirectory().getPath() + dir);
        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                return;
            }
        }
        File file = new File(myDir, filePath);
        BufferedWriter fout;
        try {
            fout = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            fout.write(message + "\n");
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
