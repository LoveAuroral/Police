package com.dark_yx.policemain.login.bean;

/**
 * Created by Ligh on 2018/2/5 14:43
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class ErrorResult {
    /**
     * result : null
     * targetUrl : null
     * success : false
     * error : {"code":0,"message":"登录失败!","details":"用户名或密码无效","validationErrors":null}
     * unAuthorizedRequest : false
     * __abp : true
     */

    private String result;
    private String targetUrl;
    private boolean success;
    private ErrorBean error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
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

    public static class ErrorBean {
        /**
         * code : 0
         * message : 登录失败!
         * details : 用户名或密码无效
         * validationErrors : null
         */

        private int code;
        private String message;
        private String details;
        private String validationErrors;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getValidationErrors() {
            return validationErrors;
        }

        public void setValidationErrors(String validationErrors) {
            this.validationErrors = validationErrors;
        }
    }
}
