package com.dark_yx.policemain.fragment.model;


import com.dark_yx.policemain.fragment.bean.AppWhiteListInput;
import com.dark_yx.policemaincommon.Models.AppWhiteListResult;
import com.dark_yx.policemain.fragment.contract.HomeContract;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;

import retrofit2.Response;

/**
 * Created by Ligh on 2018/2/6 09:10
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class HomeModel implements HomeContract.Model {
    private HomeContract.Presenter presenter;

    public HomeModel(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getAppWhiteList(AppWhiteListInput input) {
        ApiFactory.getService().getAppWhiteList(DataUtil.getToken(), input).enqueue(new MyCallBack<AppWhiteListResult>() {
            @Override
            public void onSuc(Response<AppWhiteListResult> response) {
                presenter.getAppWhiteListSuccess(response.body());
            }

            @Override
            public void onFail(String message) {
                presenter.getAppWhiteListFail(message);
            }
        });
    }
}
