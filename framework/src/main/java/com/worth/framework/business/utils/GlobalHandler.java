package com.worth.framework.business.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.worth.framework.base.core.utils.L;
import com.worth.framework.base.network.RetrofitUtils;
import com.worth.framework.business.enter.VipSdkHelper;
import com.worth.framework.business.ext.ContactsKt;

import java.lang.ref.WeakReference;

import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAKEUP_SUCCESS;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 10:34 PM
 * Description: This is GlobalHandler
 */
public class GlobalHandler {
    private static final String TAG = "GlobalHandler";
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STATUS_WAKEUP_SUCCESS:                                                         //  唤醒成功
                    WakeUpUtils.ins().wakeUp();
                    break;

                case ContactsKt.SPEAK_FINISH:                                                       //  用户输入语言结束，并且有一定的有效结果检测出来
                    if (msg.obj != null && !msg.obj.toString().trim().isEmpty()) {
                        L.e(TAG, "语音识别结果：" + msg.obj);
                        requestServer(msg.obj.toString());
                    }
                    break;

                case ContactsKt.NETWORK_RESULT:                                                     //  网络返回结果
                    RecordUtils.ins().startRecord();
                    String result = msg.obj == null ? null : msg.obj.toString();
                    SpeakUtils.ins().speak(result);
                    VipSdkHelper.Companion.getInstance().netWorkResult(result);
                    break;

                case ContactsKt.PLAY_FINISH:                                                        //  播放结束后
                    RecordUtils.ins().startRecord();
                    break;

                case ContactsKt.PLAY_START:                                                         //  播放开始
                case ContactsKt.PLAY_ERROR:                                                         //  播放过程中失败-停止录音再次开启录音
                case ContactsKt.PLAY_PROCESS:                                                       //  播放过程中
                    RecordUtils.ins().stopRecord();
                    break;

            }
        }
    };

    public WeakReference<Handler> mHandler = new WeakReference<>(handler);

    public void requestServer(String msg) {
        RetrofitUtils.ins().requestServer(msg, result -> {
            Message resultMsg = mHandler.get().obtainMessage(ContactsKt.NETWORK_RESULT, result);
            mHandler.get().sendMessage(resultMsg);
            return null;
        });
    }


    private GlobalHandler() {
    }

    private static class SingletonHolder {
        public static GlobalHandler instance = new GlobalHandler();
    }

    public static GlobalHandler ins() {
        return SingletonHolder.instance;
    }
}
