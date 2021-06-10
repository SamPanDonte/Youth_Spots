package com.example.youthspots.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.youthspots.data.entity.Point
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointDao : BaseDao<Point> {
    @Query("SELECT * FROM Point")
    abstract fun getPoints() : Flow<List<Point>>
}