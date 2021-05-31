package com.worth.framework.base.network.apiServices

import com.worth.framework.base.network.bean.ResultData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 8:20 PM
 * Description: This is ApiServices
 */
interface ApiServices {
    @POST("/api/v1/service/chat")
    fun login(@Body map: RequestBody?): Call<ResultData?>?

    @GET("/api/v1/service/chat/{owner}/{repo}")
    suspend fun test(@Path("owner") owner: String?, @Path("repo") repo: String?): Call<ResultData?>?
}