package com.dark_yx.policemain.chat.callback;

import org.json.JSONArray;

/**
 * Created by dark_yx-i on 2018/2/6.
 */

public interface IRealTimeCallback {
    void getNotification(JSONArray jsonArray);


    void getMessage(JSONArray jsonArray);

    void getMessages(JSONArray jsonArray);

    void sendMessageSuccess(String result);

    void joinGroups(JSONArray jsonArray);

    void joinGroup(JSONArray jsonArray);

    void showError(JSONArray jsonArray);

    void disconnected();

    void connected();

    void deleteGroupSuccess(JSONArray jsonArray);

    void deleteUser(JSONArray jsonArray);
}
