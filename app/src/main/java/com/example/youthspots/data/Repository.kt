package com.example.youthspots.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.youthspots.MainApplication
import com.example.youthspots.data.entity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody.Part.createFormData
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.util.*

@Suppress("BlockingMethodInNonBlockingContext")
object Repository {
    private const val PREFERENCES_TAG = "YouthSpotsSP"
    private const val LAST_SYNC_TAG = "LastSync"
    const val LOGIN_TAG = "Login"
    const val API_KEY_TAG = "ApiKey"
    const val CAMERA_LONG_TAG = "CameraLong"
    const val CAMERA_LAT_TAG = "CameraLat"
    const val CAMERA_ZOOM_TAG = "CameraZoom"
    const val CAMERA_TILT_TAG = "CameraTilt"
    const val CAMERA_BEARING_TAG = "CameraBearing"
    const val AD_COUNTER_TAG = "AdCounter"

    private val database = CacheDatabase.getInstance(MainApplication.context)
    private val serverDatabase = ServerDatabase
    private val sharedPreferences = MainApplication.context.getSharedPreferences(PREFERENCES_TAG, Context.MODE_PRIVATE)
    private val defaultPreferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.context)

    val credentials: String
        get() = "Token " + fromSP(API_KEY_TAG, "")

    private fun syncFailed() = Log.e("Synchronization", "Synchronization app with server failed")

    private fun syncComments(pointId: Long): Boolean {
        val comments = serverDatabase.pointCommentService.getComments(pointId).execute()
        return if (comments.isSuccessful) {
            database.pointCommentDao.clearPointCommentCache(pointId)
            comments.body()?.forEach { database.pointCommentDao.insert(it) }
            true
        } else {
            syncFailed()
            false
        }
    }

    private fun syncRatings(pointId: Long): Boolean {
        val ratings = serverDatabase.pointRatingService.getRating(pointId).execute()
        return if (ratings.isSuccessful) {
            database.pointRatingDao.clearPointRatingCache(pointId)
            ratings.body()?.forEach { database.pointRatingDao.insert(it) }
            true
        } else {
            syncFailed()
            false
        }
    }

    private fun syncImages(pointId: Long): Boolean {
        val images = serverDatabase.pointImageService.getImages(pointId).execute()
        return if (images.isSuccessful) {
            database.pointImageDao.clearPointImageCache(pointId)
            images.body()?.forEach { downloadImage(it) }
            true
        } else {
            syncFailed()
            false
        }
    }

    private fun syncTypes(): Boolean {
        val types = serverDatabase.pointTypeService.getTypes().execute()
        return if (types.isSuccessful) {
            database.pointTypeDao.clearPointTypeCache()
            types.body()?.forEach { database.pointTypeDao.insert(it) }
            true
        } else {
            syncFailed()
            false
        }
    }

    private fun syncUsers(): Boolean {
        val users = serverDatabase.userService.getUsers().execute()
        return if (users.isSuccessful) {
            database.userDao.clearUserCache()
            users.body()?.forEach { database.userDao.insert(it) }
            true
        } else {
            syncFailed()
            false
        }
    }

    private fun syncPoints(): Boolean {
        val points = serverDatabase.pointService.getPoints(0.0, 0.0).execute() // TODO coordinates
        return if (points.isSuccessful) {
            database.pointDao.clearPointCache()
            points.body()?.forEach {
                database.pointDao.insert(it)
                if (!syncComments(it.id) || !syncRatings(it.id) || !syncImages(it.id)) {
                    return false
                }
            }
            true
        } else {
            syncFailed()
            false
        }
    }

    private fun sync() {
        val now = Calendar.getInstance().timeInMillis
        if (fromSP(LAST_SYNC_TAG, 0) + syncTime() * 3600L * 1000 <= now) {
            saveSP(LAST_SYNC_TAG, now)
            GlobalScope.launch {
                syncUsers()
                if (syncTypes()) {
                    syncPoints()
                }
            }
        }
    }

    private fun syncTime() = defaultPreferences.getString("sync_time", "1h")!![0].code

    fun fromSP(tag: String, default: Long): Long {
        return sharedPreferences.getLong(tag, default)
    }

    fun fromSP(tag: String, default: String): String {
        return sharedPreferences.getString(tag, default)!!
    }

    fun fromSP(tag: String, default: Float): Float {
        return sharedPreferences.getFloat(tag, default)
    }

    fun saveSP(tag: String, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(tag, value)
        editor.apply()
    }

    fun saveSP(tag: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(tag, value)
        editor.apply()
    }

    fun saveSP(tag: String, value: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(tag, value)
        editor.apply()
    }

    fun getPoints() : Flow<List<Point>> {
        sync()
        return database.pointDao.getPoints()
    }

    fun getPoint(id: Long) : Flow<Point> {
        sync()
        return database.pointDao.getPoint(id)
    }

    fun getPointTypes() : Flow<List<PointType>> {
        sync()
        return database.pointTypeDao.getPointTypes()
    }

    fun addPoint(point: Point) {
        GlobalScope.launch {
            val newPoint = serverDatabase.pointService.addPoint(credentials, point).execute()
            if (newPoint.isSuccessful) {
                database.pointDao.insert(newPoint.body()!!)
            }
        }
    }

    fun userLoggedIn() = sharedPreferences.contains(LOGIN_TAG)

    fun getComments(pointId: Long) : Flow<List<PointComment>> {
        sync()
        return database.pointCommentDao.getComments(pointId)
    }

    fun getImages(pointId: Long) : Flow<List<PointImage>> {
        sync()
        return database.pointImageDao.getImages(pointId)
    }

    fun addComment(pointComment: PointComment) {
        GlobalScope.launch {
            val newComment = serverDatabase.pointCommentService.addComment(credentials, pointComment).execute()
            if (newComment.isSuccessful) {
                database.pointCommentDao.insert(newComment.body()!!)
            }
        }
    }

    fun getUsers(): Flow<List<User>> {
        sync()
        return database.userDao.getUsers()
    }

    fun getPointsMyRating(pointId: Long) = database.pointRatingDao.getRating(pointId, fromSP(LOGIN_TAG, ""))

    fun ratePoint(pointRating: PointRating) {
        GlobalScope.launch {
            val newRating = serverDatabase.pointRatingService.addRating(credentials, pointRating).execute()
            if (newRating.isSuccessful) {
                database.pointRatingDao.insert(newRating.body()!!)
            }
        }
    }

    fun addImage(pointId: Long, imageBitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        GlobalScope.launch {
            val newImage = serverDatabase.pointImageService.addImage(
                credentials, pointId,
                createFormData(
                    "image", "image.png",
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"), stream.toByteArray()
                    )
                )
            ).execute()
            if (newImage.isSuccessful) {
                downloadImage(newImage.body()!!)
            }
        }
    }

    private fun downloadImage(pointImage: PointImage) {
        val request = Request.Builder()
            .url("https://sampandonte.pythonanywhere.com" + pointImage.image)
            .build()
        val response = OkHttpClient().newCall(request).execute()
        if (response.isSuccessful) {
            val bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
            val name = pointImage.id.toString() + ".png"
            val output = MainApplication.context.openFileOutput(
                name, Context.MODE_PRIVATE
            )
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.close()
            pointImage.image = name
            database.pointImageDao.insert(pointImage)
        }
    }

    fun autoLogin() = defaultPreferences.getBoolean("autologin", true)

    fun logOut() {
        val editor = sharedPreferences.edit()
        editor.remove(LOGIN_TAG)
        editor.remove(API_KEY_TAG)
        editor.apply()
    }
}