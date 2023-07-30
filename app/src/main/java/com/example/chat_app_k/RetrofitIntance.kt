package com.example.chat_app_k

import com.example.chat_app_k.Contans.Contans
import com.example.chat_app_k.Interface.ApiSevice
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitIntance {
    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Contans.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(ApiSevice::class.java)
        }
    }
}