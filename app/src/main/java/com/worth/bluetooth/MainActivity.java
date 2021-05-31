package com.worth.bluetooth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.worth.framework.base.core.utils.L;
import com.worth.framework.base.core.utils.LDBus;
import com.worth.framework.business.enter.VipSdkHelper;
import com.worth.framework.business.ext.ContactsKt;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();

        initView();

        initSdk();

        initObserver();
    }

    private void initSdk() {
        String host = "http://192.168.0.103:8080";
        String uid = "1001";
        VipSdkHelper.Companion.getInstance().initSdk(host, uid);
    }

    private void initObserver() {
        LDBus.INSTANCE.observer(ContactsKt.EVENT_WITH_INPUT_RESULT, objResult -> {
            if (objResult != null && !objResult.toString().isEmpty()) {
                L.e(TAG, "sdk返回网络查询结果：" + objResult.toString());
                {
                    //TO-DO   进行页面数据的展示，检测到返回结果进行数据的更新。sdk会继续检测下一句的输入
                }
            }
            return null;
        });
    }

    private void initView() {
        editText = findViewById(R.id.et_input);

        findViewById(R.id.playText).setOnClickListener(v -> {
            VipSdkHelper.Companion.getInstance().wakeUpWithInputText(editText.getText().toString().trim());
            {
                // 网络查询输入结果后 会在上面的initObserver返回对应的网络结果，进行检测并展示即可
            }
        });

        findViewById(R.id.wakeUp).setOnClickListener(v -> {
            VipSdkHelper.Companion.getInstance().wakeUpWithClickBtn();
            {
                // 唤醒语音之后，可直接语音的输入，也会在上面的initObserver返回对应的网络结果，进行检测并展示即可
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
}