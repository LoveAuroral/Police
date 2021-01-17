package com.dark_yx.policemain;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.dark_yx.policemain.activity.BaseActivity;
import com.dark_yx.policemain.broadCastReceiver.BatteryReceiver;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.common.SocketSignalingChannel;
import com.dark_yx.policemain.common.WoogeenSurfaceRenderer;
import com.dark_yx.policemain.util.MyCrashHandler;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.intel.webrtc.base.ClientContext;
import com.intel.webrtc.base.LocalCameraStream;
import com.intel.webrtc.base.LocalCameraStreamParameters;
import com.intel.webrtc.base.MediaCodec.VideoCodec;
import com.intel.webrtc.base.Stream;
import com.intel.webrtc.base.WoogeenException;
import com.intel.webrtc.p2p.PeerClient;
import com.intel.webrtc.p2p.PeerClientConfiguration;

import org.jivesoftware.smack.SmackAndroid;
import org.webrtc.EglBase;
import org.webrtc.PeerConnection.IceServer;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dark_yx on 2016-03-08.
 * 应用主程序
 */
public class PoliceMainApplication extends Application {
    public final static String ADMIN_PEERID = "0000";//一键报警用户
    public static PeerClient peerClient;
    public static PeerClient.PeerClientObserver mainObserver;
    public static String iMEI;
    private LocalCameraStream localStream;
    public ArrayList<String> StreanList = new ArrayList<String>();//对讲列表
    public static final String TAG = "PoliceMainApplication";
    public static EglBase.Context eglBase;
    private static PoliceMainApplication mInstance;
    public static Context mContext;
    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    @Override
    public void onCreate() {
        super.onCreate();
        ComponentName componentName = new ComponentName(this, DeviceReceiver.class);
        PhoneInterfaceUtil.setActiveAdmin(componentName);
        PhoneInterfaceUtil.openNotCancelActivationAdmin(componentName, this);

        MyCrashHandler myCrashHandler = MyCrashHandler.getInstance();
        myCrashHandler.init();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        RegisterBatteryReceiver();

        getIMEI();

        initPeerClient();

        SmackAndroid.init(this);
        mContext = getApplicationContext();
        mInstance = this;
        initScreenSize();
    }

    private List<BaseActivity> activities = new ArrayList<BaseActivity>();

    //将activity加入集合,用于当程序退出时finish掉activity
    public void addActivity(BaseActivity activity) {
        activities.add(activity);
    }

    @Override
    public void onTerminate() {
        for (BaseActivity activity : activities) {
            Log.d(TAG, "退出activity名称: " + activity.getPackageName());
            activity.finish();
        }
        super.onTerminate();
        System.exit(0);//退出
    }

    /*
     * 注册电池电量广播,实时查看电量
     */
    private void RegisterBatteryReceiver() {
        BatteryReceiver receiver = new BatteryReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
    }


    public LocalCameraStream getNotNULLLocalStream() {
        if (localStream == null) {
            LocalCameraStreamParameters msp;
            try {
                msp = new LocalCameraStreamParameters(true, true);
                msp.setResolution(640, 480);
                localStream = new LocalCameraStream(msp);
            } catch (WoogeenException e1) {
                e1.printStackTrace();
                if (localStream != null) {
                    localStream.close();
                    localStream = null;
                }
            }
        }
        return localStream;
    }

    // 新增之前先删除，保证只有一个被监听
    public static void registerMainObServer(PeerClient.PeerClientObserver observer) {

//        observerList.add(observer);
        if (mainObserver == null) {
            mainObserver = observer;
            peerClient.addObserver(observer);
        }
    }

    public void disableView() {
        if (localStream != null) {
            localStream.disableVideo();
            localStream.disableAudio();
//            Toast.makeText(this, "Woogeen is running in the background.", Toast.LENGTH_SHORT).show();
        }
    }

    public void enableView() {
        if (localStream != null) {
            localStream.enableVideo();
            localStream.enableAudio();
//            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
        }
    }


    public static void unRegisterMainObServer() {
        if (mainObserver != null) {
            Log.d(TAG, "取消监听");
            peerClient.removeObserver(mainObserver);
            mainObserver = null;
        }
    }


