package com.dark_yx.policemain.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cpiz.android.bubbleview.BubbleLinearLayout;
import com.cpiz.android.bubbleview.BubblePopupWindow;
import com.cpiz.android.bubbleview.BubbleStyle;
import com.dark_yx.policemain.adapter.GroupFriendsAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.ChatService;
import com.dark_yx.policemain.chat.beans.ChatGroupBean;
import com.dark_yx.policemain.chat.beans.ExitGroupBean;
import com.dark_yx.policemain.chat.callback.IGroupUserCallBack;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemaincommon.Models.UsersBean;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

@ContentView(R.layout.activity_pttgroup_friends)
public class GroupFriendsActivity extends BaseActivity implements View.OnClickListener, IGroupUserCallBack {
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.lv_user)
    private ListView lvUser;
    @ViewInject(R.id.iv_add)
    private ImageView ivAdd;
    private GroupFriendsAdapter adapter;
    private final static String TAG = "GroupFriendsActivity";

    private String jid;
    private ChatDb db;
    ChatService chatService;
    private List<UsersBean> groupUsers;
    private UsersBean groupUser;
    ChatGroupBean chatGroupBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        db = new ChatDb();
        ivBack.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        jid = getIntent().getStringExtra("groupId");
        try {
            chatGroupBean = db.GetGroupById(Long.parseLong(jid));
        } catch (DbException e) {
            e.printStackTrace();
        }
        getItem();
        initChat();

        initPopWindow();
    }

    private void initPopWindow() {
        lvUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                groupUser = groupUsers.get(position);

                // 群主才可以删除
                if (db.getId() != chatGroupBean.getCreatorUserId()) {
                    return true;
                }

                // 不能删除群主
                if (groupUser.getId() == chatGroupBean.getCreatorUserId()) {
                    return true;
                }
                showDeleteUserDialog(view);
                return true;
            }
        });

    }

    private void showDeleteUserDialog(View v) {
        View rootView = LayoutInflater.from(GroupFriendsActivity.this).inflate(R.layout.simple_voice_bubble, null);
        TextView tv_delete = (TextView) rootView.findViewById(R.id.tv_delete);

        BubbleLinearLayout bubbleView = (BubbleLinearLayout) rootView.findViewById(R.id.popup_bubble);
        final BubblePopupWindow window = new BubblePopupWindow(rootView, bubbleView);
        window.setCancelOnTouch(true);
        window.setCancelOnTouchOutside(true);
        window.showArrowTo(v, BubbleStyle.ArrowDirection.Down);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                deleteUser();
            }
        });
    }


    private void initChat() {
        bindService(new Intent(this, ChatService.class), chatServiceConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    ServiceConnection chatServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d("聊天服务开启成功");
            chatService = ((ChatService.ChatServiceBinder) service).getService();

            chatService.registerGroupUserCallBack(GroupFriendsActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("聊天服务开启失败");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatService.unRegisterGroupUserCallBack();
        unbindService(chatServiceConnection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add:
                Intent intent = new Intent(GroupFriendsActivity.this, SelectGroupFriendsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("groupId", jid);
                startActivity(intent);

                finish();
                break;
        }
    }

    private void deleteUser() {
        LogUtil.d("删除用户");
        ExitGroupBean exitGroupBean = new ExitGroupBean();
        exitGroupBean.setGroupId(chatGroupBean.getId());
        exitGroupBean.setUserId(groupUser.getId());
        chatService.exitGroup(exitGroupBean);
    }

    /**
     * 获取群员
     */
    public void getItem() {
        groupUsers = db.getGroupUser(db.getUserId(jid));
        adapter = new GroupFriendsAdapter(GroupFriendsActivity.this, groupUsers);
        lvUser.setAdapter(adapter);
    }


    @Override
    public void deleteUserSuccess() {
        getItem();
    }
}
