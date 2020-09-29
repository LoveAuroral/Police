package com.dark_yx.policemain.phone.Adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.PrivatePhoneBean;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * Created by VicknL2 on 2016/8/24.
 */
public class PhoneAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<PrivatePhoneBean> list;
    private int fragment;
    private Context context;

    public PhoneAdapter(Context context, List<PrivatePhoneBean> list, int fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<PrivatePhoneBean> list) {
        LogUtil.d("list size: " + list.size());
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.phone_pal_itme, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (fragment == 3) {
//            holder.name.setTextColor(context.getResources().getColor(R.color.text_red));
//            holder.phone.setTextColor(context.getResources().getColor(R.color.text_red));
//            holder.time.setTextColor(context.getResources().getColor(R.color.text_red));
            holder.time.setText(list.get(position).getTime());
            holder.time.setVisibility(View.VISIBLE);
        } else {
            holder.time.setVisibility(View.GONE);
        }
        holder.name.setText(list.get(position).getName());//姓名
        String phoneNumber = list.get(position).getPhoneNumber();
        holder.phone.setText(TextUtils.isEmpty(phoneNumber) ? "无" : phoneNumber);//电话
//        if (list.get(position).getTime() != null) {
//
//        } else {
//
//        }
        return convertView;
    }

    class ViewHolder {
        public TextView name, phone, time;//姓名

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.tv_user_name);
            phone = (TextView) view.findViewById(R.id.tv_phone);
            time = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
