package com.worth.framework.business.ext

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 6:33 PM
 * Description: This is Contacts
 */
const val SPEAK_UTILS_PLAY_FINISH = 0x1000_001                                                      //  播放一段指定的音频结束
const val SPEAK_UTILS_PLAY_START = 0x1000_002                                                       //  开始播放一段指定的语音
const val SPEAK_UTILS_PLAY_PROCESS = 0x1000_003                                                     //  播放中
const val SPEAK_UTILS_PLAY_ERROR = 0x1000_004                                                       //  播放失败

const val USER_INPUT_SPEAK_ASR_FINISH = 0x1000_100                                                  //  检测到用户输入的语言结束

const val NETWORK_RESULT = 0x1000_200                                                               //  根据用户请求的数据返回结果了

const val EVENT_WITH_USER_INPUT_RESULT = "event_with_input_result"                                  //  网络请求返回后发送LDBus进行事件的回调
