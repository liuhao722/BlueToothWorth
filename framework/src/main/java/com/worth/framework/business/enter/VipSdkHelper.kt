package com.worth.framework.business.enter

import com.alibaba.android.arouter.launcher.ARouter
import com.worth.framework.base.core.storage.MeKV
import com.worth.framework.base.core.storage.MeKVUtil
import com.worth.framework.base.core.utils.LDBus
import com.worth.framework.base.core.utils.application
import com.worth.framework.business.ext.CALL_BACK_SDK_NET_WORKER_REQUEST_ERROR
import com.worth.framework.business.ext.ERROR_CALL_BACK
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
    var mHttpHeaders: MutableMap<String, Any>? = null   //  获取用户设置的header
    var mHttpBody: MutableMap<String, Any>? = null      //  获取用户设置的body
    var mQuickList: MutableList<String>? = null         //  获取用户设置的快捷入口

    /**
     * 初始化操作
     */
    init {
        application?.let {
            ARouter.init(it)
            MeKVUtil.initMMKV(it)
            SpeakUtils.ins().init(it)
            WakeUpUtils.ins().init(it)
            RecordUtils.ins().init(it)
        }
    }

    /**
     * 初始化sdk
     * @param host  域名地址
     * @param uid   userId
     * @param httpHeaders httpHeader要在app中设置的内容
     * @param httpBody  body要设置的内容
     */
    @JvmOverloads
    fun initSdk(
        host: String?,
        uid: String?,
        httpHeaders: MutableMap<String, Any>?,
        httpBody: MutableMap<String, Any>?
    ): VipSdkHelper {
        host?.run {
            if (!endsWith("/")) {
                MeKV.setHost("$this/")
            } else {
                MeKV.setHost(this)
            }
        } ?: run { MeKV.setHost("") }
        uid?.run { MeKV.setUserId(this) } ?: run { MeKV.setUserId("") }
        mHttpHeaders = httpHeaders
        mHttpBody = httpBody
        return this
    }

    /**
     * 单独设置uid or 初始化时候设置
     */
    fun setUserId(uid: String?) {
        uid?.run { MeKV.setUserId(this) } ?: run { MeKV.setUserId("") }
    }

    /**
     * 单独设置host or 初始化时候设置
     */
    fun setHost(host: String?) {
        host?.run {
            if (!endsWith("/")) {
                MeKV.setHost("$this/")
            } else {
                MeKV.setHost(this)
            }
        } ?: run { MeKV.setHost("") }
    }

    /**
     * 开启 or 关闭语音服务 对应关闭所有的服务
     * @param   switchOn 开启 or 关闭
     */
    fun switchSdkWakeUp(switchOn: Boolean): VipSdkHelper {
        MeKV.setWakeUpSwitch(switchOn)
        if (!switchOn) {
            SpeakUtils.ins().stopSpeak()
            WakeUpUtils.ins().stopListener()
            RecordUtils.ins().stopRecord()
        }
        return this
    }

    /**
     * 设置快捷入口的list数据
     */
    fun setQuickEnterList(list: MutableList<String>?): VipSdkHelper {
        mQuickList = list
        return this
    }

    /**
     * 启动实时 or 短音频模式--录音内容进行网络请求---对外提供：开启后 仅做唤醒的功能开启
     */
    fun startRecord() {
        SpeakUtils.ins().stopSpeak()
        WakeUpUtils.ins().stopListener()
        RecordUtils.ins().startRecord()
    }

    /**
     * 暂停实时 or 短音频模式--手动释放一直处于录音状态---提供给app调用--所有的状态都关闭
     */
    fun stopRecord() {
        WakeUpUtils.ins().stopListener()
        SpeakUtils.ins().stopSpeak()
        RecordUtils.ins().stopRecord()
        RecordUtils.ins().cancel()
    }

    /**
     * 唤醒，用户输入了内容，直接调用sdk方法进行查询结果进行返回--sdk内部调用
     */
    private fun wakeUpWithInputText(text: String) {
        SpeakUtils.ins().stopSpeak()
        WakeUpUtils.ins().stopListener()
        RecordUtils.ins().stopRecord()
        RecordUtils.ins().cancel()
        toNetWork(text)
    }

    /**
     * 唤醒语音助手-用户点击了界面--app调用
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
     * 消亡方法进行数据的清理和对象的释放--app调用
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
