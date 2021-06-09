package com.worth.framework.business.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.worth.framework.R;
import com.worth.framework.base.core.storage.MeKV;
import com.worth.framework.base.core.utils.L;
import com.worth.framework.base.core.utils.LDBus;
import com.worth.framework.base.core.utils.NetworkUrilsKt;
import com.worth.framework.base.network.RetrofitUtils;
import com.worth.framework.business.ext.ContactsKt;
import com.worth.framework.business.ext.ToAppContactsCodes;

import java.lang.ref.WeakReference;

import static com.worth.framework.base.core.utils.ResourceUtils.getString;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_NET_WORKER_DISCONNECT;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_SPEAK_ERROR;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_WAKE_UP_ERROR;
import static com.worth.framework.business.ext.ContactsKt.ERROR_CALL_BACK;
import static com.worth.framework.business.ext.ContactsKt.EVENT_WITH_INPUT_ASR_RESULT;
import static com.worth.framework.business.ext.ContactsKt.EVENT_WITH_USER_INPUT_RESULT;
import static com.worth.framework.business.ext.ContactsKt.NET_WORK_REQUEST_FINISH;
import static com.worth.framework.business.ext.ContactsKt.NET_WORK_REQUEST_START;
import static com.worth.framework.business.ext.ContactsKt.WAKEUP_XIAO_BANG_SDK_ERROR;
import static com.worth.framework.business.ext.ContactsKt.WAKEUP_XIAO_BANG_SDK_SUCCESS;
import static com.worth.framework.business.ext.ToAppContactsCodes.NETWORK_RESULT_OK;
import static com.worth.framework.business.ext.ToAppContactsCodes.SDK_TO_APP_EVENT_CODES;
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
                    if (MeKV.INSTANCE.wakeUpSwitchIsOpen()) {
                        WakeUpUtils.ins().wakeUp();
                    }
                    break;
                case WAKEUP_XIAO_BANG_SDK_ERROR:                                                    //  唤醒sdk小帮失败
                    //  自己处理一些自己的业务

                    //  发送失败信息到app端自行处理自己的一部分业务
                    LDBus.INSTANCE.sendSpecial(ERROR_CALL_BACK, CALL_BACK_SDK_WAKE_UP_ERROR);
                    break;

                case ContactsKt.USER_INPUT_SPEAK_ASR_FINISH:                                        //  用户输入语言结束，并且有一定的有效结果检测出来
                    if (msg.obj != null && !msg.obj.toString().trim().isEmpty()) {
                        String result = msg.obj.toString();
                        L.e(TAG, "语音识别结果：" + result);
                        LDBus.INSTANCE.sendSpecial(EVENT_WITH_INPUT_ASR_RESULT, result);            //  发送ars识别的结果给页面进行展示
                        LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, ToAppContactsCodes.EVENT_WITH_INPUT_ASR_RESULT, result);
                        requestServer(result);
                    }
                    break;

                case ContactsKt.NETWORK_RESULT_OK:                                                     //  网络返回结果
                    String result = msg.obj == null ? null : msg.obj.toString();
                    // 需要合成的文本text的长度不能超过1024个GBK字节。
                    SpeakUtils.ins().speak(result, false);
                    if (TextUtils.isEmpty(result)) {
                        result = getString(R.string.str_sdk_def_ref);
                    }
                    LDBus.INSTANCE.sendSpecial(EVENT_WITH_USER_INPUT_RESULT, result);
                    LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, ToAppContactsCodes.EVENT_WITH_USER_INPUT_RESULT, result);
                    WakeUpUtils.ins().startListener();
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

                case ContactsKt.SPEAK_UTILS_PLAY_ERROR:                                             //  播放过程中失败-停止录音再次开启录音
                    LDBus.INSTANCE.sendSpecial(ERROR_CALL_BACK, CALL_BACK_SDK_SPEAK_ERROR);
                    RecordUtils.ins().stopRecord();
                    break;
                case ContactsKt.SPEAK_UTILS_PLAY_START:                                             //  播放开始
                case ContactsKt.SPEAK_UTILS_PLAY_PROCESS:                                           //  播放过程中
                    RecordUtils.ins().stopRecord();
                    break;

            }
        }
    };

    public WeakReference<Handler> mHandler = new WeakReference<>(handler);

    public void requestServer(String msg) {
        LDBus.INSTANCE.sendSpecial(NET_WORK_REQUEST_START,"");
        LDBus.INSTANCE.sendSpecial2(SDK_TO_APP_EVENT_CODES, ToAppContactsCodes.NET_WORK_REQUEST_START,"");

        if (NetworkUrilsKt.isNetConnected()) {
            RetrofitUtils.ins().requestServer(msg, result -> {
                LDBus.INSTANCE.sendSpecial(NET_WORK_REQUEST_FINISH,result);
                LDBus.INSTANCE.sendSpecial2(SDK_TO_APP_EVENT_CODES,ToAppContactsCodes.NET_WORK_REQUEST_FINISH,result);

                Message resultMsg = mHandler.get().obtainMessage(ContactsKt.NETWORK_RESULT_OK, result);
                mHandler.get().sendMessage(resultMsg);
                LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, NETWORK_RESULT_OK, result);
                return null;
            });
        } else {
            LDBus.INSTANCE.sendSpecial(NET_WORK_REQUEST_FINISH,"");
            LDBus.INSTANCE.sendSpecial2(SDK_TO_APP_EVENT_CODES,ToAppContactsCodes.NET_WORK_REQUEST_FINISH,"");

            LDBus.INSTANCE.sendSpecial(ERROR_CALL_BACK, CALL_BACK_NET_WORKER_DISCONNECT);
            LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, ToAppContactsCodes.NET_WORKER_DISCONNECT, "");
        }
    }


    private GlobalHandler() {
    }

    private static class SingletonHolder {
        public static GlobalHandler instance = new GlobalHandler();
    }

    public static GlobalHandler ins() {
        return SingletonHolder.instance;
    }

    public void test(){
        LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES,"","");
    }
}
