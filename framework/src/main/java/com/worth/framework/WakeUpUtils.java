package com.worth.framework;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.baidu.aip.asrwakeup3.core.wakeup.MyWakeup;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.IWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.RecogWakeupListener;
import com.baidu.speech.asr.SpeechConstant;
import com.worth.framework.base.core.utils.AppManagerKt;
import com.worth.framework.base.core.utils.L;
import com.worth.framework.business.SdkUtils;
import com.worth.framework.business.VipAudioSdkHelper;

import java.util.HashMap;
import java.util.Map;

import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAKEUP_SUCCESS;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 12:10 PM
 * Description: This is WakeUpUtils
 */
public class WakeUpUtils {
    private static final String TAG = "WakeUpUtils";
    protected MyWakeup myWakeup;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            L.e("WakeUpUtils-->handleMessage-->: " + msg.toString());
            switch (msg.what) {
                case STATUS_WAKEUP_SUCCESS://   唤醒成功
                    wakeUp();
                    break;
            }
        }
    };

    public void wakeUp() {
        SdkUtils.ins().speak(AppManagerKt.getApplication().getString(R.string.str_sdk_def_wakeup_ref));
        VipAudioSdkHelper.Companion.getInstance().stopRecord();
        VipAudioSdkHelper.Companion.getInstance().startRecord();
    }

    public void init() {
        if (myWakeup == null) {
            IWakeupListener listener = new RecogWakeupListener(mHandler);
            myWakeup = new MyWakeup(AppManagerKt.getApplication(), listener);
        }
        startListener();
    }

    /**
     * 开启监听
     */
    private void startListener() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        myWakeup.start(params);
    }

    // 基于DEMO唤醒词集成第4.1 发送停止事件
    public void stop() {
        myWakeup.stop();
    }

    public void release() {
        // 基于DEMO唤醒词集成第5 退出事件管理器
        myWakeup.release();
    }


    private WakeUpUtils() {
    }

    /**
     * 定义一个私有的内部类，在第一次用这个嵌套类时，会创建一个实例。而类型为SingletonHolder的类，
     * 只有在Singleton.getInstance()中调用，由于私有的属性，他人无法使用SingleHolder，
     * 不调用Singleton.getInstance()就不会创建实例。
     * 优点：达到了lazy loading的效果，即按需创建实例。
     */
    private static class SingletonHolder {
        public static WakeUpUtils instance = new WakeUpUtils();
    }

    public static WakeUpUtils ins() {
        return SingletonHolder.instance;
    }

}