package com.dark_yx.policemaincommon.Util;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 回收bitmap
 * Created by Lmp on 2016/9/2.
 */
public class GcBitmap {
    private static List<Bitmap> gcBitmaps;

    /**
     * 释放图片
     *
     * @param bitmap 要释放的图片
     */
    public static void add(Bitmap bitmap) {
        if (gcBitmaps == null) {
            gcBitmaps = new ArrayList<>();
        }
        if (bitmap != null) {// && !bitmap.isRecycled()){
            gcBitmaps.add(bitmap);
        }
    }

    /**
     * 回收bitmap对象
     */
    public static void gcBitmap() {
        if (gcBitmaps != null && gcBitmaps.size() > 0) {
            Log.e("gcBitmap", "回收bitmap对象" + gcBitmaps.size());
            for (Bitmap bitmap : gcBitmaps) {
                bitmap.recycle();
                bitmap = null;
            }
            gcBitmaps.clear();
        }
    }

    /**
     * 回收bitmap对象
     */
    public static void gcBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            Log.d("GcBitmap", "图片回收");
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }
}
