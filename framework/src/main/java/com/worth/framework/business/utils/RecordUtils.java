package com.worth.framework.business.utils;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 2:03 PM
 * Description: This is RecordUtils 短录音的辅助类
 */
public class RecordUtils {
    private static final String TAG = "RecordUtils";

    /**********************************************************************************************/

    /**
     * 启动实时 or 短音频模式--录音内容进行网络请求
     */
    public void startRecord() {

    }

    /**
     * 暂停实时 or 短音频模式--手动释放一直处于录音状态
     */
    public void stopRecord() {

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
