package com.worth.framework.base.core.storage

/**
 * 封装简化获取存储的数据
 */
const val user_type = "me_kv_user_type"
const val app_imei = "me_kv_app_imei"

/**
 * 存储类
 */
object MeKV {
    /**
     *
     */
    fun setUserType(padType: Int) = MeKVUtil.set(user_type, padType)

    /**
     *
     */
    fun getUserType(): Int = MeKVUtil.get(user_type, 0)

    /**
     * 获取当前的imei
     */
    fun getImei() = MeKVUtil.get(app_imei, "")

    /**
     * 设置当前的imei
     */
    fun setImei(imei: String) = MeKVUtil.set(app_imei, imei)

}