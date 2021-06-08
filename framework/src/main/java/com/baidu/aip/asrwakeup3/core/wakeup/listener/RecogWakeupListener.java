package com.baidu.aip.asrwakeup3.core.wakeup.listener;

import android.os.Handler;

import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.baidu.aip.asrwakeup3.core.wakeup.WakeUpResult;

import static com.worth.framework.business.ext.ContactsKt.WAKEUP_XIAO_BANG_SDK_ERROR;
import static com.worth.framework.business.ext.ContactsKt.WAKEUP_XIAO_BANG_SDK_SUCCESS;

/**
 * Created by fujiayi on 2017/9/21.
 */

public class RecogWakeupListener extends SimpleWakeupListener implements IStatus {

    private static final String TAG = "RecogWakeupListener";

    private Handler handler;

    public RecogWakeupListener(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        super.onSuccess(word, result);
        handler.sendMessage(handler.obtainMessage(WAKEUP_XIAO_BANG_SDK_SUCCESS));
    }

    @Override
    public void onError(int errorCode, String errorMessage, WakeUpResult result) {
        super.onError(errorCode, errorMessage, result);
        handler.sendMessage(handler.obtainMessage(WAKEUP_XIAO_BANG_SDK_ERROR));
    }
}
