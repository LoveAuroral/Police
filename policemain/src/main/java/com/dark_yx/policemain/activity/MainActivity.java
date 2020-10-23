package com.dark_yx.policemain.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dark_yx.policemain.PoliceMainApplication;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.adapter.MyFragmentAdapter;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.chat.ChatService;
import com.dark_yx.policemain.chat.beans.ChatGroupBean;
import com.dark_yx.policemain.chat.beans.ExitGroupBean;
import com.dark_yx.policemain.chat.callback.IRecentCallback;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemain.chat.view.chatui.ui.activity.ChatActivity;
import com.dark_yx.policemain.common.WoogeenSurfaceRenderer;
import com.dark_yx.policemain.entity.GetAppWhiteListAsyncBean;
import com.dark_yx.policemain.fragment.HomepageFragment;
import com.dark_yx.policemain.fragment.MessageFragment;
import com.dark_yx.policemain.fragment.MineFragment;
import com.dark_yx.policemain.fragment.PTTFragment;
import com.dark_yx.policemain.login.bean.LoginInput;
import com.dark_yx.policemain.login.bean.LoginResult;
import com.dark_yx.policemain.login.contract.LoginContract;
import com.dark_yx.policemain.login.presenter.LoginPresenter;
import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemain.nfc.NFCHandle;
import com.dark_yx.policemain.service.AccessibilityService;
import com.dark_yx.policemain.service.ListenCallService;
import com.dark_yx.policemain.util.CheckVersionTask;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.DownloadApkUtil;
import com.dark_yx.policemain.util.PhoneInfoUtils;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemaincommon.Models.AppInfo;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.FileUtil;
import com.dark_yx.policemaincommon.Util.PowerUtil;
import com.dark_yx.policemaincommon.Util.SystemInfo;
import com.dark_yx.policemaincommon.Util.UserTreeUtil;
import com.dark_yx.policemaincommon.Util.VibratorUtil;
import com.dark_yx.policemaincommon.Util.WriteLogUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;
import com.google.gson.Gson;
import com.huawei.android.app.admin.DeviceApplicationManager;
import com.intel.webrtc.base.ActionCallback;
import com.intel.webrtc.base.RemoteStream;
import com.intel.webrtc.base.WoogeenException;
import com.intel.webrtc.p2p.PeerClient;
import com.intel.webrtc.p2p.PublishOptions;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.xutils.common.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


