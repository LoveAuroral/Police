package com.dark_yx.policemain.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.broadCastReceiver.HeadsetPlugReceiver;
import com.dark_yx.policemain.common.LocationUtils;
import com.dark_yx.policemain.common.StreamMes;
import com.dark_yx.policemain.common.WoogeenSurfaceRenderer;
import com.dark_yx.policemain.entity.LocationR;
import com.dark_yx.policemain.entity.SetLocation;
import com.dark_yx.policemain.entity.User;
import com.dark_yx.policemain.PoliceMainApplication;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.PowerUtil;
import com.dark_yx.policemaincommon.Util.SystemInfo;
import com.dark_yx.policemaincommon.Util.VibratorUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;
import com.intel.webrtc.base.ActionCallback;
import com.intel.webrtc.base.RemoteStream;
import com.intel.webrtc.base.WoogeenException;
import com.intel.webrtc.base.WoogeenIllegalArgumentException;
import com.intel.webrtc.p2p.PeerClient.PeerClientObserver;
import com.intel.webrtc.p2p.PublishOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import retrofit2.Response;

/**
 * 语音对讲
 */
public class Videop2pAllActivity extends BaseActivity {
    private Button serviceListView, btnCancel;
    private boolean mirror = true;
    private String publishPeerId = "";
    private static final String TAG = "TahuVideo";
    private PeerHandler peerHandlerVideo;
    private HandlerThread peerThreadVideo;
    private LinearLayout remoteViewContainer, localViewContainer;
    private WoogeenSurfaceRenderer localStreamRenderer;
    private WoogeenSurfaceRenderer remoteStreamRenderer;
    private Message message;
    private TextView tv_title;
    private String destId = "";
    private final int SEND_DATA = 2;
    private final int INVITE = 3;
    private final int STOP = 4;
    private final int PUBLISH = 5;
    private final int UNPUBLISH = 6;
    private final int SWITCH_CAMERA = 7;
    private boolean isInviter;
    private boolean isMonitor;
    private Myadapter myadapter;
    private ArrayList<StreamMes> remoteStreamList = new ArrayList<>();
    private ArrayList<StreamMes> inviteList = new ArrayList<>();
    private String items[];
    private List<UsersBean> list;//用户列表
    private GridView Gv_streamlist;
    private String type;
    private long firstlickTime = 0;
    private Iterator<StreamMes> iterator;
    private String fileName;//录音文件名
    private File filePath;//录音路径
    private File file;//录音文件
    public static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/Police/Videop2p";
    private HeadsetPlugReceiver headsetPlugReceiver;
    private Vibrator vibrator;
    private PowerManager.WakeLock wakeLock;
    private ChatDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ChatDb();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                System.exit(-1);
            }
        });
        setContentView(R.layout.video_p2p_all);
        try {
            createUI();
            initVideoStreamsViews();
            initView();
        } catch (WoogeenException e1) {
            e1.printStackTrace();
        }

    }

    private void initVideoStreamsViews() throws WoogeenException {
        localStreamRenderer = new WoogeenSurfaceRenderer(this);
        localViewContainer.addView(localStreamRenderer);
        localStreamRenderer.init(PoliceMainApplication.eglBase, null);
        localStreamRenderer.setMirror(mirror);
    }

    private void createUI() throws WoogeenException {
        AudioManager audioManager = ((AudioManager) getSystemService(AUDIO_SERVICE));
        audioManager.setSpeakerphoneOn(true);
        /*************/
        this.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        /*************/
        Gv_streamlist = (GridView) findViewById(R.id.gv_streamlist);
        localViewContainer = (LinearLayout) findViewById(R.id.llLocalView);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Videop2pAllActivity.this,
                        "撤销成功", Toast.LENGTH_SHORT).show();
                if (remoteStreamList.size() != 0) {
                    iterator = remoteStreamList.iterator();
                    while (iterator.hasNext()) {
                        StreamMes streamMes = iterator.next();
                        message = peerHandlerVideo.obtainMessage();
                        message.what = STOP;
                        message.obj = streamMes.peerID;
                        message.sendToTarget();
                    }
                    remoteStreamList.clear();
                }
                finish();
            }
        });

        PoliceMainApplication.peerClient.addObserver(observerVideo);
        peerThreadVideo = new HandlerThread("PeerThreadVideo");
        peerThreadVideo.start();
        peerHandlerVideo = new PeerHandler(peerThreadVideo.getLooper());
        list = db.getGroupUser();
        registerHeadsetPlugReceiver();//注册耳机监听广播
    }

    /**
     * 一键报警上传位置信息
     */
    private void getLocation() {
        Location location = LocationUtils.getInstance(Videop2pAllActivity.this).showLocation();
        if (location != null) {
            String address = location.getLongitude() + "," + location.getLatitude();
            Log.d("addressIs", address);
            SetLocation input = new SetLocation();
            input.setGeographicalPosition(address);
            ApiFactory.getService().getLocationR(DataUtil.getToken(), input).enqueue(new MyCallBack<LocationR>() {
                @Override
                public void onSuc(Response<LocationR> response) {

                }

                @Override
                public void onFail(String message) {
                    Toast.makeText(Videop2pAllActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initView() {
        Log.d(TAG, "initView:" + remoteStreamList.toString());
        tv_title = (TextView) findViewById(R.id.tv_title);
        serviceListView = (Button) findViewById(R.id.serviceList);
        myadapter = new Myadapter(this);
        Gv_streamlist.setAdapter(myadapter);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        Log.d(TAG, "对讲类型: " + type);
        if (type.equals("alarm")) {
//            getLocation();//宁夏
            tv_title.setText("一键报警");
            btnCancel.setVisibility(View.VISIBLE);
            serviceListView.setVisibility(View.GONE);
            PoliceMainApplication.peerClient.invite(PoliceMainApplication.ADMIN_PEERID, new ActionCallback<Void>() {
                @Override
                public void onSuccess(final Void result) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.e(TAG, "邀请成功");
                        }
                    });
                }

                @Override
                public void onFailure(final WoogeenException e) {
                    Log.d(TAG, e.getMessage());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Videop2pAllActivity.this,
                                    "邀请失败:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            });
        } else if (type.contains("accept")) {
            tv_title.setText("视频对讲");
            Log.d(TAG, type.substring(6));
            PoliceMainApplication.peerClient.accept(type.substring(6),
                    new ActionCallback<Void>() {

                        @Override
                        public void onSuccess(
                                Void result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StreamMes streamMes = new StreamMes();
                                    streamMes.peerID = type.substring(6);
                                    streamMes.type = null;
                                    streamMes.stream = null;
                                    remoteStreamList.add(streamMes);
                                    Log.d(TAG, "主页同意:" + remoteStreamList.toString());
                                }
                            });
                        }

                        @Override
                        public void onFailure(
                                WoogeenException e) {
                            Log.d(TAG, "同意失败:" + e.getMessage());
                        }//

                    });

        } else if (type.equals("audio")) {// 主动呼叫
            tv_title.setText("语音对讲");
            localViewContainer.setVisibility(View.INVISIBLE);
            String imei = intent.getStringExtra("imei");
            startCall(type, imei);

        } else if (type.equals("groupCall")) {
            Toast.makeText(this, "开始群聊", Toast.LENGTH_LONG).show();
            String call = intent.getStringExtra("mode");
            List<User> groups = (List<User>) intent.getSerializableExtra("groups");
            Log.d(TAG, groups.toString());
            if (call.equals("audio")) {
                tv_title.setText("语音群聊" + "(" + groups.size() + ")");
            } else {
                tv_title.setText("视频群聊" + "(" + groups.size() + ")");
            }
            for (int i = 0; i < groups.size(); i++) {
                String imei = groups.get(i).getiMEI();
                if (!imei.equals(SystemInfo.GetIMEI(this))) {
                    startCall(call, imei);
                    Log.d(TAG, imei);
                }
            }
        } else if (type.equals("video")) {
            tv_title.setText("视频对讲");
            String imei = intent.getStringExtra("imei");
            startCall(type, imei);
        }
    }

    private void registerHeadsetPlugReceiver() {
        headsetPlugReceiver = new HeadsetPlugReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlugReceiver, filter);
    }

    private void startCall(String action, String imei) {
        destId = imei;
        message = peerHandlerVideo.obtainMessage();
        message.what = INVITE;
        message.obj = action;
        message.sendToTarget();
    }

    class Myadapter extends BaseAdapter {
        private Context context;
        private View item;

        public Myadapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return remoteStreamList.size();
        }

        @Override
        public Object getItem(int position) {
            return remoteStreamList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            StreamMes mes = remoteStreamList.get(position);
            Log.d(TAG, position + ":" + mes.type);
            if (mes.type != null && mes.type.equals("audio")) {
                item = LayoutInflater.from(context).inflate(R.layout.audio_layout, null);
                TextView tv_userMes = (TextView) item.findViewById(R.id.tv_userMes);
                remoteViewContainer = (LinearLayout) item.findViewById(R.id.llRemoteView);
                remoteStreamRenderer = new WoogeenSurfaceRenderer(Videop2pAllActivity.this);
                Iterator<UsersBean> iterator = list.iterator();
                while (iterator.hasNext()) {
                    UsersBean user = iterator.next();
                    if (user.getUserName().equals(remoteStreamList.get(position).peerID)) {
                        tv_userMes.setText(user.getUserName() + " "
                                + user.getUserName());
                    }
                }
                if (getCount() == 1) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(remoteViewContainer.getLayoutParams());
                    params.height = Gv_streamlist.getHeight();
                    remoteViewContainer.setLayoutParams(params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(remoteViewContainer.getLayoutParams());
                    params.height = Gv_streamlist.getHeight();
                    remoteViewContainer.setLayoutParams(params);
                }
                remoteViewContainer.addView(remoteStreamRenderer);
                remoteStreamRenderer.init(PoliceMainApplication.eglBase, null);
                try {
                    if (mes.stream != null) {
                        mes.stream.attach(remoteStreamRenderer);
                    }
                } catch (WoogeenIllegalArgumentException e) {
                    e.printStackTrace();
                }
                ((PoliceMainApplication) getApplication()).disableVideo();//语音通话隐藏画面
                remoteViewContainer.setVisibility(View.GONE);
                /**
                 * 双击退出
                 */
                remoteViewContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (remoteStreamList.get(position).firstClickTime > 0) {
                            if (System.currentTimeMillis() - remoteStreamList.get(position).firstClickTime < 500) {
                                showRemoteCloseDialog(remoteStreamList.get(position).peerID);
                                remoteStreamList.get(position).firstClickTime = 0;
                                return;
                            }
                        }
                        remoteStreamList.get(position).firstClickTime = System.currentTimeMillis();

                    }
                });
            } else {
                item = LayoutInflater.from(context).inflate(R.layout.video_layout, null);
                TextView tv_userMes = (TextView) item.findViewById(R.id.tv_userMes);
                remoteViewContainer = (LinearLayout) item.findViewById(R.id.llRemoteView);
                remoteStreamRenderer = new WoogeenSurfaceRenderer(Videop2pAllActivity.this);
                for (UsersBean user : list) {
                    if (user.getUserName().equals(remoteStreamList.get(position).peerID)) {
                        tv_userMes.setText(user.getUserName() + " "
                                + user.getUserName());
                    }
                }
                if (getCount() == 1) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(remoteViewContainer.getLayoutParams());
                    params.height = Gv_streamlist.getHeight();
                    remoteViewContainer.setLayoutParams(params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(remoteViewContainer.getLayoutParams());
                    params.height = Gv_streamlist.getHeight() / 2;
                    remoteViewContainer.setLayoutParams(params);
                }
                remoteViewContainer.addView(remoteStreamRenderer);
                remoteStreamRenderer.init(PoliceMainApplication.eglBase, null);
                try {
                    if (mes.stream != null) {
                        mes.stream.attach(remoteStreamRenderer);
                    }
                } catch (WoogeenIllegalArgumentException e) {
                    e.printStackTrace();
                }
                remoteViewContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (remoteStreamList.get(position).firstClickTime > 0) {
                            if (System.currentTimeMillis() - remoteStreamList.get(position).firstClickTime < 500) {
                                showRemoteCloseDialog(remoteStreamList.get(position).peerID);
                                remoteStreamList.get(position).firstClickTime = 0;
                                return;
                            }
                        }
                        remoteStreamList.get(position).firstClickTime = System.currentTimeMillis();

                    }
                });
            }
            return item;
        }
    }


    public void onclick(View v) {
        message = peerHandlerVideo.obtainMessage();
        switch (v.getId()) {
            case R.id.ll_back:
                if (remoteStreamList.size() != 0) {
                    showFinishDialog();
                } else {
                    finish();
                }
                break;
            case R.id.serviceList:
                showdialog();
                break;
            case R.id.llLocalView:
                if (firstlickTime > 0) {
                    if (System.currentTimeMillis() - firstlickTime < 500) {
                        message.what = SWITCH_CAMERA;
                        message.sendToTarget();
                        firstlickTime = 0;
                        return;
                    }
                }
                firstlickTime = System.currentTimeMillis();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (remoteStreamList.size() != 0) {
                showFinishDialog();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showFinishDialog() {
        Builder builder = new Builder(Videop2pAllActivity.this);
        builder.setTitle("提示");
        builder.setMessage("退出当前界面将结束所有通话，确认退出？");
        builder.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iterator = remoteStreamList.iterator();
                        while (iterator.hasNext()) {
                            StreamMes streamMes = iterator.next();
                            message = peerHandlerVideo.obtainMessage();
                            message.what = STOP;
                            message.obj = streamMes.peerID;
                            message.sendToTarget();
                        }
                        remoteStreamList.clear();
                    }
                });
            }
        });
        builder.setNeutralButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }

    }

    private void showRemoteCloseDialog(final String peerId) {
        Builder builder = new Builder(Videop2pAllActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定结束与" + peerId + "的通话吗?");
        builder.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message = peerHandlerVideo.obtainMessage();
                        message.what = STOP;
                        message.obj = peerId;
                        message.sendToTarget();
                    }
                });
            }
        });
        builder.setNeutralButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }

    }

    int selectitem = -1;

    private void showdialog() {

        final Builder builder = new Builder(Videop2pAllActivity.this);
        builder.setTitle("通话邀请");
        items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName() + " "
                    + list.get(i).getUserName();
        }
        builder.setSingleChoiceItems(items, -1, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectitem = which;
                destId = list.get(selectitem).getUserName();
            }
        });
        builder.setPositiveButton("音频呼叫", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                message = peerHandlerVideo.obtainMessage();
                message.what = INVITE;
                message.obj = "audio";
                message.sendToTarget();
            }
        });
        builder.setNeutralButton("视频呼叫", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                message = peerHandlerVideo.obtainMessage();
                message.what = INVITE;
                message.obj = "video";
                message.sendToTarget();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public PeerClientObserver observerVideo;

    {
        observerVideo = new PeerClientObserver() {

            /**
             * 收到对方邀请消息
             *
             * @param peerId
             */
            @Override
            public void onInvited(final String peerId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "onInvited: " + peerId);
                        if (peerId.equals(PoliceMainApplication.ADMIN_PEERID)) {

                            PoliceMainApplication.peerClient.accept(
                                    PoliceMainApplication.ADMIN_PEERID,
                                    new ActionCallback<Void>() {

                                        @Override
                                        public void onSuccess(Void result) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    isMonitor = true;
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(WoogeenException e) {
                                            Log.d(TAG, e.getMessage());
                                        }

                                    });

                        } else {
                            wakeLock = PowerUtil.PowerOn(Videop2pAllActivity.this);
                            wakeLock.acquire(2000);
                            PowerUtil.PowerOn(Videop2pAllActivity.this);
                            vibrator = VibratorUtil.Vibrate(Videop2pAllActivity.this, 2);
                            MediaPlayer.create(Videop2pAllActivity.this, R.raw.office).start();
                            destId = peerId;
                            final Builder builder = new Builder(
                                    Videop2pAllActivity.this);
                            builder.setTitle("通话邀请");
                            builder.setCancelable(false);
                            builder.setMessage("想和用户 " + peerId + "连接吗?");
                            builder.setPositiveButton("接受",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            vibrator.cancel();
                                            PoliceMainApplication.peerClient.accept(destId,
                                                    new ActionCallback<Void>() {

                                                        @Override
                                                        public void onSuccess(
                                                                Void result) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    StreamMes streamMes = new StreamMes();
                                                                    streamMes.peerID = peerId;
                                                                    streamMes.firstClickTime = 0;
                                                                    streamMes.type = null;
                                                                    streamMes.stream = null;
                                                                    remoteStreamList.add(streamMes);
                                                                    Log.d(TAG, "对讲页面同意:" + remoteStreamList);
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onFailure(
                                                                WoogeenException e) {
                                                            Log.d(TAG, e.getMessage());
                                                        }

                                                    });
                                        }
                                    });
                            builder.setNeutralButton("拒绝",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            wakeLock.release();
                                            vibrator.cancel();
                                            denyUser();
                                        }
                                    });
                            builder.create().show();
                        }
                    }
                });
            }

            /**
             * 拒绝
             */
            private void denyUser() {
                Log.e(TAG, "denyUser: " + destId);
                PoliceMainApplication.peerClient.deny(destId,
                        new ActionCallback<Void>() {

                            @Override
                            public void onSuccess(
                                    Void result) {
                                Log.d(TAG,
                                        "Denied invitation from "
                                                + destId);
                            }

                            @Override
                            public void onFailure(
                                    WoogeenException e) {
                                Log.d(TAG,
                                        e.getMessage());
                            }

                        });
            }

            /**
             * 对方接受邀请
             *
             * @param peerId
             */
            @Override
            public void onAccepted(final String peerId) {
                Log.d(TAG, "onAccepted:" + peerId);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (peerId.equals(PoliceMainApplication.ADMIN_PEERID)) {
                            StreamMes streamMes = new StreamMes();
                            streamMes.type = "video";
                            streamMes.peerID = PoliceMainApplication.ADMIN_PEERID;
                            streamMes.stream = null;
                            remoteStreamList.add(streamMes);
                            Log.d(TAG, "对方接受:" + remoteStreamList.toString());
                        } else {
                            iterator = inviteList.iterator();
                            while (iterator.hasNext()) {
                                StreamMes s = iterator.next();
                                if (s.peerID.equals(destId)) {
                                    remoteStreamList.add(s);
                                    iterator.remove();
                                }
                            }
                            Log.d(TAG, "对方接受:" + remoteStreamList.toString());
                        }
                        Toast.makeText(Videop2pAllActivity.this,
                                "用户 " + peerId + "接收了邀请", Toast.LENGTH_SHORT).show();
                        isInviter = true;
                    }
                });
            }

            /**
             * 拒绝邀请
             *
             * @param peerId
             */
            @Override
            public void onDenied(final String peerId) {
                Log.d(TAG, "onDenied:" + peerId);
                runOnUiThread(new Runnable() {
                    public void run() {
                        iterator = inviteList.iterator();
                        while (iterator.hasNext()) {
                            StreamMes streamMes = iterator.next();
                            if (streamMes.peerID.equals(peerId)) {
                                iterator.remove();
                            }
                        }
                        Toast.makeText(Videop2pAllActivity.this,
                                "用户 " + peerId + "拒绝了邀请", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * 对方开启视频共享
             *
             * @param stream
             */
            @Override
            public void onStreamAdded(final RemoteStream stream) {
                Log.e(TAG, "onStreamAdded : from " + stream.getRemoteUserId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean isVideo = false;
                            iterator = remoteStreamList.iterator();
                            Log.d(TAG, remoteStreamList.toString());
                            while (iterator.hasNext()) {
                                StreamMes s = iterator.next();
                                if (s.type.equals("video")) {
                                    isVideo = true;
                                }
                                if (s.peerID.equals(stream.getRemoteUserId())) {
                                    s.stream = stream;
                                }
                            }
                            if (!isVideo) {
                                localViewContainer.setVisibility(View.GONE);
                            } else {
                                localViewContainer.setVisibility(View.VISIBLE);
                            }
                            myadapter.notifyDataSetChanged();
                            if (!stream.getRemoteUserId().equals(PoliceMainApplication.ADMIN_PEERID)) {
                                Toast.makeText(Videop2pAllActivity.this, stream.getRemoteUserId() + "加入对讲", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }

            /**
             * 收到邀请方发送的type消息
             *
             * @param peerId
             * @param msg
             */
            @Override
            public void onDataReceived(final String peerId, final String msg) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "onDataReceived:" + msg);
                        Log.d(TAG, "onDataReceived:" + remoteStreamList.toString());
                        iterator = remoteStreamList.iterator();
                        while (iterator.hasNext()) {
                            StreamMes s = iterator.next();
                            if (s.peerID.equals(peerId)) {
                                s.type = msg;
                            }
                        }
                        message = peerHandlerVideo.obtainMessage();
                        message.what = PUBLISH;
                        message.obj = peerId;
                        peerHandlerVideo.sendMessageDelayed(message, 500);
                        if (remoteStreamList.size() == 1) {
                            Gv_streamlist.setNumColumns(1);
                        } else if (remoteStreamList.size() == 2) {
                            Gv_streamlist.setNumColumns(1);
                        } else if (remoteStreamList.size() > 2) {
                            Gv_streamlist.setNumColumns(2);
                        }
                    }
                });
            }

            /**
             * 取消视频共享
             *
             * @param stream
             */
            @Override
            public void onStreamRemoved(final RemoteStream stream) {
                Log.d(TAG, "onStreamRemoved");
                runOnUiThread(new Runnable() {
                    public void run() {
                        remoteStreamRenderer.cleanFrame();
                        iterator = remoteStreamList.iterator();
                        while (iterator.hasNext()) {
                            StreamMes s = iterator.next();
                            if (s.peerID.equals(stream.getRemoteUserId())) {
                                s.stream = null;
                                myadapter.notifyDataSetChanged();
                            }
                        }
                        if (!stream.getRemoteUserId().equals(PoliceMainApplication.ADMIN_PEERID)) {
                            Toast.makeText(Videop2pAllActivity.this,
                                    stream.getRemoteUserId() + "取消视频共享",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            /**
             * 通话结束
             *
             * @param peerId
             */
            @Override
            public void onChatStopped(final String peerId) {
                Log.d(TAG, "onChatStop:" + peerId);
                if (!(peerId.equals(PoliceMainApplication.ADMIN_PEERID) && isMonitor)) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (wakeLock != null) {
                                wakeLock.release();
                            } else {
                                try {
                                    PowerUtil.PowerOn(Videop2pAllActivity.this).release();//释放亮屏
                                } catch (Exception e) {
                                    Log.d(TAG, "释放亮屏失败" + e.getMessage());
                                }
                            }
                            Log.d(TAG, "onChatStopped: " + remoteStreamList.toString());
                            boolean haivideo = false;
                            iterator = remoteStreamList.iterator();
                            while (iterator.hasNext()) {
                                StreamMes streamMes = iterator.next();
                                if (streamMes.type != null && streamMes.type.equals("video")) {
                                    haivideo = true;
                                }
                                if (streamMes.peerID.equals(peerId)) {
                                    iterator.remove();
                                    if (remoteStreamList.size() == 1) {
                                        Gv_streamlist.setNumColumns(1);
                                    } else if (remoteStreamList.size() == 2) {
                                        Gv_streamlist.setNumColumns(1);
                                    } else if (remoteStreamList.size() > 2) {
                                        Gv_streamlist.setNumColumns(2);
                                    }
                                }
                            }
                            if (!haivideo && type.equals("audio")) {
                                localViewContainer.setVisibility(View.INVISIBLE);
                            } else {
                                localViewContainer.setVisibility(View.VISIBLE);
                            }
                            myadapter.notifyDataSetChanged();//,录音文件已保存在/Police/Videop2p目录下
                            Toast.makeText(Videop2pAllActivity.this, "通话结束",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    ((PoliceMainApplication) getApplication()).detachStream(localStreamRenderer, peerId);
                    localStreamRenderer.cleanFrame();
                    if (remoteStreamRenderer != null)
                        remoteStreamRenderer.cleanFrame();
                    isInviter = false;
                } else {
                    ((PoliceMainApplication) getApplication()).detachStream(null, peerId);
                    if (isMonitor) {
                        isMonitor = false;
                    }
                }
//                finish();
            }

            /**
             * 开始通话,将type发送给对方
             *
             * @param peerId
             */
            @Override
            public void onChatStarted(final String peerId) {
                Log.d(TAG, "onChatStarted: " + peerId);
                if (!(peerId.equals(PoliceMainApplication.ADMIN_PEERID) && isMonitor)) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Videop2pAllActivity.this, "开始通话:" + peerId,
                                    Toast.LENGTH_SHORT).show();
                            if (isInviter) {
                                if (remoteStreamList.size() == 1) {
                                    Gv_streamlist.setNumColumns(1);
                                } else if (remoteStreamList.size() == 2) {
                                    Gv_streamlist.setNumColumns(1);
                                } else if (remoteStreamList.size() > 2) {
                                    Gv_streamlist.setNumColumns(2);
                                }
                                message = peerHandlerVideo.obtainMessage();
                                message.obj = peerId;
                                message.what = SEND_DATA;
                                peerHandlerVideo.sendMessageDelayed(message, 100);
                                isInviter = false;
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "onChatStarted:0000");
                    message = peerHandlerVideo.obtainMessage();
                    message.what = PUBLISH;
                    message.obj = PoliceMainApplication.ADMIN_PEERID;
                    peerHandlerVideo.sendMessageDelayed(message, 100);
                }

            }

            /**
             * 服务器断开连接
             */
            @Override
            public void onServerDisconnected() {
                Log.d(TAG, "onServerDisconnected");
                localStreamRenderer.cleanFrame();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Videop2pAllActivity.this, "服务器断开连接",
                                Toast.LENGTH_SHORT).show();
                        remoteStreamList.clear();
                        myadapter.notifyDataSetChanged();
                        ((PoliceMainApplication) getApplication()).closeLocalStream();
                        isInviter = false;
                        isMonitor = false;
                    }
                });
            }

        };
    }

    private void saveVoice() {
        fileName = getFileName() + ".mp3";
        filePath = new File(PATH);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        file = new File(filePath, fileName);
    }


    class PeerHandler extends Handler {
        public PeerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case INVITE:
                    Log.d(TAG, "INVITE");
                    final String type = (String) msg.obj;
                    Log.e(TAG, type + "xxx");
                    if (type.equals("alarm")) {
                        destId = PoliceMainApplication.ADMIN_PEERID;
                    } else {
                        if (!TextUtils.isEmpty(destId)) {
                            TelephonyManager tm = (TelephonyManager) getApplication()
                                    .getSystemService(TELEPHONY_SERVICE);
                            if (tm.getDeviceId().equals(destId)) {
                                return;
                            }
                        } else {
                            return;
                        }
                    }

                    PoliceMainApplication.peerClient.invite(destId,
                            new ActionCallback<Void>() {

                                @Override
                                public void onSuccess(final Void result) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Log.e(TAG, "invite");
                                            StreamMes streamMes = new StreamMes();
                                            if (type.equals("alarm")) {
                                                streamMes.type = "video";
                                            } else {
                                                streamMes.type = type;
                                            }

                                            Log.e(TAG, streamMes.type + "");
                                            streamMes.peerID = destId;
                                            streamMes.firstClickTime = 0;
                                            streamMes.stream = null;

                                            iterator = inviteList.iterator();
                                            while (iterator.hasNext()) {
                                                StreamMes s = iterator.next();

                                                if (s.peerID.equals(destId)) {
                                                    iterator.remove();
                                                }
                                            }
                                            inviteList.add(streamMes);
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(final WoogeenException e) {
                                    Log.d(TAG, e.getMessage());
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(Videop2pAllActivity.this,
                                                    "邀请失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            });
                    break;
                case STOP:
                    Log.e(TAG, "STOP:　" + (String) msg.obj);
                    PoliceMainApplication.peerClient.stop((String) msg.obj,
                            new ActionCallback<Void>() {

                                @Override
                                public void onSuccess(Void result) {
                                    ((PoliceMainApplication) getApplication()).detachStream(localStreamRenderer, destId);
                                    localStreamRenderer.cleanFrame();
                                }

                                @Override
                                public void onFailure(WoogeenException e) {
                                    Log.d(TAG, e.getMessage());
                                }

                            });
                    break;
                case PUBLISH:
                    Log.e(TAG, "PUBLISH: " + (String) msg.obj);
                    Log.e(TAG, "PUBLISH: " + remoteStreamList.toString());
                    publishPeerId = (String) msg.obj;
                    if (isMonitor && publishPeerId.equals(PoliceMainApplication.ADMIN_PEERID)) {
                        Log.d(TAG, "add: ADMIN_PEERID");
                        ((PoliceMainApplication) getApplication()).StreanList.add(PoliceMainApplication.ADMIN_PEERID);
                    } else {
                        ((PoliceMainApplication) getApplication()).attachStream(localStreamRenderer, publishPeerId);
                    }
                    PublishOptions option = new PublishOptions();
                    option.setMaximumVideoBandwidth(200);
                    option.setMaximumAudioBandwidth(30);

                    PoliceMainApplication.peerClient.publish(
                            ((PoliceMainApplication) getApplication())
                                    .getNotNULLLocalStream(), publishPeerId,
                            option, new ActionCallback<Void>() {

                                @Override
                                public void onFailure(WoogeenException arg0) {
                                    Log.d(TAG, "PUBLISH:onFailure:" + arg0.getMessage());
                                    if (publishPeerId.equals(PoliceMainApplication.ADMIN_PEERID) && isMonitor) {
                                        ((PoliceMainApplication) getApplication()).StreanList.remove(PoliceMainApplication.ADMIN_PEERID);
                                    } else {
                                        ((PoliceMainApplication) getApplication()).detachStream(localStreamRenderer, publishPeerId);
                                    }
                                }

                                @Override
                                public void onSuccess(Void arg0) {
                                    Log.d(TAG, "PUBLISH:onSuccess");
                                }

                            });
                    break;
                case SWITCH_CAMERA:
                    Log.e(TAG, "SWITCH_CAMERA: ");
                    if (((PoliceMainApplication) getApplication()).isLocalStreamNULL()) {
                        return;
                    }
                    ((PoliceMainApplication) getApplication()).getNotNULLLocalStream()
                            .switchCamera(new ActionCallback<Boolean>() {

                                @Override
                                public void onSuccess(final Boolean isFrontCamera) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(
                                                    Videop2pAllActivity.this,
                                                    "切换到"
                                                            + (isFrontCamera ? "前置"
                                                            : "后置") + "摄像头",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    mirror = !mirror;
                                    localStreamRenderer.setMirror(mirror);
                                }

                                @Override
                                public void onFailure(final WoogeenException e) {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(
                                                    Videop2pAllActivity.this,
                                                    "摄像头切换失败"
                                                            + e.getLocalizedMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    });
                                }

                            });
                    break;

                case SEND_DATA://发送type消息,成功则打开本地摄像头
                    Log.d(TAG, "SEND_DATA: " + remoteStreamList.toString());
                    String content = "";
                    iterator = remoteStreamList.iterator();
                    while (iterator.hasNext()) {
                        StreamMes s = iterator.next();
                        if (s.peerID.equals(msg.obj)) {
                            content = s.type;
                        }
                    }
                    Log.d(TAG, "SEND_DATA:" + "content: " + content);
                    PoliceMainApplication.peerClient.send(content, (String) msg.obj,
                            new ActionCallback<Void>() {

                                @Override
                                public void onFailure(WoogeenException arg0) {

                                }

                                @Override
                                public void onSuccess(Void arg0) {
                                    message = peerHandlerVideo.obtainMessage();
                                    message.what = PUBLISH;
                                    message.obj = msg.obj;
                                    peerHandlerVideo.sendMessageDelayed(message, 500);
                                }
                            });

                    break;
            }
            super.handleMessage(msg);
        }
    }


    @Override
    protected void onDestroy() {
        PoliceMainApplication.peerClient.removeObserver(observerVideo);
        this.unregisterReceiver(headsetPlugReceiver);
        System.gc();
        finish();
        super.onDestroy();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getFileName() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DATE);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        int mSecond = calendar.get(Calendar.SECOND);
        return mYear + "-" + (mMonth + 1) + "-" + mDay + "-" + mHour + mMinute + mSecond;//2016-9-12-94040
    }

    @Override
    protected void onResume() {
        ((PoliceMainApplication) getApplication()).enableView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        ((PoliceMainApplication) getApplication()).disableView();
        super.onPause();
    }
}