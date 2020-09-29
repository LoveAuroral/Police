package com.dark_yx.policemain.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;

import org.xutils.common.util.LogUtil;

public class CheckPerimissionActivity extends AppCompatActivity {
    private Button btn;
    private ComponentName admin = null;
    private int perimission = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_perimission);
        admin = new ComponentName(this, DeviceReceiver.class);
        perimission = getIntent().getIntExtra("perimission", 0);
        PhoneInterfaceUtil.setMicrophoneDisabled(admin, true);//禁用语音功能
        PhoneInterfaceUtil.setDataConnectivityDisabled(admin, true);//禁用数据
        PhoneInterfaceUtil.setWifiDisable(admin, true);//禁用wifi
        PhoneInterfaceUtil.setHomedisable(admin, true);//禁用HOME键
        PhoneInterfaceUtil.setBluetoothDisable(admin, true);//禁用蓝牙
        PhoneInterfaceUtil.setWifiApDisabled(admin, true);//禁用热点
        PhoneInterfaceUtil.setMenuStatus(admin, true);//禁用功能鍵
        PhoneInterfaceUtil.setBarStatus(admin, true);//禁用下拉状态栏
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d("perimission--->" + perimission);
                if (perimission == 1) {
                    Intent intent = new Intent();
                    ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
                    intent.setComponent(comp);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(CheckPerimissionActivity.this, "系统返回键已禁用", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
