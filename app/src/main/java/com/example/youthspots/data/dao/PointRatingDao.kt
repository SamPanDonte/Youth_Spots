package com.example.youthspots.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.youthspots.data.entity.PointRating
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointRatingDao : BaseDao<PointRating> {
    @Query("SELECT * FROM PointRating WHERE point = :pointId")
    abstract fun getRatings(pointId: Long) : Flow<List<PointRating>>

    @Query("SELECT COUNT(id) FROM PointRating WHERE point = :pointId AND rating")
    abstract fun getLikes(pointId: Long) : Flow<Int>

    @Query("SELECT COUNT(id) FROM PointRating WHERE point = :pointId AND NOT rating")
    abstract fun getDislikes(pointId: Long) : Flow<Int>

    @Query("SELECT * FROM PointRating WHERE point = :pointId AND author = :author LIMIT 1")
    abstract fun getRating(pointId: Long, author: String): Flow<PointRating>

    @Query("DELETE FROM PointRating")
    abstract fun clearPointRatingCache()
}