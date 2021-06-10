package com.example.youthspots.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Point::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("point"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PointRating(
    val rating: Boolean,
    val author: String,
    val point: Long,
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0
)
