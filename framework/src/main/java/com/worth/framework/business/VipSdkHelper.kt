package com.worth.framework.business

import com.worth.framework.business.utils.SpeakUtils
import com.worth.framework.business.utils.WakeUpUtils

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/25/21 --> 4:45 PM
 * Description: This is VipSdkHelper
 */
class VipSdkHelper private constructor() {
    /**
     * 初始化操作
     */
    init {
        SpeakUtils.ins().init()
        WakeUpUtils.ins().init()
    }

    /**
     * 启动实时 or 短音频模式--录音内容进行网络请求
     */
    fun startRecord() {

    }

    /**
     * 暂停实时 or 短音频模式--手动释放一直处于录音状态
     */
    fun stopRecord() {

    }

    /**
     * 唤醒，用户输入了内容，直接调用sdk方法进行查询结果进行返回
     */
    fun wakeUpWithInputText(text: String, block: () -> Unit) {
        toNetWork(text, block)
    }

    /**
     * 唤醒语音助手-用户点击了界面
     */
    fun wakeUpWithClickBtn() {
        WakeUpUtils.ins().wakeUp()
    }

    /**
     * 网络请求具体的业务场景，进行一个回调，手动输入可以直接进行业务的返回，其他场景想办法看怎么传递回去
     */
    private fun toNetWork(text: String, block: () -> Unit) {

    }


    /**
     * 对象单例
     */
    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = VipSdkHelper()
    }
}
