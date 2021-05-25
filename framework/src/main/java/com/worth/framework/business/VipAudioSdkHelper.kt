package com.worth.framework.business

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/25/21 --> 4:45 PM
 * Description: This is VipAudioSdkHelper
 */
class VipAudioSdkHelper private constructor() {
    /**
     * 初始化操作
     */
    fun init() {

    }

    /**
     * 启动实时 or 短音频模式
     */
    fun startRecord() {

    }

    /**
     * 暂停实时 or 短音频模式
     */
    fun stopRecord() {

    }

    /**
     * 唤醒，用户输入了内容，直接调用sdk方法进行查询结果进行返回
     */
    fun wakeUpWithInputText(text: String) {

    }

    /**
     * 唤醒语音助手-用户点击了界面
     */
    fun wakeUpWithClickBtn() {
        startRecord()
    }

    /**
     * 百度云返回的文字信息
     */
    fun setBaiDuYunResult(): String {
        return "百度云返回的文字信息"
    }

    /**
     * 文本转音频,转换完成直接播放
     */
    fun textToAudio(text: String) {

    }

    /**
     * 对象单例
     */
    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = VipAudioSdkHelper()
    }
}
