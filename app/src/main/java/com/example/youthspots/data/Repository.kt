package com.example.youthspots.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.youthspots.MainApplication
import com.example.youthspots.data.entity.Point
import com.example.youthspots.data.entity.PointComment
import com.example.youthspots.data.entity.PointRating
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.util.*

object Repository {
    private const val PREFERENCES_TAG = "YouthSpotsSP"
    const val LOGIN_TAG = "Login"
    const val API_KEY_TAG = "ApiKey"
    const val CAMERA_LONG_TAG = "CameraLong"
    const val CAMERA_LAT_TAG = "CameraLat"
    const val CAMERA_ZOOM_TAG = "CameraZoom"
    const val CAMERA_TILT_TAG = "CameraTilt"
    const val CAMERA_BEARING_TAG = "CameraBearing"
    const val AD_COUNTER_TAG = "AdCounter"
    private const val LAST_SYNC_TAG = "LastSync"

    private val database = CacheDatabase.getInstance(MainApplication.context)
    private val serverService = Service.service

    private fun syncWithServer() {
        if (getFromSharedPreferences(LAST_SYNC_TAG, "1").toLong() + syncTime() * 3600L > Calendar.getInstance().timeInMillis) {
            return
        }
        Log.d("SYNC", "SYNC DOING")
        saveInSharedPreferences(LAST_SYNC_TAG, Calendar.getInstance().timeInMillis.toString())
        GlobalScope.async {
            val types = serverService.getTypes().execute()
            if (types.isSuccessful) {
                database.getPointTypeDao().clearPointTypeCache()
                for (type in types.body()!!) {
                    database.getPointTypeDao().insert(type)
                }
            }
            val response = serverService.getPoints(0.0, 0.0).execute()
            if (response.isSuccessful) {
                database.getPointDao().clearPointCache()
                for (point in response.body()!!) {
                    database.getPointDao().insert(point)
                    val comments = serverService.getComments(point.id).execute()
                    if (comments.isSuccessful) {
                        database.getPointCommentDao().clearPointCommentCache()
                        for (comment in comments.body()!!) {
                            database.getPointCommentDao().insert(comment)
                        }
                    }
                    val ratings = serverService.getRating(point.id).execute()
                    if (ratings.isSuccessful) {
                        database.getPointRatingDao().clearPointRatingCache()
                        for (rating in ratings.body()!!) {
                            database.getPointRatingDao().insert(rating)
                        }
                    }
                    val images = serverService.getImages(point.id).execute()
                    if (images.isSuccessful) {
                        database.getPointImageDao().clearPointImageCache() // TODO
                        for (image in images.body()!!) {
                            database.getPointImageDao().insert(image)
                        }
                    }
                }
            }
        }
    }

    fun getPoints() {
        syncWithServer()
        database.getPointDao().getPoints()
    }

    fun getPoint(id: Long) {
        syncWithServer()
        database.getPointDao().getPoint(id)
    }

    fun getPointTypes() {
        syncWithServer()
        database.getPointTypeDao().getPointTypes()
    }

    fun addPoint(point: Point) {
        GlobalScope.async {
            val result = serverService.addPoint(getFromSharedPreferences(API_KEY_TAG), point).execute()
            if (result.isSuccessful) {
                database.getPointDao().insert(result.body()!!)
            }
        }
    }

    fun getFromSharedPreferences(tag: String, default: String = "") : String {
        val sp = MainApplication.context.getSharedPreferences(PREFERENCES_TAG, Context.MODE_PRIVATE)
        return sp.getString(tag, default)!!
    }

    fun saveInSharedPreferences(tag: String, value: String) {
        val editor = MainApplication.context.getSharedPreferences(PREFERENCES_TAG, Context.MODE_PRIVATE).edit()
        editor.putString(tag, value)
        editor.apply()
    }

    fun userLoggedIn() = MainApplication.context.getSharedPreferences(PREFERENCES_TAG, Context.MODE_PRIVATE).contains(LOGIN_TAG)

    fun getComments(pointId: Long) {
        syncWithServer()
        database.getPointCommentDao().getComments(pointId)
    }

    fun getImages(pointId: Long) {
        syncWithServer()
        database.getPointImageDao().getImages(pointId)
    }

    fun addComment(pointComment: PointComment) {
        GlobalScope.async {
            val result = serverService.addComment(getFromSharedPreferences(API_KEY_TAG), pointComment).execute()
            if (result.isSuccessful) {
                database.getPointCommentDao().insert(result.body()!!)
            }
        }
    }

    fun getTopUsers() {
        syncWithServer()
        database.getUserDao().getTopUsers()
    }

    fun getMyRanking() {
        syncWithServer()
        database.getUserDao().getMyRanking()
    }

    fun getMyPointRating(pointId: Long) = database.getPointRatingDao().getRating(
        pointId,
        getFromSharedPreferences(LOGIN_TAG)
    )

    fun ratePoint(pointRating: PointRating) {
        GlobalScope.async {
            val result = serverService.addRating(getFromSharedPreferences(API_KEY_TAG), pointRating).execute()
            if (result.isSuccessful) {
                database.getPointRatingDao().insert(result.body()!!)
            }
        }
    }

    fun addPicture(pointId: Long, imageBitmap: Bitmap) {
        //imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, MainApplication.context.openFileOutput("temp", Context.MODE_PRIVATE)) // TODO
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        GlobalScope.async {
            val result = serverService.addImage(
                getFromSharedPreferences(API_KEY_TAG),
                pointId,
                RequestBody.create(MediaType.parse("image/png"), stream.toByteArray())
            ).execute()
            if (result.isSuccessful) {
                database.getPointImageDao().insert(result.body()!!) // TODO
            }
        }
    }

    fun autoLogin() = PreferenceManager.getDefaultSharedPreferences(MainApplication.context).getBoolean("autologin", true)

    private fun syncTime() = PreferenceManager.getDefaultSharedPreferences(MainApplication.context).getString("sync_time", "1h")!![0].code

    fun logOut() {
        val editor = MainApplication.context.getSharedPreferences(PREFERENCES_TAG, Context.MODE_PRIVATE).edit()
        editor.remove(LOGIN_TAG)
        editor.remove(API_KEY_TAG)
        editor.apply()
    }
}