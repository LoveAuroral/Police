package com.dark_yx.policemain.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dark_yx.policemain.adapter.ContactsAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;
import com.dark_yx.policemaincommon.Models.UsersBean;

import java.util.List;

/**
 * 添加聊天
 * Created by dark_yx-i on 2016-06-07.
 */
public class AddChatActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lvChat;
    private ContactsAdapter rosterAdapter;
    private ImageView iv_back;
    private List<UsersBean> users;
    private ChatDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        init();
        getData();
        setAdapter();
    }

    private void setAdapter() {
        rosterAdapter = new ContactsAdapter(this, users);
        lvChat.setAdapter(rosterAdapter);
    }

    private void getData() {
        users = db.getGroupUser();
    }

    private void init() {
        db = new ChatDb();
        lvChat = (ListView) findViewById(R.id.lv_add_chat);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lvChat.setOnItemClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText("创建聊天");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UsersBean user = users.get(position);
        Constants.startChat(this, user);
        finish();
    }
}
