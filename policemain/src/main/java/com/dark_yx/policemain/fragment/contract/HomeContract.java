package com.dark_yx.policemain.fragment.contract;

import com.dark_yx.policemain.fragment.bean.AppWhiteListInput;
import com.dark_yx.policemaincommon.Models.AppWhiteListResult;

/**
 * Created by Ligh on 2018/2/6 09:10
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public interface HomeContract {

    interface Model {
        void getAppWhiteList(AppWhiteListInput input);
    }

    interface View {
        void showDialog();

        void dismissDialog();

        void getAppWhiteListSuccess(AppWhiteListResult result);

        void showError(String err);
    }

    interface Presenter {
        void getAppWhiteList(AppWhiteListInput input);

        void getAppWhiteListSuccess(AppWhiteListResult result);

        void getAppWhiteListFail(String err);
    }
}
