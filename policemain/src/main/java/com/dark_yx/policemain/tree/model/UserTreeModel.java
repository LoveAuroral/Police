package com.dark_yx.policemain.tree.model;

import com.dark_yx.policemaincommon.Models.UserTreeResult;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemain.tree.contract.UserTreeContract;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemaincommon.api.MyCallBack;

import retrofit2.Response;

/**
 * Created by Ligh on 2018/2/5 15:51
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class UserTreeModel implements UserTreeContract.Model {
    private UserTreeContract.Presenter presenter;

    public UserTreeModel(UserTreeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getUserTree() {
        ApiFactory.getService().getUserTree(DataUtil.getToken()).enqueue(new MyCallBack<UserTreeResult>() {
            @Override
            public void onSuc(Response<UserTreeResult> response) {
                presenter.getUserTreeSuccess(response.body());
            }

            @Override
            public void onFail(String message) {
                presenter.getUserTreeFail(message);
            }
        });
    }
}
