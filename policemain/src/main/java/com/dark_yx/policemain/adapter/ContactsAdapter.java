package com.dark_yx.policemain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.UsersBean;

import java.util.List;

/**
 * Created by lmp on 2016/5/30.
 */
public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private List<UsersBean> infos;

    public ContactsAdapter(Context context, List<UsersBean> infos) {
        this.context = context;
        this.infos = infos;
    }


    @Override
    public int getCount() {
        if (infos != null) {
            return infos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_contact_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText((infos.get(position).getName()));
        return convertView;
    }

    class ViewHolder {
        public TextView tv_name;
    }
}