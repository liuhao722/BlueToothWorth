package com.worth.framework.business.ext

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 6:33 PM
 * Description: This is Contacts 使用顶级函数方式
 */
const val SPEAK_UTILS_PLAY_FINISH = 0x1000_001                                                      //  播放一段指定的音频结束
const val SPEAK_UTILS_PLAY_START = 0x1000_002                                                       //  开始播放一段指定的语音
const val SPEAK_UTILS_PLAY_PROCESS = 0x1000_003                                                     //  播放中
const val SPEAK_UTILS_PLAY_ERROR = 0x1000_004                                                       //  播放失败

const val USER_INPUT_SPEAK_ASR_FINISH = 0x1000_100                                                  //  检测到用户输入的语言结束

const val NETWORK_RESULT_OK = 0x1000_200                                                            //  根据用户请求的数据返回结果了

const val WAKEUP_XIAO_BANG_SDK_SUCCESS = 0x1000_300                                                 //  唤醒成功

const val WAKEUP_XIAO_BANG_SDK_ERROR = 0x1000_301                                                   //  唤醒失败

const val EVENT_WITH_USER_INPUT_RESULT = "event_with_input_result"                                  //  网络请求返回后发送LDBus进行事件的回调，dialog中进行展示用的
const val EVENT_WITH_INPUT_ASR_RESULT = "event_with_input_asr_result"                               //  网络请求ASR的返回结果

const val ERROR_CALL_BACK = "error_call_back"                                                       //  网络失败-返回给app端自定义处理操作
const val CALL_BACK_NET_WORKER_DISCONNECT = 0x1000_400                                              //  网络失败-网络未连接
const val CALL_BACK_SDK_SPEAK_ERROR = 0x1000_401                                                    //  播放语音部分Sdk失败
const val CALL_BACK_SDK_RECORD_ERROR = 0x1000_402                                                   //  录音的asr过程中Sdk执行失败
const val CALL_BACK_SDK_WAKE_UP_ERROR = 0x1000_403                                                  //  唤醒失败Sdk失败


const val NET_WORK_REQUEST_START = "net_work_request_start"                                         //  网络请求开始--给dialog内容显示用的
const val NET_WORK_REQUEST_FINISH = "net_work_request_finish"                                       //  网络请求结束--给dialog内容显示用的
const val MAIN_DIALOG_RE_WAKE_UP = "main_dialog_re_wake_up"                                         //  再次呼叫--开关是开的状态，所以是可以被唤醒的
const val ASR_EXIT = "asr_exit"                                                                     //  asr识别结束


const val LD_BUS_CLICK_KEY = "ld_bus_click_key"                                                     //  监听会员卡点击一次的事件传递，会在蓝牙sdk中进行一个传递，此时应该监听进行唤醒





