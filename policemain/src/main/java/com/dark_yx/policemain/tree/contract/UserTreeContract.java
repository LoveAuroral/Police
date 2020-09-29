package com.dark_yx.policemain.tree.contract;

import com.dark_yx.policemaincommon.Models.UserTreeResult;

/**
 * Created by Ligh on 2018/2/5 15:51
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public interface UserTreeContract {
    interface Model {
        void getUserTree();
    }

    interface View {
        void showDialog();

        void dismissDialog();

        void getUserTreeSuccess(UserTreeResult result);

        void getUserTreeFail(String err);
    }

    interface Presenter {
        void getUserTree();

        void getUserTreeSuccess(UserTreeResult result);

        void getUserTreeFail(String err);
    }
}
