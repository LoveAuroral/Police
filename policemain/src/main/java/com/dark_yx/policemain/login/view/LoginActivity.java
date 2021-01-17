package com.dark_yx.policemain.login.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.activity.BaseActivity;
import com.dark_yx.policemain.activity.GuideActivity;
import com.dark_yx.policemain.activity.MainActivity;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.login.bean.LoginInput;
import com.dark_yx.policemain.login.bean.LoginResult;
import com.dark_yx.policemain.login.contract.LoginContract;
import com.dark_yx.policemain.login.presenter.LoginPresenter;
import com.dark_yx.policemain.nfc.NFCHandle;
import com.dark_yx.policemain.service.AccessibilityService;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.PhoneInfoUtils;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.SystemInfo;
import com.dark_yx.policemaincommon.Util.WriteLogUtil;
import com.dou361.dialogui.DialogUIUtils;
import com.huawei.android.app.admin.DeviceApplicationManager;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;


/**
 * 登录
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements LoginContract.View {
    TextView txtIp, txtVideoIp;
    @ViewInject(R.id.txtWifi)
    private TextView txtWifi;
    @ViewInject(R.id.txtUserName)
    private EditText txtUserName;
    @ViewInject(R.id.txtUserPwd)
    private EditText txtUserPwd;
    private LoginContract.Presenter presenter;
    private Dialog dialog;
    private String userName, pwd;
    private NfcAdapter nfcAdapter;//nfc閫傞厤鍣�
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private PendingIntent pendingIntent;
    public static int loginCount = 0;
    private static final String TAG = "LoginActivity";
    private ComponentName mAdminName = null;
    private NFCHandle nfcHandle;
    private String bluetoothStatus, wifeStatus, recordingState, cameraState;
    PhoneInfoUtils phoneInfoUtils;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG, Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.CALL_PHONE};
    //权限数组下标
    //权限申请返回码
    private int requestCodePre = 321;
    //系统设置权限申请返回码
    private int requestCodeSer = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdminName = new ComponentName(this, DeviceReceiver.class);
        checkPermissons();
        initNFC();
        init();
    }

    public void checkDevice() {
        ComponentName testDeviceAdmin = new ComponentName(this, DeviceReceiver.class);
        DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (!mDPM.isAdminActive(testDeviceAdmin)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, testDeviceAdmin);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "");
            startActivity(intent);
        }
        //如果手机没有打开“允许查看”的权限，跳转到引导打开页面
        if (!isUsageAgeAccessOpen(getApplicationContext())) {
            Intent intent = new Intent(LoginActivity.this, GuideActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 检测辅助功能是否开启
     *
     * @param mContext
     * @return boolean
     */
    private boolean isAccessibilitySettingsOn(Context mContext, String serviceName) {
        int accessibilityEnabled = 0;
        // 对应的服务
        final String service = getPackageName() + "/" + serviceName;
        //Log.i(TAG, "service:" + service);
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    /**
     * 检查运行时权限
     */
    private void checkPermissons() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean perimissionFlas = false;
            for (String permissionStr : permissions) {
                // 检查该权限是否已经获取
                int per = ContextCompat.checkSelfPermission(this, permissionStr);
                LogUtil.d("权限申请：" + per + "");
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (per != PackageManager.PERMISSION_GRANTED) {
                    perimissionFlas = true;
                }
            }
            if (perimissionFlas) {
                // 如果有权限没有授予允许，就去提示用户请求授权
                ActivityCompat.requestPermissions(this, permissions, requestCodePre);
            }
        }
    }

    /**
     * 用户权限申请的回调方法grantResults授权结果
     *
     * @param requestCode  是我们自己定义的权限请求码
     * @param permissions  是我们请求的权限名称数组
     * @param grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数
     *                     组的长度，数组的数据PERMISSION_GRANTED表示允许权限，PERMISSION_DENIED表示我们点击了禁止权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCodePre) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 判断该权限是否已经授权
                boolean grantFlas = false;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        //-----------存在未授权-----------
                        grantFlas = true;
                    }
                }

                if (grantFlas) {
                    //-----------未授权-----------
                    // 判断用户是否点击了不再提醒。(检测该权限是否还可以申请)
                    // shouldShowRequestPermissionRationale合理的解释应该是：如果应用之前请求过此权限
                    //但用户拒绝了请求且未勾选"Don’t ask again"(不在询问)选项，此方法将返回 true。
                    //注：如果用户在过去拒绝了权限请求，并在权限请求系统对话框中勾选了
                    //"Don’t ask again" 选项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法会返回 false。
                    boolean shouldShowRequestFlas = false;
                    for (String per : permissions) {
                        if (shouldShowRequestPermissionRationale(per)) {
                            //-----------存在未授权-----------
                            shouldShowRequestFlas = true;
                        }
                    }
                    if (shouldShowRequestFlas) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                        builder.setTitle("提示");
                        builder.setMessage("当前还有必要权限没有授权，是否前往授权？");
                        builder.setCancelable(false);
                        builder.setPositiveButton("前往", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                goToAppSetting();
                            }
                        });
                        builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else {
                    //-----------授权成功-----------
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, requestCodeSer);
    }

    /**
     * 判断查看应用使用情况权限是否打开
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isUsageAgeAccessOpen(Context context) {
        long ts = System.currentTimeMillis();
        @SuppressLint("WrongConstant")
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext()
                .getSystemService("usagestats");
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    private void init() {
        checkDevice();
//        admin = new ComponentName(this, DeviceReceiver.class);
        User account = DataUtil.getAccount();
        String userName = account.getUserName();
        String pwd = account.getUserPwd();

        txtUserName.setText(userName);
        txtUserPwd.setText(pwd);
        txtWifi.setText(getWifiMsg());//wifi连接信息
        presenter = new LoginPresenter(this);
        phoneInfoUtils = new PhoneInfoUtils(this);
    }

    private String getWifiMsg() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();//wifi连接信息
        return wifiInfo.getSSID().equals("<unknown ssid>") ? "未连接" : wifiInfo.getSSID();//如果wifi信息uknown则显示未连接,否则显示wifi信息
    }

    /**
     * 初始化NFC
     */
    private void initNFC() {
        nfcHandle = new NFCHandle();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef,};
        techListsArray = new String[][]{new String[]{NfcF.class.getName()}};
        Intent intent = this.getIntent();
        String intentActionStr = intent.getAction();// 获取到本次启动的action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intentActionStr)// NDEF类型
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentActionStr)// 其他类型
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intentActionStr)) {// 未知类型
            //在intent中读取Tag id
            byte[] bytesId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            LogUtil.d("bytesId: " + bytesId.toString());
        }

        String getNfcMessage = nfcHandle.readMessage(getIntent());
        LogUtil.d("我就看看走没走" + getNfcMessage);
