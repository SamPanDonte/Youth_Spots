package com.example.youthspots.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.*
import com.example.youthspots.MainApplication
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
    val comment: MutableLiveData<String> = MutableLiveData()

    fun addComment() {
        if (comment.value == null || comment.value == "") {
            Toast.makeText(MainApplication.context, "You must fulfil comment!", Toast.LENGTH_LONG).show()
            return
        }
        // TODO Interent
        Repository.addComment(PointComment(
            comment.value!!,
            pointId,
            Repository.getFromSharedPreferences(Repository.LOGIN_TAG),
        ))
    }
}