package com.dark_yx.policemain.fragment.presenter;

import com.dark_yx.policemain.fragment.bean.AppWhiteListInput;
import com.dark_yx.policemaincommon.Models.AppWhiteListResult;
import com.dark_yx.policemain.fragment.contract.HomeContract;
import com.dark_yx.policemain.fragment.model.HomeModel;

/**
 * Created by Ligh on 2018/2/6 09:10
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private HomeContract.Model model;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        model = new HomeModel(this);
    }

    @Override
    public void getAppWhiteList(AppWhiteListInput input) {
        view.showDialog();
        model.getAppWhiteList(input);
    }

    @Override
    public void getAppWhiteListSuccess(AppWhiteListResult result) {
        view.dismissDialog();
        view.getAppWhiteListSuccess(result);
    }

    @Override
    public void getAppWhiteListFail(String err) {
        view.dismissDialog();
        view.showError(err);
    }
}
