package com.dark_yx.policemain.util;

/**
 * Created by Ligh on 2017/2/7 16:43
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

import android.os.Environment;
import android.util.Log;

import com.dark_yx.policemaincommon.Util.WriteLogUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 全局捕获导常，保存到本地错误日志。日忿
 * 路径位于sdcard/错误日志Log/管控页面.log下〿
 */
public class MyCrashHandler implements Thread.UncaughtExceptionHandler {

    private static MyCrashHandler instance;

    public static MyCrashHandler getInstance() {
        if (instance == null) {
            instance = new MyCrashHandler();
        }
        return instance;
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 核心方法，当程序crash 会回调此方法＿ Throwable中存放这错误日志
     */
    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {

        String logPath;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            logPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + File.separator
                    + File.separator
                    + "错误日志Log";

            File file = new File(logPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                FileWriter fw = new FileWriter(logPath + File.separator + "移动警务错误日志.log", true);
                fw.write(WriteLogUtil.getFileName() + "\n");
                // 这里还可以加上当前的系统版本，机型型叿 等等信息
                StackTraceElement[] stackTrace = arg1.getStackTrace();
                fw.write(arg1.getMessage() + "\n");
                for (int i = 0; i < stackTrace.length; i++) {
                    fw.write("file:" + stackTrace[i].getFileName() + " class:"
                            + stackTrace[i].getClassName() + " method:"
                            + stackTrace[i].getMethodName() + " line:"
                            + stackTrace[i].getLineNumber() + "\n");
                }
                fw.write("\n");
                fw.close();
                // 上传错误信息到服务器
                // uploadToServer();
            } catch (IOException e) {
                Log.e("crash handler", "load file failed...", e.getCause());
            }
        }
        arg1.printStackTrace();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}