//        nfcHandle.openApp(this, getNfcMessage, mAdminName);
        nfcHandle.switchs(this, getNfcMessage, mAdminName);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String getNfcMessage = nfcHandle.readMessage(intent);
        LogUtil.d("onNewIntentLoginA: " + getNfcMessage);
        nfcHandle.switchs(this, getNfcMessage, mAdminName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataUtil.setActivity(this, "LoginActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isAccessibilitySettingsOn(LoginActivity.this, AccessibilityService.class.getCanonicalName())) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, AccessibilityService.class);
            startService(intent);
        }
//        LogUtil.d("isEnter--->>>"+DataUtil.isEnter(this));
        if (DataUtil.isEnter(this)) {
            PhoneInterfaceUtil.setPowerDisabled(mAdminName, true);//禁用通过电源键进入关机界面
            PhoneInterfaceUtil.setBarStatus(mAdminName, true);//禁用下拉状态栏
        }
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);//寮€鍚墠鍙板彂甯冪郴缁�
        }
        if (DataUtil.isEnter(getApplicationContext())) {
            DeviceApplicationManager manager = new DeviceApplicationManager();
            manager.killApplicationProcess(mAdminName, "com.tencent.mm");
            manager.killApplicationProcess(mAdminName, "com.tencent.mobileqq");
            manager.killApplicationProcess(mAdminName, "com.eg.android.AlipayGphone");
            manager.killApplicationProcess(mAdminName, "com.alibaba.android.rimet");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        System.gc();
        WriteLogUtil.writeLog("软件停止运行", "LoginActivity onDestroy");
    }

    @Event(value = {R.id.btnLogin, R.id.btnSelectWifi, R.id.btnSelectLoginMode, R.id.btnFlashWifi, R.id.btnIP}, type = View.OnClickListener.class)
    private void button_Click(View view) throws InterruptedException {
        switch (view.getId()) {
            case R.id.btnLogin://登录事件
                login();
                break;
            case R.id.btnSelectWifi://选择wifi
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));//wifi设置
                break;
            case R.id.btnFlashWifi://刷新wifi
                txtWifi.setText(getWifiMsg());
                break;
            case R.id.btnIP://ip更改设置
                startIpSet();
                break;
            case R.id.btnSelectLoginMode://ip更改设置
                final EditText input = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("请输入密码")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(input)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String result = input.getText().toString();//获取密码输入框输入值
                                if (result.equals("admin@123")) {
                                    CommonMethod.sendStatus(false, getApplicationContext());
                                    PhoneInterfaceUtil.exitApp(getApplicationContext(), mAdminName);
                                } else {
                                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();//显示dialog对话框
                break;
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loginCount = 0;
        }
    };

    private class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5 * 60 * 1000);//线程暂停5分钟，单位毫秒
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);//发送消息
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void login() {
//        boolean isRoot = PhoneInterfaceUtil.isRoot(mAdminName);
        boolean isRoot = false;
        userName = txtUserName.getText().toString();
        pwd = txtUserPwd.getText().toString();
        if (PhoneInterfaceUtil.isWifiDisable(mAdminName)) {
            wifeStatus = "已禁用";
        } else {
            wifeStatus = "已启用";
        }
        if (PhoneInterfaceUtil.isBluetoothDisable(mAdminName)) {
            bluetoothStatus = "已禁用";
        } else {
            bluetoothStatus = "已启用";
        }
        if (PhoneInterfaceUtil.isMicrophoneDisabled(mAdminName)) {
            recordingState = "已禁用";
        } else {
            recordingState = "已启用";
        }
        if (PhoneInterfaceUtil.isVideoDisabled(mAdminName)) {
            cameraState = "已禁用";
        } else {
            cameraState = "已启用";
        }
        String simSerialNumber = phoneInfoUtils.getIccid();

        LogUtil.d("bluetoothStatus2" + wifeStatus + "\n" + bluetoothStatus + "\n" + simSerialNumber + isRoot);
        LoginInput.DeviceLoginModelBean bean = new LoginInput.DeviceLoginModelBean(SystemInfo.GetIMEI(this),
                CommonMethod.getVersion(this), Build.VERSION.RELEASE, simSerialNumber, wifeStatus, bluetoothStatus);
        LoginInput input = new LoginInput(userName, pwd, bean);
        presenter.login(input);
    }


    private void startIpSet() {
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("请输入密码")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(input)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = input.getText().toString();//获取密码输入框输入值
                        if (result.equals("00")) {
                            sureIpSet();
                        } else {
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("取消", null);
        builder.show();//显示dialog对话框
    }

    private void sureIpSet() {
        LayoutInflater layoutInflater = getLayoutInflater();
        final View viewAddEmployee = layoutInflater.inflate(R.layout.ip_set_view, null);
        txtIp = (TextView) viewAddEmployee.findViewById(R.id.txtIp);
        txtVideoIp = (TextView) viewAddEmployee.findViewById(R.id.txtVideoIp);
        txtIp.setText(DataUtil.getBassIpOnly());//设置默认显示ip
        txtVideoIp.setText(DataUtil.getVideoIpOnly());//设置默认显示视频ip
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("请输入服务器IP地址")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(viewAddEmployee)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ip = txtIp.getText().toString();
                        String videoIp = txtVideoIp.getText().toString();
                        DataUtil.setIp(ip, videoIp);
                        ApiFactory.reSet();
                    }
                })
                .setNegativeButton("取消", null);
        builder.show();//显示对话框
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("xulindev", "LoginActivity 按键拦截 = keyCode " + keyCode + " isEnter " + DataUtil.isEnter(this));
        if (DataUtil.isEnter(this)) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;//return true;拦截事件传递,从而屏蔽back键。
            }
            if (KeyEvent.KEYCODE_HOME == keyCode) {
                Toast.makeText(getApplicationContext(), "请点击登录，进入管控系统", Toast.LENGTH_SHORT).show();
                return true;//同理
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void showDialog() {
        dialog = DialogUIUtils.showLoadingVertical(this, "正在登录...").show();
    }

    @Override
    public void dismissDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess(LoginResult result) {
        LogUtil.d("loginSuccess： " + result.toString());
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

        String token = "Bearer " + result.getResult();
        User user = new User(userName, pwd, token);
        DataUtil.setAccount(user);
        DataUtil.setLogin(this, true);

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    /**
     * 解锁登录限制
     */
    class UnlockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            loginCount = 0;
        }
    }
}
