package com.dark_yx.policemain.audio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.uploadlib.beans.DataDicBean;
import com.dark_yx.uploadlib.contract.UploadContract;
import com.dark_yx.uploadlib.presenter.UploadPresenter;

import java.io.File;
import java.util.List;

public class AudioMainActivity extends AppCompatActivity implements UploadContract.View {
    private static final String TAG = "MainActivity";
    private TextView tv, txtTips, tvUserName, tvUpload;
    private MediaRecorder mr;
    private Handler mHandelr = new Handler();
    private MyTask m;
    private int time = 0;
    boolean isStart = true;
    private View view;
    private Button bt_start;
    private ProgressBar pro;
    private String uploadMode, fileName, filepath = Environment.getExternalStorageDirectory() + "/Police/Audio/";
    private UploadContract.Presenter presenter;

    List<DataDicBean.ResultBean.ItemsBean> tags;
    AudioAdapter tagAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_main);
        init();
    }

    private void init() {
        tv = (TextView) findViewById(R.id.txtSecond);
        tvUpload = (TextView) findViewById(R.id.textView);
        pro = (ProgressBar) findViewById(R.id.progress_bar);
        bt_start = (Button) findViewById(R.id.btnStart);
        txtTips = (TextView) findViewById(R.id.txtTips);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserName.setText("当前用户: " + DataUtil.getAccount().getUserName());
        mr = new MediaRecorder();
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        presenter = new UploadPresenter(this);
        presenter.getData("Forensics.Tag", DataUtil.getToken());

    }

    public void start() {
        if (isStart) {
            try {
                tv.setVisibility(View.VISIBLE);
                tv.setText("0秒");
                txtTips.setText("正在录音.....");
                // 重新设置
                mr.reset();
                // 指定声音来源-->>麦克风
                mr.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 指定文件输出的格式
                mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                // 指定音频的编码格式
                mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                // 存放的文件
                fileName = System.currentTimeMillis() + ".mp3";
                File pathFile = new File(filepath);
                if (!pathFile.exists()) {
                    pathFile.mkdirs();
                }
                File file = new File(filepath, fileName);
                // 指定文件输出的路径
                mr.setOutputFile(file.getAbsolutePath());
                // 准备
                mr.prepare();
                // 开始刻录
                mr.start();
                m = new MyTask();
                mHandelr.post(m);
                bt_start.setText("停止");
                isStart = false;
            } catch (Exception e) {
            }
        } else {
            stop();
        }
    }

    @Override
    public void getDataDicBeanSuccess(DataDicBean dicBean) {
        Log.d(TAG, dicBean.toString());
        tags = dicBean.getResult().getItems();
        tagAdapter = new AudioAdapter(this, tags);
    }

    @Override
    public void uoloadSuccess() {
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        deleteFile();
        this.finish();
    }

    @Override
    public void uoloadFail(String error) {
        Toast.makeText(this, "上传失败:" + error, Toast.LENGTH_SHORT).show();
        deleteFile();
    }

    private class MyTask implements Runnable {
        @Override
        public void run() {
            mHandelr.postDelayed(this, 1000);
            time++;
            tv.setText(time + "秒");
        }
    }

    public void stop() {
        tv.setVisibility(View.GONE);
        isStart = true;
        mr.stop();
        mHandelr.removeCallbacks(m);
        txtTips.setText("点击开始录音");
        bt_start.setText("开始");
        time = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(AudioMainActivity.this);
        builder.setTitle("确认上传")
                .setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFile();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chooseMode();
                    }
                }).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mr != null) {
                try {
                    stop();
                } catch (Exception e) {

                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void deleteFile() {
        File file = new File(filepath, fileName);
        if (file.exists()) {
            file.delete();
            Toast.makeText(this, "删除文件成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseMode() {
        bt_start.setClickable(false);
        view = LayoutInflater.from(AudioMainActivity.this).inflate(R.layout.audio_tag_layout, null);
        ListView v = (ListView) view.findViewById(R.id.mListView);
        v.setAdapter(tagAdapter);
//        group.setOnCheckedChangeListener(new MyListener());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请选择上传场景");
        builder.setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataDicBean.ResultBean.ItemsBean itemsBean = tagAdapter.GetItem();
                if (itemsBean != null) {
                    pro.setVisibility(View.VISIBLE);
                    tvUpload.setVisibility(View.VISIBLE);
                    Log.d("MainActivity", fileName);
                    presenter.upload(filepath + "/" + fileName, itemsBean.getValue(), 0, DataUtil.getToken());
                } else {
                    Toast.makeText(AudioMainActivity.this, "请先选择上传场景", Toast.LENGTH_SHORT).show();
                    chooseMode();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    private class MyListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton button = (RadioButton) view.findViewById(group.getCheckedRadioButtonId());
            uploadMode = button.getText().toString();
            Log.d(TAG, "上传模式: " + uploadMode);
        }
    }
}
