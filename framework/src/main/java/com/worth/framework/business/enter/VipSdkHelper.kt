package com.worth.framework.business.enter

import com.worth.framework.R
import com.worth.framework.base.core.storage.MeKV
import com.worth.framework.base.core.storage.MeKVUtil
import com.worth.framework.base.core.utils.LDBus
import com.worth.framework.base.core.utils.application
import com.worth.framework.business.ext.EVENT_WITH_USER_INPUT_RESULT
import com.worth.framework.business.utils.GlobalHandler
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
        application?.let {
            MeKVUtil.initMMKV(it)
            SpeakUtils.ins().init(it)
            WakeUpUtils.ins().init(it)
            RecordUtils.ins().init(it)
        }
    }

    @JvmOverloads
    fun initSdk(host: String?, uid: String?, isOpenSdk: Boolean) {
        if(!isOpenSdk) {
            SpeakUtils.ins().stopSpeak()
            WakeUpUtils.ins().stopListener()
            RecordUtils.ins().stopRecord()
        }
        host?.run { MeKV.setHost(this) }
        uid?.run { MeKV.setUserId(this) }
    }

    /**
     * 查询的网络数据返回后
     */
    fun netWorkResult(result: String) {
        LDBus.sendSpecial(EVENT_WITH_USER_INPUT_RESULT, result)
    }

    /**
     * 启动实时 or 短音频模式--录音内容进行网络请求
     */
    fun startRecord() {
        SpeakUtils.ins().stopSpeak()
        WakeUpUtils.ins().stopListener()
        RecordUtils.ins().startRecord()
    }

    /**
     * 暂停实时 or 短音频模式--手动释放一直处于录音状态
     */
    fun stopRecord() {
        WakeUpUtils.ins().startListener()
        RecordUtils.ins().stopRecord()
        RecordUtils.ins().cancel()
    }

    /**
     * 唤醒，用户输入了内容，直接调用sdk方法进行查询结果进行返回
     */
    fun wakeUpWithInputText(text: String) {
        SpeakUtils.ins().stopSpeak()
        WakeUpUtils.ins().startListener()
        RecordUtils.ins().stopRecord()
        RecordUtils.ins().cancel()
        toNetWork(text)
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
    private fun toNetWork(text: String) {
        GlobalHandler.ins().requestServer(text)
    }

    /**
     * 消亡方法进行数据的清理和对象的释放
     */
    fun destroy() {
        RecordUtils.ins().release()
        SpeakUtils.ins().release()
        WakeUpUtils.ins().release()
        GlobalHandler.ins().mHandler.get()?.removeCallbacksAndMessages(null)
        GlobalHandler.ins().mHandler.clear()
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
