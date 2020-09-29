package com.dark_yx.policemaincommon.Models;

import java.util.List;

/**
 * Created by Ligh on 2018/2/5 16:02
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class UserTreeResult {

    private String targetUrl;
    private boolean success;
    private String error;
    private boolean unAuthorizedRequest;
    private boolean __abp;
    private List<ResultBean> result;

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public boolean is__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UserTreeResult{" +
                "targetUrl='" + targetUrl + '\'' +
                ", success=" + success +
                ", error='" + error + '\'' +
                ", unAuthorizedRequest=" + unAuthorizedRequest +
                ", __abp=" + __abp +
                ", result=" + result +
                '}';
    }

    public static class ResultBean {
        private String parentId;
        private String displayName;
        private int id;
        private List<ResultBean> children;
        private List<UsersBean> users;
        private String code;

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ResultBean> getChildren() {
            return children;
        }

        public void setChildren(List<ResultBean> children) {
            this.children = children;
        }

        public List<UsersBean> getUsers() {
            return users;
        }

        public void setUsers(List<UsersBean> users) {
            this.users = users;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "parentId='" + parentId + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", id=" + id +
                    ", children=" + children +
                    ", users=" + users +
                    ", code='" + code + '\'' +
                    '}';
        }
    }

}
