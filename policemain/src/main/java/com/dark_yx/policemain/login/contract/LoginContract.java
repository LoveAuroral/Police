package com.dark_yx.policemain.login.contract;

import com.dark_yx.policemain.login.bean.LoginInput;
import com.dark_yx.policemain.login.bean.LoginResult;

/**
 * Created by Ligh on 2018/2/5 11:15
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public interface LoginContract {
    interface Model {
        void login(LoginInput input);
    }

    interface View {
        void showDialog();

        void dismissDialog();

        void showError(String err);

        void loginSuccess(LoginResult result);
    }

    interface Presenter {
        void login(LoginInput input);

        void loginFail(String err);

        void loginSuccess(LoginResult result);
    }
}
