package com.example.youthspots.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.databinding.PointAddFragmentBinding
import com.example.youthspots.ui.viewmodel.PointAddViewModel
import com.example.youthspots.ui.viewmodel.SharedViewModel
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo
import com.example.youthspots.utils.PermissionUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class PointAddFragment : Fragment() {
    private lateinit var mBinding: PointAddFragmentBinding

    private val mViewModel: PointAddViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var locationProvider: FusedLocationProviderClient
    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            getLocation()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.point_add_fragment, container, false
        )
        locationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())
        sharedViewModel.loadAd(this.requireContext())
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(
            requireContext(), R.layout.support_simple_spinner_dropdown_item, mViewModel.typeList
        )
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            editType.adapter = adapter
            editType.onItemSelectedListener = mViewModel
        }

        mViewModel.pointTypes.observe(viewLifecycleOwner) { list ->
            list.forEach { mViewModel.typeList.add(it.name) }
            adapter.notifyDataSetChanged()
        }

        if (PermissionUtils.checkAndRequestPermissions(
                activity as AppCompatActivity, R.string.permission_rationale_add_point,
                launcher, { _, _ ->
                    Toast.makeText(
                        context, getString(R.string.add_point_failure), Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_pointAddFragment_to_mapsFragment)
                }, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )) {
            getLocation()
        }

        mViewModel.navigationEvent.observe(viewLifecycleOwner) {
            val ad = sharedViewModel.getAd()
            if (ad == null) { navigate(it) } else {
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() { navigate(it) }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        navigate(it)
                    }

                    override fun onAdShowedFullScreenContent() { }
                }
                ad.show(this.requireActivity())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationProvider.lastLocation.addOnSuccessListener {
            mViewModel.location = it
            Repository.lastKnownLocation = it
        }
    }

    private fun navigate(event: Event<NavigationInfo>) {
        event.getContent()?.let { info ->
            findNavController().navigate(info.action, info.getBundledParameters())
        }
    }
}