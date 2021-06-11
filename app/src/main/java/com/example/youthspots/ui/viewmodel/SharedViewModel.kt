package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.youthspots.data.Repository

class SharedViewModel : ViewModel() {
    var cameraLongitude: Double = Repository.getFromSharedPreferences(Repository.CAMERA_LONG_TAG, "0.0").toDouble()
    var cameraLatitude: Double = Repository.getFromSharedPreferences(Repository.CAMERA_LAT_TAG, "0.0").toDouble()
    var cameraZoom: Float = Repository.getFromSharedPreferences(Repository.CAMERA_ZOOM_TAG, "0.0").toFloat()
    var cameraTilt: Float = Repository.getFromSharedPreferences(Repository.CAMERA_TILT_TAG, "0.0").toFloat()
    var cameraBearing: Float = Repository.getFromSharedPreferences(Repository.CAMERA_BEARING_TAG, "0.0").toFloat()

    fun save() {
        Repository.saveInSharedPreferences(Repository.CAMERA_LONG_TAG, cameraLongitude.toString())
        Repository.saveInSharedPreferences(Repository.CAMERA_LAT_TAG, cameraLatitude.toString())
        Repository.saveInSharedPreferences(Repository.CAMERA_ZOOM_TAG, cameraZoom.toString())
        Repository.saveInSharedPreferences(Repository.CAMERA_TILT_TAG, cameraTilt.toString())
        Repository.saveInSharedPreferences(Repository.CAMERA_BEARING_TAG, cameraBearing.toString())
    }
}