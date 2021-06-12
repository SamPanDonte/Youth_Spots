package com.example.youthspots.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.youthspots.R
import com.example.youthspots.databinding.FragmentPointDetailsBinding
import com.example.youthspots.receiver.GeofenceReceiver
import com.example.youthspots.ui.viewmodel.PointDetailsViewModel
import com.example.youthspots.utils.PermissionUtils
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices

class PointDetailsFragment : BaseFragment() {

    private val args: PointDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentPointDetailsBinding
    private lateinit var geofencingClient: GeofencingClient
    private val mViewModel: PointDetailsViewModel by viewModels {
        PointDetailsViewModel.provideFactory(args.pointId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_point_details, container, false
        )
        geofencingClient = LocationServices.getGeofencingClient(this.requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        observeModelNavigation(mViewModel)

        mViewModel.geofenceEvent.observe(viewLifecycleOwner) {

            if (PermissionUtils.checkAndRequestPermissions(
                    this.requireActivity() as AppCompatActivity,
                    R.string.permission_rationale_location,
                    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                        if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                            addGeofence()
                        }
                    },
                    null,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )) {
                addGeofence()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence() {
        val pendingIntent = PendingIntent.getBroadcast(
            this.requireContext(),
            0,
            Intent(this.requireActivity(), GeofenceReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        geofencingClient.removeGeofences(arrayListOf(mViewModel.point.value!!.id.toString()))
        geofencingClient.addGeofences(mViewModel.getGeofencingRequest(), pendingIntent).run {
            addOnSuccessListener {
                Toast.makeText(this@PointDetailsFragment.requireContext(), "Geofence added!", Toast.LENGTH_LONG).show()
            }
            addOnFailureListener {
                Toast.makeText(this@PointDetailsFragment.requireContext(), "Geofence not added!", Toast.LENGTH_LONG).show()
            }
        }
    }
}