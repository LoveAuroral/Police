package com.dark_yx.policemain.phone.PhoneFragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
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

import com.dark_yx.policemain.phone.Adpter.PhoneAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.MissedPhones;
import com.dark_yx.policemaincommon.Models.PrivatePhoneBean;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.dark_yx.policemaincommon.Util.PhoneUtil.TAG;

public class FragmentC extends Fragment {
    private View v;
    private EditText et_key;
    private ListView lv_pal;
    private List<MissedPhones> list;
    private String[] back = {CallLog.Calls.NUMBER, CallLog.Calls.TYPE,
            CallLog.Calls.CACHED_NAME, CallLog.Calls.DATE};
    private List<PrivatePhoneBean> missPhone;
    private ImageView iv_search_delete;
    private PhoneAdapter adapter;
    String callDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_c, container, false);
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

    /**
     * 获取未接电话
     */
    public void initData() {
        Cursor cursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, back, null,
                null, null);
        missPhone = new ArrayList<>();
        PrivatePhoneBean bean = null;
        while (cursor.moveToNext()) {
            String numb = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
            long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
            callDate = sdf.format(new Date(date));
            if (Integer.valueOf(type) == CallLog.Calls.MISSED_TYPE) {
                bean = new PrivatePhoneBean();
                bean.setPhoneNumber(numb);
                if (name != null && name.length() > 0) {
                    bean.setName(name);
                } else {
                    bean.setName("未知来源");
                }
                bean.setTime(callDate);
                missPhone.add(bean);
                LogUtil.d("missPhone1-->" + missPhone.toString());
                System.out.println("number : " + name + "--" + numb + "--" + callDate);
            }
        }
        Collections.reverse(missPhone);
        LogUtil.d("missPhone2-->" + missPhone.toString());
        setAdapter(null);
    }

    private void setAdapter(String o) {
        if (missPhone == null) return;

        if (TextUtils.isEmpty(o)) {
            et_key.setHint("在" + missPhone.size() + "个未接电话中搜索");
        }

        final List<PrivatePhoneBean> data = getData(o);
        LogUtil.d(data.toString());

        adapter = new PhoneAdapter(getActivity(), data, 3);
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
            return missPhone;

        List<PrivatePhoneBean> privatePhoneBeans = new ArrayList<>();
        for (PrivatePhoneBean bean : missPhone) {
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
