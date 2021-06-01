package com.worth.framework.business.utils;

import android.content.Context;
import android.os.Handler;

import com.baidu.aip.asrwakeup3.core.wakeup.MyWakeup;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.IWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.RecogWakeupListener;
import com.baidu.speech.asr.SpeechConstant;
import com.worth.framework.R;
import com.worth.framework.base.core.utils.AppManagerKt;
import com.worth.framework.business.enter.VipSdkHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 12:10 PM
 * Description: This is WakeUpUtils 唤醒的辅助类
 */
public class WakeUpUtils {
    private static final String TAG = "WakeUpUtils";
    protected MyWakeup myWakeup;

    private Handler mHandler = GlobalHandler.ins().mHandler.get();

    /**********************************************************************************************/

    /**
     * 唤醒
     */
    public void wakeUp() {
        VipSdkHelper.Companion.getInstance().stopRecord();
        SpeakUtils.ins().speak(context.getString(R.string.str_sdk_def_wakeup_ref));
    }

    /**
     * 开启监听
     */
    public void startListener() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        myWakeup.start(params);
    }

    // 基于DEMO唤醒词集成第4.1 发送停止事件
    public void stopListener() {
        myWakeup.stop();
    }

    public void release() {
        // 基于DEMO唤醒词集成第5 退出事件管理器
        myWakeup.release();
    }

    /**********************************************************************************************/

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

    private Context context;

    public void init(Context applicationContext) {
        context = applicationContext;
        if (myWakeup == null) {
            IWakeupListener listener = new RecogWakeupListener(mHandler);
            myWakeup = new MyWakeup(context, listener);
        }
        startListener();
    }

    /**********************************************************************************************/

}
