package com.worth.framework.base.core.storage

/**
 * 封装简化获取存储的数据
 */
const val user_id = "me_kv_user_id"
const val app_host = "me_kv_app_host"

/**
 * 存储类
 */
object MeKV {
    /**
     *
     */
    fun setUserId(padType: String) = MeKVUtil.set(user_id, padType)

    /**
     *
     */
    fun getUserId(): String = MeKVUtil.get(user_id, "1001")

    /**
     * 获取当前的imei
     */
    fun getHost() = MeKVUtil.get(app_host, "http://192.168.0.103:8080")

    /**
     * 设置当前的imei
     */
    fun setHost(imei: String) = MeKVUtil.set(app_host, imei)

}