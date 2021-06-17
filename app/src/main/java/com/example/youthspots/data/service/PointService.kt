package com.example.youthspots.data.service

import com.example.youthspots.data.entity.Point
import retrofit2.Call
import retrofit2.http.*

interface PointService {
    @GET("point")
    fun getPoints(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ): Call<List<Point>>

    @GET("point/{id}")
    fun getPoint(@Path("id") id: Long): Call<Point>

    @POST("point/add")
    fun addPoint(
        @Header("Authorization") credentials: String,
        @Body point: Point
    ): Call<Point>

    @POST("report/{id}")
    fun reportPoint(
        @Header("Authorization") credentials: String,
        @Path("id") id: Long
    ): Call<String>
}