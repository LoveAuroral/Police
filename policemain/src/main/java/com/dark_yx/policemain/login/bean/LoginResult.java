package com.dark_yx.policemain.login.bean;

/**
 * Created by Ligh on 2018/2/5 11:38
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class LoginResult {

    @Override
    public String toString() {
        return "LoginResult{" +
                "result='" + result + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", success=" + success +
                ", error='" + error + '\'' +
                ", unAuthorizedRequest=" + unAuthorizedRequest +
                ", __abp=" + __abp +
                '}';
    }

    /**
     * result : IDf-9TRB9-tc5a0AGXSs1e5wBhd--rIaGrVe6JQ9_ewTe-mYJR6PDXZOpN81OR2V-lQ1Bb6DJgAtOxwlSt7GMli-ZWppSoj7TBKK8zYBXTCdd35eZtQsIDs1xL5q7pBTdh3elWUVsjUFPwxi6eAvKIeRhoJNUmaVi7ZmeZoHemLMQRIPpjkk2XpsD9ZWOjywAKTEAQ8Ao415zAViAk1Y24OBZvP0i5nXrwdyeM8Q-8STAnSRR1EH6436wMA6GLH6YAuQValUT8z4fLVvTtzUArplCqB46Zz2_Kd6ZAwj8qORjFb_LC8Vu8BW77Rsq9h2CLrHKRGhr9EJFai2vDCtj5KboTjNC-JqGO5HOD05Qqq1TNXuHnrXDBeIReYDx3pYlj-c4DxvjOvAGjJkxjvMRrjG9BaYCMW0mrGtOyW0Na6e5nSIYPOgbFrtjRCRN0FhbEHDIYQp4Tlqh7XGeLCWhR3P2wi63wr3Y7aPtxOljyC_ln75Ab2IWS9LWlW5-ekfU2653qz4izjQmNnBI03wEw
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private String result;
    private String targetUrl;
    private boolean success;
    private String error;
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
}
