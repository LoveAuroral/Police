package com.dark_yx.policemain.util;

/**
 * Created by Ligh on 2018/1/26 15:38
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class PollingBean {

    /**
     * success : true
     * error : {"policeNo":"P1001","isIn":true,"time":"1970-01-18 21:16:30"}
     */

    private boolean success;
    private ErrorBean error;

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

    public static class ErrorBean {
        /**
         * policeNo : P1001
         * isIn : true
         * time : 1970-01-18 21:16:30
         */

        private String policeNo;
        private boolean isIn;
        private String time;

        public String getPoliceNo() {
            return policeNo;
        }

        public void setPoliceNo(String policeNo) {
            this.policeNo = policeNo;
        }

        public boolean isIsIn() {
            return isIn;
        }

        public void setIsIn(boolean isIn) {
            this.isIn = isIn;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
