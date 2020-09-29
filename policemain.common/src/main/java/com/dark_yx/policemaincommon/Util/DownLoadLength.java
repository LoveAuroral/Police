package com.dark_yx.policemaincommon.Util;

import java.text.DecimalFormat;

/**
 * Created by Lmp on 2016/8/4.
 */
public class DownLoadLength {
    public static String getLength(Long s) {
        double kb = s / 1024.00;
        if (kb / 1024 > 1) {
            double mb = kb / 1024;
            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String length = decimalFormat.format(mb);
            return length + "M";
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String length = decimalFormat.format(kb);//format 返回的是字符串
            return length + "kb";
        }
    }

}
