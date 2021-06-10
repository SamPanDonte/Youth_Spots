package com.example.youthspots.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.youthspots.R
import com.example.youthspots.ui.viewmodel.SharedViewModel
import com.example.youthspots.ui.viewmodel.PointAddViewModel

class PointAddFragment : Fragment() {

    private val viewModel: PointAddViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("TEST", "dsadsa")
        } else {
            sharedViewModel.locationProvider.lastLocation.addOnSuccessListener {
                viewModel.location = it
                if (it == null) {
                    Log.d("TEST", "null")
                } else {
                    Log.d("TEST", it.toString())
                }
            }
        }
        return inflater.inflate(R.layout.point_add_fragment, container, false)
    }
}