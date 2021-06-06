package com.worth.framework.base.core.storage

/**
 * 封装简化获取存储的数据
 */
const val USER_ID = "me_kv_user_id"
const val APP_HOST = "me_kv_app_host"
const val WAKE_UP_SWITCH = "app_set_wake_up_switch"
const val QUICK_ENTER_LIST = "app_set_quick_enter_list"
const val SDK_HTTP_HEADER = "sdk_http_header"
const val SDK_HTTP_BODY = "sdk_http_body"

/**
 * 存储类
 */
object MeKV {
    /**
     *设置用户id
     */
    fun setUserId(padType: String) = MeKVUtil.set(USER_ID, padType)

    /**
     *获取用户id
     */
    fun getUserId(): String = MeKVUtil.get(USER_ID, "1001")


    /**********************************************************************************************/
    /**
     * 获取当前的host
     */
    fun getHost() = MeKVUtil.get(APP_HOST, "http://192.168.0.103:8080")

    /**
     * 设置当前的host
     */
    fun setHost(host: String) = MeKVUtil.set(APP_HOST, host)


    /**********************************************************************************************/
    /**
     * 用户设置唤醒的开关状态
     */
    fun setWakeUpSwitch(switch: Boolean) = MeKVUtil.set(WAKE_UP_SWITCH, switch)

    /**
     * 获取用户设置的唤醒开关状态
     */
    fun wakeUpSwitchIsOpen() = MeKVUtil.get(WAKE_UP_SWITCH, false)


    /**********************************************************************************************/

}