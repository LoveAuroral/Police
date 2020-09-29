package com.dark_yx.policemaincommon.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dark_yx.policemaincommon.Adapter.PhoneAdapter;
import com.dark_yx.policemaincommon.Models.PhoneContacts;
import com.dark_yx.policemaincommon.R;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lvSearch;
    private EditText etInput;
    private PhoneAdapter adapter;
    private ImageView ivDelete;
    private InputMethodManager imm;
    private ImageView ivBack;
    private List<PhoneContacts> contacts;
    private View llNoMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

        imm.showSoftInput(etInput, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    ivDelete.setVisibility(View.VISIBLE);
                    searchPhone();
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            searchPhone();
            if (!etInput.getText().toString().trim().isEmpty()) {
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    public void searchPhone() {
        if (!etInput.getText().toString().trim().isEmpty()) {
            contacts = PhoneContactsDb.getContacts(etInput.getText().toString());
            if (contacts != null) {
                adapter = new PhoneAdapter(this, contacts);
                lvSearch.setAdapter(adapter);
                lvSearch.setVisibility(View.VISIBLE);
                if (contacts.size() == 0) {
                    llNoMessage.setVisibility(View.VISIBLE);
                    lvSearch.setVisibility(View.GONE);
                }
            }
        } else {
            Toast.makeText(this, "请输入", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_search_delete) {
            etInput.getText().clear();
            imm.showSoftInput(etInput, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else if (i == R.id.iv_search_back) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhoneContacts contact = contacts.get(position);
        if (SystemInfo.getSimImsi(this).equals(contact.getSimNum())) {
            callPhone(contact.getPhoneNumber());
        } else {
            Toast.makeText(this, "拨号失败,当前未插卡或手机号与后台绑定不符", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 打电话
     *
     * @param phoneNumber
     */
    public void callPhone(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            callIntent.putExtra("subscription", 0);
            startActivity(callIntent);
        }
    }
}
