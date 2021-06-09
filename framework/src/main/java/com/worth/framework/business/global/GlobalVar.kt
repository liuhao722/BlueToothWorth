package com.worth.framework.business.global


/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    6/2/21 --> 7:58 PM
 * Description: This is GlobalVar 全局的常量或者变量
 */
@JvmField
var speakFinishWhenWakeUpCall: Boolean = true   //  默认的朗读完要发音的txt后，检测是来自于唤醒的自发声，还是网络返回的发声
@JvmField
var mHttpHeaders: MutableMap<String, Any>? = null   //  获取用户设置的header
@JvmField
var mHttpBody: MutableMap<String, Any>? = null      //  获取用户设置的body
@JvmField
var mQuickList: MutableList<String>? = null         //  获取用户设置的快捷入口
