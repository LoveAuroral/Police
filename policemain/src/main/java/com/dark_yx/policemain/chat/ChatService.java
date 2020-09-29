package com.dark_yx.policemain.chat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.widget.Toast;

import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.NetUtil;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemain.util.PollingBean;
import com.dark_yx.policemain.util.WhiteListUtil;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.beans.EntityBean;
import com.dark_yx.policemain.chat.beans.ChatGroupBean;
import com.dark_yx.policemain.chat.beans.ChatMessage;
import com.dark_yx.policemain.chat.beans.ExitGroupBean;
import com.dark_yx.policemain.chat.beans.InviteToGroupBean;
import com.dark_yx.policemain.chat.beans.NotificationBeans;
import com.dark_yx.policemain.chat.beans.RecentMessageBean;
import com.dark_yx.policemain.chat.beans.UploadFileResult;
import com.dark_yx.policemain.chat.callback.ICreateGroupCallback;
import com.dark_yx.policemain.chat.callback.IGroupUserCallBack;
import com.dark_yx.policemain.chat.callback.IInviteUserCallback;
import com.dark_yx.policemain.chat.callback.IMessageCallback;
import com.dark_yx.policemain.chat.callback.IRealTimeCallback;
import com.dark_yx.policemain.chat.callback.IRecentCallback;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;
import com.dark_yx.policemain.chat.view.chatui.util.FileUtils;
import com.dark_yx.policemain.chat.view.chatui.util.MediaManager;
import com.dark_yx.policemain.launcher.launcher3.Launcher;
import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemaincommon.Models.NoticeBean;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.TimeUtil;
import com.dark_yx.policemaincommon.Util.WriteLogUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zsoft.signala.ConnectionState;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dark_yx.policemain.chat.beans.NotificationBeans.ITEM_BLUETOOTHABLE;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.ITEM_BLUETOOTHDISABLE;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.ITEM_FORMAT;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.ITEM_LOCK;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.ITEM_REBOOT;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.ITEM_SHUTDOWN;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.ITEM_UNLOCK;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.ITEM_WIFIDISABLE;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.SETMICROPHONEABLED;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.SETMICROPHONEDISABLED;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.SETVIDEOABLED;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.SETVIDEODISABLED;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.TYPE_CAMERA_CHANGE;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.TYPE_DEVICE;
import static com.dark_yx.policemain.chat.beans.NotificationBeans.TYPE_NOTICE;
import static com.dark_yx.policemain.chat.view.chatui.util.Constants.GROUP;
import static com.dark_yx.policemain.chat.view.chatui.util.Constants.USER;

public class ChatService extends Service implements IRealTimeCallback {
    RealTimeConnection realTimeConnection;
    IMessageCallback messageCallBack;
    IRecentCallback recentCallBack;
    IBinder binder;
    Handler mMainHandler;
    long chatId;

    ChatDb chatDb;
    Gson gson;
    private boolean isPlay;
    private ArrayList<EntityBean<Long>> entitys;
    private ICreateGroupCallback createGroupCallBack;
    private IInviteUserCallback inviteUserCallBack;
    private boolean isSing;
    private Map<Long, Integer> tags;
    private int i = 1;
    private ComponentName mAdminName;
    private IGroupUserCallBack groupUserCallBack;

    public ChatService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public ConnectionState getConnectState() {
        return realTimeConnection.getCurrentState();
    }

    public void reSend(ChatMessage messageInfo) {
        sendMessageToUser(messageInfo);
    }

    public void resentChange() {
        if (recentCallBack != null) {
            recentCallBack.recentChanged();
        }
    }

    public class ChatServiceBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    public void registerRecentCallback(IRecentCallback recentCallBack) {
        this.recentCallBack = recentCallBack;
    }


    public void registerMessageCallback(IMessageCallback messageCallBack, long chatId) {
        this.messageCallBack = messageCallBack;
        LogUtil.d("chatId: " + chatId);
        this.chatId = chatId;
    }


    public void unregisterMessageCallback() {
        this.messageCallBack = null;
        chatId = 0;
    }

    public void registerCreateGroupCallback(ICreateGroupCallback groupCallBack) {
        this.createGroupCallBack = groupCallBack;
    }

    public void registerInviteUserCallback(IInviteUserCallback groupCallBack) {
        this.inviteUserCallBack = groupCallBack;
    }

    public void unregisterCreateGroupCallback() {
        this.createGroupCallBack = null;
    }

    public void unregisterInviteUserCallback() {
        this.inviteUserCallBack = null;
    }

