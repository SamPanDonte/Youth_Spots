package com.example.youthspots.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val username: String,
    val score: Int,
    val place: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long
) {
    fun summary() : String {
        return "$place. $username - $score"
    }
}
