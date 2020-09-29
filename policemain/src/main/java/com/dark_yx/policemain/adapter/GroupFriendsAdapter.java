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
public class GroupFriendsAdapter extends BaseAdapter {
    private static final String TAG = "GroupFriendsAdapter";
    private Context context;
    private List<UsersBean> entries;
    private UsersBean name;

    public GroupFriendsAdapter(Context context, List<UsersBean> infos) {
        this.context = context;
        this.entries = infos;
    }


    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        name = entries.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_friends, null);
            holder = new ViewHolder();
            holder.tvRealName = (TextView) convertView.findViewById(R.id.tv_real_name);
            holder.tvDel = (TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvRealName.setText(name.getName());
        return convertView;
    }

    class ViewHolder {
        TextView tvRealName, tvDel;
    }

}