package com.baidu.aip.asrwakeup3.core.recog.listener;

import android.os.Handler;
import android.os.Message;

import com.baidu.aip.asrwakeup3.core.recog.RecogResult;
import com.baidu.speech.asr.SpeechConstant;
import com.worth.framework.base.core.utils.L;
import com.worth.framework.base.core.utils.LDBus;
import com.worth.framework.business.ext.ContactsKt;
import com.worth.framework.business.ext.ToAppContactsCodes;

import static com.worth.framework.business.ext.ContactsKt.ASR_EXIT;
import static com.worth.framework.business.ext.ContactsKt.CALL_BACK_SDK_RECORD_ERROR;
import static com.worth.framework.business.ext.ContactsKt.ERROR_CALL_BACK;
import static com.worth.framework.business.ext.ToAppContactsCodes.ASR_EXIT_TO_APP;
import static com.worth.framework.business.ext.ToAppContactsCodes.SDK_TO_APP_EVENT_CODES;
import static com.worth.framework.business.ext.ToAppContactsCodes.USER_INPUT_SPEAK_ASR_FINISH;

/**
 * Created by fujiayi on 2017/6/16.
 */

public class MessageStatusRecogListener extends StatusRecogListener {
    private Handler handler;

    private long speechEndTime = 0;

    private boolean needTime = true;

    private static final String TAG = "MesStatusRecogListener";

    public MessageStatusRecogListener(Handler handler) {
        this.handler = handler;
    }


    @Override
    public void onAsrReady() {
        super.onAsrReady();
        speechEndTime = 0;
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_WAKEUP_READY, "引擎就绪，可以开始说话。");
    }

    @Override
    public void onAsrBegin() {
        super.onAsrBegin();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN, "检测到用户说话");
    }

    @Override
    public void onAsrEnd() {
        super.onAsrEnd();
        speechEndTime = System.currentTimeMillis();
        sendMessage("【asr.end事件】检测到用户说话结束");
    }

    /**
     * 识别过程中每个字的回调
     */
    @Override
    public void onAsrPartialResult(String[] results, RecogResult recogResult) {

//        L.e("onAsrPartialResult-1", "result:" + results[0]);
//        L.e("onAsrPartialResult-1", "原始json:" + recogResult.getOrigalJson());
//
//        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL,
//                "临时识别结果，结果是“" + results[0] + "”；原始json：" + recogResult.getOrigalJson());
        super.onAsrPartialResult(results, recogResult);
    }


    /**
     * 识别整段文字后的回调，识别完后就结束了
     */
    @Override
    public void onAsrFinalResult(String[] results, RecogResult recogResult) {
        super.onAsrFinalResult(results, recogResult);
        if (results != null && results.length > 0 && !results[0].isEmpty()) {
            Message msg = new Message();
            msg.what = ContactsKt.USER_INPUT_SPEAK_ASR_FINISH;
            msg.obj = results[0];
            handler.sendMessage(msg);
            LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, USER_INPUT_SPEAK_ASR_FINISH, results[0]);
        }
//        L.e("onAsrFinalResult-2", "result:" + results[0]);
//        L.e("onAsrFinalResult-2", "原始json:" + recogResult.getOrigalJson());

//
//        String message = "识别结束，结果是”" + results[0] + "”";
//        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL,
//                message + "；原始json：" + recogResult.getOrigalJson());
//        if (speechEndTime > 0) {
//            long currentTime = System.currentTimeMillis();
//            long diffTime = currentTime - speechEndTime;
//            message += "；说话结束到识别结束耗时【" + diffTime + "ms】" + currentTime;
//
//        }
//        speechEndTime = 0;
//        sendMessage(message, status, true);
    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage,
                                 RecogResult recogResult) {
        super.onAsrFinishError(errorCode, subErrorCode, descMessage, recogResult);
        LDBus.INSTANCE.sendSpecial(ERROR_CALL_BACK, CALL_BACK_SDK_RECORD_ERROR);

        String message = "【asr.finish事件】识别错误, 错误码：" + errorCode + " ," + subErrorCode + " ; " + descMessage;
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, message);
        if (speechEndTime > 0) {
            long diffTime = System.currentTimeMillis() - speechEndTime;
            message += "。说话结束到识别结束耗时【" + diffTime + "ms】";
        }
        speechEndTime = 0;
        sendMessage(message, status, true);
        speechEndTime = 0;

        LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, ToAppContactsCodes.RECORD_ERROR, message);
    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {
        super.onAsrOnlineNluResult(nluResult);
        if (!nluResult.isEmpty()) {
            sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, "原始语义识别结果json：" + nluResult);
        }
    }

    @Override
    public void onAsrFinish(RecogResult recogResult) {
        super.onAsrFinish(recogResult);
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_FINISH, "识别一段话结束。如果是长语音的情况会继续识别下段话。");

    }

    /**
     * 长语音识别结束
     */
    @Override
    public void onAsrLongFinish() {
        super.onAsrLongFinish();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH, "长语音识别结束。");
    }


    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineLoaded() {
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_LOADED, "离线资源加载成功。没有此回调可能离线语法功能不能使用。");
    }

    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineUnLoaded() {
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_UNLOADED, "离线资源卸载成功。");
    }

    @Override
    public void onAsrExit() {
        super.onAsrExit();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_EXIT, "识别引擎结束并空闲中");
    }

    private void sendStatusMessage(String eventName, String message) {
        message = "[" + eventName + "]" + message;
        if (
                message.contains("识别错误")  // 全部过滤
//                message.contains("100")
//                || message.contains("200")
//                || message.contains("2100")
//                || message.contains("300")
//                || message.contains("310")
//                || message.contains("400")
//                || message.contains("500")
//                || message.contains("600")
//                || message.contains("700")
//                || message.contains("800")
//                || message.contains("900")
        ) {
            LDBus.INSTANCE.sendSpecial(ASR_EXIT, "");
            LDBus.INSTANCE.sendSpecial2(SDK_TO_APP_EVENT_CODES, ASR_EXIT_TO_APP, "");
        }
        switch (eventName) {
            case "asr.begin":           //  检测到用户说话
                break;
            case "asr.partial":         //  临时识别结果
                break;
            case "asr.finish":          //  识别一段话结束。
                break;
            case "asr.exit":            //  识别引擎结束并空闲中
                break;

        }
        L.i(TAG, "RecordUtils-Listener-->" + message);
        sendMessage(message, status);
    }

    private void sendMessage(String message) {
        sendMessage(message, WHAT_MESSAGE_STATUS);
    }

    private void sendMessage(String message, int what) {
        sendMessage(message, what, false);
    }


    private void sendMessage(String message, int what, boolean highlight) {
        if (needTime && what != STATUS_FINISHED) {
            message += "  ;time=" + System.currentTimeMillis();
        }
        if (handler == null) {
            L.i(TAG, message);
            return;
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = status;
        if (highlight) {
            msg.arg2 = 1;
        }
        msg.obj = message + "\n";
        handler.sendMessage(msg);
    }
}
