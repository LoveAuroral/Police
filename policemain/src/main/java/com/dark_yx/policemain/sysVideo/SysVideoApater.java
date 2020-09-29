package com.dark_yx.policemain.sysVideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.uploadlib.beans.DataDicBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SysVideoApater extends BaseAdapter {
    List<DataDicBean.ResultBean.ItemsBean> tags = new ArrayList<>();
    Context context;
    HashMap<String, Boolean> states = new HashMap<>();
    private int position = -1;

    public SysVideoApater(Context context, List<DataDicBean.ResultBean.ItemsBean> tags) {
        this.context = context;
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    public DataDicBean.ResultBean.ItemsBean GetItem() {
        if (position == -1)
            return null;
        return tags.get(position);
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tags.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        DataDicBean.ResultBean.ItemsBean itemsBean = tags.get(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.sysvideo_tag_items, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.txtKey);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.mRadioBtn);
            convertView.setTag(holder);
        }

        holder.textView.setText(itemsBean.getDisplayName());

        final ViewHolder finalHolder = holder;
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String key : states.keySet()) {
                    states.put(key, false);
                }

                states.put(String.valueOf(position), finalHolder.radioButton.isChecked());
                SysVideoApater.this.notifyDataSetChanged();
                SysVideoApater.this.position = position;
            }
        });

        boolean res = false;
        if (states.get(String.valueOf(position)) == null || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position), false);
        } else
            res = true;

        holder.radioButton.setChecked(res);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        RadioButton radioButton;
    }
}
