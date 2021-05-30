package com.worth.framework.base.network

import android.util.Log
import com.worth.framework.base.core.storage.MeKV
import com.worth.framework.base.core.utils.L
import com.worth.framework.base.network.apiServices.ApiServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 8:15 PM
 * Description: This is RetrofitUtils
 */
class RetrofitUtils private constructor() {
    private val baseUrl = MeKV.getHost()
    private val json = MediaType.parse("application/json; charset=utf-8")

    /**
     * 网络请求
     */
    fun requestServer(queryWord: String?, block: (String) -> Unit) {
        GlobalScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val retrofitApi = retrofit.create(ApiServices::class.java)
            var map: HashMap<String, Any?> = HashMap<String, Any?>()
            map["logId"] = UUID.randomUUID()
            map["query"] = queryWord
            map["userId"] = MeKV.getUserId()
            try {
                val jsonStr = JSONObject(map).toString()
                val body = RequestBody.create(json, jsonStr)
                val result = retrofitApi.login(body)?.await()
                result?.run {
                    L.e(TAG, result.toString())
                    block.invoke(result.result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                block.invoke("")
            }
        }
    }

    private object SingletonHolder {
        var instance = RetrofitUtils()
    }

    companion object {
        private const val TAG = "RetrofitUtils"

        @JvmStatic
        fun ins(): RetrofitUtils {
            return SingletonHolder.instance
        }
    }
}