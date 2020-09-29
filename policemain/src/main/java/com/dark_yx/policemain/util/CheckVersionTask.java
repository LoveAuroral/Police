package com.dark_yx.policemain.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.entity.UpdataInfo;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.SystemInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ligh on 2016/10/19 14:36
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

/*
 * 从服务器获取xml解析并进行比对版本号
 */
public class CheckVersionTask extends Thread {
    private Activity activity;
    private UpdataInfo info;
    private static final String TAG = "CheckVersionTask";
    private static final int INSTALL_SUCCESS = 1;//安装成功
    private static final int INSTALL_FAIL = 2;//安装失败
    private static final int UPDATA_CLIENT = 5;//更新软件
    private static final int GET_UNDATAINFO_ERROR = 6;//获取服务器更新信息失败
    private static final int DOWN_ERROR = 3;//下载失败
    private static final int UPDATE_DIALOG = 4;//更新进度条标题
    private String XML_PATH = DataUtil.getBassIp() + "/update/update.xml";//xml解析地址
    private String APK_PATH = DataUtil.getBassIp() + "/update/police.apk";//apk下载地址

//    private String APK_PATH1 = DataUtil.getBassIp() + "/update/police1.apk";//apk下载地址1(安装包)
//    private String APK_PATH2 = DataUtil.getBassIp() + "/update/police2.apk";//apk下载地址2(安装包)
//    private String APK_PATH3 = DataUtil.getBassIp() + "/update/police3.apk";//apk下载地址3(安装包)
//    private String APK_PATH4 = DataUtil.getBassIp() + "/update/police4.apk";//apk下载地址4(安装包)
//    private String APK_PATH5 = DataUtil.getBassIp() + "/update/police5.apk";//apk下载地址5(安装包)
//    private String APK_PATH6 = DataUtil.getBassIp() + "/update/police6.apk";//apk下载地址6(安装包)
//    private List<String> imeis = new ArrayList<>();
//    private String[] imeis1 = {"866215036588058", "866215036606041", "866215036585369", "864229034055129", "865584038278179", "886215036580063", "866215036599253", "866215036613047", "866215036613682", "860482034583160", "866215036599295", "869287034077522", "866215036610076", "866215036605316", "866215036597984", "866215036603964", "866215036598263", "866215036608518", "866215036615273", "866215036600333", "866215036609904", "866215036607692", "866215036590872", "866215036587217", "866215036611132", "868633037458071", "866215036582127", "866215036608393", "866215036618335", "866215036596309", "866215036581772", "866215036591144", "866215036617782", "864113033311545", "869982036622305", "866215036610555", "869377037971126", "869287038406073", "866215036595426", "866215036612965", "866215036610589", "866215036589221", "868608039561197", "866215036607379", "866215036608013", "866215036598743", "866215036590666", "866215036585310", "866215036585344", "866215036600879"};
//    private String[] imeis2 = {"866215036378047", "866642035543703", "866215036592100", "866215036594783", "866215036597265", "868405022804027", "867950048524779", "866215036615398", "866215036600580", "866215036579354", "866215036590427", "866215036583323", "866215036611843", "866215036597190", "866215036613112", "866215036581400", "866215036605597", "866215036608690", "866215036594148", "866215036597802", "866215036610167", "866215036598537", "866215036594239", "866215036605134", "866215036592852", "866215036618178", "866215036588926", "866215036598479", "866215036600119", "869287038406453", "866215036604020", "866215036599808", "869285031372771", "866215036613807", "866215037236772", "866215037242143", "866215037225304", "866215036590500", "866215036606413", "866215036603550", "866215036613583", "866215036591094", "866215036577796", "868632038877321", "866215036581657", "866215036604319", "866215036588397", "866215036593629", "869573032079234"};
//    private String[] imeis3 = {"866215036584271", "866215036600440", "866215036588314", "866215036591920", "863125038794526", "866215036593694", "866215036588959", "866215036590971", "866215036617758", "866215036591987", "866215036610548", "866215036612718", "866215036597539", "866215036585252", "866215036593777", "866215036614979", "866215036588066", "866215036577390", "866215036606744", "866215036597141", "866215036603634", "866215036602438", "358520085741788", "866215036587266", "866215036615679", "866215036582689", "866215036606249", "866215036585732", "866215036591466", "866215036605829", "866215036589387", "869377034205866", "866215036608302", "866215036588223", "866215036616016", "866215036586904", "866215036596655", "866215036583125", "866215036591706", "866215036588496", "866215036597810", "866215036613054", "866215036598883", "866215036613401", "866215036579982", "866215036609912", "866215036598388", "866215036612593", "866215036599352",};
//    private String[] imeis4 = {"869377035077926", "866215036612890", "868792041039408", "868792041026330", "868792041031140", "868792041022800", "868792041037873", "866215036595525", "866215036592514", "866215036607544", "866215036614565", "866215036577275", "866215036609995", "866215036603493", "866215036596549", "866215036610498", "866215036607668", "866215036600994", "864351042625758", "869287036051632", "866215036587894", "866215036594163", "866215036599246", "866215036612502", "862791036696657", "866215036611603", "866215036606934", "866215036584495", "866215036582739", "866215036613898", "866215036588140", "866215036584529", "866215036612304", "866215036582523", "866215036590153", "866215036587860", "866215036597000", "866215036582028", "866215036605886", "868792041037527", "868792040841127", "868792041016935", "868792040844360", "868792041021489", "866215036653639", "866215036609862", "866215036581566", "866215036611827", "866215036599352",};
//    private String[] imeis5 = {"866215036605100", "866215036593215", "866215036597570", "866215036580345", "866215036612395", "866158034209345", "866215036581517", "869287034060916", "866215036594544", "866215036603618", "866215036580196", "866215036613773", "866215036590773", "866215036611348", "866215036606736", "866215036614938", "866215036596481", "866215036583893", "866215036583380", "866215036617063", "866215036586441", "866215036603758", "866215036602388", "866215036611900", "868551036787599", "866215036577127", "866215036590492", "866215036588009", "866215036612932", "866215036370044", "866215036586821", "866215036613146", "864398040227962", "866215036591342", "868792041040919", "868792040785829", "868792040844022", "868792041035083", "868792040836663", "868792041022602", "868792041016851", "866215036596531", "866215036606629", "866215036600564", "866215036607007", "866215036595954", "868577039464654", "866215036595848", "866215036597844",};
//    private String[] imeis6 = {"866215036606108", "866215036600705", "866215036595616", "869573031100411", "866215036589304", "866215036593462", "866215036583562", "866215036591904", "868794037807731", "869287038679570", "866215036579297", "866215036583844", "866215036610894", "866215036588819", "863687046310009", "866215036606082", "865925049006440", "869573031538685", "866215036593256", "866215036615166", "866215036581335", "868382034555436", "866215036608203", "866215036601034", "866215036595582", "869287038896679", "866215036606223", "866215036615919", "868792040837398", "868792041031249", "868792041019400", "868792041022339", "868792041016745", "866158037461463"};

//    private List<String> list1 = new ArrayList<>();
//    private List<String> list2 = new ArrayList<>();
//    private List<String> list3 = new ArrayList<>();
//    private List<String> list4 = new ArrayList<>();
//    private List<String> list5 = new ArrayList<>();
//    private List<String> list6 = new ArrayList<>();
    private ProgressDialog pd;//下载进度条
    private int length;//下载长度,转换为M之后的大小

