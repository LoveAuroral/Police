package com.dark_yx.policemain.broadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OpenAppReceiverJZ extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {/*
        ComponentName admin = new ComponentName(context, DeviceReceiver.class);
        Log.d("getAction", intent.getAction());
        int cid = intent.getIntExtra("cid", -1);
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        //获取当前时间
        String str = formatter.format(curDate);
        writeLog("时间：" + str + "\n" + "Cid:" + cid + "");
        if (cid != 0) {
            if (cid == 174911792) {
                Log.d("打开app", cid + "");
                DataUtil.setEnter(context, true);
                PhoneInterfaceUtil.openInit(admin, context.getPackageName(), LoginActivity.class.getCanonicalName(), context.getApplicationContext());
                Intent myIntent = new Intent(context, PhoneMainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myIntent);
            } else {
                if (DataUtil.isEnter(context)) {
                    Log.d("退出app", cid + "");
                    CommonMethod.sendStatus(false, context);
                    PhoneInterfaceUtil.exitApp(context, admin);
                }
            }
        } else {
            if (DataUtil.isEnter(context)) {
                Log.d("退出app", cid + "");
                CommonMethod.sendStatus(false, context);
                PhoneInterfaceUtil.exitApp(context, admin);
            }
        }
    }

    public void writeLog(String message) {
        File myDir = new File(Environment.getExternalStorageDirectory().getPath() + "/基站測試");
        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                return;
            }
        }
        File file = new File(myDir, "missedPhone.txt");
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
        }*/
    }
}
