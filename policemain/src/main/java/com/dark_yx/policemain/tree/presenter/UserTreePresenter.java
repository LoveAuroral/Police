package com.dark_yx.policemain.tree.presenter;

import com.dark_yx.policemain.tree.contract.UserTreeContract;
import com.dark_yx.policemain.tree.model.UserTreeModel;
import com.dark_yx.policemaincommon.Models.UserTreeResult;

/**
 * Created by Ligh on 2018/2/5 15:51
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class UserTreePresenter implements UserTreeContract.Presenter {
    private UserTreeContract.View view;
    private UserTreeContract.Model model;

    public UserTreePresenter(UserTreeContract.View view) {
        this.view = view;
        model = new UserTreeModel(this);
    }

    @Override
    public void getUserTree() {
        model.getUserTree();
        view.showDialog();
    }

    @Override
    public void getUserTreeSuccess(UserTreeResult result) {
        view.getUserTreeSuccess(result);
        view.dismissDialog();
    }

    @Override
    public void getUserTreeFail(String err) {
        view.getUserTreeFail(err);
        view.dismissDialog();
    }
}
