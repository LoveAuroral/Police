package com.dark_yx.policemaincommon.api;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ligh on 2017/9/19 10:30
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public abstract class MyCallBack<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuc(response);
        } else {
            try {
                onFail(response.errorBody().string());

                String err = response.code() + "";
                LogUtil.e(err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {
            //
        } else if (t instanceof ConnectException) {
            //
        } else if (t instanceof RuntimeException) {
            //
        }

        onFail(t.toString());

//        LogUtil.d("onFailure");
//        try {
//            AbpRequestBase abpRequestBase = new Gson().fromJson(t.toString(), AbpRequestBase.class);
//            if (abpRequestBase.unAuthorizedRequest) {
//                LogUtil.d("未登录： " + t.toString());
//                x.app().sendBroadcast(new Intent(BIND_SUCCESS_ACTION));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public abstract void onSuc(Response<T> response);

    public abstract void onFail(String message);

}
