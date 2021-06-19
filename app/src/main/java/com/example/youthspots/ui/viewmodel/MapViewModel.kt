package com.example.youthspots.ui.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
import androidx.core.content.ContextCompat
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

class MapViewModel(
    private val lifecycleOwner: LifecycleOwner,
    private val locationClient: FusedLocationProviderClient
) : BaseViewModel(), OnMapReadyCallback {
    companion object {
        fun provideFactory(
            lifecycle: LifecycleOwner,
            locationClient: FusedLocationProviderClient
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MapViewModel(lifecycle, locationClient) as T
            }
        }
    }

    private var cameraLongitude = Repository.fromSP(Repository.CAMERA_LONG_TAG, 0.0f).toDouble()
    private var cameraLatitude = Repository.fromSP(Repository.CAMERA_LAT_TAG, 0.0f).toDouble()
    private var cameraZoom = Repository.fromSP(Repository.CAMERA_ZOOM_TAG, 0.0f)
    private var cameraTilt = Repository.fromSP(Repository.CAMERA_TILT_TAG, 0.0f)
    private var cameraBearing = Repository.fromSP(Repository.CAMERA_BEARING_TAG, 0.0f)

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
            locationClient.requestLocationUpdates(
                LocationRequest.create().also { it.interval = 20000 },
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        for (location in locationResult.locations){
                            Repository.lastKnownLocation = location
                        }
                    }
                },
                Looper.getMainLooper()
            )
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
                    .icon(bitMapFromVector(when (it.type) {
                        2L -> R.drawable.ic_baseline_music_note_24
                        3L -> R.drawable.ic_baseline_restaurant_24
                        4L -> R.drawable.ic_baseline_beach_access_24
                        5L -> R.drawable.ic_baseline_deck_24
                        6L -> R.drawable.ic_baseline_nature_people_24
                        7L -> R.drawable.ic_baseline_home_24
                        8L -> R.drawable.ic_baseline_house_siding_24
                        9L -> R.drawable.ic_baseline_wine_bar_24
                        else -> R.drawable.ic_baseline_location_on_24
                    }))
                    .anchor(0.5f, 0.5f)
                    .position(LatLng(it.latitude, it.longitude))
                    .title(it.name)
            )
        }

        points.observe(lifecycleOwner) { list ->
            map.clear()
            list.forEach {
                map.addMarker(
                    MarkerOptions()
                        .icon(bitMapFromVector(when (it.type) {
                            2L -> R.drawable.ic_baseline_music_note_24
                            3L -> R.drawable.ic_baseline_restaurant_24
                            4L -> R.drawable.ic_baseline_beach_access_24
                            5L -> R.drawable.ic_baseline_deck_24
                            6L -> R.drawable.ic_baseline_nature_people_24
                            7L -> R.drawable.ic_baseline_home_24
                            8L -> R.drawable.ic_baseline_house_siding_24
                            9L -> R.drawable.ic_baseline_wine_bar_24
                            else -> R.drawable.ic_baseline_location_on_24
                        }))
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

    private fun bitMapFromVector(vectorResID:Int): BitmapDescriptor {
        val background = ContextCompat.getDrawable(MainApplication.context, R.drawable.ic_location_background)
        background!!.setBounds(
            0, 0, background.intrinsicWidth, background.intrinsicHeight
        )
        val vectorDrawable = ContextCompat.getDrawable(MainApplication.context, vectorResID)
        vectorDrawable!!.setBounds(
            8,8, vectorDrawable.intrinsicWidth + 8, vectorDrawable.intrinsicHeight + 8
        )
        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}