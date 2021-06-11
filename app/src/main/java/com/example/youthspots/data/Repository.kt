package com.example.youthspots.data

import android.content.Context
import com.example.youthspots.MainApplication
import com.example.youthspots.data.entity.Point
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
    private val database = CacheDatabase.getInstance(MainApplication.context)

    fun getPoints() = database.getPointDao().getPoints()

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
}