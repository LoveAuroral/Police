package com.dark_yx.policemain.tree.factory;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.BaseNodeViewBinder;

/**
 * Created by Ligh on 2018/2/4 10:30
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class NodeViewBinder extends BaseNodeViewBinder {
    TextView tv_name;
    LinearLayout ll_c;
    ImageView iv_icon;
    int level;


    public NodeViewBinder(View itemView, int level) {
        super(itemView);
        this.level = level;
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        ll_c = (LinearLayout) itemView.findViewById(R.id.ll_c);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_node;
    }

    @Override
    public void bindView(final TreeNode treeNode) {
        ll_c.setPadding((level + 1) * 50, 5, 5, 5);
        Object value = treeNode.getValue();
        if (value instanceof UsersBean) {
            iv_icon.setVisibility(View.GONE);
            final UsersBean usersBean = (UsersBean) value;
            tv_name.setText(usersBean.getName());
            ll_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constants.startChat(x.app(), usersBean);
                }
            });
        } else {
            iv_icon.setVisibility(View.VISIBLE);
            iv_icon.setRotation(treeNode.isExpanded() ? 90 : 0);
            tv_name.setText(treeNode.getValue().toString());
        }

    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        LogUtil.d("点击树形图--->" + expand + "\n" + treeNode.toString());
        if (expand) {
            iv_icon.animate().rotation(90).setDuration(200).start();
        } else {
            iv_icon.animate().rotation(0).setDuration(200).start();
        }

    }

}
