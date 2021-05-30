package com.worth.framework.business;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.SynthesizerTool;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.control.InitConfig;
import com.baidu.tts.sample.control.MySyntherizer;
import com.baidu.tts.sample.listener.FileSaveListener;
import com.baidu.tts.sample.util.Auth;
import com.baidu.tts.sample.util.AutoCheck;
import com.baidu.tts.sample.util.FileUtil;
import com.baidu.tts.sample.util.IOfflineResourceConst;
import com.baidu.tts.sample.util.OfflineResource;
import com.worth.framework.R;
import com.worth.framework.base.core.utils.AppManagerKt;
import com.worth.framework.base.core.utils.L;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 10:59 AM
 * Description: This is SdkUtils
 */
public class SdkUtils {
    private static final String TAG = "SdkUtils";

    public MySyntherizer synthesizer;

    public String appId;

    public String appKey;

    public String secretKey;

    public String sn; // 纯离线合成SDK授权码；离在线合成SDK没有此参数

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； TtsMode.OFFLINE 纯离线合成，需要纯离线SDK
    public TtsMode ttsMode = IOfflineResourceConst.DEFAULT_SDK_TTS_MODE;

    public boolean isOnlineSDK = TtsMode.ONLINE.equals(IOfflineResourceConst.DEFAULT_SDK_TTS_MODE);

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_vXXXXXXX.dat为离线男声模型文件；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_vXXXXX.dat为离线女声模型文件;
    // assets目录下bd_etts_common_speech_yyjw_mand_eng_high_am-mix_vXXXXX.dat 为度逍遥模型文件;
    // assets目录下bd_etts_common_speech_as_mand_eng_high_am_vXXXX.dat 为度丫丫模型文件;
    // 在线合成sdk下面的参数不生效
    public String offlineVoice = OfflineResource.VOICE_MALE;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**********************************************************************************************/

    public void init() {
        Auth auth;
        try {
            auth = Auth.getInstance(AppManagerKt.getApplication());
        } catch (Auth.AuthCheckException e) {
            return;
        }
        appId = auth.getAppId();
        appKey = auth.getAppKey();
        secretKey = auth.getSecretKey();
        sn = auth.getSn(); // 离线合成SDK必须有此参数；在线合成SDK没有此参数
        initialTts(); // 初始化TTS引擎
        if (!isOnlineSDK) {
            log("SynthActivity", "so version:" + SynthesizerTool.getEngineInfo());
        }
    }

    /**
     * 与SynthActivity相比，修改listener为FileSaveListener 可实现保存录音功能。
     * 获取的音频内容同speak方法播出的声音
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    private void initialTts() {
        String tmpDir = FileUtil.createTmpDir(AppManagerKt.getApplication());
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new FileSaveListener(mHandler, tmpDir);

        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = getInitConfig(listener);
        if (synthesizer == null){
            synthesizer = new MySyntherizer(AppManagerKt.getApplication(), initConfig, mHandler); // 此处可以改为MySyntherizer 了解调用过程
        }
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
        params.put(SpeechSynthesizer.PARAM_VOLUME, "15");
        // 设置合成的语速，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");
        if (!isOnlineSDK) {
            // 在线SDK版本没有此参数。

            /*
            params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
            // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
            // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
            // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
            // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
            // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
            // params.put(SpeechSynthesizer.PARAM_MIX_MODE_TIMEOUT, SpeechSynthesizer.PARAM_MIX_TIMEOUT_TWO_SECOND);
            // 离在线模式，强制在线优先。在线请求后超时2秒后，转为离线合成。
            */
            // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
            OfflineResource offlineResource = createOfflineResource(offlineVoice);
            // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());
        }
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
        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(AppManagerKt.getApplication()).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
                        log(message); // 可以用下面一行替代，在logcat中查看代码
                    }
                }
            }

        });
        return initConfig;
    }

    private OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(AppManagerKt.getApplication(), voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            log("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    /**
     * 暂停播放。仅调用speak后生效
     */
    public void pause() {
        int result = synthesizer.pause();
        checkResult(result, "pause");
    }

    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    public void resume() {
        int result = synthesizer.resume();
        checkResult(result, "resume");
    }

    /**
     * 停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    public void stop() {
        int result = synthesizer.stop();
        checkResult(result, "stop");
    }
    /**
     * 释放资源成功
     */
    public void release() {
        synthesizer.release();
        Log.i(TAG, "释放资源成功");
    }

    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    public void speak(String text) {
        // 需要合成的文本text的长度不能超过1024个GBK字节。
        if (TextUtils.isEmpty(text)) {
            text = AppManagerKt.getApplication().getString(R.string.str_sdk_def_ref);
        }

        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // params.put(SpeechSynthesizer.PARAM_SPEAKER, "3"); // 设置为度逍遥
        // synthesizer.setParams(params);
        int result = synthesizer.speak(text);
        checkResult(result, "speak");
    }

    /**********************************************************************************************/

    private void checkResult(int result, String method) {
        if (result != 0) {
            log("error code :" + result + " method:" + method);
        }
    }

    private void log(String msg) {
        L.e(msg);
    }

    private void log(String tag, String msg) {
        L.el(tag, msg);
    }

    /**********************************************************************************************/

    private SdkUtils() {
    }

    /**
     * 定义一个私有的内部类，在第一次用这个嵌套类时，会创建一个实例。而类型为SingletonHolder的类，
     * 只有在Singleton.getInstance()中调用，由于私有的属性，他人无法使用SingleHolder，
     * 不调用Singleton.getInstance()就不会创建实例。
     * 优点：达到了lazy loading的效果，即按需创建实例。
     */
    private static class SingletonHolder {
        public static SdkUtils instance = new SdkUtils();
    }

    public static SdkUtils ins() {
        return SingletonHolder.instance;
    }

    /**********************************************************************************************/
}