/**
 * Created by lmp on 2016/4/29.
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, IRecentCallback, View.OnClickListener, LoginContract.View {
    private final static String TAG = "PhoneMainActivity";
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private List<Fragment> fragments;
    private MyFragmentAdapter adapter;
    private Intent intent;
    private UnReadReceiver unReadReceiver;
    private BadgeView badgeView;
    private int item = 0;
    private ReflashReceiver receiver;
    private Vibrator vibrator;
    private PowerManager.WakeLock wakeLock;
    private boolean isLongPressKey;//是否长按
    private ComponentName mAdminName = null;
    private static final String phone_pkg_name = "com.example.vicknl2.phone";
    private static final String phone_version_name = "3.1";
    List<String> packageNameList;
    ChatService chatService;
    private ChatDb chatDb;
    private MessageFragment messageFragment;
    private ImageView ivSearch;
    private ImageView ivAdd;
    private ScreenListener listener;
    private static final String CAMERA_PKG = "com.huawei.camera";
    boolean isLock;
    private LoginContract.Presenter presenter;
    PhoneInfoUtils phoneInfoUtils;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.CALL_PHONE};
    private AlertDialog mFirstDialog;
    private String bluetoothStatus, wifeStatus, recordingState, cameraState, phoneJosn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.d("PhoneMainActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_activity_main);
        mAdminName = new ComponentName(this, DeviceReceiver.class);
        packageNameList = new ArrayList<>();
        nfcHandle = new NFCHandle();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this,
                        getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        setScreenListener();
        init();
        getAppWhiteListAsync();
//        thread.start();
        // 程序崩溃时触发线程,以下用来捕获程序崩溃异常
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    /**
     * 获取允许使用的app
     */
    private void getAppWhiteListAsync() {
        String file = new File(Environment.getExternalStorageDirectory() + "/Police/", "canuseApp.txt").
                getAbsolutePath();
        ApiFactory.getService().getAppWhiteListAsync(DataUtil.getToken()).enqueue(new MyCallBack<GetAppWhiteListAsyncBean>() {
            @Override
            public void onSuc(Response<GetAppWhiteListAsyncBean> response) {
                GetAppWhiteListAsyncBean body = response.body();
                for (int i = 0; i < body.getResult().size(); i++) {
                    String packageN = body.getResult().get(i).getPackageName();
                    packageNameList.add(packageN);
                }
                String str = new Gson().toJson(packageNameList);
                UserTreeUtil.deleteFile(file);
                writeLog(packageNameList.toString(), "/Police", "canuseApp.txt");
                LogUtil.d("packageNameList" + packageNameList.toString());
            }

            @Override
            public void onFail(String message) {
                File file = new File(Environment.getExternalStorageDirectory() + "/Police/", "canuseApp.txt");
                if (file.exists()) {
                    phoneJosn = FileUtil.getFile("canuseApp.txt");
                    Log.d("phoneJosn=", phoneJosn);
                    String[] ss = phoneJosn.split(",");
                    for (int s = 0; s < ss.length; s++) {
                        String pkg = ss[s];
                        packageNameList.add(pkg);
                    }
                }
                LogUtil.d("packageNames---->>" + packageNameList.toString());
            }
        });

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

    /**
     * 实时监测允许使用的应用是否在屏幕最顶层
     */
    Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (true) {
                LogUtil.d("enter状态---->" + DataUtil.isEnter(getApplicationContext()));
                if (DataUtil.isEnter(getApplicationContext())) {
                    LogUtil.d("packageNameList" + packageNameList.toString());
                    try {
                        String str = ApkTool.getForegroundApp(getApplicationContext());
                        LogUtil.d("use app pkgName: " + str);
                        LogUtil.d("can use app list: " + packageNameList);
                        if (packageNameList != null && packageNameList.size() > 0 && str != null && str.length() > 0) {
                            if (!packageNameList.contains(str)) {
//                            ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                                    .getSystemService(Context.ACTIVITY_SERVICE);
//                            activityManager.moveTaskToFront(getTaskId(), 0);
                                DataUtil.setEnter(getApplicationContext(), true);
                                PhoneInterfaceUtil.openInit(mAdminName, getApplicationContext().getPackageName(), MainActivity.class.getCanonicalName(), getApplicationContext());
                                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myIntent);
                            }
                        }
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        }
    };

    private void initCanUseAppData() {
    }


    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            //发生崩溃异常时退出管控
            CommonMethod.sendStatus(false, getApplicationContext());
            PhoneInterfaceUtil.exitApp(getApplicationContext(), mAdminName);
        }
    };

    public static final String TAG_EXI4T = "exit";
    private NFCHandle nfcHandle;
    private NfcAdapter nfcAdapter;//nfc
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;

    /**
     * 初始化NFC
     */
    private void initNFC() {
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
        nfcHandle.switchs(this, getNfcMessage, mAdminName);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String getNfcMessage = nfcHandle.readMessage(intent);
        LogUtil.d("onNewIntent: " + getNfcMessage);
        nfcHandle.switchs(this, getNfcMessage, mAdminName);
    }

    /**
     * 监听屏幕
     */
