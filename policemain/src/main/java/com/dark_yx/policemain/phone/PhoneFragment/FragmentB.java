package com.dark_yx.policemain.phone.PhoneFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dark_yx.policemain.phone.Adpter.PhoneAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.PrivatePhoneBean;
import com.dark_yx.policemaincommon.Models.PrivatePhoneWhiteResult;
import com.dark_yx.policemaincommon.Util.PrivateNumberUtil;
import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class FragmentB extends Fragment {
    private View v;
    private ListView lv_pal;
    private PhoneAdapter adapter;
    private final static String TAG = FragmentB.class.getName();
    private EditText et_key;
    private ImageView iv_search_delete;
    private List<PrivatePhoneBean> beans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_b, container, false);
        initView();
        initData();
        return v;
    }

    private void initData() {
        String s = PrivateNumberUtil.readString();
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(getContext(), "无亲情号码，请同步白名单之后再试", Toast.LENGTH_LONG).show();
            return;
        }
        beans = new Gson().fromJson(s, PrivatePhoneWhiteResult.class).getResult().getItems();
        setAdapter(null);
    }

    private void initView() {
        lv_pal = (ListView) v.findViewById(R.id.lv_pal);
        et_key = (EditText) v.findViewById(R.id.et_key);
        iv_search_delete = (ImageView) v.findViewById(R.id.iv_search_delete);

        et_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean empty = TextUtils.isEmpty(s);
                iv_search_delete.setVisibility(empty ? View.GONE : View.VISIBLE);

                setAdapter(s.toString());
            }
        });

        iv_search_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_key.getText().clear();
            }
        });
    }

    private void setAdapter(String o) {
        if (beans == null) return;

        if (TextUtils.isEmpty(o)) {
            et_key.setHint("在" + beans.size() + "个亲情电话中搜索");
        }

        final List<PrivatePhoneBean> data = getData(o);
        LogUtil.d(data.toString());

        adapter = new PhoneAdapter(getActivity(), data, 2);
        lv_pal.setAdapter(adapter);
        lv_pal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callPhone(data.get(position).getPhoneNumber());
            }
        });
    }

    private List<PrivatePhoneBean> getData(String key) {
        if (TextUtils.isEmpty(key))
            return beans;

        List<PrivatePhoneBean> privatePhoneBeans = new ArrayList<>();
        for (PrivatePhoneBean bean : beans) {
            if (bean.getName().contains(key) || bean.getPhoneNumber().contains(key))
                privatePhoneBeans.add(bean);
        }
        return privatePhoneBeans;
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
                Log.d(TAG, phoneNumber);
                startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
