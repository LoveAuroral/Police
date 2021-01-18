package com.dark_yx.policemain.util;

import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.api.ApiFactory;
import com.dark_yx.policemain.chat.database.ChatDb;
import com.dark_yx.policemaincommon.Models.PrivatePhoneBean;
import com.dark_yx.policemaincommon.Models.PrivatePhoneWhiteResult;
import com.dark_yx.policemaincommon.Models.UserTreeResult;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.DbHelp;
import com.dark_yx.policemaincommon.Util.PrivateNumberUtil;
import com.dark_yx.policemaincommon.Util.UserTreeUtil;
import com.dark_yx.policemaincommon.api.MyCallBack;
import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by Ligh on 2018/1/8 11:21
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class WhiteListUtil {
    private final ComponentName mAdminName;
    private Context context;
    private DbHelp dbHelp;
    private boolean isShow;
    private ChatDb chatDb;

    public WhiteListUtil(Context context, boolean isShow) {
        this.context = context;
        dbHelp = new DbHelp();
        chatDb = new ChatDb();
        this.isShow = isShow;
        mAdminName = new ComponentName(context, DeviceReceiver.class);
    }


    public void getData() {
//        getPublicPhoneList();
//        getPrivatePhoneList();
    }

    public void getData2() {
        getPublicPhoneList();
        getPrivatePhoneList();
    }

    private void getPrivatePhoneList() {
        ApiFactory.getService().getPrivatePhoneWhite(DataUtil.getToken()).enqueue(new MyCallBack<PrivatePhoneWhiteResult>() {
            @Override
            public void onSuc(Response<PrivatePhoneWhiteResult> response) {
                PrivatePhoneWhiteResult body = response.body();
                String privatePhone = null;
                if (DataUtil.phoneNub2.size() > 0 && DataUtil.phoneNub2 != null) {
                    DataUtil.phoneNub2.clear();
                }
                if (body.getResult().getItems() != null && body.getResult().getItems().size() > 0) {
                    for (int m = 0; m < body.getResult().getItems().size(); m++) {
                        privatePhone = body.getResult().getItems().get(m).getPhoneNumber();
                        DataUtil.phoneNub2.add(privatePhone);
                    }
                }
                LogUtil.d("privatePhone--->" + DataUtil.phoneNub2.size() + "---" + DataUtil.phoneNub2.toString());
                savePrivate(body);

                if (isShow)
                    Toast.makeText(context, "白名单同步成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String message) {
                if (isShow)
                    Toast.makeText(context, "白名单同步失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPublicPhoneList() {
        ApiFactory.getService().getUserTree(DataUtil.getToken()).enqueue(new MyCallBack<UserTreeResult>() {
            @Override
            public void onSuc(Response<UserTreeResult> response) {
                UserTreeResult body = response.body();
                LogUtil.d(body.getResult().toString());
                String landLine = null;
                String phoneNumber = null;
                LogUtil.d("UserTreeResult===>" + body.toString());
                if (DataUtil.phoneNub1.size() > 0 && DataUtil.phoneNub1 != null) {
                    DataUtil.phoneNub1.clear();
                }
                if (body.getResult() != null && body.getResult().size() > 0) {
                    for (int i = 0; i < body.getResult().get(0).getChildren().size(); i++) {
                        for (int j = 0; j < body.getResult().get(0).getChildren().get(i).getUsers().size(); j++) {
                            landLine = body.getResult().get(0).getChildren().get(i).getUsers().get(j).getLandline();
                            phoneNumber = body.getResult().get(0).getChildren().get(i).getUsers().get(j).getPhoneNumber();
                            DataUtil.phoneNub1.add(landLine);
                            DataUtil.phoneNub1.add(phoneNumber);
                            LogUtil.d("linePhone---->" + landLine + "---" + body.getResult().get(0).getChildren().get(i).getUsers().size());
                        }
                    }
                }
                LogUtil.d("phoneNub--->" + DataUtil.phoneNub1.size() + "---" + DataUtil.phoneNub1);
                String str = new Gson().toJson(body);
                String file = new File(Environment.getExternalStorageDirectory() + "/Police/", "treeUser.txt").
                        getAbsolutePath();
                UserTreeUtil.deleteFile(file);
//                writeLog(str, "/Police", "treeUser.txt");
                UserTreeUtil.writeString(str);
                savePublic(body);
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(context, "白名单同步失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void writeLog(String message, String dir, String filePath) {
        File myDir = new File(Environment.getExternalStorageDirectory().getPath() + dir);
        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                return;
            }
        }
        File file = new File(myDir, filePath);
        BufferedWriter fout;
        try {
            fout = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            fout.write(message + "\n");
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePublic(UserTreeResult body) {
        ArrayList<String> publicNumbers = CommonMethod.getPublicNumbers(body);
        LogUtil.d("publicNumbers: " + publicNumbers.size() + " " + publicNumbers.toString());
        List<UsersBean> publicUsers = CommonMethod.getPublicUsers(body);

//        if (publicNumbers.size() > 0) {
//            chatDb.saveUser(publicUsers);
//            PhoneInterfaceUtil.addMdmNumberList(mAdminName, publicNumbers);
//        }
    }

    public void savePrivate(PrivatePhoneWhiteResult body) {
        List<PrivatePhoneBean> items = body.getResult().getItems();
        ArrayList<String> privateNumbers = CommonMethod.getPrivateNumbers(items);
        LogUtil.d("privateNumbers: " + privateNumbers.size() + " " + privateNumbers.toString());
        String file = new File(Environment.getExternalStorageDirectory() + "/Police/", "privateNumber1.txt").
                getAbsolutePath();
        PrivateNumberUtil.deleteFile(file);
        String str = new Gson().toJson(body);
        writeLog(str, "/Police", "privateNumber1.txt");
//        PrivateNumberUtil.writeString(str);
//        if (privateNumbers.size() > 0) {
//            PhoneInterfaceUtil.addMdmNumberList(mAdminName, privateNumbers);
//        }
    }
}
