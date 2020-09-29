package com.dark_yx.policemain.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dark_yx.policemain.adapter.GroupAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemain.chat.ChatService;
import com.dark_yx.policemain.chat.beans.InviteToGroupBean;
import com.dark_yx.policemain.chat.callback.IInviteUserCallback;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dou361.dialogui.DialogUIUtils;


import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 选择联系人
 */
public class SelectGroupFriendsActivity extends Activity implements View.OnClickListener, IInviteUserCallback {
    private ImageView back;
    private Button yes;
    private ListView lv_user;
    private GroupAdapter adapter;
    private String groupNameId;
    private static final String TAG = "SelectGroupFriends";
    private ChatDb db;
    private List<UsersBean> groupUser;
    private ChatService chatService;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_groupchat);
        initView();
        setAdapter();
        initChat();
    }

    private void initChat() {
        bindService(new Intent(this, ChatService.class), chatServiceConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }


    private void setAdapter() {
        adapter = new GroupAdapter(this, groupUser);
        lv_user.setAdapter(adapter);
    }

    private void initView() {
        db = new ChatDb();
        back = (ImageView) findViewById(R.id.start_group_back);
        yes = (Button) findViewById(R.id.btn_yes);
        lv_user = (ListView) findViewById(R.id.lv_user);

        groupNameId = getIntent().getStringExtra("groupId");

        back.setOnClickListener(this);
        yes.setOnClickListener(this);

        groupUser = db.getGroupUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_group_back:
                finish();
                break;
            case R.id.btn_yes:
                inviteUser(groupNameId, Constants.groupList);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.groupList.clear();

        chatService.unregisterInviteUserCallback();
        unbindService(chatServiceConnection);
    }

    @Override
    public void inviteUser(String groupId, List<Long> beans) {
        dialog = DialogUIUtils.showLoadingVertical(this, "加载中。。。").show();

        InviteToGroupBean bean = new InviteToGroupBean();
        bean.setGroupId(Long.parseLong(groupId));
        bean.setUserIds(beans);
        chatService.inviteToGroup(bean);

        myHandler.sendEmptyMessageDelayed(1, 5000);
    }

    @Override
    public void inviteSuccess() {
        myHandler.removeMessages(1);

        dialog.dismiss();
        Toast.makeText(this, "邀请成功",
                Toast.LENGTH_SHORT).show();
        Constants.groupList.clear();
        finish();
    }

    @Override
    public void inviteFail(String err) {
        dialog.dismiss();

        Toast.makeText(this, "邀请失败： " + err,
                Toast.LENGTH_SHORT).show();
    }

    ServiceConnection chatServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d("聊天服务连接成功");
            chatService = ((ChatService.ChatServiceBinder) service).getService();
            chatService.registerInviteUserCallback(SelectGroupFriendsActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("聊天服务连接失败");
        }
    };

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                inviteFail("请检查网络设置或稍后重试");
            }
        }
    };

}
