package com.dark_yx.policemain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.beans.ChatGroupBean;

import java.util.List;

/**
 * Created by lmp on 2016/5/19.
 */
public class VoiceGroupAdapter extends BaseAdapter {
    private Context context;
    private List<ChatGroupBean> lists;
    private long defaultGroup;

    public VoiceGroupAdapter(Context context, List<ChatGroupBean> lists, long defaultGroup) {
        this.context = context;
        this.defaultGroup = defaultGroup;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatGroupBean mucInfo = lists.get(position);
        String room = mucInfo.getName();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group, null);
            holder = new ViewHolder();
            holder.groupName = (TextView) convertView.findViewById(R.id.tv_group_name);
            holder.tv_default = (TextView) convertView.findViewById(R.id.tv_default_group);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.groupName.setText(room);
        if (defaultGroup == mucInfo.getId())
            holder.tv_default.setVisibility(View.VISIBLE);
        else
            holder.tv_default.setVisibility(View.GONE);
        return convertView;
    }

    class ViewHolder {
        TextView groupName;
        TextView tv_default;
    }
}
