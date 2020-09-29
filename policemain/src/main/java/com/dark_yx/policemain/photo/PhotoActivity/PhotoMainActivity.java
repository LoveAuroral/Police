package com.dark_yx.policemain.photo.PhotoActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.photo.Adpter.PhotoTagAdapter;
import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.uploadlib.beans.DataDicBean;
import com.dark_yx.uploadlib.contract.UploadContract;
import com.dark_yx.uploadlib.presenter.UploadPresenter;
import com.yanzhenjie.permission.FileProvider;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PhotoMainActivity extends AppCompatActivity implements UploadContract.View {
    private ProgressBar processBar;
    private String fileName, filePath, userName, uploadMode;
    private TextView tvUserName, textView;
    private RadioGroup group;
    private static final String TAG = "MainActivity";
    private Button btnTakePhoto;
    UploadContract.Presenter presenter;
    List<DataDicBean.ResultBean.ItemsBean> tags;
    PhotoTagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_main);
        init();
    }

    private void init() {
//      userName = UserUtil.getUserName();
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserName.setText("当前用户: " + DataUtil.getAccount().getUserName());
        processBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.textView);
        btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        presenter = new UploadPresenter(this);
        presenter.getData("Forensics.Tag", DataUtil.getToken());
    }

    public void openCamera() {
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri mImageCaptureUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
                mImageCaptureUri= FileProvider.getUriForFile(this,
                        getApplicationInfo().packageName + ".fileprovider",getFile());
            }else{
                mImageCaptureUri = Uri.fromFile(getFile());
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(this, "sdcard不可用", Toast.LENGTH_SHORT).show();
        }

    }

    private File getFile() {
        filePath = Environment.getExternalStorageDirectory().getPath() + "/Police/Image";
        File path = new File(filePath);
        fileName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        Log.d(TAG, "拍照之前文件名: " + fileName);
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                showDia();
                btnTakePhoto.setClickable(false);
            }
        }
    }

    private void showDia() {
        LayoutInflater layoutInflater = LayoutInflater.from(PhotoMainActivity.this);
        View view = LayoutInflater.from(PhotoMainActivity.this).inflate(R.layout.photo_tag_layout, null);
        ListView v = (ListView) view.findViewById(R.id.mListView);
        v.setAdapter(tagAdapter);
//        group.setOnCheckedChangeListener(new MyListener());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请选择上传场景");
        builder.setView(view);
        builder.setPositiveButton("确定", okListener);
        builder.setCancelable(false);
        builder.show();
    }

    DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DataDicBean.ResultBean.ItemsBean itemsBean = tagAdapter.GetItem();
            if (itemsBean != null) {
                processBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
//                UpLoadSuccessEvent event = new UpLoadSuccessEvent(MainActivity.this);
                Log.d(TAG, "文件地址: " + filePath + "文件名称: " + fileName + "上传模式: " + uploadMode);
//                event.upload(userName, filePath, fileName, uploadMode, 0);
                presenter.upload(filePath + "/" + fileName, itemsBean.getValue(), 2, DataUtil.getToken());
            } else {
                Toast.makeText(PhotoMainActivity.this, "请先选择上传场景", Toast.LENGTH_SHORT).show();
                showDia();
            }
        }
    };

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
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void getDataDicBeanSuccess(DataDicBean dicBean) {
        tags = dicBean.getResult().getItems();
        tagAdapter = new PhotoTagAdapter(this, tags);
    }

    @Override
    public void uoloadSuccess() {
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        if (filePath != null && filePath.length() > 0) {
            File photoFile = new File(filePath);
            if (photoFile.exists()) {
                deleteFile(photoFile);
            }
            this.finish();
        }
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
