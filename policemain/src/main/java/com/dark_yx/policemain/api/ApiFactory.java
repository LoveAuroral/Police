package com.dark_yx.policemain.api;


import com.dark_yx.policemaincommon.Util.DataUtil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ligh on 2017/8/25 14:05
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class ApiFactory {
    private static ApiService apiService;

    private static Retrofit stringRetrofit = new Retrofit.Builder()
            .baseUrl(DataUtil.getBassIp())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiService getService() {
        if (apiService == null)
            apiService = stringRetrofit.create(ApiService.class);
        return apiService;
    }

    public static void reSet() {
        apiService = new Retrofit.Builder()
                .baseUrl(DataUtil.getBassIp())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }

}
