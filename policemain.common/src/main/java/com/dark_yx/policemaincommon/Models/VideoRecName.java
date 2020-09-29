package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Ligh on 2016/9/12 09:44
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */
@Table(name = "VideoRecName")
public class VideoRecName {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "recTime")
    private String recTime;//录音保存的时间

    @Override
    public String toString() {
        return "VideoRecInfo{" +
                "id=" + id +
                ", recTime='" + recTime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecTime() {
        return recTime;
    }

    public void setRecTime(String recTime) {
        this.recTime = recTime;
    }

    public VideoRecName(String recTime) {
        this.recTime = recTime;
    }

    public VideoRecName() {
    }
}
