package com.example.youthspots.ui.viewmodel

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
    private val imagePick = MutableLiveData<Event<Any?>>()
    val imagePickEvent: LiveData<Event<Any?>>
        get() = imagePick

    val images: LiveData<List<PointImage>> = Repository.getImages(pointId).asLiveData()

    fun addImage() {
        imagePick.value = Event(null)
    }
}