package com.dark_yx.policemaincommon.Util;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件读写工具类
 *
 * @author bear
 */
public class FileUtil {
    static String file = new File(Environment.getExternalStorageDirectory() + "/Police", "phone.txt").
            getAbsolutePath();
    static String file_mode = new File(Environment.getExternalStorageDirectory() + "/Police", "MODE.txt").
            getAbsolutePath();
    static String file_missedPhone = new File(Environment.getExternalStorageDirectory() + "/Police/", "missedPhone.txt").
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
     * 向文件中写入字符串String类型的内容
     *
     * @param content 文件内容
     */
    public static void writeMissedPhone(String content) {
        try {
            createFile(file_missedPhone);
            byte[] data = content.getBytes("utf-8");
            writeBytes(file_missedPhone, data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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
     * 向文件中写入字符串String类型的内容
     *
     * @param content 文件内容
     */
    public static void writeMode(String content) {
        try {
            createFile(file_mode);
            byte[] data = content.getBytes("utf-8");
            writeBytes(file_mode, data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    /**
     * 读取文件里面的内容
     *
     * @return
     */
    public static String getFile(String fileName) {
        try {
            // 创建文件
            File file = new File(Environment.getExternalStorageDirectory() + "/Police/", fileName);
            // 创建FileInputStream对象
            FileInputStream fis = new FileInputStream(file);
            // 创建字节数组 每次缓冲1M
            byte[] b = new byte[1024];
            int len = 0;// 一次读取1024字节大小，没有数据后返回-1.
            // 创建ByteArrayOutputStream对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 一次读取1024个字节，然后往字符输出流中写读取的字节数
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            // 将读取的字节总数生成字节数组
            byte[] data = baos.toByteArray();
            // 关闭字节输出流
            baos.close();
            // 关闭文件输入流
            fis.close();
            // 返回字符串对象
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * 从文件中读取数据，返回类型是字符串String类型
     *
     * @return
     */
    public static String readMode() {
        byte[] data = readBytes(file_mode);
        String ret = "";
        try {
            ret = new String(data, "utf-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    //删除文件
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {

        }
    }

    public static boolean isExist() {
        File file1 = new File(file);
        return file1.exists();
    }
}
