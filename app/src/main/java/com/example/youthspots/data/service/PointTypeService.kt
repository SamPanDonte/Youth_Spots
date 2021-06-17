package com.example.youthspots.data.service

import com.example.youthspots.data.entity.PointType
import retrofit2.Call
import retrofit2.http.GET

interface PointTypeService {
    @GET("type")
    fun getTypes(): Call<List<PointType>>
}