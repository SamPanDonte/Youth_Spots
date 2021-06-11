package com.example.youthspots.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.youthspots.data.dao.*
import com.example.youthspots.data.entity.*

@Database(
    entities = [
        Point::class, PointComment::class, PointImage::class,
        PointRating::class, PointType::class, User::class
    ],
    version = 2,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun getPointDao() : PointDao
    abstract fun getPointCommentDao() : PointCommentDao
    abstract fun getPointImageDao() : PointImageDao
    abstract fun getPointRatingDao() : PointRatingDao
    abstract fun getPointTypeDao() : PointTypeDao
    abstract fun getUserDao() : UserDao

    companion object {
        @Volatile
        private var instance: CacheDatabase? = null

        fun getInstance(context: Context): CacheDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CacheDatabase {
            return Room.databaseBuilder(
                context,
                CacheDatabase::class.java,
                "CacheDatabase"
            ).build()
        }
    }
}