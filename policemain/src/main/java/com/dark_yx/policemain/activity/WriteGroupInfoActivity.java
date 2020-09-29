package com.dark_yx.policemain.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.ChatService;
import com.dark_yx.policemain.chat.beans.ChatGroupBean;
import com.dark_yx.policemain.chat.callback.ICreateGroupCallback;
import com.dou361.dialogui.DialogUIUtils;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 填写群资料
 */
public class WriteGroupInfoActivity extends BaseActivity implements View.OnClickListener, ICreateGroupCallback {
    private ImageView back;
    private EditText et_group;
    private Button submit;
    private String groupname;
    private ChatService chatService;
    private String name;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupdatum);
        inteView();
        onClick();
        initChat();
    }

    private void inteView() {
        back = (ImageView) findViewById(R.id.iv_back);
        et_group = (EditText) findViewById(R.id.et_group_name);
        submit = (Button) findViewById(R.id.btn_submit);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText("编辑群资料");
    }

    private void onClick() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        et_group.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    submit.setBackgroundColor(Color.parseColor("#FF3595EF"));
                } else {
                    submit.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.btn_submit://选择好友
                groupname = et_group.getText().toString();
                if (TextUtils.isEmpty(groupname)) {
                    Toast.makeText(WriteGroupInfoActivity.this, "群组名不能为空", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    createGroup(groupname);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(chatServiceConnection);
        chatService.unregisterCreateGroupCallback();
    }

    private void initChat() {
        bindService(new Intent(this, ChatService.class), chatServiceConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }


    ServiceConnection chatServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d("聊天服务连接成功");
            chatService = ((ChatService.ChatServiceBinder) service).getService();
            chatService.registerCreateGroupCallback(WriteGroupInfoActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("聊天服务连接失败");
        }
    };

    @Override
    public void createGroup(String name) {
        dialog = DialogUIUtils.showLoadingVertical(this, "加载中...").show();
        dialog.setCancelable(false);
        this.name = name;
        chatService.createGroup(name);

        myHandler.sendEmptyMessageDelayed(1, 5000);
    }

    @Override
    public void showError(String message) {
        myHandler.removeMessages(1);

        Toast.makeText(WriteGroupInfoActivity.this, message, Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }

    @Override
    public void success(List<ChatGroupBean> beans) {
        myHandler.removeMessages(1);
        Toast.makeText(WriteGroupInfoActivity.this, "创建群成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(WriteGroupInfoActivity.this, SelectGroupFriendsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("groupId", getGroupId(beans));
        startActivity(intent);
        dialog.dismiss();
        finish();
    }

    private String getGroupId(List<ChatGroupBean> beans) {
        for (ChatGroupBean bean : beans) {
            if (bean.getName().equals(name))
                return String.valueOf(bean.getId());
        }
        return "";
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                showError("创建群失败，请检查网络设置或稍后重试");
            }
        }
    };
}
