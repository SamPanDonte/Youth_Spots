package com.example.youthspots.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.youthspots.data.entity.PointImage
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointImageDao : BaseDao<PointImage> {
    @Query("SELECT * FROM PointImage WHERE point = :pointId")
    abstract fun getImages(pointId: Long) : Flow<List<PointImage>>

    @Query("DELETE FROM PointImage")
    abstract fun clearPointImageCache()
}