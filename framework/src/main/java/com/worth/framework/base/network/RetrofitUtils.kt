package com.worth.framework.base.network

import android.text.TextUtils
import android.util.Log
import com.worth.framework.base.core.storage.MeKV
import com.worth.framework.base.core.utils.L
import com.worth.framework.base.core.utils.LDBus.sendSpecial2
import com.worth.framework.base.network.apiServices.ApiServices
import com.worth.framework.business.enter.VipSdkHelper
import com.worth.framework.business.ext.ToAppContactsCodes
import com.worth.framework.business.ext.ToAppContactsCodes.SDK_TO_APP_EVENT_CODES
import com.worth.framework.business.global.mHttpBody
import com.worth.framework.business.global.mHttpHeaders
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

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
                .client(addHeaders())
                .build()
            val retrofitApi = retrofit.create(ApiServices::class.java)
            var map: HashMap<String, Any?> = HashMap()
            map[MeKV.getAiInstructionSetKey()] = queryWord
            mHttpBody?.mapKeys {
                Log.e(TAG, it.key + it.value)
                map.put(it.key, it.value)
            }

            try {
                val jsonStr = JSONObject(map).toString()
                val body = RequestBody.create(json, jsonStr)
                val result = retrofitApi.getRefResult(body)?.await()
                result?.run {           //  返回结果不为空
                    L.e(TAG, toString())
                    when (code) {
                        200 -> {
                            block.invoke(this.result)
                        }
                        else -> {
                            sendSpecial2(
                                SDK_TO_APP_EVENT_CODES,
                                ToAppContactsCodes.NET_WORKER_REQUEST_ERROR,
                                this
                            )
                            block.invoke("")
                        }
                    }
                } ?: run {
                    block.invoke("")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                block.invoke("")
            }
        }
    }

    private val logEnable: Boolean = true   //  日志开关

    /**
     * 对头部进行操作
     */
    private fun addHeaders(): OkHttpClient {
        val header = mHttpHeaders
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            header?.mapKeys {
                if (!it.key.isNullOrEmpty() && it.value != null){
                    request.header(it.key, it.value as String)
                }
            }
            request.method(original.method(), original.body())
            chain.proceed(request.build())
        }.addInterceptor(HttpLoggingInterceptor().apply {
            level = when (logEnable) {
                true -> HttpLoggingInterceptor.Level.BODY
                false -> HttpLoggingInterceptor.Level.BASIC
            }
        })
        return httpClient.build()
    }

    /**
     *
     * .addInterceptor(HttpLoggingInterceptor().apply {
    level = when (logEnable) {
    true -> HttpLoggingInterceptor.Level.BODY
    false -> HttpLoggingInterceptor.Level.BASIC
    }
    }).addInterceptor(ErrorInterceptor())
    //            .addInterceptor(RequestHeadInterceptor())
    .retryOnConnectionFailure(true).build()
     */

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