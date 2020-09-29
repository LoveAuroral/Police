package com.dark_yx.policemain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dark_yx.policemain.adapter.UnreadAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.util.L;
import com.dark_yx.policemaincommon.Models.NoticeInfo;
import com.dark_yx.policemaincommon.Util.DbHelp;
import com.dark_yx.policemaincommon.Util.HttpCallBackEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 未读公告
 * Created by dark_yx-i on 2016-06-06.
 */
public class UnreadNoticeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lvUnread;
    private UnreadAdapter adapter;
    private DbHelp db;
    private String packegName;
    private String className;
    private NoticeInfo noticeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unreadnotice);
        init();
    }

    private void init() {
        db = new DbHelp();
        lvUnread = (ListView) findViewById(R.id.lv_unread);
        if (db.getUnreadNotice().size() != 0) {
            adapter = new UnreadAdapter(this, db.getUnreadNotice());
            lvUnread.setAdapter(adapter);
            lvUnread.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        noticeInfo = db.getUnreadNotice().get(position);
        updateView();

        if (!noticeInfo.isSend()) {//消息已读未发送则请求
            DbHelp help = new DbHelp();
            HttpCallBackEvent feedBackEvent = new feedBackEvent();//公告已读反馈
            Map<String, Object> params = new HashMap<>();
//            params.put("userName", help.getUser().getUserName());
            params.put("readId", noticeInfo.getId());
//            HttpHelp.Get(feedBackEvent, help.getBassIP().GetIp() + "Api/SetNewsStateByApp", params);
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
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);//标记activity为程序的开始页面
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                Intent intent = new Intent();
                intent.setClassName(packegName, className);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "当前还没有安装此应用", Toast.LENGTH_SHORT).show();
                }

            } else if (noticeInfo.getType().equals("message")) {
                DbHelp help = new DbHelp();
//                String ip = help.getBassIP().GetIp();
//                String url = ip + "api/notice?noticeId=" + noticeInfo.getId();
                Intent intent = new Intent();
                intent.setAction("com.lgh.mybrowser");
//                intent.putExtra("url", url);
                startActivity(intent);

            }
        }

    }

    public void updateView() {
        noticeInfo.setRead(true);
        db.changeNotices(noticeInfo);
        init();
        adapter.notifyDataSetChanged();
    }

    private class feedBackEvent implements HttpCallBackEvent {
        @Override
        public void Excute(String result) throws JSONException {
            L.d(result);
            JSONObject jsonObject = new JSONObject(result);
//            int errorCode = Integer.parseInt(jsonObject.getString(HttpHelp.ERRORCODE));
            String errorMsg = jsonObject.getString("errormsg");
//            switch (errorCode) {
//                case 0:
//                    Toast.makeText(UnreadNoticeActivity.this, "公告反馈成功！", Toast.LENGTH_SHORT).show();
//                    noticeInfo.setSend(true);
//                    db.changeNotices(noticeInfo);
//                    break;
//                case 1:
//                    L.d(errorMsg);
//                    break;
//                case 2:
//                    L.d(errorMsg);
//                    break;
//                case 3:
//                    L.d(errorMsg);
//                    break;
//                case 4:
//                    L.d(errorMsg);
//                    break;
//            }
        }

        @Override
        public void OnError(Throwable ex) throws JSONException {
            Toast.makeText(UnreadNoticeActivity.this, "公告消息已读反馈失败！", Toast.LENGTH_SHORT).show();

        }
    }
}
