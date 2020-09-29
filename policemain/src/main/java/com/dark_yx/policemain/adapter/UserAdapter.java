package com.dark_yx.policemain.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.UsersBean;

import java.util.List;

/**
 * Created by VicknL2 on 2016/8/6.
 */
public class UserAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<UsersBean> list;
    private UsersBean user;

    public UserAdapter(Context context, List<UsersBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<UsersBean> list) {
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
        user = list.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_voice_user, null);
            holder = new ViewHolder();
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_real_name = (TextView) convertView.findViewById(R.id.tv_real_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_real_name.setText(user.getName());
        holder.tv_user_name.setText(user.getUserName());
        String phoneNumber = list.get(position).getLandline();
        holder.tv_phone.setText(TextUtils.isEmpty(phoneNumber) ? "无" : phoneNumber);//电话
        return convertView;
    }

    class ViewHolder {
        public TextView tv_real_name, tv_user_name, tv_phone;//姓名
    }
}
