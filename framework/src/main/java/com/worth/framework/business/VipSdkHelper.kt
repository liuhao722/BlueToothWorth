package com.worth.framework.business

import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream
import com.worth.framework.R
import com.worth.framework.base.core.utils.application
import com.worth.framework.business.utils.RecordUtils
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
        InFileStream.setContext(application)
        SpeakUtils.ins().init()
        WakeUpUtils.ins().init()
        RecordUtils.ins().init()
    }

    /**
     * 启动实时 or 短音频模式--录音内容进行网络请求
     */
    fun startRecord() {
        RecordUtils.ins().startRecord()
    }

    /**
     * 暂停实时 or 短音频模式--手动释放一直处于录音状态
     */
    fun stopRecord() {
        RecordUtils.ins().stopRecord()
    }

    /**
     * 唤醒，用户输入了内容，直接调用sdk方法进行查询结果进行返回
     */
    fun wakeUpWithInputText(text: String, block: (String) -> Unit) {
        stopRecord()    //  先停止录音内容
        if (text.isNullOrEmpty()) {
            SpeakUtils.ins().speak(application?.getString(R.string.str_sdk_def_check_input_is_empty)) {
                startRecord()   //  播放完默认的回复后自动再开启录音内容
            }
        } else {
            SpeakUtils.ins().speak("搜索内容为：$text ，正在为您努力的搜索中") {

            }
            toNetWork(text, block)
        }
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
    private fun toNetWork(text: String, block: (String) -> Unit) {

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
