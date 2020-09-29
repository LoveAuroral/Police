package com.dark_yx.policemain.chat.view.chatui.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.dark_yx.policemain.R;

public class VoiceDialog extends Dialog {

    public VoiceDialog(@NonNull Context context) {
        super(context, R.style.Theme_AppCompat_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_xml);
    }

    @Override
    protected void onStart() {
        super.onStart();
        resize();
    }

    private void resize() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }
}
