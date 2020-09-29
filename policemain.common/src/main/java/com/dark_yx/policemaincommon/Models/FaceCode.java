package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Ligh on 2016/9/6 16:16
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */
@Table(name = "FaceCode")
public class FaceCode {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "code")
    private String code;

    @Override
    public String toString() {
        return "FaceCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public FaceCode(String code) {
        this.code = code;
    }

    public FaceCode() {
    }
}
