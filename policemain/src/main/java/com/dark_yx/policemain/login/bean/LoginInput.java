package com.dark_yx.policemain.login.bean;

/**
 * Created by Ligh on 2018/2/5 11:21
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class LoginInput {

    public LoginInput(String usernameOrEmailAddress, String password, DeviceLoginModelBean deviceLoginModel) {
        this.usernameOrEmailAddress = usernameOrEmailAddress;
        this.password = password;
        this.deviceLoginModel = deviceLoginModel;


    }

    /**
     * usernameOrEmailAddress :   用户名
     * password :  密码
     * deviceLoginModel : {"imei":"imei","appVersion":"app 版本","systemVersion":"系统版本"}
     */

    private String tenancyName;
    private String usernameOrEmailAddress;
    private String password;


    private DeviceLoginModelBean deviceLoginModel;

    public String getTenancyName() {
        return tenancyName;
    }

    public void setTenancyName(String tenancyName) {
        this.tenancyName = tenancyName;
    }

    public String getUsernameOrEmailAddress() {
        return usernameOrEmailAddress;
    }

    public void setUsernameOrEmailAddress(String usernameOrEmailAddress) {
        this.usernameOrEmailAddress = usernameOrEmailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DeviceLoginModelBean getDeviceLoginModel() {
        return deviceLoginModel;
    }

    public void setDeviceLoginModel(DeviceLoginModelBean deviceLoginModel) {
        this.deviceLoginModel = deviceLoginModel;
    }

    public static class DeviceLoginModelBean {
        public DeviceLoginModelBean(String imei, String appVersion, String systemVersion/*, boolean isRoot*/, String simSerialNumber, String bluetoothStatus, String wifeStatus) {
            this.imei = imei;
            this.appVersion = appVersion;
            this.systemVersion = systemVersion;
            this.bluetoothStatus = bluetoothStatus;
            this.wifeStatus = wifeStatus;
//            this.isRoot = isRoot;
            this.simSerialNumber = simSerialNumber;
        }

        public String getSimSerialNumber() {
            return simSerialNumber;
        }

        public void setSimSerialNumber(String simSerialNumber) {
            simSerialNumber = simSerialNumber;
        }

        /**
         * imei : string
         * appVersion : string
         * systemVersion : string
         * bluetoothStatus : string
         * wifeStatus : string
         */


        private String imei;
        private String appVersion;
        private String systemVersion;
        private String bluetoothStatus;
        private String wifeStatus;
        private String simSerialNumber;
        private String recordingState;
        private String cameraState;

        public String getRecordingState() {
            return recordingState;
        }

        public void setRecordingState(String recordingState) {
            this.recordingState = recordingState;
        }

        public String getCameraState() {
            return cameraState;
        }

        public void setCameraState(String cameraState) {
            this.cameraState = cameraState;
        }

        public String getBluetoothStatus() {
            return bluetoothStatus;
        }

        public void setBluetoothStatus(String bluetoothStatus) {
            this.bluetoothStatus = bluetoothStatus;
        }

        public String getWifeStatus() {
            return wifeStatus;
        }

        public void setWifeStatus(String wifeStatus) {
            this.wifeStatus = wifeStatus;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getSystemVersion() {
            return systemVersion;
        }

        public void setSystemVersion(String systemVersion) {
            this.systemVersion = systemVersion;
        }

        @Override
        public String toString() {
            return "DeviceLoginModelBean{" +
                    "imei='" + imei + '\'' +
                    ", appVersion='" + appVersion + '\'' +
                    ", systemVersion='" + systemVersion + '\'' +
                    ", bluetoothStatus='" + bluetoothStatus + '\'' +
                    ", wifeStatus='" + wifeStatus + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginInput{" +
                "tenancyName='" + tenancyName + '\'' +
                ", usernameOrEmailAddress='" + usernameOrEmailAddress + '\'' +
                ", password='" + password + '\'' +
                ", deviceLoginModel=" + deviceLoginModel +
                '}';
    }
}
