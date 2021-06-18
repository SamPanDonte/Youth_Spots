package com.example.youthspots.ui.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.youthspots.MainApplication
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo
import com.example.youthspots.utils.PermissionUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapViewModel(private val lifecycleOwner: LifecycleOwner) : BaseViewModel(), OnMapReadyCallback {
    companion object {
        fun provideFactory(lifecycle: LifecycleOwner) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MapViewModel(lifecycle) as T
            }
        }
    }

    var cameraLongitude = Repository.fromSP(Repository.CAMERA_LONG_TAG, 0.0f).toDouble()
    var cameraLatitude = Repository.fromSP(Repository.CAMERA_LAT_TAG, 0.0f).toDouble()
    var cameraZoom = Repository.fromSP(Repository.CAMERA_ZOOM_TAG, 0.0f)
    var cameraTilt = Repository.fromSP(Repository.CAMERA_TILT_TAG, 0.0f)
    var cameraBearing = Repository.fromSP(Repository.CAMERA_BEARING_TAG, 0.0f)

    private lateinit var map : GoogleMap

    private val points = Repository.getPoints().asLiveData()

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (PermissionUtils.checkPermissions(
                MainApplication.context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            map.isMyLocationEnabled = true
        }

        map.setOnMarkerClickListener { marker ->
            points.value?.find {
                it.latitude == marker.position.latitude && it.longitude == marker.position.longitude
            }?.let {
                navigateToFragment.value = Event(NavigationInfo(
                        R.id.action_mapsFragment_to_pointDetailsFragment,
                        arrayListOf(Pair("pointId", it.id))
                ))
                true
            }
            false
        }

        map.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    LatLng(cameraLatitude, cameraLongitude), cameraZoom, cameraTilt, cameraBearing
                )
            )
        )

        points.value?.forEach {
            map.addMarker(
                MarkerOptions()
                .position(LatLng(it.latitude, it.longitude))
                .title(it.name)
            )
        }

        points.observe(lifecycleOwner) { list ->
            map.clear()
            list.forEach {
                map.addMarker(
                    MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .title(it.name)
                )
            }
        }
    }

    fun save() {
        if (this::map.isInitialized) {
            cameraBearing = map.cameraPosition.bearing
            cameraLatitude = map.cameraPosition.target.latitude
            cameraLongitude = map.cameraPosition.target.longitude
            cameraTilt = map.cameraPosition.tilt
            cameraZoom = map.cameraPosition.zoom
        }
        Repository.saveSP(Repository.CAMERA_LONG_TAG, cameraLongitude.toFloat())
        Repository.saveSP(Repository.CAMERA_LAT_TAG, cameraLatitude.toFloat())
        Repository.saveSP(Repository.CAMERA_ZOOM_TAG, cameraZoom)
        Repository.saveSP(Repository.CAMERA_TILT_TAG, cameraTilt)
        Repository.saveSP(Repository.CAMERA_BEARING_TAG, cameraBearing)
    }
}