    public CheckVersionTask(Activity activity) {
        this.activity = activity;
    }

    public List<String> setList(List<String> list, String[] imeis) {
        for (int i = 0; i < imeis.length; i++) {
            list.add(imeis[i]);
        }
        return list;
    }

    public void run() {
        try {
            //服务器版本信息
            Log.i(TAG, "xml解析地址: " + XML_PATH);
            URL url = new URL(XML_PATH);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.connect();
            InputStream is = conn.getInputStream();
            info = getUpdataInfo(is);
            LogUtil.d("运行顺序：" + 1);
            Log.d(TAG, "本地版本:" + CommonMethod.getVersion(activity));
            Log.d(TAG, "升级版本:" + info.getVersion());
            if (!info.getVersion().equals(CommonMethod.getVersion(activity))) {
                Log.i(TAG, "版本号不同 ,提示用户升级 ");
                LogUtil.d("运行顺序：" + 2);
                Message msg = new Message();
                msg.what = UPDATA_CLIENT;
                handler.sendMessage(msg);
            } else {
                LogUtil.d("运行顺序：" + 3);
            }
        } catch (Exception e) {
            LogUtil.d("Exception ：" + e.toString());
            Message msg = new Message();
            msg.what = GET_UNDATAINFO_ERROR;
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }


    Handler handler;

    {
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATA_CLIENT:
                        //对话框通知用户升级程序
                        showUpdataDialog();
                        break;
                    case GET_UNDATAINFO_ERROR:
                        //服务器超时
                        Toast.makeText(activity, "获取服务器更新信息失败", Toast.LENGTH_LONG).show();
                        break;
                    case DOWN_ERROR:
                        //下载apk失败
                        Toast.makeText(activity, "下载新版本失败", Toast.LENGTH_LONG).show();
                        break;
                    case UPDATE_DIALOG:
                        pd.setTitle("正在下载更新:共" + length + "kb");
                        break;
                    case INSTALL_SUCCESS:
                        Toast.makeText(activity, "安装成功", Toast.LENGTH_LONG).show();
                        break;
                    case INSTALL_FAIL:
                        Toast.makeText(activity, "安装失败", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    /*
     * 弹出对话框通知用户更新程序
     */
    protected void showUpdataDialog() {
        LogUtil.d("运行顺序：" + 10);
        final AlertDialog.Builder builer = new AlertDialog.Builder(activity);
        builer.setTitle("系统升级提示");
        builer.setCancelable(false);
        builer.setMessage("尊敬的用户您好！系统检测到最新版本，请立即升级，否则将影响到您的正常使用。请按提示进行安装，感谢您的支持！");
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
                DataUtil.setLogin(activity, false);
                downLoadApk();
            }
        });
        builer.create();
        builer.show();
    }

