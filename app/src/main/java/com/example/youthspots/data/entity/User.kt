package com.example.youthspots.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val name: String,
    val score: Int,
    val place: Int,
    @PrimaryKey(autoGenerate = false)
    val id: Long
) {
    fun summary() : String {
        return "$place. $name - $score"
    }
}
