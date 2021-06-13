package com.example.youthspots.data.service

import com.example.youthspots.data.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ServerService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") login: String,
        @Field("password") password: String
    ) : Call<Token>

    @POST("report/{id}")
    fun report(
        @Header("Authorization") credentials: String,
        @Path("id") id: Long
    ) : Call<String>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") login: String,
        @Field("password") password: String
    ) : Call<Token>

    @GET("point")
    fun getPoints(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ) : Call<List<Point>>

    @POST("point/add")
    fun addPoint(
        @Header("Authorization") credentials: String,
        @Body point: Point
    ) : Call<Point>

    @GET("point/{id}")
    fun getPoint(@Path("id") id: Long) : Call<Point>

    @GET("type")
    fun getTypes() : Call<List<PointType>>

    @GET("comment/{id}")
    fun getComments(@Path("id") id: Long) : Call<List<PointComment>>

    @POST("comment/add")
    fun addComment(
        @Header("Authorization") credentials: String,
        @Body comment: PointComment
    ) : Call<PointComment>

    @GET("image/{id}")
    fun getImages(@Path("id") id: Long) : Call<List<PointImage>>

    @POST("image/add/{id}")
    fun addImage(
        @Header("Authorization") credentials: String,
        @Path("id") id: Long,
        image: RequestBody
    ) : Call<PointImage>

    @GET("user")
    fun getUsers() : Call<User>

    @GET("rating/{id}")
    fun getRating(@Path("id") id: Long) : Call<List<PointRating>>

    @POST("rating/add")
    fun addRating(
        @Header("Authorization") credentials: String,
        @Body rating: PointRating
    ) : Call<PointRating>
}