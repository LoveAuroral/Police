package com.dark_yx.policemain.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.beans.RecentMessageBean;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;
import com.dark_yx.policemaincommon.Util.TimeUtil;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.List;

/**
 * Created by dark_yx-i on 2016-05-18.
 */
public class RecentAdapter extends BaseAdapter {
    private List<RecentMessageBean> recentMessageBeans;
    private Context mContext;
    private View view;
    private String name;

    public RecentAdapter(Context context, View view, List<RecentMessageBean> recentMessageBeans) {
        this.recentMessageBeans = recentMessageBeans;
        this.view = view;
        mContext = context;
    }

    public void setRecentMessageBeans(List<RecentMessageBean> recentMessageBeans) {
        this.recentMessageBeans = recentMessageBeans;
    }

    @Override
    public int getCount() {
        if (recentMessageBeans != null && recentMessageBeans.size() != 0) {
            view.setVisibility(View.GONE);
            return recentMessageBeans.size();
        } else {
            view.setVisibility(View.VISIBLE);
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return recentMessageBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.recent_item, null);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_message_count);
            holder.tv_message = (TextView) convertView.findViewById(R.id.tv_recentMsg);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_num = (ImageView) convertView.findViewById(R.id.tv_num);
            holder.contact_image1 = (CircularImageView) convertView.findViewById(R.id.contact_image1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecentMessageBean recentMessageBean = recentMessageBeans.get(position);

        holder.tv_name.setText(recentMessageBean.getName());

        switch (recentMessageBean.getChatSendType()) {
            case Constants.USER:
                holder.contact_image1.setImageResource(R.mipmap.qq);
                name = "";
                break;
            case Constants.GROUP:
                holder.contact_image1.setImageResource(R.mipmap.group_detail);
                String s = recentMessageBean.getSendName();
                if (!TextUtils.isEmpty(s))
                    this.name = s + ": ";
                else name = "";
                break;
        }

        switch (recentMessageBean.getChatMessageType()) {
            case Constants.CANCEL:
                if (recentMessageBean.getType() == 0) {
                    holder.tv_message.setText("你撤回了一条消息");
                } else {
                    holder.tv_message.setText("对方撤回了一条消息");
                }
                break;
            case Constants.PICTURE:
                holder.tv_message.setText(name + "[图片]");
                break;
            case Constants.AUDIO:
                holder.tv_message.setText(name + "[语音消息]");
                break;
            case Constants.PTT:
                holder.tv_message.setText(name + "[语音消息]");
                break;
            case Constants.TEXT:
                holder.tv_message.setText(name + recentMessageBeans.get(position).getMessage());
                break;
        }

        holder.tv_time.setText(TimeUtil.getChatTime((recentMessageBeans.get(position).getTickets())));
        showStudus(holder, position);
        return convertView;
    }

    class ViewHolder {
        public TextView tv_name;
        public TextView tv_message;
        public TextView tv_count;
        public TextView tv_time;
        public ImageView tv_num;
        public CircularImageView contact_image1;
    }


    /**
     * 显示未读消息
     */
    private void showStudus(ViewHolder holder, int position) {
        if (!recentMessageBeans.get(position).isRead()) {
            holder.tv_num.setVisibility(View.VISIBLE);
        } else {
            holder.tv_num.setVisibility(View.GONE);
        }
    }

}
