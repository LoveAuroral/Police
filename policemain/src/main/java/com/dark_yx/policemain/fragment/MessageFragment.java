package com.dark_yx.policemain.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dark_yx.policemain.adapter.RecentAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.beans.RecentMessageBean;
import com.dark_yx.policemain.chat.callback.IRecentCallback;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemain.chat.view.chatui.ui.activity.ChatActivity;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by lmp on 2016/4/29.
 */
public class MessageFragment extends BaseFragment implements  IRecentCallback {
    private View view;
    private ListView listView;
    private RecentAdapter recentAdapter;
    private Context mContext;
    private View noMessage;
    ChatDb chatDb;
    public List<RecentMessageBean> recentMessageBeans;

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_message, null);
        return view;
    }

    @Override
    public void initData() {
        mContext = getContext();
        chatDb = new ChatDb();
        init();

        recentMessageBeans = chatDb.getRecentMessages();
        recentAdapter = new RecentAdapter(getContext(), noMessage, recentMessageBeans);
        listView.setAdapter(recentAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    chatDb.deleteRecentMessage(recentMessageBeans.get(position));
                    recentMessageBeans.remove(position);
                    recentAdapter.setRecentMessageBeans(recentMessageBeans);
                    recentAdapter.notifyDataSetChanged();

                } catch (DbException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClick(position);
            }
        });
    }

    private void init() {
        noMessage = view.findViewById(R.id.ll_no_message);
        listView = (ListView) view.findViewById(R.id.lv_recent);
    }


    private void itemClick(int position) {
        RecentMessageBean recentMessageBean = recentMessageBeans.get(position);
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("chatSendType", recentMessageBean.getChatSendType());
        intent.putExtra("sendId", recentMessageBean.getSendObjectId());
        intent.putExtra("sendName", recentMessageBean.getName());
        startActivity(intent);

        recentMessageBean.setRead(true);
        try {
            chatDb.saveRecentMessage(recentMessageBean);
        } catch (DbException e) {
            e.printStackTrace();
        }

        recentAdapter.setRecentMessageBeans(recentMessageBeans);
        recentAdapter.notifyDataSetChanged();
    }

    @Override
    public void recentChanged() {
        mHandler.sendEmptyMessage(1);
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                recentMessageBeans = chatDb.getRecentMessages();
                recentAdapter.setRecentMessageBeans(recentMessageBeans);
                recentAdapter.notifyDataSetChanged();
            }
        }
    };
}
