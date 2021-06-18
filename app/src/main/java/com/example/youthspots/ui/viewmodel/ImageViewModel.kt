package com.example.youthspots.ui.viewmodel

import android.content.Intent
import android.provider.MediaStore
import androidx.lifecycle.*
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.PointImage
import com.example.youthspots.utils.Event

class ImageViewModel(pointId: Long) : ViewModel() {
    companion object {
        fun provideFactory(id: Long) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ImageViewModel(id) as T
            }
        }
    }
    private val imagePick = MutableLiveData<Event<Intent>>()
    val imagePickEvent: LiveData<Event<Intent>>
        get() = imagePick

    val images: LiveData<List<PointImage>> = Repository.getImages(pointId).asLiveData()

    fun takeImage() {
        imagePick.value = Event(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    fun pickImage() {
        imagePick.value = Event(Intent(Intent.ACTION_PICK).also { it.type = "image/*" })
    }
}