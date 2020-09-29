package com.dark_yx.policemain.api;

import com.dark_yx.policemain.entity.GetAppWhiteListAsyncBean;
import com.dark_yx.policemain.entity.SetLocation;
import com.dark_yx.policemain.entity.LocationR;
import com.dark_yx.policemain.fragment.bean.AppWhiteListInput;

import com.dark_yx.policemain.beans.WorkInput;
import com.dark_yx.policemain.beans.WorkResult;
import com.dark_yx.policemain.chat.beans.ModeInput;
import com.dark_yx.policemaincommon.Models.TeleTimeAndNubBean;
import com.dark_yx.policemaincommon.Models.TeleTimeAndNubBeanResult;
import com.dark_yx.policemaincommon.Models.UserTreeResult;
import com.dark_yx.policemain.chat.beans.UploadFileResult;
import com.dark_yx.policemain.login.bean.LoginInput;
import com.dark_yx.policemain.login.bean.LoginResult;
import com.dark_yx.policemaincommon.Models.AppWhiteListResult;
import com.dark_yx.policemaincommon.Models.PrivatePhoneWhiteResult;

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


    /**
     * 上传打卡时间
     *
     * @return
     */
    @POST("/api/services/app/kqDetail/CreateAllDetailAsync")
    Call<WorkResult> workIng(@Header("Authorization") String token, @Body WorkInput input);

    /**
     * 登录
     *
     * @return
     */
    @POST("/api/Account")
    Call<LoginResult> login(@Body LoginInput input);

    /**
     * 上传通话时间和号码
     *
     * @return
     */
    @POST("/api/services/app/callRecord/CreateAsync")
    Call<TeleTimeAndNubBeanResult> setTeleTimeAndNub(@Header("Authorization") String token, @Body TeleTimeAndNubBean input);

    /**
     * 获取组织机构
     *
     * @param token
     * @return
     */
    @POST("/api/services/app/organizationUnit/GetOuWithUsersAsync")
    Call<UserTreeResult> getUserTree(@Header("Authorization") String token);


    /**
     * 获取应用白名单
     *
     * @param token
     * @param input
     * @return
     */
    @POST("/api/services/app/appWhiteList/GetPagedAsync")
    Call<AppWhiteListResult> getAppWhiteList(@Header("Authorization") String token, @Body AppWhiteListInput input);

    /**
     * 上传头像
     *
     * @return
     */
    @Multipart
    @POST("/FileRecord/SimpleUpload")
    Call<UploadFileResult> uploadFile(@PartMap Map<String, RequestBody> params);

    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    @Streaming //大文件时要加不然会OOM
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);


    /**
     * 获取个人白名单
     *
     * @param token
     * @return
     */
    @POST("/api/services/app/privatePhoneWhite/GetPhoneWhites")
    Call<PrivatePhoneWhiteResult> getPrivatePhoneWhite(@Header("Authorization") String token);

    /**
     * 更新模式
     *
     * @param token
     * @return
     */
    @POST("/api/services/app/device/UpdateStatusAsymc")
    Call<ResponseBody> updateStatus(@Header("Authorization") String token, @Body ModeInput mode);

    /**
     * 更新模式
     *
     * @param token
     * @return
     */
    @POST("/api/services/app/notification/MakeAllUserNotificationsAsRead")
    Call<ResponseBody> makeAsRead(@Header("Authorization") String token);

    /**
     * 上传经纬度
     *
     * @param token
     * @return
     */
    @POST("/api/services/app/device/GeographicalPosition")
    Call<LocationR> getLocationR(@Header("Authorization") String token, @Body SetLocation input);

    /**
     * 获取允许运行的app
     *
     * @param token
     * @return
     */
    @POST("/api/services/app/appWhiteList/GetAppWhiteListAsync")
    Call<GetAppWhiteListAsyncBean> getAppWhiteListAsync(@Header("Authorization") String token);

}
