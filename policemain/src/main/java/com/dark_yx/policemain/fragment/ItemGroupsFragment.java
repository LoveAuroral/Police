package com.dark_yx.policemain.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.activity.MainActivity;
import com.dark_yx.policemain.adapter.VoiceGroupAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.beans.ChatGroupBean;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemaincommon.Util.DataUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 我的群组列表
 * Created by lmp on 2016/4/29.
 */
public class ItemGroupsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View view;
    private ListView groupListView;
    private PopupWindow popupWindow;
    private View popupView;
    private static final String TAG = "ItemGroupsFragment";
    private ChatGroupBean info;
    private List<ChatGroupBean> bean;
    private ChatDb db;
    private MainActivity mainActivity;

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_my_ptt_group, null);
        return view;
    }

    @Override
    public void initData() {
        LogUtil.d("ItemGroupsFragment init");
        db = new ChatDb();
        init();
        initPopupWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }


    private void init() {
        groupListView = (ListView) view.findViewById(R.id.videoGroupList);
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_voice_group, null);
        TextView tvChange = (TextView) popupView.findViewById(R.id.tv_change_group);
        TextView tvDel = (TextView) popupView.findViewById(R.id.tv_del_group);
        TextView tvDefault = (TextView) popupView.findViewById(R.id.tv_default_group);

        groupListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                info = bean.get(position);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                TextView tvDel = (TextView) popupView.findViewById(R.id.tv_del_group);
                if (info.getCreatorUserId() != db.getId()) {
                    tvDel.setText("退出群组");
                } else {
                    tvDel.setText("删除群组");
                }
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();//暗屏
                lp.alpha = 0.5f;
                getActivity().getWindow().setAttributes(lp);
                return true;
            }
        });

        tvDel.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        tvDefault.setOnClickListener(this);

        bean = db.getGroups();
        getGroup();
    }

    /**
     * 获取群组
     */

    private void getGroup() {
        if (bean != null) {
            VoiceGroupAdapter adapter = new VoiceGroupAdapter(getContext(), bean, DataUtil.getDefaultGroup());
            groupListView.setAdapter(adapter);
            groupListView.setOnItemClickListener(this);
        }
    }


    /**
     * 删除群组
     */
    private void exitGroup() {
        mainActivity.deleteExitGroup(info);
        popupWindow.dismiss();
    }

    /**
     * 设置默认群组
     */
    private void setDefaultGroup() {
        popupWindow.dismiss();
        DataUtil.setDefaultGroup(info.getId());
        Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();
        getGroup();
    }

    /**
     * 初始化弹出框
     */
    private void initPopupWindow() {
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.color.popu_dialog
        ));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_del_group:
                exitGroup();
                break;
            case R.id.tv_default_group:
                setDefaultGroup();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupEvent(List<ChatGroupBean> bean) {
        this.bean = bean;
        getGroup();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChatGroupBean mucInfo = bean.get(position);
        startGroupChat(mucInfo);
    }

    private void startGroupChat(ChatGroupBean bean) {
        Intent intent = new Intent(getContext(), com.dark_yx.policemain.chat.view.chatui.ui.activity.ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("chatSendType", 1);
        intent.putExtra("sendId", bean.getId());
        intent.putExtra("sendName", bean.getName());

        startActivity(intent);
    }


}