package com.example.youthspots.data.entity

import android.graphics.BitmapFactory
import android.widget.ImageView
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
    val image: String,
    val point: Long,
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0
) {
    fun setImage(view: ImageView) {
        view.setImageBitmap(BitmapFactory.decodeStream(MainApplication.context.openFileInput(image)))
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
