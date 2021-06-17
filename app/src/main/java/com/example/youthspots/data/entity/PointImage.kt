package com.example.youthspots.data.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.youthspots.MainApplication

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
    var image: String,
    @ColumnInfo(index = true)
    val point: Long,
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0
) {
    fun getBitmap(): Bitmap = BitmapFactory.decodeStream(MainApplication.context.openFileInput(image))
}
