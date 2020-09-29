package com.dark_yx.policemain.tree.factory;

import android.view.View;

import me.texy.treeview.base.BaseNodeViewBinder;
import me.texy.treeview.base.BaseNodeViewFactory;

/**
 * Created by Ligh on 2018/2/4 10:58
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class MyNodeViewFactory extends BaseNodeViewFactory {
    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {
        return new NodeViewBinder(view, level);
    }
}
