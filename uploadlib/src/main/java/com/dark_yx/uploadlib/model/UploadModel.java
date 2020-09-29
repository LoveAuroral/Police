package com.dark_yx.uploadlib.model;

import android.util.Log;

import com.dark_yx.uploadlib.api.ApiFactory;
import com.dark_yx.uploadlib.beans.DataDicBean;
import com.dark_yx.uploadlib.beans.GetDataDicInput;
import com.dark_yx.uploadlib.beans.UploadBean;
import com.dark_yx.uploadlib.beans.UploadFileResult;
import com.dark_yx.uploadlib.contract.UploadContract;
import com.dark_yx.policemaincommon.api.MyCallBack;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by dark_yx-i on 2018/2/26.
 */

public class UploadModel implements UploadContract.Model {

    private final UploadContract.Presenter presenter;

    public UploadModel(UploadContract.Presenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void getData(String key, String token) {
        GetDataDicInput input = new GetDataDicInput();
        input.setDicKey(key);
        ApiFactory.getService().getDataDic(token,input).enqueue(new MyCallBack<DataDicBean>() {
            @Override
            public void onSuc(Response<DataDicBean> response) {
                presenter.getDataDicBeanSuccess(response.body());
            }

            @Override
            public void onFail(String message) {
                Log.d("Main",message);
            }
        });
    }

    @Override
    public void upload(final String filePath, final String tag, final int type, final String token) {
        Map<String, RequestBody> params = new HashMap<>();
        final File file = new File(filePath);
        String name = filePath.substring(filePath.lastIndexOf("/") + 1);
        params.put("photos\"; filename=\"" + name, RequestBody.create(MediaType.parse("multipart/form-data"), file));
        LogUtil.d(params.toString());

        ApiFactory.getService().uploadFile(params).
                enqueue(new MyCallBack<UploadFileResult>() {
                    @Override
                    public void onSuc(Response<UploadFileResult> response) {
                        UploadFileResult body = response.body();
                        String url = body.getResult().getUrl();
                        UploadBean uploadBean = new UploadBean();
                        UploadBean.ForensicsRecordEditDtoBean forensicsRecordEditDtoBean = new UploadBean.ForensicsRecordEditDtoBean();
                        forensicsRecordEditDtoBean.setSrc(url);
                        forensicsRecordEditDtoBean.setForensicsRecordType(type);
                        forensicsRecordEditDtoBean.setMode(tag);
                        Log.d("FileName",file.getName());
                        forensicsRecordEditDtoBean.setDes(file.getName());
                        uploadBean.setForensicsRecordEditDto(forensicsRecordEditDtoBean);
                        ApiFactory.getService().forensicsRecordCreate(token,uploadBean).enqueue(new MyCallBack<ResponseBody>() {
                            @Override
                            public void onSuc(Response<ResponseBody> response) {
                                presenter.uoloadSuccess();
                            }

                            @Override
                            public void onFail(String message) {
                                presenter.uoloadFail(message);
                            }
                        });
                    }

                    @Override
                    public void onFail(String message) {
                        presenter.uoloadFail(message);
                    }
                });
    }
}
