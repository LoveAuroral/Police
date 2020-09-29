package com.dark_yx.policemain.chat.view.chatui.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpiz.android.bubbleview.BubbleLinearLayout;
import com.cpiz.android.bubbleview.BubblePopupWindow;
import com.cpiz.android.bubbleview.BubbleStyle;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.activity.GroupFriendsActivity;
import com.dark_yx.policemain.chat.ChatService;
import com.dark_yx.policemain.chat.beans.ChatMessage;
import com.dark_yx.policemain.chat.beans.RecentMessageBean;
import com.dark_yx.policemain.chat.callback.IMessageCallback;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemain.chat.view.chatui.adapter.ChatAdapter;
import com.dark_yx.policemain.chat.view.chatui.adapter.CommonFragmentPagerAdapter;
import com.dark_yx.policemain.chat.view.chatui.enity.FullImageInfo;
import com.dark_yx.policemain.chat.view.chatui.ui.fragment.ChatEmotionFragment;
import com.dark_yx.policemain.chat.view.chatui.ui.fragment.ChatFunctionFragment;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;
import com.dark_yx.policemain.chat.view.chatui.util.GlobalOnItemClickManagerUtils;
import com.dark_yx.policemain.chat.view.chatui.util.MediaManager;
import com.dark_yx.policemain.chat.view.chatui.widget.EmotionInputDetector;
import com.dark_yx.policemain.chat.view.chatui.widget.NoScrollViewPager;
import com.dark_yx.policemain.chat.view.chatui.widget.StateButton;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatActivity extends AppCompatActivity implements IMessageCallback {
    @Bind(R.id.chat_list)
    EasyRecyclerView chatList;
    @Bind(R.id.emotion_voice)
    ImageView emotionVoice;
    @Bind(R.id.edit_text)
    EditText editText;
    @Bind(R.id.voice_text)
    TextView voiceText;
    @Bind(R.id.emotion_button)
    ImageView emotionButton;
    @Bind(R.id.emotion_add)
    ImageView emotionAdd;
    @Bind(R.id.emotion_send)
    StateButton emotionSend;
    @Bind(R.id.viewpager)
    NoScrollViewPager viewpager;
    @Bind(R.id.emotion_layout)
    RelativeLayout emotionLayout;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.iv_detail)
    ImageView iv_detail;
    @Bind(R.id.iv_back)
    ImageView iv_back;

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<ChatMessage> messageInfos;
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;
    ChatService chatService;
    private ChatDb db;

    int chatSendType;
    long sendId;
    String sendName;
    private boolean isPtt;
    private boolean isLongPressKey;//是否长按
    private int currentPage = 1;
    private int totalPages;
    private int totalSize;
    public static final int count = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        db = new ChatDb();
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
        initChat();

        initWidget();
    }

    private void initData() {
        chatSendType = getIntent().getIntExtra("chatSendType", 0);
        sendId = getIntent().getLongExtra("sendId", 0);
        sendName = getIntent().getStringExtra("sendName");
        isPtt = getIntent().getBooleanExtra("isPtt", false);

        tv_title.setText(sendName);

        if (chatSendType == Constants.GROUP)
            iv_detail.setVisibility(View.VISIBLE);

        iv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, GroupFriendsActivity.class);
                intent.putExtra("groupId", String.valueOf(sendId));
                startActivity(intent);
            }
        });
        if (isPtt) {
            popupHandler.sendEmptyMessageDelayed(0, 100);
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWidget() {
        try {
            totalSize = db.getUserChatMessagesCount(sendId, chatSendType);
            totalPages = totalSize / count;
        } catch (DbException e) {
            e.printStackTrace();
        }

        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();

        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);

        chatAdapter = new ChatAdapter(this);
        loadData();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        chatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    chatAdapter.handler.removeCallbacksAndMessages(null);
                    chatAdapter.notifyDataSetChanged();
                } else {
                    chatAdapter.handler.removeCallbacksAndMessages(null);
                    mDetector.hideEmotionLayout(false);
                    mDetector.hideSoftInput();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatList.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.d("onRefresh");
                if (currentPage > totalPages)
                    chatList.setRefreshing(false);
                else {
                    currentPage++;
                    loadData();
                }
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
        chatList.scrollToPosition(chatAdapter.getCount() - 1);
    }

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
//            Toast.makeText(ChatActivity.this, "onHeaderClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageClick(View view, int position) {
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            FullImageInfo fullImageInfo = new FullImageInfo();
            fullImageInfo.setLocationX(location[0]);
            fullImageInfo.setLocationY(location[1]);
            fullImageInfo.setWidth(view.getWidth());
            fullImageInfo.setHeight(view.getHeight());
            fullImageInfo.setImageUrl(messageInfos.get(position).getFilePath());
            EventBus.getDefault().postSticky(fullImageInfo);
            startActivity(new Intent(ChatActivity.this, FullImageActivity.class));
            overridePendingTransition(0, 0);
        }

        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (messageInfos.get(position).getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            MediaManager.playSound(messageInfos.get(position).
                    getFilePath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                }
            });
        }

        @Override
        public void onSendFailClick(View view, int position) {
            ChatMessage messageInfo = messageInfos.get(position);
            showSendFailDialog(messageInfo);
        }

        @Override
        public void onTextLongClick(View view, int position) {
            LogUtil.d("position: " + position);
            showTextDialog(view, position);
        }

        @Override
        public void onVoiceLongClick(View view, int position) {
            showVoiceDialog(view, position);
        }

        @Override
        public void onImageLongClick(View view, int position) {
            showVoiceDialog(view, position);
        }

    };

    private void showTextDialog(View v, final int position) {
        final ChatMessage chatMessage = messageInfos.get(position);
        View rootView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.simple_text_bubble, null);
        TextView tv_copy = (TextView) rootView.findViewById(R.id.tv_copy);
        TextView tv_delete = (TextView) rootView.findViewById(R.id.tv_delete);
        BubbleLinearLayout bubbleView = (BubbleLinearLayout) rootView.findViewById(R.id.popup_bubble);
        final BubblePopupWindow window = new BubblePopupWindow(rootView, bubbleView);
        window.setCancelOnTouch(true);
        window.setCancelOnTouchOutside(true);
        window.showArrowTo(v, BubbleStyle.ArrowDirection.Down);

        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", chatMessage.getMessage());
                cm.setPrimaryClip(mClipData);

                Toast.makeText(getApplicationContext(), "复制成功！",
                        Toast.LENGTH_SHORT).show();
                window.dismiss();
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage(position);
                window.dismiss();
            }
        });
    }

    private void showVoiceDialog(View v, final int position) {
        View rootView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.simple_voice_bubble, null);
        TextView tv_delete = (TextView) rootView.findViewById(R.id.tv_delete);

        BubbleLinearLayout bubbleView = (BubbleLinearLayout) rootView.findViewById(R.id.popup_bubble);
        final BubblePopupWindow window = new BubblePopupWindow(rootView, bubbleView);
        window.setCancelOnTouch(true);
        window.setCancelOnTouchOutside(true);
        window.showArrowTo(v, BubbleStyle.ArrowDirection.Down);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage(position);
                window.dismiss();
            }
        });
    }

    private void deleteMessage(int position) {
        db.deleteMessage(messageInfos.get(position));

        messageInfos.remove(position);
        chatAdapter.remove(position);
        chatAdapter.notifyDataSetChanged();

        resentChange();
    }

    private void resentChange() {
        int size = messageInfos.size();
        try {
            RecentMessageBean recentMessage = db.getRecentMessage(sendId, chatSendType);
            if (size != 0) {
                ChatMessage chatMessage = messageInfos.get(size - 1);
                if (recentMessage != null) {
                    recentMessage.setMessage(chatMessage.getMessage());
                    recentMessage.setChatMessageType(chatMessage.getChatMessageType());
                    db.saveRecentMessage(recentMessage);
                }
            } else {
                if (recentMessage != null) {
                    db.deleteRecentMessage(recentMessage);
                }
            }

            chatService.resentChange();
        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    private void showSendFailDialog(final ChatMessage messageInfo) {
        List<String> strings = new ArrayList<>();
        strings.add("重新发送");
        DialogUIUtils.showBottomSheetAndCancel(this, strings, "取消", new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);

                chatAdapter.notifyDataSetChanged();
                chatService.reSend(messageInfo);
            }
        }).show();
    }

    /**
     * 构造聊天数据
     */
    private void loadData() {
        LogUtil.d("loadData: " + sendId);
        try {
            messageInfos = db.getUserChatMessages(sendId, chatSendType, totalSize, totalSize - currentPage * count);

            if (messageInfos != null && messageInfos.size() > 0) {
                if (currentPage != 1) {
                    chatAdapter.clear();
                    chatAdapter.addAll(messageInfos);

                    chatAdapter.notifyDataSetChanged();
                    chatList.scrollToPosition(chatAdapter.getCount() - (totalSize - currentPage * count + 1));
                    chatList.setRefreshing(false);
                } else {
                    chatAdapter.addAll(messageInfos);
                }
            } else messageInfos = new ArrayList<>();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initChat() {
        bindService(new Intent(this, ChatService.class), chatServiceConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    ServiceConnection chatServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d("聊天服务连接成功");
            chatService = ((ChatService.ChatServiceBinder) service).getService();
            chatService.registerMessageCallback(ChatActivity.this, sendId);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("聊天服务连接失败");
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final ChatMessage messageInfo) {
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setChatSendType(chatSendType);
        messageInfo.setSendName(sendName);
        if (chatSendType == Constants.USER) {
            messageInfo.setToUserId(sendId);
        } else {
            messageInfo.setToGroupId(sendId);
        }
        messageInfo.setTickets(System.currentTimeMillis());
        messageInfos.add(messageInfo);
        chatAdapter.add(messageInfo);
        chatList.scrollToPosition(chatAdapter.getCount() - 1);
        chatService.sendMessage(messageInfo);
        LogUtil.d("sendMessage: " + messageInfo.toString());
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(chatServiceConnection);
        ButterKnife.unbind(this);
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);

        // 必须去掉消息接收回调
        if (chatService != null)
            chatService.unregisterMessageCallback();
    }

    @Override
    public void sendMessageSuccess(long id) {
        for (ChatMessage chatMessage : messageInfos) {
            if (chatMessage.getTickets() == id) {
                chatMessage.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                break;
            }
        }
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendMessageFail(long id) {
        for (ChatMessage chatMessage : messageInfos) {
            if (chatMessage.getTickets() == id) {
                chatMessage.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
                break;
            }
        }
        chatAdapter.notifyDataSetChanged();
    }



    @Override
    public void newMessage(ChatMessage chatMessage) {
        // 判断当前收到的消息是不是这个窗口的消息
        long sendId = 0;
        if (chatMessage.getChatSendType() == 0) {
            sendId = chatMessage.getCreatorUserId();
        } else {
            sendId = chatMessage.getToGroupId();
        }
        if (sendId == this.sendId) {
            LogUtil.d("收到我的消息");
            messageInfos.add(chatMessage);
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
            chatList.scrollToPosition(chatAdapter.getCount() - 1);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int repeatCount = event.getRepeatCount();
        LogUtil.d("getRepeatCount: " + repeatCount);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (repeatCount == 0) {//识别长按短按的代码
                    event.startTracking();
                    isLongPressKey = false;
                } else {
                    isLongPressKey = true;
                }
                return true;
//          case KeyEvent.KEYCODE_VOLUME_DOWN:
//                if (repeatCount == 0) {//识别长按短按的代码
//                    event.startTracking();
//                    isLongPressKey = false;
//                } else {
//                    isLongPressKey = true;
//                }
//                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (isLongPressKey) {
                isLongPressKey = false;
                LogUtil.d("长按松开");
//                mDetector.stopPtt();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//            Toast.makeText(getApplicationContext(), "长按按下", Toast.LENGTH_SHORT).show();
            LogUtil.d("长按按下");
            mDetector.startPtt();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @SuppressLint("HandlerLeak")
    private Handler popupHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                LogUtil.d("长按按下");
                mDetector.startPtt();
            }
        }
    };

}
