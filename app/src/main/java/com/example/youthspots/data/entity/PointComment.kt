package com.example.youthspots.data.entity

import androidx.room.ColumnInfo
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
data class PointComment(
    val content: String,
    @ColumnInfo(index = true)
    val point: Long,
    val author: String,
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0
)
