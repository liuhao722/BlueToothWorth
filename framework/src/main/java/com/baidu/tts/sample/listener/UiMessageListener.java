package com.baidu.tts.sample.listener;

import android.os.Handler;
import android.os.Message;

import com.baidu.tts.client.SpeechError;
import com.worth.framework.base.core.utils.L;
import com.worth.framework.base.core.utils.LDBus;
import com.worth.framework.business.ext.ContactsKt;
import com.worth.framework.business.ext.ToAppContactsCodes;

import static com.worth.framework.business.ext.ToAppContactsCodes.SPEAK_UTILS_PLAY_ERROR;
import static com.worth.framework.business.ext.ToAppContactsCodes.SPEAK_UTILS_PLAY_FINISH;
import static com.worth.framework.business.ext.ToAppContactsCodes.SPEAK_UTILS_PLAY_PROCESS;
import static com.worth.framework.business.ext.ToAppContactsCodes.SPEAK_UTILS_PLAY_START;

/**
 * 在 MessageListener的基础上，和UI配合。
 * Created by fujiayi on 2017/9/14.
 */

public class UiMessageListener extends MessageListener {

    private Handler mainHandler;

    private static final String TAG = "UiMessageListener";

    public UiMessageListener(Handler mainHandler) {
        super();
        this.mainHandler = mainHandler;
    }

    @Override
    public void onSynthesizeStart(String utteranceId) {
        super.onSynthesizeStart(utteranceId);
        mainHandler.sendEmptyMessage(ContactsKt.SPEAK_UTILS_PLAY_START);
        LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, SPEAK_UTILS_PLAY_START, utteranceId);

    }

    @Override
    public void onError(String utteranceId, SpeechError speechError) {
        super.onError(utteranceId, speechError);
        mainHandler.sendEmptyMessage(ContactsKt.SPEAK_UTILS_PLAY_ERROR);
        LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, SPEAK_UTILS_PLAY_ERROR, speechError);

    }

    /**
     * 语音流 16K采样率 16bits编码 单声道 。
     * <p>
     * 合成数据和进度的回调接口，分多次回调。
     * 注意：progress表示进度，与播放到哪个字无关
     *
     * @param utteranceId
     * @param data        合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     * engineType  下版本提供 1:音频数据由离线引擎合成； 0：音频数据由在线引擎（百度服务器）合成。
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
        super.onSynthesizeDataArrived(utteranceId, data, progress);
        mainHandler.sendMessage(mainHandler.obtainMessage(UI_CHANGE_SYNTHES_TEXT_SELECTION, progress, 0));
    }

    /**
     * 播放进度回调接口，分多次回调
     * 注意：progress表示进度，与播放到哪个字无关
     *
     * @param utteranceId
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
//        mainHandler.sendMessage(mainHandler.obtainMessage(UI_CHANGE_INPUT_TEXT_SELECTION, progress, 0));
        mainHandler.sendEmptyMessage(ContactsKt.SPEAK_UTILS_PLAY_PROCESS);
        LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, SPEAK_UTILS_PLAY_PROCESS, progress);


    }

    @Override
    public void onSpeechFinish(String utteranceId) {
        super.onSpeechFinish(utteranceId);
        mainHandler.sendEmptyMessage(ContactsKt.SPEAK_UTILS_PLAY_FINISH);
        LDBus.INSTANCE.sendSpecial2(ToAppContactsCodes.SDK_TO_APP_EVENT_CODES, SPEAK_UTILS_PLAY_FINISH, utteranceId);
    }

    protected void sendMessage(String message) {
        sendMessage(message, false);
    }

    @Override
    protected void sendMessage(String message, boolean isError) {
        sendMessage(message, isError, PRINT);
    }


    protected void sendMessage(String message, boolean isError, int action) {
        super.sendMessage(message, isError);
        if (mainHandler != null) {
            Message msg = Message.obtain();
            msg.what = action;
            msg.obj = message + "\n";
            mainHandler.sendMessage(msg);
            L.i(TAG, message);
        }
    }
}
