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
import java.util.Arrays;

import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_NET_WORKER_ERROR;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_RECORD_ERROR;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_SPEAK_ERROR;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_WAKE_UP_ERROR;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText editText;
    private final int PERMISSION_REQUEST_CODE = 1000;
    private VipSdkHelper vipSdkHelper;
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
        vipSdkHelper = VipSdkHelper.Companion.getInstance().initSdk(host, uid)
                .switchSdkWakeUp(true)
                .setQuickEnterList(Arrays.asList("呼叫服务员", "点餐", "结账"));
    }

    private void initObserver() {
        LDBus.INSTANCE.observer(ContactsKt.ERROR_CALL_BACK, objResult -> {
            if (objResult != null) {
                L.e(TAG, "sdk返回错误code结果：" + objResult.toString());
                int code = (int) objResult;
                switch (code) {
                    case CALL_BACK_NET_WORKER_ERROR:
                        //TO-DO   网络监测错误返回的回调code

                        break;
                    case CALL_BACK_SDK_SPEAK_ERROR:
                        //TO-DO   语音合成过程中出错回调

                        break;
                    case CALL_BACK_SDK_RECORD_ERROR:
                        //TO-DO   语音识别过程中，asr识别错误

                        break;
                    case CALL_BACK_SDK_WAKE_UP_ERROR:
                        //TO-DO   唤醒失败

                        break;
                }
            }
            return null;
        });
    }

    private void initView() {
        editText = findViewById(R.id.et_input);

        findViewById(R.id.playText).setOnClickListener(v -> {
            vipSdkHelper.wakeUpWithInputText(editText.getText().toString().trim());
            {
                // 网络查询输入结果后 会在上面的initObserver返回对应的网络结果，进行检测并展示即可
            }
        });

        findViewById(R.id.wakeUp).setOnClickListener(v -> {
            vipSdkHelper.wakeUpWithClickBtn();
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
                Manifest.permission.SYSTEM_ALERT_WINDOW,
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
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), PERMISSION_REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        if (requestCode == PERMISSION_REQUEST_CODE) {

        }
    }

    @Override
    protected void onDestroy() {
        vipSdkHelper.destroy();
        super.onDestroy();
    }
}