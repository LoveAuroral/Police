package com.dark_yx.policemaincommon.Models;

/**
 * Created by Ligh on 2018/3/1 16:19
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class NoticeBean {
    public String title;
    public String time;

    public NoticeBean(String title, String time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "NoticeBean{" +
                "title='" + title + '\'' +
                ", time=" + time +
                '}';
    }
}
