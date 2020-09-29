package com.dark_yx.uploadlib.contract;

import com.dark_yx.uploadlib.beans.DataDicBean;

/**
 * Created by dark_yx-i on 2018/2/26.
 */

public interface UploadContract {
    interface Model {

        /**
         * 获取数据字典
         *
         * @param key
         * @param token
         * @return
         */
        void getData(String key, String token);

        /**
         * 上传文件
         */
        void upload(String file, String tag, int type, String token);
    }

    interface View {

        void getDataDicBeanSuccess(DataDicBean dicBean);

        void uoloadSuccess();

        void uoloadFail(String error);
    }

    interface Presenter {

        void getData(String key, String token);

        void getDataDicBeanSuccess(DataDicBean dicBean);

        void upload(String filePath, String tag, int type, String token);

        void uoloadSuccess();

        void uoloadFail(String error);
    }
}
