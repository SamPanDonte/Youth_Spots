package com.example.youthspots.data

import android.content.Context
import android.graphics.Bitmap
import com.example.youthspots.MainApplication
import com.example.youthspots.data.entity.Point
import com.example.youthspots.data.entity.PointComment
import com.example.youthspots.data.entity.PointImage
import com.example.youthspots.data.entity.PointRating
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

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

    private val database = CacheDatabase.getInstance(MainApplication.context)

    fun getPoints() = database.getPointDao().getPoints()

    fun getPoint(id: Long) = database.getPointDao().getPoint(id)

    fun getPointTypes() = database.getPointTypeDao().getPointTypes()

    fun addPoint(point: Point) {
        GlobalScope.async {
            database.getPointDao().insert(point)
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

    fun getComments(pointId: Long) = database.getPointCommentDao().getComments(pointId)

    fun getImages(pointId: Long) = database.getPointImageDao().getImages(pointId)

    fun addComment(pointComment: PointComment) {
        GlobalScope.async {
            database.getPointCommentDao().insert(pointComment)
        }
    }

    fun getTopUsers() = database.getUserDao().getTopUsers()

    fun getMyRanking() = database.getUserDao().getMyRanking()

    fun getMyPointRating(pointId: Long) = database.getPointRatingDao().getRating(
        pointId,
        getFromSharedPreferences(LOGIN_TAG)
    )

    fun ratePoint(pointRating: PointRating) {
        GlobalScope.async {
            database.getPointRatingDao().insert(pointRating)
        }
    }

    fun addPicture(pointId: Long, imageBitmap: Bitmap) {
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, MainApplication.context.openFileOutput("test", Context.MODE_PRIVATE))
        GlobalScope.async {
            database.getPointImageDao().insert(PointImage("test", pointId))
        }
    }
}