package com.dark_yx.policemain.fragment.bean;

/**
 * Created by Ligh on 2018/2/6 09:18
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class AppWhiteListInput {

    /**
     * maxResultCount : 5
     */

    private int maxResultCount;

    public int getMaxResultCount() {
        return maxResultCount;
    }

    public void setMaxResultCount(int maxResultCount) {
        this.maxResultCount = maxResultCount;
    }

    public AppWhiteListInput(int maxResultCount) {
        this.maxResultCount = maxResultCount;
    }
}
