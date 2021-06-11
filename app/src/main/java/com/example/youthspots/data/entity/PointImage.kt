package com.example.youthspots.data.entity

import android.content.Context
import android.media.Image
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
data class PointImage(
    val image: String,
    val point: Long,
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0
) {
    fun getImage(context: Context): Image? {
        context.openFileInput(image).use {
            it // TODO
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PointImage
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
