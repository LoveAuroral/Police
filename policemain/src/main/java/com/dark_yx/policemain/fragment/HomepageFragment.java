package com.dark_yx.policemain.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.activity.SearchActivity;
import com.dark_yx.policemain.adapter.AppsAdapter;
import com.dark_yx.policemain.audio.AudioMainActivity;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.common.MyGridView;
import com.dark_yx.policemain.fragment.bean.AppWhiteListInput;
import com.dark_yx.policemain.fragment.contract.HomeContract;
import com.dark_yx.policemain.fragment.presenter.HomePresenter;
import com.dark_yx.policemain.nfcDemo.NfcMainActivity;
import com.dark_yx.policemain.phone.PhoneActivity.PhoneMainActivity;
import com.dark_yx.policemain.photo.PhotoActivity.PhotoMainActivity;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.sysVideo.VideoMainActivity;
import com.dark_yx.policemain.util.DownloadApkUtil;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemain.chat.view.chatui.ui.activity.WebActivity;
import com.dark_yx.policemaincommon.Models.AppInfo;
import com.dark_yx.policemaincommon.Models.AppWhiteListResult;
import com.dark_yx.policemaincommon.Models.NoticeBean;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.DbHelp;
import com.dark_yx.policemaincommon.Util.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.List;


/**
 * Created by lmp on 2016/4/29.
 */
