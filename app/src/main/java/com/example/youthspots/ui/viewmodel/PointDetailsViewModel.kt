package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.Point
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo

class PointDetailsViewModel(private val pointId: Long) : BaseViewModel() {
    companion object {
        fun provideFactory(id: Long) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PointDetailsViewModel(id) as T
            }
        }
    }
    val point: LiveData<Point> = Repository.getPoint(pointId).asLiveData()

    fun viewImages() {
        navigateToFragment.value = Event(NavigationInfo(
            R.id.action_pointDetailsFragment_to_imagesViewFragment,
            arrayListOf(Pair("pointId", pointId))
        ))
    }

    fun viewComments() {
        navigateToFragment.value = Event(NavigationInfo(
            R.id.action_pointDetailsFragment_to_commentViewFragment,
            arrayListOf(Pair("pointId", pointId))
        ))
    }
}