    private void getIMEI() {
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        iMEI = tm.getDeviceId();
    }

    private void initPeerClient() {
        try {
            // Initialization work.
            List<IceServer> iceServers = new ArrayList<IceServer>();

            iceServers.add(new IceServer("stun:stun.qvod.com:3478")); // 30ms
            iceServers.add(new IceServer("stun:203.183.172.196:3478")); // 30ms
            iceServers.add(new IceServer("stun:stun.ekiga.net"));
            iceServers.add(new IceServer("stun:numb.viagenie.ca")); // ipv6
            iceServers.add(new IceServer("stun:stun.voiparound.com"));
            iceServers.add(new IceServer("stun:stun.voipbuster.com"));
            iceServers.add(new IceServer("stun:stun.voipstunt.com"));
            iceServers.add(new IceServer("stun:stun.voipcheap.com:3478"));
            iceServers.add(new IceServer("stun:stun.sipgate.net"));
            iceServers.add(new IceServer("stun:stun.jappix.com:3478"));
            iceServers.add(new IceServer("stun:23.21.150.121:3478"));
            iceServers.add(new IceServer("stun:iphone-stun.strato-iphone.de:3478"));
            iceServers.add(new IceServer("stun:stun.schlund.de"));

            iceServers.add(new IceServer("turn:numb.viagenie.ca", "webrtc@live.com", "muazkh"));
            iceServers.add(new IceServer("turn:192.158.29.39:3478?transport=udp", "28224511:1379330808", "JZEOEt2V3Qb0y27GRntt2u2PAYA="));
            iceServers.add(new IceServer("turn:192.158.29.39:3478?transport=tcp", "28224511:1379330808", "JZEOEt2V3Qb0y27GRntt2u2PAYA="));

            PeerClientConfiguration config = new PeerClientConfiguration();
            config.setIceServers(iceServers);
            config.setVideoCodec(VideoCodec.VP8);
            peerClient = new PeerClient(config, new SocketSignalingChannel());

            eglBase = EglBase.create().getEglBaseContext();
            ClientContext.setApplicationContext(this);
            ClientContext.setVideoHardwareAccelerationOptions(eglBase);
        } catch (WoogeenException e1) {
            e1.printStackTrace();
        }
    }

    public void closeLocalStream() {
        if (localStream != null) {
            localStream.close();
            localStream = null;
            Log.e(TAG, "closeLocalStream");
        }
    }

    public boolean isLocalStreamNULL() {
        if (localStream != null)
            return false;
        else
            return true;

    }

    public void disableVideo() {
        if (localStream != null) {
            localStream.disableVideo();
        }
    }

    /**
     * 对讲连接
     *
     * @param videoRendererInterface
     * @param peerID
     */
    public void attachStream(Stream.VideoRendererInterface videoRendererInterface, String peerID) {
        try {
            if (videoRendererInterface != null) {
                getNotNULLLocalStream().attach(videoRendererInterface);
            }
            StreanList.add(peerID);
            Log.e(TAG, "add" + StreanList.size());
        } catch (Exception e) {
        }

    }

    /**
     * 对讲分离
     *
     * @param VideoStreamsView
     * @param peerID
     */
    public void detachStream(WoogeenSurfaceRenderer VideoStreamsView, String peerID) {
        try {
            Iterator<String> iterators = StreanList.iterator();
            while (iterators.hasNext()) {
                String s = iterators.next();
                if (s.equals(peerID)) {
                    iterators.remove();
                }
            }
            if (StreanList.size() == 0 || (StreanList.size() == 1 && isHasADMIN())) {

                Log.e(TAG, "进来了" + StreanList.size());
                if (VideoStreamsView != null) {
                    getNotNULLLocalStream().detach(VideoStreamsView);
                }
                if (StreanList.size() == 0) {
                    closeLocalStream();
                }
                if (VideoStreamsView != null) {
                    VideoStreamsView.cleanFrame();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isHasADMIN() {
        Iterator<String> iterators = StreanList.iterator();
        while (iterators.hasNext()) {
            String s = iterators.next();
            if (s.equals(ADMIN_PEERID)) {
                return true;
            }
        }
        return false;
    }

    public static Context getInstance() {
        return mInstance;
    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }
}