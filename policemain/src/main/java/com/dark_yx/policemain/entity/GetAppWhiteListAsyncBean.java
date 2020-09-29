package com.dark_yx.policemain.entity;

import java.util.List;

/**
 * Created by Administrator on 2019/4/25.
 */

public class GetAppWhiteListAsyncBean {


    @Override
    public String toString() {
        return "GetAppWhiteListAsyncBean{" +
                "targetUrl=" + targetUrl +
                ", success=" + success +
                ", error=" + error +
                ", unAuthorizedRequest=" + unAuthorizedRequest +
                ", __abp=" + __abp +
                ", result=" + result +
                '}';
    }

    /**
     * result : [{"name":"通话白名单","packageName":"com.example.vicknl2.phone","src":"/FileRecords/20181121/636784049206544075.apk","version":"1.0","id":10033},{"name":"视频取证","packageName":"com.dark_yx.sysvideocamera","src":"/FileRecords/20181121/636784048899691536.apk","version":"1.0","id":10034},{"name":"录音取证","packageName":"com.dark_yx.audioupload","src":"/FileRecords/20181121/636784048760851292.apk","version":"1.0","id":10035},{"name":"图片取证","packageName":"com.dark_yx.photoupload","src":"/FileRecords/20181121/636784048586910987.apk","version":"1.0","id":10036},{"name":"闹钟","packageName":"mobi.ktfga.nbx2","src":"/FileRecords/20181024/636759743513469355.apk","version":"1.1","id":10038},{"name":"手电筒","packageName":"com.baidu.flashlight","src":"/FileRecords/20181024/636759743866497976.apk","version":"1.0.3","id":10039},{"name":"门禁控制","packageName":"com.newabel.nfcdemo","src":"/FileRecords/20181205/636796033826515979.apk","version":"1.2","id":10040},{"name":"计算器","packageName":"neo.android.calculatorade","src":"/FileRecords/20181121/636784058525532443.apk","version":"1.0","id":10041},{"name":"融合通信","packageName":"com.microsys.pocdroidgv","src":"/FileRecords/20181204/636795338237206241.apk","version":"1.0","id":10042},{"name":"电话","packageName":"123456","src":null,"version":null,"id":10045}]
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private Object targetUrl;
    private boolean success;
    private Object error;
    private boolean unAuthorizedRequest;
    private boolean __abp;
    private List<ResultBean> result;

    public Object getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(Object targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
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

    public static class ResultBean {
        @Override
        public String toString() {
            return "ResultBean{" +
                    "name='" + name + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", src='" + src + '\'' +
                    ", version='" + version + '\'' +
                    ", id=" + id +
                    '}';
        }

        /**
         * name : 通话白名单
         * packageName : com.example.vicknl2.phone
         * src : /FileRecords/20181121/636784049206544075.apk
         * version : 1.0
         * id : 10033
         */

        private String name;
        private String packageName;
        private String src;
        private String version;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
