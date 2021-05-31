package com.worth.framework.business.ext

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 6:33 PM
 * Description: This is Contacts
 */
const val PLAY_FINISH = 0x1000_001                                          //  播放音频结束
const val PLAY_START = 0x1000_002                                           //  开始播放语音
const val PLAY_PROCESS = 0x1000_003                                         //  播放中
const val PLAY_ERROR = 0x1000_004                                           //  播放失败

const val SPEAK_FINISH = 0x1000_100                                         //  检测到用户输入的语言结束

const val NETWORK_RESULT = 0x1000_200                                       //  根据用户请求的数据返回结果了

const val EVENT_WITH_INPUT_RESULT = "event_with_input_result" //  网络请求返回后发送LDBus进行事件的回调
