package com.example.youthspots.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.youthspots.data.entity.PointComment
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointCommentDao : BaseDao<PointComment> {
    @Query("SELECT * FROM PointComment WHERE id = :pointId")
    abstract fun getComments(pointId: Long): Flow<List<PointComment>>
}