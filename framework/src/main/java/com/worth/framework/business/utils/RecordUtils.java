package com.worth.framework.business.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.aip.asrwakeup3.core.recog.MyRecognizer;
import com.baidu.aip.asrwakeup3.core.recog.listener.IRecogListener;
import com.baidu.aip.asrwakeup3.core.recog.listener.MessageStatusRecogListener;
import com.baidu.aip.asrwakeup3.uiasr.params.CommonRecogParams;
import com.baidu.aip.asrwakeup3.uiasr.params.OfflineRecogParams;
import com.baidu.aip.asrwakeup3.uiasr.params.OnlineRecogParams;
import com.worth.framework.base.core.utils.AppManagerKt;
import com.worth.framework.base.core.utils.L;
import com.worth.framework.business.ext.ContactsKt;

import java.util.Map;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 2:03 PM
 * Description: This is RecordUtils 短录音的辅助类
 */
public class RecordUtils {

    private static final String TAG = "RecordUtils";
    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;
    /**
     * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
     */
    protected boolean enableOffline;
    /**
     * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
     */
    private CommonRecogParams apiParams = new OnlineRecogParams();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ContactsKt.SPEAK_FINISH:           //  用户输入语言结束，并且有一定的有效结果检测出来
                    if (msg.obj != null && !msg.obj.toString().trim().isEmpty()) {
                        Log.e(TAG, "结果：" + msg.obj);

                        start();
                    }
                    break;
            }
        }
    };
    private Context context;

    public void init() {
        context = AppManagerKt.getApplication();
        apiParams.initSamplePath(context);

        // 基于DEMO集成第1.1, 1.2, 1.3 步骤 初始化EventManager类并注册自定义输出事件
        // DEMO集成步骤 1.2 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
        IRecogListener listener = new MessageStatusRecogListener(mHandler);
        // DEMO集成步骤 1.1 1.3 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        myRecognizer = new MyRecognizer(context, listener);


//        if (enableOffline) {
//            // 基于DEMO集成1.4 加载离线资源步骤(离线时使用)。offlineParams是固定值，复制到您的代码里即可
//            Map<String, Object> offlineParams = OfflineRecogParams.fetchOfflineParams();
//            myRecognizer.loadOfflineEngine(offlineParams);
//        }
        // BluetoothUtil.start(this,BluetoothUtil.FULL_MODE); // 蓝牙耳机开始，注意一部分手机这段代码无效
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     * 基于DEMO集成2.1, 2.2 设置识别参数并发送开始事件
     */
    public void start() {
        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        final Map<String, Object> params = fetchParams();
        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印


//        L.i("设置的start输入参数：" + params);
//        // 复制此段可以自动检测常规错误
//        (new AutoCheck(context, new Handler() {
//            public void handleMessage(Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
//                        L.e(message + "\n");
//                    }
//                }
//            }
//        }, enableOffline)).checkAsr(params);


        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        // DEMO集成步骤2.2 开始识别
        myRecognizer.start(params);
    }

    protected Map<String, Object> fetchParams() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        Map<String, Object> params = apiParams.fetch(sp);
        //  集成时不需要上面的代码，只需要params参数。
        return params;
    }

    /**********************************************************************************************/

    /**
     * 暂停实时 or 短音频模式--手动释放一直处于录音状态
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     * 发送停止事件 停止录音
     */
    public void stopRecord() {
        myRecognizer.stop();
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     * 基于DEMO集成4.2 发送取消事件 取消本次识别
     */
    protected void cancel() {
        myRecognizer.cancel();
    }

    /**
     * 销毁时需要释放识别资源。
     */
    protected void release() {
        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        // 基于DEMO5.1 卸载离线资源(离线时使用) release()方法中封装了卸载离线资源的过程
        // 基于DEMO的5.2 退出事件管理器
        myRecognizer.release();
        // BluetoothUtil.destory(this); // 蓝牙关闭
    }

    /**********************************************************************************************/

    /**********************************************************************************************/

    /**********************************************************************************************/

    /**********************************************************************************************/

    private RecordUtils() {
    }

    /**
     * 定义一个私有的内部类，在第一次用这个嵌套类时，会创建一个实例。而类型为SingletonHolder的类，
     * 只有在Singleton.getInstance()中调用，由于私有的属性，他人无法使用SingleHolder，
     * 不调用Singleton.getInstance()就不会创建实例。
     * 优点：达到了lazy loading的效果，即按需创建实例。
     */
    private static class SingletonHolder {
        public static RecordUtils instance = new RecordUtils();
    }

    public static RecordUtils ins() {
        return SingletonHolder.instance;
    }
    /**********************************************************************************************/

}
