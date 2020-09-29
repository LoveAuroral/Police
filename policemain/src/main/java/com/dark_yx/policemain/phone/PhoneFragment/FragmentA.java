package com.dark_yx.policemain.phone.PhoneFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import com.dark_yx.policemain.phone.Bean.UsersPhone;
import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.PrivatePhoneBean;
import com.dark_yx.policemaincommon.Util.FileUtil;
import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentA extends Fragment {
    private View v;
    String phoneJosn;
    private ListView lv_pal;
    private PhoneAdapter adapter;
    private final static String TAG = FragmentB.class.getName();
    private EditText et_key;
    private ImageView iv_search_delete;
    private List<UsersPhone.ResultBean.ChildrenBean.UsersBean> userBeans;
    List<PrivatePhoneBean> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_a, container, false);
        initView();
        initData();
        return v;
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
//                tree.setVisibility(empty ? View.VISIBLE : View.GONE);
//                lv_pal.setVisibility(empty ? View.GONE : View.VISIBLE);
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

    private void initData() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Police/", "treeUser.txt");
        if (file.exists()) {
            phoneJosn = FileUtil.getFile("treeUser.txt");
            Log.d("phoneJosn=", phoneJosn);
            mList = new ArrayList<>();
            UsersPhone usersPhone = new Gson().fromJson(phoneJosn, UsersPhone.class);
            LogUtil.d("usersPhone--" + usersPhone.getResult().toString());
            for (int i = 0; i < usersPhone.getResult().get(0).getChildren().size(); i++) {
                for (int j = 0; j < usersPhone.getResult().get(0).getChildren().get(i).getUsers().size(); j++) {
                    String name = usersPhone.getResult().get(0).getChildren().get(i).getUsers().get(j).getName();
                    String phone = usersPhone.getResult().get(0).getChildren().get(i).getUsers().get(j).getLandline();
                    PrivatePhoneBean tb_contacts = new PrivatePhoneBean();
                    tb_contacts.setName(name);
                    tb_contacts.setPhoneNumber(phone);
                    mList.add(tb_contacts);
                }
            }
        } else {
            Toast.makeText(getContext(), "无所内号码，请同步白名单之后再试", Toast.LENGTH_SHORT).show();
        }

        Log.d("mList", mList.toString());
        setAdapter(null);
    }

    private void setAdapter(String o) {
        if (mList == null)
            return;

        if (TextUtils.isEmpty(o)) {
            et_key.setHint("在" + mList.size() + "个所内电话中搜索");
        }
        final List<PrivatePhoneBean> data = getData(o);
        LogUtil.d(data.toString());
//        adapter = new AAdapter(getActivity(), data);
        adapter = new PhoneAdapter(getActivity(), data, 1);
        lv_pal.setAdapter(adapter);

        lv_pal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "点击的position: " + position);
                callPhone(data.get(position).getPhoneNumber());
            }
        });
    }

    private List<PrivatePhoneBean> getData(String o) {
        if (TextUtils.isEmpty(o)) {
            return mList;
        } else {
            List<PrivatePhoneBean> list1 = new ArrayList<>();
            for (PrivatePhoneBean phoneWhites : mList) {
                if ((phoneWhites.getPhoneNumber() != null &&
                        phoneWhites.getPhoneNumber().contains(o)) || phoneWhites.getName().contains(o))
                    list1.add(phoneWhites);
            }
            return list1;
        }
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
