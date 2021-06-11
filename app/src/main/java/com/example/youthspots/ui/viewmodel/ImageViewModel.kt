package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.PointImage

class ImageViewModel(private val pointId: Long) : ViewModel() {
    companion object {
        fun provideFactory(id: Long) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ImageViewModel(id) as T
            }
        }
    }

    val images: LiveData<List<PointImage>> = Repository.getImages(pointId).asLiveData()
}