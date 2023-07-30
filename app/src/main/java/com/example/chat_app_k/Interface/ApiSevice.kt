package com.example.chat_app_k.Interface

import com.example.chat_app_k.Contans.Contans.Companion.CONTENT_TYPE
import com.example.chat_app_k.Contans.Contans.Companion.SERVER_KEY
import com.example.chat_app_k.Model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiSevice {

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification:PushNotification
    ):Response<ResponseBody>
}