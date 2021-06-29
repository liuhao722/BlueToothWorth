package com.worth.framework.business.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.control.InitConfig;
import com.baidu.tts.sample.control.MySyntherizer;
import com.baidu.tts.sample.control.NonBlockSyntherizer;
import com.baidu.tts.sample.listener.UiMessageListener;
import com.baidu.tts.sample.util.Auth;
import com.baidu.tts.sample.util.IOfflineResourceConst;
import com.worth.framework.R;
import com.worth.framework.business.global.GlobalVarKt;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 10:59 AM
 * Description: This is SpeakUtils 语音转换的辅助类
 */
public class SpeakUtils {
    private static final String TAG = "SpeakUtils";
    public MySyntherizer synthesizer;
    public String appId;
    public String appKey;
    public String secretKey;
    public String sn; // 纯离线合成SDK授权码；离在线合成SDK没有此参数
    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； TtsMode.OFFLINE 纯离线合成，需要纯离线SDK
    public TtsMode ttsMode = IOfflineResourceConst.DEFAULT_SDK_TTS_MODE;
    // 在线合成sdk下面的参数不生效
    private Handler mHandler = GlobalHandler.ins().mHandler.get();
    /**********************************************************************************************/
    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    public void speak(String text, boolean isWakeUp) {
        GlobalVarKt.speakFinishWhenWakeUpCall = isWakeUp;
        // 需要合成的文本text的长度不能超过1024个GBK字节。
        if (TextUtils.isEmpty(text)) {
            text = context.getString(R.string.str_sdk_def_ref);
        }
        synthesizer.speak(text);
    }

    /**
     * 暂停播放。仅调用speak后生效
     */
    public void pause() {
        synthesizer.pause();
    }

    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    public void resume() {
        synthesizer.resume();
    }

    /**
     * 停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    public void stopSpeak() {
        synthesizer.stop();
    }

    /**
     * 释放资源成功
     */
    public void release() {
        synthesizer.release();
        Log.i(TAG, "释放资源成功");
    }

    /**********************************************************************************************/
    private Context context;

    public void init(Context applicationContext) {
        context = applicationContext;
        Auth auth;
        try {
            auth = Auth.getInstance(context);
        } catch (Auth.AuthCheckException e) {
            return;
        }
        appId = auth.getAppId();
        appKey = auth.getAppKey();
        secretKey = auth.getSecretKey();
        sn = auth.getSn(); // 离线合成SDK必须有此参数；在线合成SDK没有此参数
        initialTts(); // 初始化TTS引擎
    }

    /**
     * 与SynthActivity相比，修改listener为FileSaveListener 可实现保存录音功能。
     * 获取的音频内容同speak方法播出的声音
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    private void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mHandler);
        InitConfig config = getInitConfig(listener);
        synthesizer = new NonBlockSyntherizer(context, config, mHandler); // 此处可以改为MySyntherizer 了解调用过程
    }

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return 合成参数Map
     */
    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>, 其它发音人见文档
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "8");
        // 设置合成的语速，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");
        return params;
    }

    private InitConfig getInitConfig(SpeechSynthesizerListener listener) {
        Map<String, String> params = getParams();
        // 添加你自己的参数
        InitConfig initConfig;
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        if (sn == null) {
            initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
        } else {
            initConfig = new InitConfig(appId, appKey, secretKey, sn, ttsMode, params, listener);
        }
        return initConfig;
    }

    /**********************************************************************************************/

    private SpeakUtils() {
    }

    /**
     * 定义一个私有的内部类，在第一次用这个嵌套类时，会创建一个实例。而类型为SingletonHolder的类，
     * 只有在Singleton.getInstance()中调用，由于私有的属性，他人无法使用SingleHolder，
     * 不调用Singleton.getInstance()就不会创建实例。
     * 优点：达到了lazy loading的效果，即按需创建实例。
     */
    private static class SingletonHolder {
        public static SpeakUtils instance = new SpeakUtils();
    }

    public static SpeakUtils ins() {
        return SingletonHolder.instance;
    }

    /**********************************************************************************************/
}
