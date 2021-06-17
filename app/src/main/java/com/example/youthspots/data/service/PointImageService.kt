package com.example.youthspots.data.service

import com.example.youthspots.data.entity.PointImage
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PointImageService {
    @GET("image/{id}")
    fun getImages(@Path("id") id: Long): Call<List<PointImage>>

    @Multipart
    @POST("image/add/{id}")
    fun addImage(
        @Header("Authorization") credentials: String,
        @Path("id") id: Long,
        @Part image: MultipartBody.Part
    ): Call<PointImage>
}