    /*
     * 从服务器中下载APK
     */
    private void downLoadApk() {
        pd = new ProgressDialog(activity);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("正在下载更新");
        pd.setCancelable(false);
        pd.show();
        Toast.makeText(activity, "正在下载新版本", Toast.LENGTH_LONG).show();
        new Thread() {
            @Override
            public void run() {
                try {
                    getFileFromServer();
                    LogUtil.d("运行顺序：" + 5);
                } catch (Exception e) {
                    LogUtil.d("运行顺序：" + 6);
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    pd.dismiss(); //结束掉进度条对话框
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 安装apk
     *
     * @param file 安装apk
     */
    private void installApk(File file) {
        if (activity == null || !file.exists()) {
            return;
        }
        LogUtil.d("安装apk>>>" + "正在安装" + file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            LogUtil.d("安装apk--->" + "7.0以上，正在安装apk...");
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(activity,
                    activity.getApplicationInfo().packageName + ".fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            LogUtil.d("安装apk--->" + "7.0以下，正在安装apk...");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }

    /**
     * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
     *
     * @param is 输入流
     * @return
     */
    public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");//设置解析的数据源
        int type = parser.getEventType();
        UpdataInfo info = new UpdataInfo();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        info.setVersion(parser.nextText()); //获取版本号
                    } else if ("description".equals(parser.getName())) {
                        info.setDescription(parser.nextText()); //获取该文件的信息
                    }
                    break;
            }
            type = parser.next();
        }
        Log.d(TAG, "服务器版本: " + info.getVersion());
        return info;
    }

    private void getFileFromServer() throws Exception {
        LogUtil.d("运行顺序：" + 7);
//        ComponentName admin = new ComponentName(x.app(), DeviceReceiver.class);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            String APK_PATH = null;
//            if (setList(list1, imeis1).contains(SystemInfo.GetIMEI(activity))) {
////                APK_PATH = APK_PATH1;
////            } else if (setList(list2, imeis2).contains(SystemInfo.GetIMEI(activity))) {
////                APK_PATH = APK_PATH2;
////            } else if (setList(list3, imeis3).contains(SystemInfo.GetIMEI(activity))) {
////                APK_PATH = APK_PATH3;
////            } else if (setList(list4, imeis4).contains(SystemInfo.GetIMEI(activity))) {
////                APK_PATH = APK_PATH4;
////            } else if (setList(list5, imeis5).contains(SystemInfo.GetIMEI(activity))) {
////                APK_PATH = APK_PATH5;
////            } else if (setList(list6, imeis6).contains(SystemInfo.GetIMEI(activity))) {
////                APK_PATH = APK_PATH6;
////            }
            LogUtil.d("apk下载地址:" + APK_PATH);
            URL url = new URL(APK_PATH);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.connect();

            //获取到文件的大小
            length = conn.getContentLength() / 1024;
            Message msg = new Message();
            msg.what = UPDATE_DIALOG;
            handler.sendMessage(msg);
            pd.setMax(length);

            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "dandian.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            LogUtil.d("运行顺序：" + 8);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
//                获取当前下载量
                pd.setProgress(total / 1024 / 1024);
            }
            fos.close();
            bis.close();
            is.close();
//            PhoneInterfaceUtil.installApp(admin,file.getAbsolutePath());
            Log.d(TAG, "path3" + file.getAbsolutePath());
            installApk(file);

            pd.dismiss(); //结束掉进度条对话框
        }
    }
}