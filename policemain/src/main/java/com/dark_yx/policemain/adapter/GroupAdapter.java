package com.dark_yx.policemain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;
import com.dark_yx.policemaincommon.Models.UsersBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by VicknL2 on 2016/8/6.
 */
public class GroupAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<UsersBean> list;
    private UsersBean user;
    private HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public GroupAdapter(Context context, List<UsersBean> list) {
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
        if (lmap.get(position) == null) {
            convertView = inflater.inflate(R.layout.group_item, null);
            holder = new ViewHolder(convertView, position);
            lmap.put(position, convertView);
            convertView.setTag(holder);
        } else {
            convertView = lmap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(user.getName());//姓名
        return convertView;
    }

    class ViewHolder {
        public TextView name;//姓名
        public CheckBox chepal;//多选框

        public ViewHolder(View view, final int position) {
            name = (TextView) view.findViewById(R.id.tv_pal_name);
            chepal = (CheckBox) view.findViewById(R.id.checkbox_group);
            chepal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                      if (isChecked && !Constants.groupList.contains(list.get(position).getId())) {
                                                          Constants.groupList.add(list.get(position).getId());
                                                      } else {
                                                          Constants.groupList.remove(list.get(position));
                                                      }
                                                  }
                                              }

            );
        }

    }
}
