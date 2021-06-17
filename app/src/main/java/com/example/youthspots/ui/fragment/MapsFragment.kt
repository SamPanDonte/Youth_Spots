package com.example.youthspots.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.youthspots.R
import com.example.youthspots.data.entity.Point
import com.example.youthspots.ui.viewmodel.SharedViewModel
import com.example.youthspots.utils.NavigationInfo
import com.example.youthspots.utils.PermissionUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var map : GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (sharedViewModel.interstitialAd == null) {
            InterstitialAd.load(
                this.requireContext(),
                "ca-app-pub-3940256099942544/1033173712",
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) { sharedViewModel.interstitialAd = ad }
                }
            )
        }
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
        if (PermissionUtils.checkPermissions(activity as AppCompatActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            map.isMyLocationEnabled = true
        }

        map.setOnMarkerClickListener { marker ->
            sharedViewModel.points.value?.find {
                it.latitude == marker.position.latitude && it.longitude == marker.position.longitude
            }?.let {
                pointClick(it)
                true
            }
            false
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

        sharedViewModel.points.value?.forEach {
            map.addMarker(MarkerOptions()
                .position(LatLng(it.latitude, it.longitude))
                .title(it.name)
            )
        }

        sharedViewModel.points.observe(viewLifecycleOwner) { list ->
            map.clear()
            list.forEach {
                map.addMarker(MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .title(it.name)
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::map.isInitialized) {
            sharedViewModel.cameraBearing = map.cameraPosition.bearing
            sharedViewModel.cameraLatitude = map.cameraPosition.target.latitude
            sharedViewModel.cameraLongitude = map.cameraPosition.target.longitude
            sharedViewModel.cameraTilt = map.cameraPosition.tilt
            sharedViewModel.cameraZoom = map.cameraPosition.zoom
        }
    }

    private fun navigate(event: NavigationInfo) {
        event.let { info ->
            findNavController().navigate(info.action, info.getBundledParameters())
        }
    }

    private fun pointClick(marker: Point) {
        val info = NavigationInfo(
            R.id.action_mapsFragment_to_pointDetailsFragment,
            arrayListOf(Pair("pointId", marker.id))
        )
        if (sharedViewModel.interstitialAd != null && sharedViewModel.adCounter == 0L) {
            sharedViewModel.interstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    sharedViewModel.interstitialAd = null
                    navigate(info)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    sharedViewModel.interstitialAd = null
                    navigate(info)
                }

                override fun onAdShowedFullScreenContent() { sharedViewModel.interstitialAd = null }
            }
            sharedViewModel.interstitialAd!!.show(requireActivity())
        } else {
            navigate(info)
        }
    }
}