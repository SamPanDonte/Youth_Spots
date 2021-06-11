package com.example.youthspots.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.ui.viewmodel.SharedViewModel
import com.example.youthspots.utils.PermissionUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    lateinit var map : GoogleMap
    @SuppressLint("MissingPermission")
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            map.isMyLocationEnabled = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (PermissionUtils.checkPermissions(
                activity as AppCompatActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )) {
            map.isMyLocationEnabled = true
        }
        map.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    LatLng(sharedViewModel.cameraLatitude, sharedViewModel.cameraLongitude),
                    sharedViewModel.cameraZoom,
                    sharedViewModel.cameraTilt,
                    sharedViewModel.cameraBearing,
                )
            )
        )

        Repository.getPoints().asLiveData().observe(viewLifecycleOwner) {
            for (point in it) {
                map.addMarker(MarkerOptions()
                    .position(LatLng(point.latitude, point.longitude))
                    .title("${point.name} ${point.author} ${point.description}")
                )
            }
        }
    }
}