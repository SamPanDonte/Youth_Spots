package com.example.youthspots.ui.viewmodel

import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.Point
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class SharedViewModel : BaseViewModel(), GoogleMap.OnMarkerClickListener {
    var cameraLongitude: Double = Repository.getFromSharedPreferences(Repository.CAMERA_LONG_TAG, "0.0").toDouble()
    var cameraLatitude: Double = Repository.getFromSharedPreferences(Repository.CAMERA_LAT_TAG, "0.0").toDouble()
    var cameraZoom: Float = Repository.getFromSharedPreferences(Repository.CAMERA_ZOOM_TAG, "0.0").toFloat()
    var cameraTilt: Float = Repository.getFromSharedPreferences(Repository.CAMERA_TILT_TAG, "0.0").toFloat()
    var cameraBearing: Float = Repository.getFromSharedPreferences(Repository.CAMERA_BEARING_TAG, "0.0").toFloat()

    var markers: ArrayList<Pair<Point, Marker>> = arrayListOf()

    fun save() {
        Repository.saveInSharedPreferences(Repository.CAMERA_LONG_TAG, cameraLongitude.toString())
        Repository.saveInSharedPreferences(Repository.CAMERA_LAT_TAG, cameraLatitude.toString())
        Repository.saveInSharedPreferences(Repository.CAMERA_ZOOM_TAG, cameraZoom.toString())
        Repository.saveInSharedPreferences(Repository.CAMERA_TILT_TAG, cameraTilt.toString())
        Repository.saveInSharedPreferences(Repository.CAMERA_BEARING_TAG, cameraBearing.toString())
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        markers.find { it.second == p0 }?.let {
            navigateToFragment.value = Event(NavigationInfo(
                R.id.action_mapsFragment_to_pointDetailsFragment,
                arrayListOf(Pair("pointId", it.first.id))
            ))
            return true
        }
        return false
    }
}