public class HomepageFragment extends BaseFragment implements View.OnClickListener, HomeContract.View {
    private MyGridView mGrid;//网格视图
    private View view;
    private LinearLayout phone, photoupload, sysvideocamera, audioupload, nfcDemo;
    private TextView tvAbstract;
    private TextView tvTime;
    private HomeReceiver noticeReceiver;
    private DbHelp db;
    private List<AppInfo> appInfos;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉布局
    private boolean isRefreshing = false;
    private HomeContract.Presenter presenter;
    private AppsAdapter adapter;
    private static final File FILE = new File(Environment.getExternalStorageDirectory() + "/Police/token.txt");

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_homepage, null);
        return view;
    }

    @Override
    public void initData() {
        presenter = new HomePresenter(this);
        initView();
        initApp();//初始化
        EventBus.getDefault().register(this);
    }

    private void initView() {
        phone = (LinearLayout) view.findViewById(R.id.phone);
        photoupload = (LinearLayout) view.findViewById(R.id.photoupload);
        sysvideocamera = (LinearLayout) view.findViewById(R.id.sysvideocamera);
        audioupload = (LinearLayout) view.findViewById(R.id.audioupload);
        nfcDemo = (LinearLayout) view.findViewById(R.id.nfcDemo);
        phone.setOnClickListener(this);
        photoupload.setOnClickListener(this);
        sysvideocamera.setOnClickListener(this);
        audioupload.setOnClickListener(this);
        nfcDemo.setOnClickListener(this);

        mGrid = (MyGridView) view.findViewById(R.id.apps_lists);
        tvAbstract = (TextView) view.findViewById(R.id.tv_message_abstract);
        tvTime = (TextView) view.findViewById(R.id.tv_message_time);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(myRefreshListener);

        noticeReceiver = new HomeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        getContext().registerReceiver(noticeReceiver, filter);

        initNotice();
    }

    /**
     * 系统设置安装未知来源权限
     */
    public void setInstallPermission(AppInfo info) {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先判断是否有安装未知来源应用的权限
            haveInstallPermission = getActivity().getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                //弹框提示用户手动打开
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("安装权限")
                        .setMessage("需要打开允许来自此来源，请去设置中开启此权限")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    //此方法需要API>=26才能使用
                                    toInstallPermissionSettingIntent();
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();//显示dialog对话框
                return;
            } else {
                DownloadApkUtil util = new DownloadApkUtil(getActivity(), info);
                util.showDownloadDialog();
            }
        }
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        getActivity().startActivityForResult(intent, 123);
    }

    private void initNotice() {
        NoticeBean notice = DataUtil.getNotice();
        tvAbstract.setText(notice.getTitle());
        tvTime.setText(notice.getTime());
    }

    private String getAppVersion(String pkg) {
        PackageManager pm = getContext().getPackageManager();
        PackageInfo packageInfo = null;
        String version = null;
        try {
            packageInfo = pm.getPackageInfo(pkg, 0);
            version = packageInfo.versionName;
            LogUtil.d(pkg + " version name: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "";
        }
        return version;
    }


    private void initApp() {
        db = new DbHelp();
        appInfos = db.getAppInfos();
        if (appInfos != null && appInfos.size() > 0) {
            LogUtil.d(appInfos.toString());
            setAdapter();
        } else getData();
    }


    /**
     * 下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener myRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (!isRefreshing) {
                isRefreshing = true;
                getData();
            }
        }
    };

    private void getData() {
        presenter.getAppWhiteList(new AppWhiteListInput(999));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.phone:
                startActivity(new Intent(getContext(), PhoneMainActivity.class));
                break;
            case R.id.photoupload:
                if (DataUtil.getIsCameraDisable(getContext())) {
                    Toast.makeText(getContext(), "照相功能已被禁用", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), PhotoMainActivity.class));
                }
                break;
            case R.id.sysvideocamera:
                if (DataUtil.getIsCameraDisable(getContext())) {
                    Toast.makeText(getContext(), "照相功能已被禁用", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), VideoMainActivity.class));
                }
                break;
            case R.id.audioupload:
                if (DataUtil.getIsAudioDisable(getContext())) {
                    Toast.makeText(getContext(), "录音功能已被禁用", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), AudioMainActivity.class));
                }
                break;
            case R.id.nfcDemo:
                startActivity(new Intent(getContext(), NfcMainActivity.class));
                break;
        }
    }

    @Override
    public void showDialog() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissDialog() {
        isRefreshing = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getAppWhiteListSuccess(AppWhiteListResult result) {
        List<AppInfo> items = result.getResult().getItems();
        db.updateAppInfo(items);
        appInfos = items;

        LogUtil.d(appInfos.toString());

        setAdapter();
    }

    private void setAdapter() {
        adapter = new AppsAdapter(getActivity(), appInfos);
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(listener);
    }

    @Override
    public void showError(String err) {
        Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
    }

    public class HomeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("app: " + intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
                initApp();
            }
        }

    }


    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(noticeReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(NoticeBean bean) {
        tvAbstract.setText(bean.getTitle());
        tvTime.setText(bean.getTime());
        DataUtil.setNoticeRead(false);
        initApp();
    }

    //应用程序子应用点击事件
    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (appInfos != null) {
                if (position != appInfos.size()) {
                    AppInfo info = appInfos.get(position);
                    openApp(info);
//                    if (isLatestVersion(info.getPackageName(), info.getVersion())) {
//                        openApp(info);
//                    }else{
//                        showUpdateDialog(info);
//                    }
                } else {
                    if (!DataUtil.isNoticeRead()) {
                        DataUtil.setNoticeRead(true);
                        initApp();
                    }
                    openNotice(getContext());
                }
            }
        }

    };

    private void showUpdateDialog(final AppInfo info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setCancelable(false);
        builder.setTitle("更新提示")
                .setMessage("系统检测到" + info.getName() + "非最新版本，是否立即更新？")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        download1(info);
                    }
                }).show();
    }


    private boolean isLatestVersion(String packageName, String version) {
        return TextUtils.isEmpty(getAppVersion(packageName)) || version.equals(getAppVersion(packageName));
    }


    /**
     * 打开隐藏的及过滤后的app,先根据包名和类名打开,如果打开失败,
     * 再o根据包名直接打开.本来打算直接通过包名打开,但是隐藏的app不能打开,
     * 并且这种方式无法获取到打开的类名.
     *
     * @param info app
     */
    private void openApp(AppInfo info) {
        LogUtil.d("包名: " + info.getPackageName());
        Intent i1 = new Intent();
        i1.putExtra("token", DataUtil.getToken());
        LogUtil.d("poliemainToken" + DataUtil.getToken());
        i1.putExtra("name", DataUtil.getAccount().getUserName());
        i1.putExtra("base_IP", DataUtil.getBassIp());
        FileUtil.deleteFile(FILE);
//        FileUtil.writeToken(DataUtil.getToken());
        i1.setClassName(info.getPackageName(), info.getPackageName() + ".Activity.PhoneMainActivity");
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            getContext().startActivity(i1);
            LogUtil.d("运行顺序：" + "第一步");
        } catch (Exception e) {
            i1 = getContext().getPackageManager().getLaunchIntentForPackage(info.getPackageName());
            LogUtil.d("运行顺序：" + "第二步");
        }
        try {
            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            getContext().startActivity(i1);
            LogUtil.d("运行顺序：" + "第三步");
        } catch (Exception ex) {
            LogUtil.d("Exception ex is ?" + ex.toString());
            download(info);
            LogUtil.d("运行顺序：" + "第四步");
        }
    }

    private void download(AppInfo info) {
        setInstallPermission(info);
    }

    private void download1(AppInfo info) {
        ComponentName componentName = new ComponentName(getContext(), DeviceReceiver.class);
        PhoneInterfaceUtil.uninstallPackage(componentName, info.getPackageName());
        DownloadApkUtil util = new DownloadApkUtil(getActivity(), info);
        util.downLoadApk();
    }

    /**
     * 打开公告
     */
    public static void openNotice(Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        User account = DataUtil.getAccount();
        intent.putExtra(WebActivity.URL, DataUtil.getBassIp() + "/app/index?userName=" + account.getUserName() + "&password=" + account.getUserPwd() + "&urlCode=anno");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }
}
