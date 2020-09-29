package com.dark_yx.policemaincommon.Util;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * 图片缓存
 * Created by chenbiao on 2016/9/1.
 */
public class BitmapCacheUtil {
    private static final String TAG = "BitmapCacheUtil";
    private static LruCache<String, Bitmap> bitmapLruCache;

    public BitmapCacheUtil() {
        long maxSize = Runtime.getRuntime().maxMemory();
        int memorySize = (int) (maxSize / 4);
        bitmapLruCache = new LruCache<>(memorySize);
    }


    public static void addCache(String key, Bitmap bitmap) {
        if (getCache(key) == null) {
            Log.e(TAG, "存进去");
            bitmapLruCache.put(key, bitmap);
        }
    }


    public static Bitmap getCache(String key) {
        Log.e(TAG, "取出来");
        return bitmapLruCache.get(key);
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public synchronized void removeImageCache(String key) {
        if (key != null) {
            if (bitmapLruCache != null) {
                Bitmap bm = bitmapLruCache.remove(key);
                if (bm != null)
                    bm.recycle();
            }
        }
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        Log.d(TAG, "清理缓存: " + bitmapLruCache.size());
        if (bitmapLruCache != null) {
            if (bitmapLruCache.size() > 0) {
                Log.d(TAG, "清理之前缓存大小: " + bitmapLruCache.size());
                bitmapLruCache.evictAll();
                Log.d(TAG, "清理之后缓存大小: " + bitmapLruCache.size());
            }
            bitmapLruCache = null;
        }
    }
}
