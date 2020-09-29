package com.dark_yx.policemain.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.dark_yx.policemain.adapter.UserAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页面
 * Created by lmp on 2016/5/24.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lvSearch;
    private EditText etInput;
    private UserAdapter adapter;
    private ImageView ivDelete;
    private ImageView ivBack;
    private View llNoMessage;
    private List<UsersBean> groupUser;
    private ChatDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {
        lvSearch = (ListView) findViewById(R.id.lv_search);
        etInput = (EditText) findViewById(R.id.et_search_input);
        ivDelete = (ImageView) findViewById(R.id.iv_search_delete);
        ivBack = (ImageView) findViewById(R.id.iv_search_back);
        llNoMessage = findViewById(R.id.ll_no_message_search);
        ivDelete.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        lvSearch.setOnItemClickListener(this);
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
                searchUser();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        CommonMethod.openKeyboard(etInput, this);
        db = new ChatDb();

        searchUser();
    }

    private void searchUser() {
        groupUser = db.getUserByKey(etInput.getText().toString());
        setAdapter();
    }


    private void setAdapter() {
        lvSearch.setVisibility(groupUser.size() > 0 ? View.VISIBLE : View.GONE);
        llNoMessage.setVisibility(groupUser.size() > 0 ? View.GONE : View.VISIBLE);

        adapter = new UserAdapter(this, groupUser);
        lvSearch.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_delete:
                etInput.getText().clear();
                CommonMethod.openKeyboard(etInput, this);
                searchUser();
                break;
            case R.id.iv_search_back:
                CommonMethod.closeKeyboard(this);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonMethod.closeKeyboard(this);
        showDialog(groupUser.get(position));
    }


    private void showDialog(final UsersBean usersBean) {
        List<String> strings = new ArrayList<>();
        strings.add("聊天");
        strings.add("电话");
        DialogUIUtils.showBottomSheetAndCancel(this, strings, "取消", new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                switch (text.toString()) {
                    case "聊天":
                        Constants.startChat(SearchActivity.this, usersBean);
                        break;
                    case "电话":
                        callPhone(usersBean.getLandline());
                        break;
                }
            }
        }).show();
    }

    /**
     * 打电话
     *
     * @param phoneNumber
     */
    public void callPhone(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber.trim()));
            try {
                startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
