package com.dark_yx.policemaincommon.Util;


import android.util.Log;
import android.widget.Toast;

import com.dark_yx.policemaincommon.Models.BassIP;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

/**
 * Created by dark_yx on 2016-02-29.
 * XUtilsHttp服务帮助类
 */
public class HttpHelp {
    //原始IP
    private static BassIP BAAS_IP = null;// http://192.168.1.116:88/MainService.asmx

    public static String ERRORCODE = "errorCode";//错误参数

    public static String TOKEN = "token";

    public static String ACTION = "action";//行为

    //获取从数据库中得到的原始IP，用来更新配置
    public static BassIP GetExtraIP() {
        BassIP ip;
        DbHelp dbHelp = new DbHelp();
//        ip = dbHelp.getBassIP();
        ip=new BassIP();
        return ip;
    }


    public static BassIP GetBaasIp() {
        DbHelp dbHelp = new DbHelp();
        BassIP ip;
        if (BAAS_IP == null) {
            ip=new BassIP();
//            ip = dbHelp.getBassIP();
            ip.SetIP(ip.GetIp() + "webservice/mainservice.asmx");
            ip.SetIP(ip.GetIp());
            BAAS_IP = ip;
        } else {
            ip = BAAS_IP;
        }
        return ip;
    }

    public static void setIp(String ip, String videoIp) {
        BAAS_IP.SetIP(ip + "webservice/mainservice.asmx");
        BAAS_IP.setVideoIP(videoIp);
        DbHelp dbHelp = new DbHelp();
//        dbHelp.SetBassIP(ip, videoIp);
    }

    public static void Get(final HttpCallBackEvent callBackEvent, String url, Map<String, Object> params) {
        RequestParams requestParamsparams = new RequestParams(url);
        if (params != null) {
            for (String key : params.keySet()) {
                requestParamsparams.addQueryStringParameter(key, params.get(key).toString());
            }
        }
        x.http().get(requestParamsparams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("Request", result);
                try {
                    if (callBackEvent != null) {
                        callBackEvent.Excute(result);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                try {
                    callBackEvent.OnError(ex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void Post(final HttpCallBackEvent callBackEvent, String url, final RequestParams params, final int id) {
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (callBackEvent != null) {
                    DbHelp dbHelp = new DbHelp();
//                    Toast.makeText(x.app(), id, Toast.LENGTH_LONG).show();
//                    dbHelp.deleteRecord(id);//当上传成功之后通过fileName删除掉数据库中上传失败的数据
                    try {
                        callBackEvent.Excute(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                try {
                    callBackEvent.OnError(ex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(x.app(), "服务器连接失败" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 下载文件
     *
     * @param callBackEvent 回调
     * @param url           地址
     * @param filePath      文件保存地址
     * @param params        参数
     */
    public static void DownLoadFile(final HttpCallBackEvent callBackEvent, String url, String filePath, Map<String, Object> params) {
        final RequestParams requestParamsparams = new RequestParams(url);
        if (params != null) {
            for (String key : params.keySet()) {
                requestParamsparams.addQueryStringParameter(key, params.get(key).toString());
            }
        }
        //文件保存在本地的路径
        Log.d("download",filePath);
        requestParamsparams.setSaveFilePath(filePath);
        requestParamsparams.setAutoResume(true);
        x.http().get(requestParamsparams, new Callback.CacheCallback<File>() {
            @Override
            public void onSuccess(File f) {
                Log.d("ok", f.getAbsolutePath());
                try {
                    callBackEvent.Excute("downLoad_Success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(File s) {
                return false;
            }
        });
    }

}