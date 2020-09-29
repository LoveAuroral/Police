package com.dark_yx.policemain.chat.view.chatui.util;

import android.content.Context;
import android.content.Intent;

import com.dark_yx.policemain.chat.view.chatui.ui.activity.ChatActivity;
import com.dark_yx.policemaincommon.Models.UsersBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Rance on 2016/12/20 16:51
 * 邮箱：rance935@163.com
 */
public class Constants {
    public static final String TAG = "rance";
    /**
     * 0x001-接受消息  0x002-发送消息
     **/
    public static final int CHAT_ITEM_TYPE_LEFT = 0x001;
    public static final int CHAT_ITEM_TYPE_RIGHT = 0x002;
    /**
     * 0x003-发送中  0x004-发送失败  0x005-发送成功
     **/
    public static final int CHAT_ITEM_SENDING = 0x003;
    public static final int CHAT_ITEM_SEND_ERROR = 0x004;
    public static final int CHAT_ITEM_SEND_SUCCESS = 0x005;

    /**
     *
     */
    public static final int TEXT = 0;
    public static final int AUDIO = 1;
    public static final int PICTURE = 2;
    public static final int CANCEL = 3;
    public static final int PTT = 4;

    public static final int USER = 0;
    public static final int GROUP = 1;

    public static List<Long> groupList = new ArrayList<>();

    public static void startChat(Context context, UsersBean usersBean) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("chatSendType", Constants.USER);
        intent.putExtra("sendId", usersBean.getId());
        intent.putExtra("sendName", usersBean.getName());
        context.startActivity(intent);
    }
}
