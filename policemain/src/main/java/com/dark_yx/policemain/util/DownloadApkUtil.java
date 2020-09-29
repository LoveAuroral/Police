package com.dark_yx.policemain.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemaincommon.Models.AppInfo;
import com.dark_yx.policemaincommon.Util.DataUtil;

import org.xutils.common.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ligh on 2016/10/19 14:36
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

/*
 * 从服务器下载apk
 */
public class DownloadApkUtil {
    private Activity activity;
    private AppInfo appInfo;
    private static final int DOWN_ERROR = 3;//下载失败
    private static final int UPDATE_DIALOG = 4;//更新进度条标题
    private ProgressDialog pd;//下载进度条
    private int length;//下载长度,转换为kb之后的大小

    public DownloadApkUtil(Activity activity, AppInfo appInfo) {
        this.activity = activity;
        this.appInfo = appInfo;
    }

    private Handler handler;

    {
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWN_ERROR:
                        Toast.makeText(activity, "下载" + appInfo.getName() + "失败", Toast.LENGTH_LONG).show();
                        break;
                    case UPDATE_DIALOG:
                        pd.setTitle("正在下载更新:共" + length + "kb");
                        break;
                }
            }
        };
    }

    /*
     * 弹出对话框通知用户更新程序
     */
    public void showDownloadDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(activity);
        builer.setTitle("下载:" + appInfo.getName() + "?");
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });
        builer.setNegativeButton("取消", null);
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /**
     * 从服务器中下载APK
     */
    public void downLoadApk() {
        pd = new ProgressDialog(activity);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("正在下载");
        pd.setCancelable(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    getFileFromServer(pd);
                } catch (Exception e) {
                    LogUtil.d("Exception>>>>" + e.toString());
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    pd.dismiss(); //结束掉进度条对话框
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 安装程序
     */
    private void installApk(File file) {
        if (activity == null || !file.exists()) {
            return;
        }
        LogUtil.d("安装apk>>>" + "正在安装" + file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            LogUtil.d("安装apk--->" + "7.0以上，正在安装apk...");
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(activity,
                    activity.getApplicationInfo().packageName + ".fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            LogUtil.d("安装apk--->" + "7.0以下，正在安装apk...");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        activity.startActivity(intent);


//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        Uri apkUri = null;
//        if (Build.VERSION.SDK_INT >= 26) {
//            apkUri = FileProvider.getUriForFile(activity.getApplicationContext(),
//                    activity.getApplicationInfo().packageName + ".fileprovider", file);
//        } else {
//            apkUri = Uri.fromFile(file);
//        }
//        LogUtil.d("uri---->" + apkUri.toString());
//        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        activity.startActivity(intent);
    }

    private void getFileFromServer(ProgressDialog pd) throws Exception {
        ComponentName componentName = new ComponentName(activity, DeviceReceiver.class);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(DataUtil.getBassIp() + appInfo.getSrc());
            LogUtil.d(url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.connect();

            //获取到文件的大小
            length = conn.getContentLength() / 1024;
            Message msg = new Message();
            msg.what = UPDATE_DIALOG;
            handler.sendMessage(msg);
            pd.setMax(length);

            InputStream is = conn.getInputStream();
            File filePath = new File(Environment.getExternalStorageDirectory() + "/Police/Download");
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            String path = appInfo.getPackageName();
            path = path.replaceAll("\\.", "");
            File file = new File(filePath, path + ".apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total / 1024);
            }

            fos.close();
            bis.close();
            is.close();
            installApk(file);
            pd.dismiss(); //结束掉进度条对话框
        }
    }

    public void getFileFromServer() throws Exception {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    ComponentName componentName = new ComponentName(activity, DeviceReceiver.class);
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        URL url = new URL(DataUtil.getBassIp() + appInfo.getSrc());
                        LogUtil.d(url.toString());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        File filePath = new File(Environment.getExternalStorageDirectory() + "/Police/Download");
                        if (!filePath.exists()) {
                            filePath.mkdirs();
                        }
                        File file = new File(filePath, appInfo.getName() + ".apk");
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }

                        fos.close();
                        bis.close();
                        is.close();
//                        PhoneInterfaceUtil.installPackage(componentName, file.getPath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}