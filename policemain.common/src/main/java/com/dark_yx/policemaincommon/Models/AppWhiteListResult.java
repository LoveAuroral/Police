package com.dark_yx.policemaincommon.Models;

import java.util.List;

/**
 * Created by Ligh on 2018/2/6 09:19
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class AppWhiteListResult {

    /**
     * result : {"totalCount":2,"items":[{"name":"123","packageName":"123","src":"/FileRecords/20180205/636534418009840800.apk","version":"123","id":2},{"name":"33","packageName":"12321","src":"/FileRecords/20180205/636534415961758120.apk","version":"123","id":1}]}
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private ResultBean result;
    private String targetUrl;
    private boolean success;
    private String error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

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

    public static class ResultBean {
        /**
         * totalCount : 2
         * items : [{"name":"123","packageName":"123","src":"/FileRecords/20180205/636534418009840800.apk","version":"123","id":2},{"name":"33","packageName":"12321","src":"/FileRecords/20180205/636534415961758120.apk","version":"123","id":1}]
         */

        private int totalCount;
        private List<AppInfo> items;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<AppInfo> getItems() {
            return items;
        }

        public void setItems(List<AppInfo> items) {
            this.items = items;
        }

    }
}
