package com.dark_yx.policemain.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by dark_yx-i on 2016-05-08.
 *  保存用户信息至sd卡上，供其他程序调用
 */
public class StorageUserInfor {

    // 保存用户信息至sd卡上，供其他程序调用
    public static boolean storageUserInfor(int userID,String userName,String password,String realName) throws IOException {

        String path = "/sdcard/PolicePass/Share/";
        File pathFile =new File(path);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }

        String filepath = "/sdcard/PolicePass/Share/uInfor.txt";
        File file = new File(filepath);
        if(file.exists())
        {
            file.delete();
        }
        file.createNewFile();
        FileWriter fw = null;
        BufferedWriter bw = null;
        fw = new FileWriter(file, true);//
        bw = new BufferedWriter(fw); // 将缓冲对文件的输出
        bw.write(userID);
        bw.newLine();
        bw.write(userName);
        bw.newLine();
        bw.write(password);
        bw.newLine();
        bw.write(realName);
        bw.flush(); // 刷新该流的缓冲
        bw.close();
        fw.close();
        return true;
    }


}
