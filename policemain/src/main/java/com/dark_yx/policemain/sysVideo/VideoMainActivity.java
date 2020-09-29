package com.dark_yx.policemain.sysVideo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.uploadlib.beans.DataDicBean;
import com.dark_yx.uploadlib.contract.UploadContract;
import com.dark_yx.uploadlib.presenter.UploadPresenter;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class VideoMainActivity extends AppCompatActivity implements SurfaceHolder.Callback, UploadContract.View {
    private static final String TAG = "MainActivity";
    private SurfaceView mSurfaceview;
    private Button mBtnStartStop;
    private Button mBtnPlay;
    private int mStartedFlg = 0;//是否正在录像
    private boolean mIsPlay = false;//是否正在播放录像
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private ImageView mImageView;
    private Camera camera;
    private MediaPlayer mediaPlayer;
    private String path;
    private TextView textView;
    private int text = 0;

    private UploadContract.Presenter presenter;

    List<DataDicBean.ResultBean.ItemsBean> tags;
    SysVideoApater tagAdapter;

    private android.os.Handler handler = new android.os.Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            text++;
            textView.setText("计时开始：" + text
            );
            handler.postDelayed(this, 1000);
        }
    };
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_main);
        mSurfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        mImageView = (ImageView) findViewById(R.id.imageview);
        mBtnStartStop = (Button) findViewById(R.id.btnStartStop);
        mBtnPlay = (Button) findViewById(R.id.btnPlayVideo);
        textView = (TextView) findViewById(R.id.text);
        presenter = new UploadPresenter(this);
        presenter.getData("Forensics.Tag", DataUtil.getToken());

        mBtnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsPlay) {
                    if (mediaPlayer != null) {
                        mIsPlay = false;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
                if (mStartedFlg == 0) {
                    handler.postDelayed(runnable, 1000);
                    mImageView.setVisibility(View.GONE);
                    if (mRecorder == null) {
                        mRecorder = new MediaRecorder();
                    }

                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    if (camera != null) {
                        camera.setDisplayOrientation(90);
                        camera.unlock();
                        mRecorder.setCamera(camera);
                    }

                    try {
                        // 这两项需要放在setOutputFormat之前
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                        // Set output file format
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

                        // 这两项需要放在setOutputFormat之后
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

                        mRecorder.setVideoSize(640, 480);
//                        mRecorder.setVideoFrameRate(20);
                        mRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
                        mRecorder.setOrientationHint(90);
                        //设置记录会话的最大持续时间（毫秒）
//                        mRecorder.setMaxDuration(30 * 1000);
                        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

                        path = Environment.getExternalStorageDirectory() + "/Police/Audio/";
                        if (path != null) {
                            File dir = new File(path);
                            if (!dir.exists()) {
                                dir.mkdir();
                            }
                            filePath = path = dir + "/" + getDate() + ".mp4";
                            mRecorder.setOutputFile(path);
                            mRecorder.prepare();
                            mRecorder.start();
                            mStartedFlg = 1;
                            mBtnStartStop.setText("停止");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (mStartedFlg == 1) {
                    try {
                        handler.removeCallbacks(runnable);
                        mRecorder.stop();
                        mRecorder.reset();
                        mRecorder.release();
                        mRecorder = null;
                        mBtnStartStop.setText("上传");
                        if (camera != null) {
                            camera.release();
                            camera = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mStartedFlg = 2;
                } else if (mStartedFlg == 2) {
                    choseMode();
//                    Toast.makeText(MainActivity.this,"开始上传",0).show();
                }
            }
        });

        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsPlay = true;
                mImageView.setVisibility(View.GONE);
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.reset();
                Uri uri = Uri.parse(path);
                mediaPlayer = MediaPlayer.create(VideoMainActivity.this, uri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(mSurfaceHolder);
                try {
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });

        SurfaceHolder holder = mSurfaceview.getHolder();
        holder.addCallback(this);
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void choseMode() {
        mBtnStartStop.setClickable(false);
        View view = LayoutInflater.from(VideoMainActivity.this).inflate(R.layout.sysvideo_tag_layout, null);
        ListView v = (ListView) view.findViewById(R.id.mListView);
        v.setAdapter(tagAdapter);
//        group.setOnCheckedChangeListener(new MyListener());
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoMainActivity.this);
        builder.setMessage("请选择上传场景");
        builder.setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataDicBean.ResultBean.ItemsBean itemsBean = tagAdapter.GetItem();
                if (itemsBean != null) {
                    ProgressDialog progressDialog = new ProgressDialog(VideoMainActivity.this);
                    progressDialog.setTitle("加载中");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    presenter.upload(filePath, itemsBean.getValue(), 1, DataUtil.getToken());
                } else {
                    Toast.makeText(VideoMainActivity.this, "请先选择上传场景", Toast.LENGTH_SHORT).show();
                    choseMode();
                }
                //deleteVideo();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStartedFlg == 0) {
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒

        String date = "" + year + (month + 1) + day + hour + minute + second;
        Log.d(TAG, "date:" + date);

        return date;
    }

    /**
     * 获取SD path
     *
     * @return
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }

        return null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceview = null;
        mSurfaceHolder = null;
        handler.removeCallbacks(runnable);
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
            Log.d(TAG, "surfaceDestroyed release mRecorder");
        }
        if (camera != null) {
            camera.release();
            camera = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (filePath != null && filePath.length() > 0) {
            File photoFile = new File(filePath);
            if (photoFile.exists()) {
                deleteFile(photoFile);
            }
        }
        super.onBackPressed();
    }

    @Override
    public void getDataDicBeanSuccess(DataDicBean dicBean) {
        tags = dicBean.getResult().getItems();
        tagAdapter = new SysVideoApater(this, tags);
    }

    @Override
    public void uoloadSuccess() {
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        if (filePath != null && filePath.length() > 0) {
            File photoFile = new File(filePath);
            if (photoFile.exists()) {
                deleteFile(photoFile);
            }
        }
        this.finish();
    }

    @Override
    public void uoloadFail(String error) {
        Toast.makeText(this, "上传失败:" + error, Toast.LENGTH_SHORT).show();
        if (filePath != null && filePath.length() > 0) {
            File photoFile = new File(filePath);
            if (photoFile.exists()) {
                deleteFile(photoFile);
            }
        }
    }

    private void deleteFile(File file) {
        LogUtil.d("filePath； " + filePath);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete();
                Toast.makeText(this, "文件删除成功", Toast.LENGTH_SHORT).show();
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                if (files == null || files.length == 0) {
                    file.delete();
                    return;
                }
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            scanFileAsync();
        } else {
            return;
        }
    }

    //刷新缩略图
    private void scanFileAsync() {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        sendBroadcast(scanIntent);
    }
}
