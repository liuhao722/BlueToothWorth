package com.worth.bluetooth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.worth.framework.base.core.utils.L;
import com.worth.framework.business.VipSdkHelper;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * 注意区分在线SDK和纯离线SDK，在线SDK没有离线功能。其中纯离线SDK有在线SDK的所有功能。
 * 界面启动时会看见本次使用的是在线SDK还是纯离线SDK，在线SDK是不需要序列号的
 * <p>
 * SynthActivity 完整合成
 * MiniActivity 精简版合成
 * SaveFileActivity 保存合成后的音频
 */
public class MainActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        initPermission();
        VipSdkHelper.Companion.getInstance();
    }

    private void initButtons() {
        editText = findViewById(R.id.et_input);
        findViewById(R.id.playText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VipSdkHelper.Companion.getInstance().wakeUpWithInputText(editText.getText().toString().trim(), s -> {
                    L.d(s);
                    //  业务逻辑处理
                    return null;
                });
            }
        });

        findViewById(R.id.wakeUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VipSdkHelper.Companion.getInstance().wakeUpWithClickBtn();
            }
        });
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, // demo使用

                Manifest.permission.RECORD_AUDIO,
                /* 下面是蓝牙用的，可以不申请
                Manifest.permission.BROADCAST_STICKY,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
                */
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }


    private void startAct(Class activityClass) {
        startActivity(new Intent(this, activityClass));
    }

}