package com.dark_yx.policemain.login.presenter;

import android.text.TextUtils;

import com.dark_yx.policemain.login.bean.LoginInput;
import com.dark_yx.policemain.login.bean.LoginResult;
import com.dark_yx.policemain.login.contract.LoginContract;
import com.dark_yx.policemain.login.model.LoginModel;

/**
 * Created by Ligh on 2018/2/5 11:15
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private LoginContract.Model model;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        model = new LoginModel(this);
    }

    @Override
    public void login(LoginInput input) {
        if (TextUtils.isEmpty(input.getUsernameOrEmailAddress())) {
            view.showError("请输入用户名!");
            return;
        }
        if (TextUtils.isEmpty(input.getPassword())) {
            view.showError("请输入密码!");
            return;
        }

        view.showDialog();
        model.login(input);

    }

    @Override
    public void loginFail(String err) {
        view.showError(err);
        view.dismissDialog();
    }

    @Override
    public void loginSuccess(LoginResult result) {
        view.dismissDialog();
        view.loginSuccess(result);
    }
}
