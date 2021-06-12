package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.*
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.Point
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest

class PointDetailsViewModel(private val pointId: Long) : BaseViewModel() {
    companion object {
        fun provideFactory(id: Long) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PointDetailsViewModel(id) as T
            }
        }
    }
    private val mGeofenceEvent = MutableLiveData<Event<Any?>>()
    val geofenceEvent: LiveData<Event<Any?>>
        get() = mGeofenceEvent


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

    private fun createGeofence() = Geofence.Builder()
        .setRequestId(point.value!!.id.toString())
        .setCircularRegion(point.value!!.latitude, point.value!!.longitude, 90f)
        .setExpirationDuration(86400000) // 24h
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
        .build()

    fun getGeofencingRequest(): GeofencingRequest = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(createGeofence())
        .build()

    fun addGeofence() {
        mGeofenceEvent.value = Event(null)
    }
}