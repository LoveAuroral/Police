package com.dark_yx.policemain.entity;

/**
 * Created by Lmp on 2016/8/30.
 */

/**
 * 群组
 */
public class VoiceGroup {
    private String id;
    private String groupName;
    private String createUser;

    @Override
    public String toString() {
        return "VoiceGroup{" +
                "id='" + id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", userName='" + createUser + '\'' +
                '}';
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
