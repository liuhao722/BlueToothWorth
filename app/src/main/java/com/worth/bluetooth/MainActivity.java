package com.worth.bluetooth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.worth.framework.base.core.utils.L;
import com.worth.framework.base.core.utils.LDBus;
import com.worth.framework.business.enter.VipSdkHelper;
import com.worth.framework.business.ext.ContactsKt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_NET_WORKER_ERROR;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_RECORD_ERROR;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_SPEAK_ERROR;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_WAKE_UP_ERROR;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int PERMISSION_REQUEST_CODE = 1000;
    private VipSdkHelper vipSdkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();       //  初始化百度sdk依赖的权限

        initView();

        initSdk();              //  初始化sdk

        initObserver();         //  监听sdk错误的返回
    }

    private void initSdk() {
        String host = "http://192.168.0.103:8080/api/v1/service/chat";
        String uid = "testUid_1001";
        Map header = new HashMap<>();
        header.put("testHeader", "1234");
        Map body = new HashMap<>();
        body.put("testBody", "lili");
        vipSdkHelper = VipSdkHelper.Companion.getInstance().initSdk(host, uid, header, body)
                .switchSdkWakeUp(true)
                .setQuickEnterList(Arrays.asList("点餐", "催菜", "结账", "呼叫服务员", "猜谜语", "叫老板"));
    }

    private void initObserver() {
        LDBus.INSTANCE.observer(ContactsKt.ERROR_CALL_BACK, objResult -> {
            if (objResult != null) {
                L.e(TAG, "sdk返回错误code结果：" + objResult.toString());
                int code = (int) objResult;
                switch (code) {
                    case CALL_BACK_NET_WORKER_ERROR:
                        //TO-DO   网络监测错误返回的回调code
                        Log.e("Error","initObserver-1");
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

        LDBus.INSTANCE.observer(ContactsKt.ERROR_CALL_BACK, objResult -> {
            if (objResult != null) {
                L.e(TAG, "sdk返回错误code结果：" + objResult.toString());
                int code = (int) objResult;
                switch (code) {
                    case CALL_BACK_NET_WORKER_ERROR:
                        //TO-DO   网络监测错误返回的回调code
                        Log.e("Error","initObserver-2");
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
        findViewById(R.id.wakeUp).setOnClickListener(v -> {
            vipSdkHelper.wakeUpWithClickBtn();
            {
                // 唤醒语音之后，可直接语音的输入，也会在上面的initObserver返回对应的网络结果，进行检测并展示即可
            }
        });
    }

    /**
     * 开启or关闭语音唤醒功能，但不会关闭语音识别功能
     *
     * @param openRecord
     */
    private void switchSdkWakeUp(boolean openRecord) {
        vipSdkHelper.switchSdkWakeUp(openRecord);
    }

    /**
     * app端中有对应的录音写作功能时候可以选择关闭，传false，当写心情或者其他写作录音完毕之后记得重置状态传true；
     *
     * @param openRecord 当前sdk的录音开启状态
     */
    private void switchRecord(boolean openRecord) {
        if (openRecord) {
            vipSdkHelper.startRecord();
        } else {
            vipSdkHelper.stopRecord();
        }
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