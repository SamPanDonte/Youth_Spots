package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.PointComment

class CommentViewModel(private val pointId: Long) : ViewModel() {
    companion object {
        fun provideFactory(id: Long) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(id) as T
            }
        }
    }

    val comments: LiveData<List<PointComment>> = Repository.getComments(pointId).asLiveData()
}