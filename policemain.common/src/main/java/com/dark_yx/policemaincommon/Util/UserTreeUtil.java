package com.dark_yx.policemaincommon.Util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件读写工具类
 *
 * @author bear
 */
public class UserTreeUtil {
    static String file = new File(Environment.getExternalStorageDirectory() + "/Police/", "treeUser.txt").
            getAbsolutePath();

    /**
     * 如果文件不存在，就创建文件
     *
     * @param path 文件路径
     * @return
     */
    public static String createFile(String path) {
        File file = new File(path);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return path;
    }

    /**
     * 向文件中写入数据
     *
     * @param filePath 目标文件全路径
     * @param data     要写入的数据
     * @return true表示写入成功  false表示写入失败
     */
    public static boolean writeBytes(String filePath, byte[] data) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(data);
            fos.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 从文件中读取数据
     *
     * @param file
     * @return
     */
    public static byte[] readBytes(String file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            int len = fis.available();
            byte[] buffer = new byte[len];
            fis.read(buffer);
            fis.close();
            return buffer;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 向文件中写入字符串String类型的内容
     *
     * @param content 文件内容
     */
    public static void writeString(String content) {
        try {
            createFile(file);
            byte[] data = content.getBytes("utf-8");
            writeBytes(file, data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * 从文件中读取数据，返回类型是字符串String类型
     *
     * @return
     */
    public static String readString() {
        byte[] data = readBytes(file);
        String ret = null;
        try {
            ret = new String(data, "utf-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }


    public static boolean isExist() {
        File file1 = new File(file);
        return file1.exists();
    }
}
