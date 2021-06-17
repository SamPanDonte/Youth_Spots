package com.example.youthspots.data.service

import com.example.youthspots.data.entity.AuthData
import com.example.youthspots.data.entity.Token
import com.example.youthspots.data.entity.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST("login")
    fun login(@Body credentials: AuthData): Call<Token>

    @POST("register")
    fun register(@Body credentials: AuthData): Call<Token>

    @GET("user")
    fun getUsers() : Call<List<User>>
}