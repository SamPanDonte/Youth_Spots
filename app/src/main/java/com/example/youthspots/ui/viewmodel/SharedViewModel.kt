package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.LiveData
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.Point
import com.google.android.gms.ads.interstitial.InterstitialAd

class SharedViewModel : BaseViewModel() {
    var cameraLongitude = Repository.fromSP(Repository.CAMERA_LONG_TAG, 0.0f).toDouble()
    var cameraLatitude = Repository.fromSP(Repository.CAMERA_LAT_TAG, 0.0f).toDouble()
    var cameraZoom = Repository.fromSP(Repository.CAMERA_ZOOM_TAG, 0.0f)
    var cameraTilt = Repository.fromSP(Repository.CAMERA_TILT_TAG, 0.0f)
    var cameraBearing = Repository.fromSP(Repository.CAMERA_BEARING_TAG, 0.0f)

    lateinit var points: LiveData<List<Point>>

    var interstitialAd: InterstitialAd? = null
    var adsLoaded = false
    var adCounter: Long = Repository.fromSP(Repository.AD_COUNTER_TAG, 4)
        private set
        get () {
            return if (field == 0L) {
                field = 4
                0
            } else {
                field -= 1
                field
            }
        }

    fun save() {
        Repository.saveSP(Repository.CAMERA_LONG_TAG, cameraLongitude.toFloat())
        Repository.saveSP(Repository.CAMERA_LAT_TAG, cameraLatitude.toFloat())
        Repository.saveSP(Repository.CAMERA_ZOOM_TAG, cameraZoom)
        Repository.saveSP(Repository.CAMERA_TILT_TAG, cameraTilt)
        Repository.saveSP(Repository.CAMERA_BEARING_TAG, cameraBearing)
        Repository.saveSP(Repository.AD_COUNTER_TAG, adCounter)
    }
}