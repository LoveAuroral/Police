package com.dark_yx.policemain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.NoticeInfo;

import java.util.List;

/**
 * Created by dark_yx-i on 2016-06-06.
 */
public class UnreadAdapter extends BaseAdapter {
    private Context context;
    private List<NoticeInfo> infos;

    public UnreadAdapter(Context context, List<NoticeInfo> infos) {
        this.context = context;
        this.infos = infos;

    }

    public UnreadAdapter() {
    }

    @Override
    public int getCount() {
        return infos.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.unread_item, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_unread_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_unread_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(infos.get(position).getTitle());
        holder.tvTime.setText(infos.get(position).getAddTime());
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
    }
}