    public void registerGroupUserCallBack(IGroupUserCallBack groupUserCallBack) {
        this.groupUserCallBack = groupUserCallBack;
    }

    public void unRegisterGroupUserCallBack() {
        this.groupUserCallBack = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("chatService Created");

//        thread.start();
        binder = new ChatServiceBinder();
        mMainHandler = new Handler();

        chatDb = new ChatDb();

        gson = new Gson();

        String url = DataUtil.getBassIp() + "/signalr/hubs";
        realTimeConnection = new RealTimeConnection(this, url, this);

        initReceiver();
        tags = new HashMap<>();
        mAdminName = new ComponentName(this, DeviceReceiver.class);

        startTimer();
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetWorkReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("chatService onDestroy");
        unregisterReceiver(myNetWorkReceiver);
        stopTimer();
    }

    public void connection() {
        String s = getConnectState().toString();
        WriteLogUtil.writeLog("signalR status", s);

        if (!isConnected()) {
            LogUtil.d("signalR 未连接，正在连接");
            realTimeConnection.stopConnection();
            realTimeConnection.startConnection(DataUtil.getToken());
        } else {
            LogUtil.d("signalR 已连接： " + s);
        }
    }

    public boolean isConnected() {
        ConnectionState connectState = getConnectState();
        return connectState != ConnectionState.Disconnected && connectState != ConnectionState.Reconnecting;
    }

