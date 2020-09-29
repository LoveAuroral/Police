package com.dark_yx.policemain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.NoticeInfo;

import java.util.List;

/**
 * Created by lmp on 2016/5/19.
 */
public class NoticeAdapter extends BaseAdapter {
    private Context context;
    private List<NoticeInfo> infos;

    public NoticeAdapter(Context context, List<NoticeInfo> infos) {
        this.context = context;
        this.infos = infos;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_item, null);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.notice_title);
            holder.tvAddtime = (TextView) convertView.findViewById(R.id.notice_time);
            holder.ivNew = (ImageView) convertView.findViewById(R.id.iv_new);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(infos.get(position).getTitle());
        holder.tvAddtime.setText(infos.get(position).getAddTime());
        if (!infos.get(position).isRead()) {
            holder.ivNew.setVisibility(View.VISIBLE);
        } else {
            holder.ivNew.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle, tvAddtime;
        ImageView ivNew;
    }
}
