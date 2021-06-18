package com.example.youthspots.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.youthspots.R
import com.example.youthspots.ui.viewmodel.MapViewModel
import com.example.youthspots.ui.viewmodel.SharedViewModel
import com.example.youthspots.utils.NavigationInfo
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.maps.SupportMapFragment

class MapsFragment : Fragment() {
    private val adViewModel: SharedViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by viewModels {
        MapViewModel.provideFactory(viewLifecycleOwner)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adViewModel.loadAd(this.requireContext())
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(mapViewModel)

        mapViewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
                val ad = adViewModel.getCounterAd()
                if (ad == null) { navigate(it) } else {
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() { navigate(it) }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                            navigate(it)
                        }

                        override fun onAdShowedFullScreenContent() { }
                    }
                    ad.show(requireActivity())
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapViewModel.save()
    }

    private fun navigate(info: NavigationInfo) {
        findNavController().navigate(info.action, info.getBundledParameters())
    }
}