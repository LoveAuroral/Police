package com.dark_yx.policemain.chat;

import android.content.Context;
import android.content.OperationApplicationException;

import com.dark_yx.policemain.chat.callback.IRealTimeCallback;
import com.zsoft.signala.ConnectionState;
import com.zsoft.signala.hubs.HubConnection;
import com.zsoft.signala.hubs.HubInvokeCallback;
import com.zsoft.signala.hubs.HubOnDataCallback;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.util.LogUtil;

import java.util.Collection;

/**
 * Created by Ligh on 2017/8/28 09:34
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class RealTimeConnection {
    private String mUrl;
    private Context mContext;
    private HubConnection con;
    private com.zsoft.signala.hubs.IHubProxy hub;
    private IRealTimeCallback realTimeCallBack;

    /**
     * 初始化连接
     *
     * @param mUrl     连接地址
     * @param mContext 上下文
     */
    public RealTimeConnection(IRealTimeCallback mCallback, String mUrl, Context mContext) {
        this.mUrl = mUrl;
        LogUtil.d(mUrl);
        this.mContext = mContext;
        realTimeCallBack = mCallback;
    }

    public ConnectionState getCurrentState() {
        if (con != null)
            return con.getCurrentState().getState();
        else return ConnectionState.Disconnected;
    }

    /**
     * 开始连接
     *
     * @param token 验证
     */
    public void startConnection(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        LogUtil.d("signala startConnection");
        if (con == null) {
            initPolling();
        }
        con.addHeader("Authorization", token);
        con.Start();
    }


    /**
     * 终止连接
     */
    public void stopConnection() {
        if (con != null) {
            LogUtil.d("signala stopConnection");
            con.Stop();
            con = null;
        }
    }

    /**
     * 初始化推送
     */
    private void initPolling() {
        con = new HubConnection(mUrl, mContext, new LongPollingTransport()) {
            @Override
            public void OnStateChanged(StateBase oldState, StateBase newState) {
                LogUtil.d("signalA connect state: " + newState.getState().toString());
                switch (newState.getState()) {
                    case Connected:
                        realTimeCallBack.connected();
                        break;
                    case Reconnecting:
                        realTimeCallBack.disconnected();
                        break;
                }
            }

            @Override
            public void OnError(Exception exception) {
            }
        };
        try {
            hub = con.CreateHubProxy("policeCommonHub");
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        initOnListers();
    }

    private void initOnListers() {
        hub.On("getNotification", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray jsonArray) {
                realTimeCallBack.getNotification(jsonArray);
            }
        });

        hub.On("getMessages", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray jsonArray) {
                realTimeCallBack.getMessages(jsonArray);
            }
        });
        hub.On("getMessage", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray jsonArray) {
                realTimeCallBack.getMessage(jsonArray);
            }
        });
        hub.On("showError", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray jsonArray) {
                realTimeCallBack.showError(jsonArray);
            }
        });
        hub.On("joinGroups", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray jsonArray) {
                realTimeCallBack.joinGroups(jsonArray);
            }
        });
        hub.On("joinGroup", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray jsonArray) {
                realTimeCallBack.joinGroup(jsonArray);
            }
        });

        hub.On("deleteGroup", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray jsonArray) {
                realTimeCallBack.deleteGroupSuccess(jsonArray);
            }
        });
        hub.On("deleteUser", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray jsonArray) {
                realTimeCallBack.deleteUser(jsonArray);
            }
        });
    }

    public void getHistories() {
        hub.Invoke("getHistories", (Collection<?>) null, null);
    }

    public void joinGroups() {
        hub.Invoke("joinGroups", (Collection<?>) null, null);
    }

    public void readMessages(JSONArray jsonArray) throws JSONException {
        hub.Invoke("readMessages", jsonArray, null);
    }

    public void sendMessage(JSONArray jsonArray) {
        hub.Invoke("sendMessage", jsonArray, new HubInvokeCallback() {
            @Override
            public void OnResult(boolean b, String s) {
                LogUtil.d(s);
                realTimeCallBack.sendMessageSuccess(s);
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void createGroup(JSONArray jsonArray) {
        hub.Invoke("createGroup", jsonArray, null);
    }

    public void inviteToGroup(JSONArray jsonArray) {
        hub.Invoke("inviteToGroup", jsonArray, new HubInvokeCallback() {
            @Override
            public void OnResult(boolean b, String s) {
                LogUtil.d(s);
            }

            @Override
            public void OnError(Exception e) {
                LogUtil.d("OnError: " + e.toString());
            }
        });
    }

    public void deleteGroup(JSONArray jsonArray) {
        hub.Invoke("deleteGroup", jsonArray, new HubInvokeCallback() {
            @Override
            public void OnResult(boolean b, String s) {
                LogUtil.d(s);
            }

            @Override
            public void OnError(Exception e) {

            }
        });
    }

    public void exitGroup(JSONArray jsonArray) {
        hub.Invoke("exitGroup", jsonArray, new HubInvokeCallback() {
            @Override
            public void OnResult(boolean b, String s) {
                LogUtil.d(s);
            }

            @Override
            public void OnError(Exception e) {

            }
        });
    }

}