package com.example.youthspots.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PointType::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("type"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Point(
    val name: String,
    val description: String,
    val author: String,
    val longitude: Double,
    val latitude: Double,
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val type: Long = 0
)
