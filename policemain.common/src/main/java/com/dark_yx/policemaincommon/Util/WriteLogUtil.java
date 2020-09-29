package com.dark_yx.policemaincommon.Util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Calendar;

/**
 * Created by VicknL2 on 2016/8/3.
 * 打印日志工具类
 */
public class WriteLogUtil {
    //服务器返回信息日志
    //获取当前时间，用于语音的文件名
    public static void writeLog(String name, String result) {
        result = getFileName() + "\n" + result;
        File file = new File(Environment.getExternalStorageDirectory() + "/移动警务日志");
        String fileName = file.getAbsolutePath() + "/" + name + ".txt";//文件名
        try {
            if (!file.exists()) {
                file.mkdirs();
                FileOutputStream os = new FileOutputStream(fileName);
                os.write(result.getBytes());
            } else {
                appendMethodA(fileName, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileName() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DATE);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        int mSecond = calendar.get(Calendar.SECOND);
        return mYear + "年" + (mMonth + 1) + "月" + mDay + "日" + mHour + "点" + mMinute + "分" + mSecond + "秒";
    }

    /**
     * A方法追加文件：使用RandomAccessFile
     */
    private static void appendMethodA(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeUTF("\n" + "\n" + "\n" + "\n" + content);
            randomFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
