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
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.ui.viewmodel.BaseViewModel
import com.example.youthspots.ui.viewmodel.SharedViewModel
import com.example.youthspots.utils.Event
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
    lateinit var map : GoogleMap
    private var interstitialAd: InterstitialAd? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this.requireContext(),
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(p0: InterstitialAd) {
                    interstitialAd = p0
                }
            }
        )

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        observeModelNavigation(sharedViewModel)
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
        map.setOnMarkerClickListener(sharedViewModel)
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
                    .title(point.name)
                )?.let { marker ->
                    sharedViewModel.markers.add(Pair(point, marker))
                }

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

    private fun observeModelNavigation(model : BaseViewModel) {
        model.navigationEvent.observe(this.viewLifecycleOwner, {
            if (interstitialAd == null) {
                navigate(it)
            } else {
                interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        navigate(it)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        navigate(it)
                    }

                    override fun onAdShowedFullScreenContent() {
                        interstitialAd = null
                    }
                }
                interstitialAd?.show(requireActivity())
            }
        })
    }

    private fun navigate(event: Event<NavigationInfo>) {
        event.getContent()?.let { info ->
            findNavController().navigate(info.action, info.getBundledParameters())
        }
    }
}