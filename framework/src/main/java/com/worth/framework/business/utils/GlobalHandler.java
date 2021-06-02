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

import static com.baidu.aip.asrwakeup3.core.recog.IStatus.WAKEUP_XIAO_BANG_SDK_SUCCESS;
import static com.worth.framework.business.global.GlobalVarKt.speakFinishWhenWakeUpCall;

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
                case WAKEUP_XIAO_BANG_SDK_SUCCESS:                                                  //  唤醒sdk小帮成功
                    WakeUpUtils.ins().wakeUp();
                    break;

                case ContactsKt.USER_INPUT_SPEAK_ASR_FINISH:                                        //  用户输入语言结束，并且有一定的有效结果检测出来
                    if (msg.obj != null && !msg.obj.toString().trim().isEmpty()) {
                        L.e(TAG, "语音识别结果：" + msg.obj);

                        requestServer(msg.obj.toString());

                    }
                    break;

                case ContactsKt.NETWORK_RESULT:                                                     //  网络返回结果
                    String result = msg.obj == null ? null : msg.obj.toString();
                    SpeakUtils.ins().speak(result, false);
                    WakeUpUtils.ins().startListener();
                    VipSdkHelper.Companion.getInstance().netWorkResult(result);
                    break;

                case ContactsKt.SPEAK_UTILS_PLAY_FINISH:                                            //  播放结束后
                    if (speakFinishWhenWakeUpCall) {                                                //  是被wakeUp唤醒
                        WakeUpUtils.ins().stopListener();
                        SpeakUtils.ins().stopSpeak();
                        RecordUtils.ins().startRecord();
                    } else {                                                                        //  网络返回的发声
                        SpeakUtils.ins().stopSpeak();
                        RecordUtils.ins().stopRecord();
                        WakeUpUtils.ins().startListener();
                    }
                    break;

                case ContactsKt.SPEAK_UTILS_PLAY_START:                                             //  播放开始
                case ContactsKt.SPEAK_UTILS_PLAY_ERROR:                                             //  播放过程中失败-停止录音再次开启录音
                case ContactsKt.SPEAK_UTILS_PLAY_PROCESS:                                           //  播放过程中
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
