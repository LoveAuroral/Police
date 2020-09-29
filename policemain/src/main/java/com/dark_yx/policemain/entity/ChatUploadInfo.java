package com.dark_yx.policemain.entity;

/**
 * Created by Lmp on 2016/7/15.
 */
public class ChatUploadInfo {
    private int duration;//录音时长
    private String guid;//返回编码
    private String fileName;//文件名称

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