//    public void setScreenListener() {
//        listener = new ScreenListener(this);
//        listener.register(new ScreenListener.ScreenStateListener() {
//            @Override
//            public void onScreenOn() {
//                isLock = checkPasswordToUnLock();
//                //亮屏
//                LogUtil.d("isLock:" + isLock);
//            }
//
//            @Override
//            public void onScreenOff() {
//                //息屏
//                /*for (int i = 0; i < packageNameList.size(); i++) {
//                    if (packageNameList.get(i).equals(CAMERA_PKG)) {
//                        packageNameList.remove(i);
//                    }
//                }*/
//                LogUtil.d("onScreenOff: " + packageNameList.toString());
//            }
//
//            @Override
//            public void onUserPresent() {
//                //解锁
//                /*if (isLock) {
//                    if (!packageNameList.contains(CAMERA_PKG)) {
//                        packageNameList.add(CAMERA_PKG);
//                    }
//                }*/
//            }
//        });
//    }


    /**
     * 检测屏幕是否设置密码
     *
     * @return
     */
    private boolean checkPasswordToUnLock() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
            return keyguardManager.isKeyguardSecure();
        } else {
            return isSecured();
        }
    }

    private boolean isSecured() {
        boolean isSecured = false;
        String classPath = "com.android.internal.widget.LockPatternUtils";
        try {
            Class<?> lockPatternClass = Class.forName(classPath);
            Object lockPatternObject = lockPatternClass.getConstructor(Context.class).newInstance(getApplicationContext());
            Method method = lockPatternClass.getMethod("isSecure");
            isSecured = (boolean) method.invoke(lockPatternObject);
        } catch (Exception e) {
            isSecured = false;
        }
        return isSecured;
    }


    private void checkPhone() {
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(phone_pkg_name, 0);
            String version = packageInfo.versionName;
            LogUtil.d("white phone version name: " + version);
            if (!version.equals(phone_version_name)) {
                unInstallPhone();
                installPhone();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            installPhone();
        }
    }

    private void unInstallPhone() {
        ComponentName componentName = new ComponentName(this, DeviceReceiver.class);
        PhoneInterfaceUtil.uninstallPackage(componentName, phone_pkg_name);
    }

    private void installPhone() {
        Toast.makeText(this, "正在更新通话白名单", Toast.LENGTH_LONG).show();
        AppInfo appInfo = new AppInfo();
        appInfo.setPackageName(phone_pkg_name);
        appInfo.setName("通话白名单");
        DownloadApkUtil util = new DownloadApkUtil(this, appInfo);
        try {
            util.getFileFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "通话白名单更新失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        PhoneInterfaceUtil.setBarStatus(mAdminName, false);//启用下拉状态栏
        DataUtil.setActivity(this, "PhoneMainActivity");
        CheckVersionTask task = new CheckVersionTask(this);
        task.start();
    }

    private void init() {
        initNFC();
        phoneInfoUtils = new PhoneInfoUtils(this);
        presenter = new LoginPresenter(this);
        mAdminName = new ComponentName(this, DeviceReceiver.class);
        chatDb = new ChatDb();
        try {
//            initAdmin();
        } catch (Exception e) {
            Log.d(TAG, "init: " + e.getMessage());
        }
        initView();
        registBro();
    }

    private void initAdmin() {
        if (DataUtil.isEnter(this)) {
            LogUtil.d("关机重启--->" + "true");
            PhoneInterfaceUtil.openInit(mAdminName, getPackageName(), CommonMethod.getCurrentActivityName(this), getApplicationContext());
            CommonMethod.sendStatus(true, this);
            loginIn();
        } else {
            LogUtil.d("关机重启--->" + "false");
//            startActivity(new Intent(this, LoginActivity.class));
            DataUtil.setEnter(this, false);
            CommonMethod.sendStatus(false, this);
            PhoneInterfaceUtil.exitApp(this, mAdminName);
        }
    }

    private void loginIn() {
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
        LogUtil.d("bluetoothStatus1" + wifeStatus + "\n" + bluetoothStatus);
        LoginInput.DeviceLoginModelBean bean = new LoginInput.DeviceLoginModelBean(SystemInfo.GetIMEI(this),
                CommonMethod.getVersion(this), Build.VERSION.RELEASE, simSerialNumber, wifeStatus, bluetoothStatus);
        User account = DataUtil.getAccount();
        LoginInput input = new LoginInput(account.getUserName(), account.getUserPwd(), bean);
        presenter.login(input);
    }


    private void initChat() {
        if (chatService == null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(new Intent(this, BaseSationService.class));
//            }
            bindService(new Intent(this, ChatService.class), chatServiceConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
        } else {
            LogUtil.d("初始化聊天" + chatService.getConnectState().toString());
            chatService.connection();
        }
    }

    ServiceConnection chatServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d("聊天服务开启成功");
            chatService = ((ChatService.ChatServiceBinder) service).getService();

            chatService.registerRecentCallback(MainActivity.this);

            chatService.connection();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("聊天服务开启失败");
        }
    };


    public void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        badgeView = new BadgeView(this, radioGroup);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ivAdd.setOnClickListener(this);
        ivSearch.setOnClickListener(this);

        HomepageFragment homepageFragment = new HomepageFragment();
        messageFragment = new MessageFragment();
        PTTFragment pttFragment = new PTTFragment();
        MineFragment mineFragment = new MineFragment();

        fragments = new ArrayList<>();
        fragments.add(homepageFragment);
        fragments.add(messageFragment);
        fragments.add(pttFragment);
        fragments.add(mineFragment);

        adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int number) {
                ((RadioButton) radioGroup.getChildAt(number)).setChecked(true);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        radioGroup.setOnCheckedChangeListener(this);

        mApplication = (PoliceMainApplication) getApplication();
        PoliceMainApplication.registerMainObServer(observerMain);
        login();
    }

    private void registBro() {
        IntentFilter unUeadFilter = new IntentFilter("com.lgh.unread");
        unReadReceiver = new UnReadReceiver();
        registerReceiver(unReadReceiver, unUeadFilter);

        IntentFilter filter = new IntentFilter("com.lgh.reflash");
        receiver = new ReflashReceiver();
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        /*if (isLock) {
            if (!packageNameList.contains(CAMERA_PKG)) {
                packageNameList.add(CAMERA_PKG);
            }
        }*/
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "<-------------------onDestroy");
//        listener.unregister();
        try {
            unregisterReceiver(unReadReceiver);
            unregisterReceiver(receiver);
            PoliceMainApplication.unRegisterMainObServer();
            unbindService(chatServiceConnection);
            LogUtil.d("onDestroy");
            stopService(new Intent(this, ListenCallService.class));
            WriteLogUtil.writeLog("软件停止运行", "ChatActivity onDestroy");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_homepage:
                item = 0;
                break;
            case R.id.rb_message:
                item = 1;
                badgeView.setVisibility(View.GONE);
                break;
            case R.id.rb_ptt:
                item = 2;
                break;
            case R.id.rb_mine:
                item = 3;
                break;
        }
        viewPager.setCurrentItem(item, false);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "点击按键:" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (event.getRepeatCount() == 0) {//识别长按短按的代码
                    event.startTracking();
                    isLongPressKey = false;
                } else {
                    isLongPressKey = true;
                }
                return event.getRepeatCount() == 0;
