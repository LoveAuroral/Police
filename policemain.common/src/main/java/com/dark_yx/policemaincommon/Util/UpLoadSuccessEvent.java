package com.dark_yx.policemaincommon.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.dark_yx.policemaincommon.Models.ErrorPhoto;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by dark_yx on 2016-03-18.
 */
public class UpLoadSuccessEvent implements HttpCallBackEvent {
    private Activity mActivity;
    private ErrorPhoto errorPhoto;
    private String filePath;
    private int id;
    private String mode;
    private String fileName;
    private static final String TAG = "UpLoadSuccessEvent";

    public UpLoadSuccessEvent(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void upload(String userName, String filePath, String fileName, String mode, int id) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.id = id;
        this.mode = mode;
        errorPhoto = new ErrorPhoto();
        errorPhoto.setFilePath(filePath);
        errorPhoto.setUserName(userName);
        errorPhoto.setUploadMode(mode);
        errorPhoto.setFileName(fileName);
        errorPhoto.setId(id);

        postDandian(mode, userName);
    }

    @Override
    public void Excute(String result) throws JSONException {
        Log.d("uploadFiles", result);
        JSONObject object = new JSONObject(result);
        if (object.getInt("errorCode") == 0) {
        } else {
            Toast.makeText(mActivity, "上传错误:" + object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnError(Throwable ex) throws JSONException {
        DbHelp dbHelp = new DbHelp();
        Toast.makeText(mActivity, "文件上传失败", Toast.LENGTH_SHORT).show();
        showRestartUp();
    }

    /**
     * 上传成功之后删除文件
     *
     * @param file
     */

    private void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete();
                Toast.makeText(mActivity, "文件删除成功", Toast.LENGTH_SHORT).show();
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                if (files == null || files.length == 0) {
                    file.delete();
                    return;
                }
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            scanFileAsync();
        } else {
            return;
        }
    }

    //刷新缩略图
    private void scanFileAsync() {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        x.app().sendBroadcast(scanIntent);
    }


    private void showRestartUp() {
        new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setMessage("是否重新上传")
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "重新上传 :" + errorPhoto.toString());
                        upload(errorPhoto.getUserName(), filePath, fileName, mode, id);
                    }
                }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFile(new File(filePath));
                DbHelp dbHelp = new DbHelp();
                mActivity.finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.finish();
            }
        }).show();
    }

    /**
     * 上传到单点
     *
     * @param mode     上传模式
     * @param token 用户名
     */
    private void postDandian(String mode, String token) {
        RequestParams params = new RequestParams();
        // 加到url里的参数, http://xxxx/s?wd=xUtils
        params.addQueryStringParameter("filename", fileName);
        params.addHeader("Authrization",token);
        // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        //  params.addBodyParameter("filename", "xUtils");
        // 使用multipart表单上传文件
        params.setMultipart(false);
        params.addBodyParameter(
                "file",
                new File(filePath + "/" + fileName),
                null); // 如果文件没有扩展名, 最好设置contentType参数.
    }

}