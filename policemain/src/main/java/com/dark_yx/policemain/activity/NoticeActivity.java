package com.dark_yx.policemain.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.adapter.NoticeAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.NoticeInfo;
import com.dark_yx.policemaincommon.Util.DbHelp;
import com.dark_yx.policemaincommon.Util.HttpCallBackEvent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公告
 * Created by lmp on 2016/5/19.
 */
@ContentView(R.layout.activity_notice)
public class NoticeActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    @ViewInject(R.id.lv_notice)
    private ListView lvNotice;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    private NoticeAdapter adapter;
    private DbHelp db;
    private List<NoticeInfo> infos;
    private NoticeInfo noticeInfo;
    private String packegName;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        lvNotice.setOnItemClickListener(this);
        lvNotice.setOnItemLongClickListener(this);
        ivBack.setOnClickListener(this);
        initAdapter();
        tv_title.setText("公告");
    }

    private void initAdapter() {
        db = new DbHelp();
        infos = db.getNotices();
        if (infos != null) {
            adapter = new NoticeAdapter(this, infos);
            lvNotice.setAdapter(adapter);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        noticeInfo = infos.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
        builder.setTitle("确认删除该公告?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteNotice(noticeInfo);
                        updateView();
                    }
                })
                .show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        noticeInfo = infos.get(position);
        Log.d("noticeInfo", noticeInfo.toString());
        Log.d("position", position + "");
        updateView();
        if (!noticeInfo.isSend()) {//消息已读未发送则请求
            HttpCallBackEvent feedBackEvent = new feedBackEvent();//公告已读反馈
            Map<String, Object> params = new HashMap<>();
//            params.put("userName", UserUtil.getUserName());
//            params.put("readId", noticeInfo.getId());
//            HttpHelp.Get(feedBackEvent, UserUtil.getIp() + "Api/SetNewsStateByApp", params);
        }
        if (noticeInfo.getType() != null) {
            if (noticeInfo.getType().equals("lanuchar")) {
                try {
                    JSONObject json = new JSONObject(noticeInfo.getContent());
                    packegName = json.getString("PackegName");
                    Log.d("packegName", json.getString("PackegName"));
                    className = json.getString("ClassName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClassName(packegName, className);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "当前还没有安装此应用", Toast.LENGTH_SHORT).show();
                }

            } else if (noticeInfo.getType().equals("message")) {
                try {
//                    String url = UserUtil.getIp() + "api/notice?noticeId=" + noticeInfo.getId();
                    Intent intent = new Intent(NoticeActivity.this, WebViewActivity.class);
//                    intent.putExtra("url", url);
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }
    }

    public void updateView() {
        if (!noticeInfo.isRead()) {
            noticeInfo.setRead(true);
            db.changeNotices(noticeInfo);
        }
        initAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private class feedBackEvent implements HttpCallBackEvent {
        @Override
        public void Excute(String result) throws JSONException {
            JSONObject jsonObject = new JSONObject(result);
            int errorCode = Integer.parseInt(jsonObject.getString("errorcode"));
            String errorMsg = jsonObject.getString("erormsg");
            switch (errorCode) {
                case 0:
                    Toast.makeText(NoticeActivity.this, "公告已读反馈成功", Toast.LENGTH_SHORT).show();
                    noticeInfo.setSend(true);
                    db.changeNotices(noticeInfo);
                    break;
                case 1:
                    Toast.makeText(NoticeActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(NoticeActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(NoticeActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(NoticeActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        @Override
        public void OnError(Throwable ex) throws JSONException {
            Toast.makeText(NoticeActivity.this, "公告反馈失败！", Toast.LENGTH_SHORT).show();

        }
    }

}
