package com.example.mybrowser.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybrowser.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@ContentView(R.layout.activity_main)
public class WebViewActivity extends BaseActivity {
    @ViewInject(R.id.wv_myWebView)
    private WebView webView;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    private ProgressDialog mDialog;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        String url = getIntent().getStringExtra("url");
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.requestFocus();
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public class MyWebViewClient extends WebViewClient {
        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。
        public boolean shouldOverviewUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgressDialog();
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            closeProgressDialog();
            super.onPageFinished(view, url);
        }

    }


    /**
     * 下载文件监听
     */
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Log.d("url", url);
            String s = "UTF-8''";
            try {
                if (contentDisposition.contains(s)) {
                    name = URLDecoder.decode(contentDisposition.split("=")[1].replace(s, ""), "UTF-8");
                } else {
                    name = URLDecoder.decode(contentDisposition.split("=")[1], "UTF-8");
                }
                Log.d("MyWebView", name);

                View view = LayoutInflater.from(WebViewActivity.this).inflate(R.layout.dialog_down, null);
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
//                TextView tvLength = (TextView) view.findViewById(R.id.tv_length);
                tvName.setText(name);
//                tvLength.setText(DownLoadLength.getLength(contentLength));

                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                builder.setView(view)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                    Toast t = Toast.makeText(WebViewActivity.this, "需要SD卡。", Toast.LENGTH_LONG);
                                    t.setGravity(Gravity.CENTER, 0, 0);
                                    t.show();
                                    return;
                                } else {
                                    File file = new File(Environment.getExternalStorageDirectory() + "/Police/Download");
                                    if (!file.exists()) {
                                        file.mkdirs();
                                    }
                                    String fileName = file.getAbsolutePath() + "/" + name;
                                    Log.d("MyWebView", fileName);
                                    if (new File(fileName).exists()) {
                                        Toast.makeText(WebViewActivity.this, "文件已存在Police/Download目录下", Toast.LENGTH_LONG).show();
                                        openDialog(fileName);
                                    } else {
                                        DownLoadFile(url, fileName);
                                    }
                                }
                            }
                        })
                        .show();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 用xutils下载文件
     *
     * @param url
     * @param filePath
     */
    public void DownLoadFile(String url, final String filePath) {
        Log.d("MyWebView1", url);
        final RequestParams requestParamsparams = new RequestParams(url);
        //文件保存在本地的路径
        requestParamsparams.setSaveFilePath(filePath);
        requestParamsparams.setAutoResume(true);
        x.http().get(requestParamsparams, new Callback.CacheCallback<File>() {
            @Override
            public void onSuccess(File f) {
                Log.d("MyWebView", f.getAbsolutePath());
                Toast.makeText(WebViewActivity.this, "文件已下载至Police/Download", Toast.LENGTH_LONG).show();
                openDialog(filePath);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Log.d("MyWebView1", throwable.toString());

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(File s) {
                return false;
            }
        });
    }

    /**
     * 显示对话框
     */
    private void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(WebViewActivity.this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            mDialog.setMessage("正在加载 ，请等待...");
            mDialog.setIndeterminate(false);//设置进度条是否为不明确
            mDialog.setCancelable(true);//设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mDialog = null;
                }
            });
            mDialog.show();

        }
    }

    /**
     * 隐藏对话框
     */
    private void closeProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 下载后打开文件
     *
     * @param file
     * @return
     */
    public Intent getFileIntent(File file) {
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    /**
     * 获得文件类型
     *
     * @param f
     * @return
     */
    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
      /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
        if (end.equals("pdf")) {
            type = "application/pdf";//
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("apk")) {
            // android.permission.INSTALL_PACKAGES  安装软件的权限
            type = "application/vnd.android.package-archive";
        } else if (end.equals("pptx") || end.equals("ppt")) {
            type = "application/vnd.ms-powerpoint";
        } else if (end.equals("docx") || end.equals("doc")) {
            type = "application/vnd.ms-word";
        } else if (end.equals("xlsx") || end.equals("xls")) {
            type = "application/vnd.ms-excel";
        } else {
            //如果无法直接打开，就跳出软件列表给用户选择
            type = "*/*";
        }
        return type;
    }

    private void openDialog(final String file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
        builder.setMessage("名称:" + name)
                .setTitle("是否打开")
                .setCancelable(false)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = getFileIntent(new File(file));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(WebViewActivity.this, "请先安装相关应用", Toast.LENGTH_LONG).show();
                        }
                    }
                }).show();
    }

}
