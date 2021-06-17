package com.example.youthspots.data.service

import com.example.youthspots.data.entity.PointRating
import retrofit2.Call
import retrofit2.http.*

interface PointRatingService {
    @GET("rating/{id}")
    fun getRating(@Path("id") id: Long): Call<List<PointRating>>

    @POST("rating/add")
    fun addRating(
        @Header("Authorization") credentials: String,
        @Body rating: PointRating
    ): Call<PointRating>
}