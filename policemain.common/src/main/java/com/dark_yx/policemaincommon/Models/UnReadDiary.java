package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Lmp on 2016/6/12.
 */
@Table(name = "UnReadDiary")
public class UnReadDiary implements Serializable {
    @Column(name = "Id", isId = true)
    private String ID;

    @Column(name = "IsHandle")
    private boolean IsHandle;//是否解决

    @Column(name = "Result")
    private String Result;//返回

    @Column(name = "Pusher")
    private String Pusher;//发起人

    @Column(name = "PushId")
    private int PushId;

    @Column(name = "Time")
    private String Time;//当前时间

    @Column(name = "Title")
    private String Title;//标题

    @Column(name = "isRead")
    private boolean isRead;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isHandle() {
        return IsHandle;
    }

    public void setHandle(boolean handle) {
        IsHandle = handle;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getPusher() {
        return Pusher;
    }

    public void setPusher(String pusher) {
        Pusher = pusher;
    }

    public int getPushId() {
        return PushId;
    }

    public void setPushId(int pushId) {
        PushId = pushId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "UnReadDiary{" +
                "ID='" + ID + '\'' +
                ", IsHandle=" + IsHandle +
                ", Result='" + Result + '\'' +
                ", Pusher='" + Pusher + '\'' +
                ", PushId=" + PushId +
                ", Time='" + Time + '\'' +
                ", Title='" + Title + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
