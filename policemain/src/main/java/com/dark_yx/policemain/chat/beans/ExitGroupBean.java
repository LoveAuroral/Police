package com.dark_yx.policemain.chat.beans;

/**
 * Created by Ligh on 2018/3/9 17:40
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class ExitGroupBean {
    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    private long groupId;

    private long userId;
}