package com.dark_yx.policemain.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Ligh on 2016/10/25 17:32
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 * 图片压缩
 */

public class ZoomBitmap {
    public static Bitmap getBitmap(String fileName, int zoom) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = zoom;
        options.inDither = false;    //不进行图片抖动处理
        options.inPreferredConfig = null;  //设置让解码器以最佳方式解码
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bm = BitmapFactory.decodeFile(fileName, options);
        try {
            Log.d("ZoomBitmap", " bitmap大小: " + bm.getAllocationByteCount() / 1024);
            if (bm.getByteCount() / 1024 < 100) {
                Log.d("ZoomBitmap", "小图片");
                options.inSampleSize = 2;
                bm = BitmapFactory.decodeFile(fileName, options);
            }
        } catch (NullPointerException e) {
            Log.d("ZoomBitmap", "bitmap空");
        }
        return bm;
    }
}
