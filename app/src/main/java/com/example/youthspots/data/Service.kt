package com.example.youthspots.data

import com.example.youthspots.data.service.ServerService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service {

    val service: ServerService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("127.0.0.1:8000") //todo
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        service = retrofit.create(ServerService::class.java)
    }
}