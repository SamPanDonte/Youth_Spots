package com.example.youthspots.data

import com.example.youthspots.data.service.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerDatabase {

    val userService: UserService
    val pointImageService: PointImageService
    val pointCommentService: PointCommentService
    val pointRatingService: PointRatingService
    val pointService: PointService
    val pointTypeService: PointTypeService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sampandonte.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        userService = retrofit.create(UserService::class.java)
        pointImageService = retrofit.create(PointImageService::class.java)
        pointCommentService = retrofit.create(PointCommentService::class.java)
        pointRatingService = retrofit.create(PointRatingService::class.java)
        pointService = retrofit.create(PointService::class.java)
        pointTypeService = retrofit.create(PointTypeService::class.java)
    }
}