//                PoliceMainApplication.unRegisterMainObServer();
//                Log.d(TAG, "PTT");
//                intent = new Intent();
//                intent.setClass(this, Videop2pAllActivity.class);
//                intent.putExtra("type", "audio");
//                this.startActivity(intent);
//            return true;
            case 261://SOS
                PoliceMainApplication.unRegisterMainObServer();
                Log.d(TAG, "SOS");
                intent = new Intent();
                intent.setClass(this, Videop2pAllActivity.class);
                intent.putExtra("type", "alarm");
                this.startActivity(intent);
                break;
            case 260://PTT
                PoliceMainApplication.unRegisterMainObServer();
                Log.d(TAG, "PTT");
                intent = new Intent();
                intent.setClass(this, Videop2pAllActivity.class);
                intent.putExtra("type", "audio");
                this.startActivity(intent);
                break;
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(MainActivity.this, "系统返回键已禁用", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            LogUtil.d("长按音量下" + "-----ture");
            ChatGroupBean user = chatDb.getDefaultGroup();
            if (user != null) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("chatSendType", 1);
                intent.putExtra("sendId", user.getId());
                intent.putExtra("sendName", user.getName());
                intent.putExtra("isPtt", true);
                startActivity(intent);
            } else {
                showDia();
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    private void showDia() {
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setTitle("一键对讲默认群组")
                .setMessage("请先设置默认群组：聊天->我的群组->长按群名->设为默认群聊").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item = 2;
                viewPager.setCurrentItem(item, false);
            }
        }).show();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (isLongPressKey) {
                isLongPressKey = false;
                return false;
            } else {
                PoliceMainApplication.unRegisterMainObServer();
                Log.d(TAG, "SOS");
                intent = new Intent();
                intent.setClass(this, Videop2pAllActivity.class);
                intent.putExtra("type", "alarm");
                this.startActivity(intent);
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        /*if (!packageNameList.contains(CAMERA_PKG)) {
            packageNameList.add(CAMERA_PKG);
        }*/
        Log.d("PhoneMainActivity", "MainAconRestart");
        PoliceMainApplication.registerMainObServer(observerMain);
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

    @Override
    protected void onResume() {
        if (DataUtil.isLogin(getApplicationContext())) {
            PhoneInterfaceUtil.setPowerDisabled(mAdminName, false);//禁用通过电源键进入关机界面
            Log.d(TAG, "mainActivity---------onResume");
            if (nfcAdapter != null) {
                nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);//寮€鍚墠鍙板彂甯冪郴缁�
            }
        /*if (isLock) {
            if (packageNameList.contains(CAMERA_PKG)) {
                packageNameList.remove(CAMERA_PKG);
            } else {
                if (!packageNameList.contains(CAMERA_PKG)) {
                    packageNameList.add(CAMERA_PKG);
                }
            }
        }*/
            initChat();
            if (!isAccessibilitySettingsOn(MainActivity.this, AccessibilityService.class.getCanonicalName())) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, AccessibilityService.class);
                startService(intent);
            }
            if (DataUtil.isEnter(getApplicationContext())) {
                DeviceApplicationManager manager = new DeviceApplicationManager();
                manager.killApplicationProcess(mAdminName, "com.tencent.mm");
                manager.killApplicationProcess(mAdminName, "com.tencent.mobileqq");
                manager.killApplicationProcess(mAdminName, "com.eg.android.AlipayGphone");
                manager.killApplicationProcess(mAdminName, "com.alibaba.android.rimet");
            }
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        super.onResume();
    }

    public void toIntent(String extra) {
        PoliceMainApplication.unRegisterMainObServer();
        Intent intent = new Intent(MainActivity.this, Videop2pAllActivity.class);
        intent.putExtra("type", extra);
        startActivity(intent);
    }

    private AlertDialog dialog;
    private String destId = "";
    public PeerClient.PeerClientObserver observerMain = new PeerClient.PeerClientObserver() {
        private boolean mirror;

        @Override
        public void onInvited(final String peerId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    destId = peerId;
                    Log.d(TAG, "用户呼叫: " + destId);
                    if (destId.equals(PoliceMainApplication.ADMIN_PEERID)) {
                        PoliceMainApplication.peerClient.accept(destId,
                                new ActionCallback<Void>() {
                                    @Override
                                    public void onSuccess(Void result) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(WoogeenException e) {
                                        Log.d(TAG, e.getMessage());
                                    }

                                });

                    } else {
                        wakeLock = PowerUtil.PowerOn(MainActivity.this);
                        wakeLock.acquire(2000);
                        vibrator = VibratorUtil.Vibrate(MainActivity.this, 2);
                        MediaPlayer.create(MainActivity.this, R.raw.office).start();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("通话邀请");
                        builder.setCancelable(false);
                        builder.setMessage(peerId + " 发起通话");
                        builder.setPositiveButton("同意",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        vibrator.cancel();
                                        toIntent("accept" + peerId);
                                    }
                                });
                        builder.setNeutralButton("拒绝",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        try {
                                            wakeLock.release();
                                        } catch (Exception e) {
                                        }
                                        vibrator.cancel();
                                        denyUser(peerId);
                                    }
                                });
                        if (dialog != null) {
                            dialog = null;
                        }
                        dialog = builder.create();
                        dialog.show();
                    }
                }

            });
        }

        /**
         * 拒绝
         */
        private void denyUser(final String peerId) {
            PoliceMainApplication.peerClient.deny(peerId,
                    new ActionCallback<Void>() {

                        @Override
                        public void onSuccess(
                                Void result) {
                            Log.d(TAG,
                                    "拒绝:"
                                            + peerId);
                        }

                        @Override
                        public void onFailure(
                                WoogeenException e) {
                            Log.d(TAG,
                                    e.getMessage());
                        }

                    });
        }

        @Override
        public void onAccepted(final String peerId) {
            Log.d(TAG, "接收: " + peerId);
            runOnUiThread(new Runnable() {
                public void run() {
                }

            });
        }

        @Override
        public void onDenied(final String peerId) {
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
        }

        @Override
        public void onStreamAdded(final RemoteStream stream) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        @Override
        public void onDataReceived(final String peerId, final String msg) {
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });
        }

        @Override
        public void onStreamRemoved(final RemoteStream stream) {
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });

        }

        @Override
        public void onChatStopped(final String peerId) {
            Log.d(TAG, "结束: " + peerId);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (peerId.equals(PoliceMainApplication.ADMIN_PEERID)) {
                        ((PoliceMainApplication) getApplication()).detachStream(null, peerId);
                    }
                }
            });
        }

        @Override
        public void onChatStarted(final String peerId) {
            Log.d(TAG, "开始: " + peerId);
            if (peerId.equals(PoliceMainApplication.ADMIN_PEERID)) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        ((PoliceMainApplication) getApplication()).getNotNULLLocalStream();


                        WoogeenSurfaceRenderer localView = new WoogeenSurfaceRenderer(
                                MainActivity.this);

                        EglBase rootEglBase = EglBase.create();
                        localView.init(rootEglBase.getEglBaseContext(), null);
                        localView.setZOrderMediaOverlay(true);
                        mirror = true;
                        localView.setMirror(mirror);
                        try {

                            ((PoliceMainApplication) getApplication()).getNotNULLLocalStream().attach(localView);
                        } catch (Exception e) {
                        }

                        PublishOptions option = new PublishOptions();
                        option.setMaximumVideoBandwidth(200);
                        option.setMaximumAudioBandwidth(50);
                        PoliceMainApplication.peerClient.publish(((PoliceMainApplication) getApplication()).getNotNULLLocalStream(), peerId,
                                option, new ActionCallback<Void>() {

                                    @Override
                                    public void onFailure(WoogeenException arg0) {
                                        ((PoliceMainApplication) getApplication()).closeLocalStream();
                                        Log.e(TAG, arg0.toString());

                                    }

                                    @Override
                                    public void onSuccess(Void arg0) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((PoliceMainApplication) getApplication()).StreanList.add(PoliceMainApplication.ADMIN_PEERID);
                                            }
                                        });
                                    }

                                });

                    }
                });
            }
        }

        @Override
        public void onServerDisconnected() {
            Log.d(TAG, "服务器断开连接");
        }
    };

    private void login() {
        Thread theard = new Thread() {
            @Override
            public void run() {
                super.run();
                JSONObject loginObject = new JSONObject();
                try {
                    loginObject.put("host",
                            DataUtil.getVideoIp());
                    loginObject.put("token",
                            DataUtil.getAccount().getUserName());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                PoliceMainApplication.peerClient.connect(loginObject.toString(),
                        new ActionCallback<String>() {

                            @Override
                            public void onSuccess(String result) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Log.d(TAG, "语音对讲登录成功");
                                    }
                                });
                            }

                            @Override
                            public void onFailure(WoogeenException e) {
                                Log.d(TAG,
                                        "连接服务器失败:"
                                                + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        //
                                    }
                                });
                            }
                        });
            }
        };
        theard.start();
    }

    @Override
    public void recentChanged() {
        messageFragment.recentChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.iv_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void showError(String err) {
        LogUtil.e("login error: " + err);
    }

    @Override
    public void loginSuccess(LoginResult result) {
        LogUtil.d("login success");
        String token = "Bearer " + result.getResult();
        DataUtil.setToken(token);
        /*SugarRecord.delete(TokenBean.class);
        TokenBean tokenBean = new TokenBean(token);
        tokenBean.save();*/
        LogUtil.d("tokenis" + token);
    }


    /**
     * 未读消息广播
     */
    class UnReadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (item != 1) {
                badgeView.setBadgeMargin(610, 7);
                badgeView.setTextColor(Color.RED);
                badgeView.setTextSize(9);
                badgeView.setText(".");
                badgeView.show();
            }
        }
    }

    public void deleteExitGroup(ChatGroupBean groupBean) {
        if (groupBean.getCreatorUserId() == chatDb.getId()) {
            chatService.deleteGroup(groupBean.getId());
        } else {
            ExitGroupBean exitGroupBean = new ExitGroupBean();
            exitGroupBean.setGroupId(groupBean.getId());
            exitGroupBean.setUserId(chatDb.getId());
            chatService.exitGroup(exitGroupBean);
        }
    }

    /**
     * 刷新界面广播
     */
    class ReflashReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initView();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "MainActivity保存状态");
        //阻止activity保存fragment的状态
//        super.onSaveInstanceState(outState);
    }


}
