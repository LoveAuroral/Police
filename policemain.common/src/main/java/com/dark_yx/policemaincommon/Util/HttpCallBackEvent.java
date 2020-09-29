package com.dark_yx.policemaincommon.Util;

import org.json.JSONException;

/**
 * Created by dark_yx on 2016-03-09.
 * Http访问回调接口
 */
public interface HttpCallBackEvent {
    public void Excute(String result) throws JSONException;
    public void OnError(Throwable ex) throws JSONException;
}
