package com.dark_yx.policemain.fragment;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.tree.contract.UserTreeContract;
import com.dark_yx.policemain.tree.factory.MyNodeViewFactory;
import com.dark_yx.policemaincommon.Models.UserTreeResult;
import com.dark_yx.policemain.tree.presenter.UserTreePresenter;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemaincommon.Util.UserTreeUtil;
import com.google.gson.Gson;


import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;


/**
 * 对讲树状联系人
 * Created by lmp on 2016/4/29.
 */
public class ItemContactsFragment extends BaseFragment implements View.OnClickListener, UserTreeContract.View {
    private View view;
    private ListView videoListView;
    private final static String TAG = "ItemContactsFragment";
    private PopupWindow popupWindow;
    private View popupView;
    private TextView tv_video, tv_audio, tv_mask, tv_text;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉布局
    private boolean isRefreshing = false;
    private UserTreeContract.Presenter presenter;
    private LinearLayout ll_a;
    private View tree;
    private List<UsersBean> usersBeans;
    private ChatDb db;

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_my_ptt_contacts, null);
        return view;
    }

    @Override
    public void initData() {
        presenter = new UserTreePresenter(this);
        db = new ChatDb();
        initVideoListView();
        initSwipeRefreshLayout();
        initUserTree();
    }

    private void initUserTree() {
        if (UserTreeUtil.isExist()) {
            String string = UserTreeUtil.readString();
            showTree(string);
        } else getUserTree();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        ll_a = (LinearLayout) view.findViewById(R.id.ll_a);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(myRefreshListener);
    }

    /**
     * 下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener myRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (!isRefreshing) {
                if (UserTreeUtil.readString() != null)
                    ll_a.removeView(tree);
                getUserTree();
            }
        }
    };

    private void getUserTree() {
        usersBeans = new ArrayList<>();
        presenter.getUserTree();
    }

    private void initVideoListView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_chat_method, null);
        initPopupWindow();
        tv_mask = (TextView) popupView.findViewById(R.id.tv_mask);
        tv_text = (TextView) popupView.findViewById(R.id.tv_text);
        tv_video = (TextView) popupView.findViewById(R.id.tv_video);
        tv_audio = (TextView) popupView.findViewById(R.id.tv_audio);

        tv_mask.setOnClickListener(this);
        tv_text.setOnClickListener(this);
        tv_video.setOnClickListener(this);
        tv_audio.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_text:

                break;
            case R.id.tv_audio:
                LogUtil.d("tv_voice");
//                startCall("audio", user.getiMEI());
                popupWindow.dismiss();
                break;
            case R.id.tv_video:
                LogUtil.d("tv_video");
//                startCall("video", user.getiMEI());
                popupWindow.dismiss();
                break;
            case R.id.tv_mask:
                LogUtil.d("tv_mask");
//                MaskUserActivity.addMaskUser(user.getRealName(), user.getiMEI());
                popupWindow.dismiss();
                break;
        }
    }


    private void showPopupWindow() {
        popupWindow.showAtLocation(videoListView, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 初始化弹出框
     */
    private void initPopupWindow() {
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        // 设置此参数获得焦点，否则无法点击
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
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
    public void showDialog() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissDialog() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getUserTreeSuccess(UserTreeResult result) {
        String str = new Gson().toJson(result);
        UserTreeUtil.writeString(str);
        showTree(str);
    }

    private void showTree(String str) {
        UserTreeResult result = new Gson().fromJson(str, UserTreeResult.class);
        TreeNode root = TreeNode.root();
        root.setLevel(0);
        for (UserTreeResult.ResultBean bean : result.getResult()) {
            root.addChild(buildTree(bean));
        }

        TreeView treeView = new TreeView(root, getContext(), new MyNodeViewFactory());
        tree = treeView.getView();
        tree.setBackgroundColor(getResources().getColor(R.color.color_white));
        tree.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_a.addView(tree);

        treeView.expandLevel(0);
        if (usersBeans != null)
            db.saveUser(usersBeans);
    }

    private TreeNode buildTree(UserTreeResult.ResultBean bean) {
        TreeNode treeNode = new TreeNode(bean.getDisplayName());
        String[] split = bean.getCode().split("\\.");
        int level = split.length - 1;
        treeNode.setLevel(level);

        for (UserTreeResult.ResultBean child : bean.getChildren()) {
            treeNode.addChild(buildTree(child));
        }
        for (UsersBean user : bean.getUsers()) {
            TreeNode userNode = new TreeNode(user);
            userNode.setLevel(level + 1);
            treeNode.addChild(userNode);
            if (usersBeans != null) {
                if (user != null) {
                    if (user.getLandline() != null) {
                        usersBeans.add(user);
                    }
                }
            }
        }
        return treeNode;
    }

    @Override
    public void getUserTreeFail(String err) {
        Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
    }
}