package com.dark_yx.uploadlib.api;

import com.dark_yx.uploadlib.beans.DataDicBean;
import com.dark_yx.uploadlib.beans.GetDataDicInput;
import com.dark_yx.uploadlib.beans.UploadBean;
import com.dark_yx.uploadlib.beans.UploadFileResult;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Ligh on 2017/8/22 17:37
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public interface ApiService {
    @Multipart
    @POST("/FileRecord/SimpleUpload")
    Call<UploadFileResult> uploadFile(@PartMap Map<String, RequestBody> params);

    @Streaming //大文件时要加不然会OOM
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 添加取证记录
     * @param token
     * @param input
     * @return
     */
    @POST("/api/services/app/forensicsRecord/CreateAsync")
    Call<ResponseBody> forensicsRecordCreate(@Header("Authorization") String token, @Body UploadBean input);

    /**
     * 获取数据字典
     * @param token
     * @param input
     * @return
     */
    @POST("/api/services/app/dataDictionary/GetDataDictionaryItemsByDicName")
    Call<DataDicBean> getDataDic(@Header("Authorization") String token, @Body GetDataDicInput input);



}