    // 声音提示
    private void playSing() {
        sendBroadcast(new Intent("com.lgh.unread"));
        if (!DataUtil.isEnter(this)) return;

        if (!isPlay) {
            isPlay = true;
            MediaPlayer mp = MediaPlayer.create(this, R.raw.office);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    LogUtil.d("播放铃声完毕");
                    isPlay = false;
                    mp.release();
                }
            });
            mp.start();
        }

    }

    @Override
    public void getNotification(JSONArray jsonArray) {
        LogUtil.d(jsonArray.toString());
        Type type = new TypeToken<List<NotificationBeans>>() {
        }.getType();
        List<NotificationBeans> beans = new Gson().fromJson(jsonArray.toString(), type);
        for (NotificationBeans notificationBeans : beans) {
            String name = notificationBeans.getNotification().getData().getMessage();
            switch (notificationBeans.getNotification().getNotificationName()) {
                case TYPE_DEVICE://通知设备
                    dealDevice(name);
                    break;
                case TYPE_NOTICE://公告
                    playNewMessage();
                    dealNotice(name);
                    break;
                case TYPE_CAMERA_CHANGE://宁夏人脸识别
                    dealFace(jsonArray.toString());
                    break;
            }
        }
        CommonMethod.makeAsRead();
    }

    /**
     * 这样解析实在太麻烦了，有本事你自己解析啊！
     *
     * @param json
     */
    private void dealFace(String json) {
        try {
            JSONArray data = new JSONArray(json);
            for (int i = 0; i < data.length(); i++) {
                String notification = data.getJSONObject(i).getString("notification");
                String da = new JSONObject(notification).getString("data");
                String properties = new JSONObject(da).getString("properties");
                String da1 = new JSONObject(properties).getString("data");
                PollingBean pollingBean = new Gson().fromJson(da1, PollingBean.class);
                if (pollingBean == null) return;
                boolean isIn = pollingBean.getError().isIsIn();
                LogUtil.d("notificationChanged: " + isIn);
                if (isIn) {
//                    startActivity(new Intent(this, PhoneMainActivity.class));
//                    CommonMethod.sendStatus(true, this);
                    openApp(this, mAdminName);
                    WriteLogUtil.writeLog("人脸识别", "进入");
                } else {
                    DataUtil.setEnter(this, false);
                    CommonMethod.sendStatus(false, this);
                    PhoneInterfaceUtil.exitApp(this, mAdminName);
                    WriteLogUtil.writeLog("人脸识别", "出");

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void openApp(Context context, ComponentName admin) {
        /* 启动当前应用程序 */
        DataUtil.setEnter(context, true);
        CommonMethod.sendStatus(true, context);
        PhoneInterfaceUtil.openInit(admin, context.getPackageName(), LoginActivity.class.getCanonicalName(),
                context.getApplicationContext());
        Intent myIntent = new Intent(context, LoginActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }

    private void playNewMessage() {
        if (!DataUtil.isEnter(this)) return;

        if (!isPlay) {
            isPlay = true;
            MediaPlayer mp = MediaPlayer.create(this, R.raw.newmessage);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    LogUtil.d("播放铃声完毕");
                    isPlay = false;
                    mp.release();
                }
            });
            mp.start();
        }
    }

    private void dealNotice(String name) {
        Toast.makeText(this, "您有新的消息公告,请注意查看!", Toast.LENGTH_LONG).show();

        long l = System.currentTimeMillis();
        DataUtil.setNotice(name, l);

        EventBus.getDefault().post(new NoticeBean(name, TimeUtil.getChatTime(l)));
    }

    private void dealDevice(String message) {
        String s = null;
        switch (message) {
            case ITEM_REBOOT:
                s = "后台重启";
                PhoneInterfaceUtil.reboot(mAdminName);//重启
                break;
            case ITEM_FORMAT:
                PhoneInterfaceUtil.format(mAdminName);//格式化
                s = "后台格式化";
                break;
            case ITEM_SHUTDOWN:
                PhoneInterfaceUtil.shutDown(mAdminName);//关机
                s = "后台关机";
                break;
            case ITEM_UNLOCK:
                s = "后台解锁";
                break;
            case ITEM_LOCK:
                Intent intent = new Intent(ChatService.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                s = "后台已禁用设备";
                break;
            case ITEM_WIFIDISABLE:
                PhoneInterfaceUtil.setWifiDisable(mAdminName, true);//禁用wifi
                s = "设置wifi禁用";
                break;
            case ITEM_BLUETOOTHDISABLE:
                PhoneInterfaceUtil.setBluetoothDisable(mAdminName, true);//禁用蓝牙
                s = "设置蓝牙禁用";
                break;
            case ITEM_BLUETOOTHABLE:
                PhoneInterfaceUtil.setBluetoothDisable(mAdminName, false);//启用蓝牙
                s = "设置蓝牙启用";
                break;
            case SETMICROPHONEABLED:
                DataUtil.setIsAudioDisable(getApplicationContext(), false);
                s = "设置录音启用";
                break;
            case SETMICROPHONEDISABLED:
                DataUtil.setIsAudioDisable(getApplicationContext(), true);
                s = "设置录音禁用";
                break;
            case SETVIDEOABLED:
                DataUtil.setIsCameraDisable(getApplicationContext(), false);
                s = "设置录像启用";
                break;
            case SETVIDEODISABLED:
                DataUtil.setIsCameraDisable(getApplicationContext(), true);
                s = "设置录像禁用";
                break;
        }
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getMessage(final JSONArray jsonArray) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Type type = new TypeToken<ChatMessage>() {
                }.getType();
                try {
                    ChatMessage chatMessage = gson.fromJson(jsonArray.get(0).toString(), type);
                    if (chatMessage.getChatSendType() == GROUP && chatDb.getId() == chatMessage.getCreatorUserId())
                        return;//群聊，自己发的消息

                    entitys = new ArrayList<>();
                    dealMsg(chatMessage);
                    realTimeConnection.readMessages(getJsonArray(entitys));

                    LogUtil.d(chatMessage.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                playSing();
            }
        });
    }

    @Override
    public void getMessages(final JSONArray jsonArray) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Type type = new TypeToken<List<ChatMessage>>() {
                }.getType();
                try {
                    List<ChatMessage> chatMessages = gson.fromJson(jsonArray.get(0).toString(), type);
                    LogUtil.d(chatMessages.toString());
                    if (!chatMessages.isEmpty()) {
                        entitys = new ArrayList<>();
                        for (ChatMessage chatMessage : chatMessages) {
                            dealMsg(chatMessage);
                        }
                        realTimeConnection.readMessages(getJsonArray(entitys));
                        if (isSing)
                            playSing();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void dealMsg(ChatMessage chatMessage) throws DbException {
        if (chatMessage.getFromUser() != null) {
            chatMessage.setSendName(chatMessage.getFromUser().getName());
        }
        chatMessage.setType(Constants.CHAT_ITEM_TYPE_LEFT);
        EntityBean<Long> entityBean = new EntityBean<>(chatMessage.getId());
        entitys.add(entityBean);

        if (chatMessage.getChatSendType() == GROUP && chatDb.getId() == chatMessage.getCreatorUserId())
            return;//群聊，自己发的消息
        isSing = true;

        switch (chatMessage.getChatMessageType()) {
            case Constants.AUDIO:
                startDownload(chatMessage);
                break;
            case Constants.PTT:
                startDownload(chatMessage);
                break;
            case Constants.CANCEL:
                dealText(chatMessage);
                break;
            case Constants.PICTURE:
                startDownload(chatMessage);
                break;
            case Constants.TEXT:
                dealText(chatMessage);
                break;
        }
    }

    private void dealText(ChatMessage chatMessage) throws DbException {
        Message message = mHandler.obtainMessage();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putSerializable("chatMessage", chatMessage);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }


    private void startDownload(final ChatMessage chatMessage) {
        String url = chatMessage.getMessage();
        final String name;
        if (url != null) {
            name = url.substring(url.lastIndexOf("/") + 1);
        } else name = String.valueOf(SystemClock.currentThreadTimeMillis());
        ApiFactory.getService().downloadFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                final File file = FileUtils.createFile(ChatService.this, name);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        FileUtils.writeFile2Disk(response, file);
                        try {
                            chatMessage.setFilePath(file.getPath());
                            setPath(chatMessage);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                try {
                    setPath(chatMessage);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setPath(ChatMessage chatMessage) throws DbException {
        dealText(chatMessage);
        if (chatMessage.getChatMessageType() == Constants.PTT) {
            if (!DataUtil.isEnter(this)) return;

            MediaManager.playSound(chatMessage.getFilePath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        }

    }

    private void recentMessage(ChatMessage chatMessage, boolean isSend) {
        LogUtil.d(chatMessage.toString());
        long objId;
        String name, sendName = null;
        if (isSend) {//发送
            if (chatMessage.getChatSendType() == USER) {
                objId = chatMessage.getToUserId();
                name = chatMessage.getToUser().getName();
            } else {
                objId = chatMessage.getToGroupId();
                name = chatMessage.getToGroup().getName();
                sendName = "";
            }
        } else {
            if (chatMessage.getChatSendType() == USER) {
                objId = chatMessage.getCreatorUserId();
                name = chatMessage.getFromUser().getName();
            } else {
                objId = chatMessage.getToGroupId();
                name = chatMessage.getToGroup().getName();
                sendName = chatMessage.getFromUser().getName();
            }
        }
        try {
            LogUtil.d("ChatMessage: " + chatMessage.toString());
            RecentMessageBean recentMessage = chatDb.getRecentMessage(objId, chatMessage.getChatSendType());
            if (recentMessage == null) {
                recentMessage = new RecentMessageBean();
            }
            LogUtil.d("db: " + recentMessage.toString());

            if (chatId == objId)
                isSend = true;

            recentMessage.setRead(isSend);
            recentMessage.setSendObjectId(objId);
            recentMessage.setChatMessageType(chatMessage.getChatMessageType());
            recentMessage.setChatSendType(chatMessage.getChatSendType());
            recentMessage.setMessage(chatMessage.getMessage());
            recentMessage.setName(name);
            recentMessage.setSendName(sendName);
            recentMessage.setTickets(chatMessage.getTickets());
            recentMessage.setType(chatMessage.getType());
            recentMessage.setCreationTime(chatMessage.getCreationTime());
            chatDb.saveRecentMessage(recentMessage);

            if (recentCallBack != null) {
                recentCallBack.recentChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageSuccess(String result) {
        LogUtil.d(result);
        Type type = new TypeToken<ChatMessage>() {
        }.getType();
        ChatMessage chatMessage = gson.fromJson(result, type);
        if (messageCallBack != null && chatMessage != null) {
            long tickets = chatMessage.getTickets();
            chatDb.updateMessage(chatMessage, Constants.CHAT_ITEM_SEND_SUCCESS);
            messageCallBack.sendMessageSuccess(tickets);

            messageHandler.removeMessages(tags.get(tickets));
            tags.remove(tickets);
        }
    }

    /**
     * 建群成功
     *
     * @param jsonArray
     */
    @Override
    public void joinGroups(JSONArray jsonArray) {
        Type type = new TypeToken<List<ChatGroupBean>>() {
        }.getType();
        try {
            List<ChatGroupBean> chatGroupBeans = gson.fromJson(jsonArray.get(0).toString(), type);
            chatDb.deleteGroupUsers();//删除群员
            for (ChatGroupBean chatGroupBean : chatGroupBeans) {
                LogUtil.d(chatGroupBean.getName() + " " + chatGroupBean.getChatGroupUsers().toString());
                chatDb.saveGroupUsers(chatGroupBean.getChatGroupUsers());//保存群员
            }
            if (createGroupCallBack != null) {
                createGroupCallBack.success(chatGroupBeans);//建群成功
                EventBus.getDefault().post(chatGroupBeans);
            } else {
                chatDb.saveGroups(chatGroupBeans);
            }

            if (inviteUserCallBack != null) {
                inviteUserCallBack.inviteSuccess();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
        LogUtil.d(jsonArray.toString());
    }

    /**
     * 其他人邀请进群
     *
     * @param jsonArray
     */
    @Override
    public void joinGroup(JSONArray jsonArray) {
        try {
            ChatGroupBean chatGroupBean = gson.fromJson(jsonArray.get(0).toString(), ChatGroupBean.class);
            LogUtil.d(chatGroupBean.getName() + " " + chatGroupBean.getChatGroupUsers().toString());
            chatDb.saveGroupUsers(chatGroupBean.getChatGroupUsers());
            chatDb.saveGroup(chatGroupBean);
            List<ChatGroupBean> group = chatDb.getGroups();

            EventBus.getDefault().post(group);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
        LogUtil.d(jsonArray.toString());
    }

    /**
     * 创建群失败
     *
     * @param jsonArray
     */
    @Override
    public void showError(JSONArray jsonArray) {
        try {
            String message = jsonArray.get(0).toString();
            LogUtil.d(message);
            if (createGroupCallBack != null)
                createGroupCallBack.showError(message);

            if (inviteUserCallBack != null)
                inviteUserCallBack.inviteFail(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnected() {
        LogUtil.d("service:disconnected");
        realTimeConnection.stopConnection();
        realTimeConnection.startConnection(DataUtil.getToken());
    }

    @Override
    public void connected() {
        LogUtil.d("connected");
        // 加入群组
        realTimeConnection.joinGroups();
        realTimeConnection.getHistories();
    }

    @Override
    public void deleteGroupSuccess(JSONArray jsonArray) {
        LogUtil.d("删除群组成功" + jsonArray.toString());

        try {
            ChatGroupBean chatGroupBean = gson.fromJson(jsonArray.get(0).toString(), ChatGroupBean.class);
            chatDb.deleteGroup(chatGroupBean.getId());
            if (recentCallBack != null)
                recentCallBack.recentChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
        List<ChatGroupBean> groups = chatDb.getGroups();
        EventBus.getDefault().post(groups);
    }


    /**
     * message = new ChatMessage();
     * message.setChatMessageType(0); 0:Text,1:Audio,2:Video
     * message.setChatSendType(0); 0:User,1:Group
     * message.setMessage("手机发送");
     * message.setTickets(10000);
     * message.setToUserId(2);
     * message.setCreationTime("..当前时间");
     * message.setCreatorUserId("...当前用户");
     * message.setToGroupId("..当前群组Id");
     *
     * @param message
     */
    public void sendMessage(ChatMessage message) {
        switch (message.getChatMessageType()) {
            case Constants.TEXT:
                sendMessageAndSave(message);
                break;
            case Constants.CANCEL:
                sendMessageAndSave(message);
                break;
            case Constants.AUDIO:
                uploadFile(message);
                break;
            case Constants.PICTURE:
                uploadFile(message);
                break;
            case Constants.PTT:
                uploadFile(message);
                break;
        }


    }

    private void uploadFile(final ChatMessage msg) {
        List<String> str = new ArrayList<>();
        str.add(msg.getFilePath());
        Map<String, RequestBody> params = new HashMap<>();
        for (String url : str) {
            File file = new File(url);
            String name = url.substring(url.lastIndexOf("/") + 1);
            params.put("photos\"; filename=\"" + name, RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        LogUtil.d(params.toString());

        ApiFactory.getService().uploadFile(params).
                enqueue(new MyCallBack<UploadFileResult>() {
                    @Override
                    public void onSuc(Response<UploadFileResult> response) {
                        UploadFileResult body = response.body();
                        msg.setMessage(body.getResult().getUrl());
                        sendMessageAndSave(msg);
                    }

                    @Override
                    public void onFail(String message) {
                        LogUtil.e(message);
                        long tickets = msg.getTickets();
                        messageCallBack.sendMessageFail(tickets);
                        msg.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
                        try {
                            chatDb.saveMessage(msg);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        if (msg.getChatSendType() == USER) {
                            msg.setToUser(new UsersBean(msg.getSendName()));
                        } else {
                            msg.setToGroup(new ChatGroupBean(msg.getSendName()));
                        }
                        recentMessage(msg, true);
                    }
                });
    }

    private void sendMessageAndSave(ChatMessage message) {
        sendMessageToUser(message);
        saveMessage(message);
    }

    private void sendMessageToUser(ChatMessage message) {
        if (NetUtil.isNetConnect(this)) {
            if (isConnected()) {
                realTimeConnection.sendMessage(getJsonArray(message));
                Message message1 = messageHandler.obtainMessage();
                message1.what = i;
                Bundle bundle = new Bundle();
                bundle.putSerializable("msg", message);
                message1.setData(bundle);
                messageHandler.sendMessageDelayed(message1, 5000);
                tags.put(message.getTickets(), i);
                i++;
            } else {
                connection();
                sendFail(message);
            }
        } else {
            sendFail(message);
        }
    }

    private void sendFail(ChatMessage message) {
        message.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
        if (messageCallBack != null)
            messageCallBack.sendMessageFail(message.getTickets());
    }

    private void saveMessage(ChatMessage message) {
        try {
            chatDb.saveMessage(message);
            if (message.getChatSendType() == Constants.USER) {
                message.setToUser(new UsersBean(message.getSendName()));
            } else {
                message.setToGroup(new ChatGroupBean(message.getSendName()));
            }
            recentMessage(message, true);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建群组
     *
     * @param groupName
     */
    public void createGroup(String groupName) {
        JSONArray array = new JSONArray();
        array.put(groupName);
        realTimeConnection.createGroup(array);
    }

    /**
     * 邀请用户加入群组
     *
     * @param bean
     */
    public void inviteToGroup(InviteToGroupBean bean) {
        LogUtil.d(bean.toString());
        realTimeConnection.inviteToGroup(getJsonArray(bean));
    }

    private JSONArray getJsonArray(Object object) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(gson.toJson(object));
        return jsonArray;
    }

    public void exitGroup(ExitGroupBean bean) {
        realTimeConnection.exitGroup(getJsonArray(bean));
    }

    @Override
    public void deleteUser(JSONArray jsonArray) {
        try {
            ExitGroupBean exitGroupBean = gson.fromJson(jsonArray.get(0).toString(), ExitGroupBean.class);
            chatDb.deleteGroupUser(exitGroupBean.getGroupId(), exitGroupBean.getUserId());
            if (groupUserCallBack != null) {
                groupUserCallBack.deleteUserSuccess();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver myNetWorkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("myNetWorkReceiver");
            switch (intent.getAction()) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    if (NetUtil.isNetConnect(context)) {
                        LogUtil.d("网络连接");

                        realTimeConnection.stopConnection();
                        realTimeConnection.startConnection(DataUtil.getToken());
                    } else {
                        LogUtil.d("网络断开");
                        Toast.makeText(context, "连接服务器失败,请检查网络设置!",
                                Toast.LENGTH_SHORT).show();
                        realTimeConnection.stopConnection();
                    }
                    break;
            }
        }
    };

    public void deleteGroup(long groupId) {
        realTimeConnection.deleteGroup(getJsonArray(new EntityBean<Long>(groupId)));
    }

    private Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtil.d("what: " + msg.what);

            Bundle data = msg.getData();
            ChatMessage msg1 = (ChatMessage) data.getSerializable("msg");

            if (msg1 != null) {
                LogUtil.d("receive msg");
                sendFail(msg1);
                chatDb.updateMessage(msg1, Constants.CHAT_ITEM_SEND_ERROR);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Bundle data = msg.getData();
                ChatMessage chatMessage = (ChatMessage) data.getSerializable("chatMessage");
                try {
                    chatDb.saveMessage(chatMessage);
                    recentMessage(chatMessage, false);
                    if (messageCallBack != null) {
                        messageCallBack.newMessage(chatMessage);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // 初始化定时器
    Timer timer = new Timer();

    // 停止定时器
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }

    private void startTimer() {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                connection();
            }
        }, 100, 60 * 1000);
    }

//    Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            Timer timer = new Timer();
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    LogUtil.d("checkLauncher run");
//                    if (!DataUtil.isEnter(ChatService.this)) {
//                        LogUtil.d("police is exit");
//                        if (!CommonMethod.isDefaultLauncher(ChatService.this)) {
//                            LogUtil.d("police is not default launcher");
//                            PhoneInterfaceUtil.setAppAsLauncher(mAdminName, ChatService.this.getPackageName(), Launcher.class.getCanonicalName());
//                            Intent startMain = new Intent(Intent.ACTION_MAIN);
//                            startMain.addCategory(Intent.CATEGORY_HOME);
//                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            ChatService.this.startActivity(startMain);
//                        }
//                    }
//                }
//            };
//            timer.schedule(task, 0, 3000);
//        }
//    });
}
