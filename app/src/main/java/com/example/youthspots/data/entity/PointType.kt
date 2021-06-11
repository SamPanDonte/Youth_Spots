package com.example.youthspots.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PointType(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
