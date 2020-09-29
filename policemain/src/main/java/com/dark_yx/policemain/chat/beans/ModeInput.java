package com.dark_yx.policemain.chat.beans;

/**
 * Created by Ligh on 2018/3/9 21:11
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class ModeInput {

    /**
     * status : 生活模式
     */

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ModeInput(String status) {
        this.status = status;
    }
}
