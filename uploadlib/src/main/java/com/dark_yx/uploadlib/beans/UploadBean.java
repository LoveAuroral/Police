package com.dark_yx.uploadlib.beans;

/**
 * Created by dark_yx-i on 2018/2/26.
 */

public class UploadBean {

    /**
     * forensicsRecordEditDto : {"id":0,"mode":"string","des":"string","src":"string","forensicsRecordType":0}
     */

    private ForensicsRecordEditDtoBean forensicsRecordEditDto;

    public ForensicsRecordEditDtoBean getForensicsRecordEditDto() {
        return forensicsRecordEditDto;
    }

    public void setForensicsRecordEditDto(ForensicsRecordEditDtoBean forensicsRecordEditDto) {
        this.forensicsRecordEditDto = forensicsRecordEditDto;
    }

    public static class ForensicsRecordEditDtoBean {
        /**
         * id : 0
         * mode : string
         * des : string
         * src : string
         * forensicsRecordType : 0
         */

        private int id;
        private String mode;
        private String des;
        private String src;
        private int forensicsRecordType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public int getForensicsRecordType() {
            return forensicsRecordType;
        }

        public void setForensicsRecordType(int forensicsRecordType) {
            this.forensicsRecordType = forensicsRecordType;
        }
    }
}
