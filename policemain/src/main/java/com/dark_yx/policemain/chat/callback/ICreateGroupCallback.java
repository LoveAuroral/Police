package com.dark_yx.policemain.chat.callback;

import com.dark_yx.policemain.chat.beans.ChatGroupBean;

import java.util.List;

/**
 * Created by dark_yx-i on 2018/2/7.
 */

public interface ICreateGroupCallback {
    void createGroup(String name);

    void showError(String message);

    void success(List<ChatGroupBean> beans);
}
