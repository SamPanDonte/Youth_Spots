package com.example.youthspots.data.service

import com.example.youthspots.data.entity.PointComment
import retrofit2.Call
import retrofit2.http.*

interface PointCommentService {
    @GET("comment/{id}")
    fun getComments(@Path("id") id: Long): Call<List<PointComment>>

    @POST("comment/add")
    fun addComment(
        @Header("Authorization") credentials: String,
        @Body comment: PointComment
    ): Call<PointComment>
}