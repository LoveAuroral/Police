package com.dark_yx.policemain.login.model;

import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemaincommon.api.MyCallBack;
import com.dark_yx.policemain.login.bean.LoginInput;
import com.dark_yx.policemain.login.bean.LoginResult;
import com.dark_yx.policemain.login.contract.LoginContract;

import org.xutils.common.util.LogUtil;

import retrofit2.Response;

/**
 * Created by Ligh on 2018/2/5 11:15
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class LoginModel implements LoginContract.Model {
    private LoginContract.Presenter presenter;

    public LoginModel(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void login(LoginInput input) {
        LogUtil.d(input.toString());

        ApiFactory.getService().login(input).enqueue(new MyCallBack<LoginResult>() {
            @Override
            public void onSuc(Response<LoginResult> response) {
                presenter.loginSuccess(response.body());
            }

            @Override
            public void onFail(String message) {
                LogUtil.d("onFail: " + message);
                presenter.loginFail(message);
            }
        });
    }
}
