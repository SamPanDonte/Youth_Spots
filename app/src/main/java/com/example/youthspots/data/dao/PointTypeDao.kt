package com.example.youthspots.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.youthspots.data.entity.PointType
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointTypeDao : BaseDao<PointType> {
    @Query("SELECT * FROM PointType")
    abstract fun getPointTypes() : Flow<List<PointType>>
}