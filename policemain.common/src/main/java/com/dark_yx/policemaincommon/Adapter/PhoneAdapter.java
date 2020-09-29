package com.dark_yx.policemaincommon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dark_yx.policemaincommon.Models.PhoneContacts;
import com.dark_yx.policemaincommon.R;

import java.util.List;

/**
 * Created by VicknL2 on 2016/8/24.
 */
public class PhoneAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<PhoneContacts> list;

    public PhoneAdapter(Context context, List<PhoneContacts> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<PhoneContacts> list) {
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
            convertView = inflater.inflate(R.layout.item_phone, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getUserName());//姓名
        holder.phone.setText(list.get(position).getPhoneNumber());//电话
        holder.role.setText(list.get(position).getUnit());//电话
        return convertView;
    }

    class ViewHolder {
        public TextView name, phone, role;//姓名

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.tv_user_name);
            phone = (TextView) view.findViewById(R.id.tv_phone);
            role = (TextView) view.findViewById(R.id.tv_role);
        }
    }
}
