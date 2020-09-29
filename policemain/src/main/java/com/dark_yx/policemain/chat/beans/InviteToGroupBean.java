package com.dark_yx.policemain.chat.beans;

import java.util.List;

/**
 * Created by dark_yx-i on 2018/2/7.
 */

public class InviteToGroupBean {
    private long groupId;

    private List<Long> UserIds;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getUserIds() {
        return UserIds;
    }

    public void setUserIds(List<Long> userIds) {
        UserIds = userIds;
    }

    @Override
    public String toString() {
        return "InviteToGroupBean{" +
                "groupId=" + groupId +
                ", UserIds=" + UserIds +
                '}';
    }
}
