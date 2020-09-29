package com.dark_yx.policemain.nfcDemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.R;
import com.newabel.nfcsdk.MainDES;
import com.newabel.nfcsdk.NfcHelper;

public class NfcMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PERMISSION = 101;
    private final String TAG = this.getClass().getSimpleName();
    private SpHelper mSpHelper;
    private String CardId = "";
    private NfcAdapter mNfcAdapter;
    private MyBroadcastReceiver mBroadcastReceiver;
    TextView mtv, textView;
    private Intent intent;
    int mode, state;
    LinearLayout appPolice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        registerReceiver();
        requestPermissions();
//        textView = (TextView) findViewById(R.id.textView1);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout_1);
        TextView tv = (TextView) findViewById(R.id.textView1);
        if (mNfcAdapter == null) {
            ll.setBackgroundColor(Color.parseColor("#7Feb6100"));
            tv.setText("\uf05e 没有NFC功能");
        } else if (!mNfcAdapter.isEnabled()) {
            ll.setBackgroundColor(Color.parseColor("#7Feb6100"));
            Toast.makeText(this, "没有打开NFC，请点击设置，打开NFC功能", Toast.LENGTH_LONG).show();
            tv.setText("\uf05c 没有打开NFC");
        } else {
            ll.setBackgroundColor(Color.parseColor("#7F00307e"));
            tv.setText("\uf05d 功能正常");
        }


        Button icbtn = (Button) findViewById(R.id.iccardsetting);
        icbtn.setTextColor(Color.parseColor("#00307e"));
        icbtn.setText("SN: " + mSpHelper.getString(mSpHelper.mCardID, ""));
    }

    private void initView() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        appPolice = (LinearLayout) findViewById(R.id.linearLayout_1);
        Button btn = (Button) findViewById(R.id.set_nfc);
        btn.setTextColor(Color.parseColor("#00307e"));
        btn.setText("\uf013设置");
        btn.setOnClickListener(this);
        appPolice.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        unRegisterReceiver();
        super.onDestroy();
    }

    private void initData() {
        try {
            mSpHelper = new SpHelper(this);
            if (TextUtils.isEmpty(mSpHelper.getString(mSpHelper.mCardID, ""))) {
                CardId = getDefaultSN();
//                CardId = new NfcHelper ().getSN("username");
                mSpHelper.putString(mSpHelper.mCardID, CardId);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.NFC, Manifest.permission.READ_PHONE_STATE};
            requestPermissions(permissions, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public String getDefaultSN() {
        TelephonyManager mtm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String mandroidid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String mimei = mandroidid;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            mimei = mtm.getDeviceId();
            if (mandroidid.length() < 16) {
                for (int i = 0; i < 16 - mandroidid.length(); i++)
                    mandroidid = mandroidid + "0";
            }
            if (mimei == null || mimei.length() < 15) {
                mimei = mandroidid;
            }
        } else {
            Toast.makeText(this, "权限不足，无法获取IMEI", Toast.LENGTH_SHORT).show();
        }
        byte[] cardid = MainDES.imei_id(mimei, mandroidid);
        return new NfcHelper().ByteArrayToHexString(cardid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_nfc: {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
                break;
            }
            case R.id.linearLayout_1: {
//                Intent intent = new Intent("ACTION_WORK_MODE");
//                intent.putExtra("MODE", 3);
//                intent.putExtra("STATE", 0);
//                sendBroadcast(intent);
                break;
            }
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mode = intent.getIntExtra("MODE", -1);
            state = intent.getIntExtra("STATE", -1);
//            textView.setText("mode:" + mode + "\t" + "state" + state);
//            Toast.makeText(MainActivity.this, "工作模式：" + mode + " STATE" + state, Toast.LENGTH_SHORT).show();
        }
    }

    private void registerReceiver() {
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_WORK_MODE");
        registerReceiver(mBroadcastReceiver, filter);
    }

    private void unRegisterReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }
}
