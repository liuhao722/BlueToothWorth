package com.worth.framework.business.ext

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    6/9/21 --> 7:13 AM
 * Description: This is ToAppCodeList
 */
object ToAppContactsCodes {
    const val SDK_TO_APP_EVENT_CODES = "sdk_to_app_codes"                                           //  sdk将目前所有的code都发送到app端一份

    const val SPEAK_UTILS_PLAY_FINISH = 10000_001                                                   //  播放一段指定的音频结束
    const val SPEAK_UTILS_PLAY_START = 10000_002                                                    //  开始播放一段指定的语音
    const val SPEAK_UTILS_PLAY_PROCESS = 10000_003                                                  //  播放中
    const val SPEAK_UTILS_PLAY_ERROR = 10000_004                                                    //  播放失败
    const val USER_INPUT_SPEAK_ASR_FINISH = 10000_005                                               //  检测到用户输入的语言结束
    const val NETWORK_RESULT_OK = 10000_006                                                         //  根据用户请求的数据返回结果了
    const val WAKEUP_XIAO_BANG_SDK_SUCCESS = 10000_007                                              //  唤醒成功
    const val WAKEUP_XIAO_BANG_SDK_ERROR = 10000_008                                                //  唤醒失败
    const val EVENT_WITH_USER_INPUT_RESULT = 10000_009                                              //  网络请求返回后发送LDBus进行事件的回调，dialog中进行展示
    const val EVENT_WITH_INPUT_ASR_RESULT = 10000_010                                               //  网络请求ASR的返回结果
    const val NET_WORKER_DISCONNECT = 10000_011                                                     //  网络失败-网络未连接
    const val RECORD_ERROR = 10000_012                                                              //  录音的asr过程中Sdk执行失败
    const val NET_WORKER_REQUEST_ERROR = 10000_013                                                  //  请求服务失败-非200
    const val NET_WORK_REQUEST_START = 10000_014                                                    //  网络请求开始--给dialog内容显示用的
    const val NET_WORK_REQUEST_FINISH = 10000_015                                                   //  网络请求结束--给dialog内容显示用的
    const val MAIN_DIALOG_RE_WAKE_UP = 10000_016                                                    //  再次呼叫--开关是开的状态，可以被唤醒的

}