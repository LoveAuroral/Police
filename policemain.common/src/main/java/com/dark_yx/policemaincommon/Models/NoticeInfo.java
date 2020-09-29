package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by lmp on 2016/5/18.
 */
@Table(name = "NoticeInfo")
public class NoticeInfo {
    @Column(name = "ids", isId = true)
    private int ids;

    @Column(name = "id")
    private String Id;

    @Column(name = "title")
    private String Title;

    @Column(name = "content")
    private String Content;

    @Column(name = "addTime")
    private String AddTime;

    @Column(name = "type")
    private String Type;

    @Column(name = "belongSys")
    private String BelongSys;

    @Column(name = "isRead")
    private boolean isRead;

    @Column(name = "isSend")
    private boolean isSend;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getBelongSys() {
        return BelongSys;
    }

    public void setBelongSys(String belongSys) {
        BelongSys = belongSys;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    @Override
    public String toString() {
        return "NoticeInfo{" +
                "Id=" + Id +
                ", Title='" + Title + '\'' +
                ", Content='" + Content + '\'' +
                ", AddTime='" + AddTime + '\'' +
                ", Type='" + Type + '\'' +
                ", BelongSys='" + BelongSys + '\'' +
                ", isRead=" + isRead +
                ", isSend=" + isSend +
                '}';
    }

}
