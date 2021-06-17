package com.example.youthspots.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.youthspots.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao : BaseDao<User> {
    @Query("SELECT * FROM User ORDER BY place")
    abstract fun getUsers(): Flow<List<User>>

    @Query("DELETE FROM User")
    abstract fun clearUserCache()
}