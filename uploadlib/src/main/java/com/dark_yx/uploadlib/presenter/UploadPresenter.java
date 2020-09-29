package com.dark_yx.uploadlib.presenter;

import android.util.Log;

import com.dark_yx.uploadlib.beans.DataDicBean;
import com.dark_yx.uploadlib.contract.UploadContract;
import com.dark_yx.uploadlib.model.UploadModel;

/**
 * Created by dark_yx-i on 2018/2/26.
 */

public class UploadPresenter implements UploadContract.Presenter {

    UploadContract.View view;
    UploadContract.Model model;

    public UploadPresenter(UploadContract.View view){
        this.view = view;
        this.model = new UploadModel(this);
    }

    @Override
    public void getData(String key, String token) {
        Log.d("Main","获取数据");
        this.model.getData(key,token);
    }

    @Override
    public void getDataDicBeanSuccess(DataDicBean dicBean) {
        this.view.getDataDicBeanSuccess(dicBean);
    }

    @Override
    public void upload(String filePath, String tag,int type, String token) {
        Log.d("Persenter",filePath);
        this.model.upload(filePath,tag,type,token);
    }

    @Override
    public void uoloadSuccess() {
        this.view.uoloadSuccess();
    }

    @Override
    public void uoloadFail(String error) {
            this.view.uoloadFail(error);
    }
}
