package com.example.youthspots.data.service

import com.example.youthspots.data.entity.PointImage
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PointImageService {
    @GET("image/{id}")
    fun getImages(@Path("id") id: Long): Call<List<PointImage>>

    @POST("image/add/{id}")
    fun addImage(
        @Header("Authorization") credentials: String,
        @Path("id") id: Long,
        image: RequestBody
    ): Call<PointImage>
}