package com.dark_yx.policemain.chat.callback;

import com.dark_yx.policemain.chat.beans.ChatMessage;

/**
 * Created by dark_yx-i on 2018/2/7.
 */

public interface IMessageCallback {
    void sendMessageSuccess(long id);

    void sendMessageFail(long id);

    void newMessage(ChatMessage chatMessage);
}
