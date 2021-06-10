package com.example.youthspots.ui

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.youthspots.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    lateinit var map : GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        enableMyLocation()
        val sydney = LatLng(-35.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMyLocationClickListener {
            Log.d("MAPS", "Clicked")
        }
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.requireContext(), "LOL", Toast.LENGTH_LONG).show()
            map.isMyLocationEnabled = true
        } else {
            Toast.makeText(this.requireContext(), ":c", Toast.LENGTH_LONG).show()
            // Permission to access the location is missing. Show rationale and request permission
            //requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
            //    Manifest.permission.ACCESS_FINE_LOCATION, true
            //
        }
        }
        // [END maps_check_location_permission]

//    fun requestPermission(
//        activity: AppCompatActivity, requestId: Int,
//        permission: String, finishActivity: Boolean
//    ) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
//            // Display a dialog with rationale.
//            RationaleDialog.newInstance(requestId, finishActivity)
//                .show(activity.supportFragmentManager, "dialog")
//        } else {
//            // Location permission has not been granted yet, request it.
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(permission),
//                requestId
//            )
//        }
//    }

//    class RationaleDialog : DialogFragment() {
//        private var finishActivity = false
//        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//            val requestCode =
//                arguments?.getInt(ARGUMENT_PERMISSION_REQUEST_CODE) ?: 0
//            finishActivity =
//                arguments?.getBoolean(ARGUMENT_FINISH_ACTIVITY) ?: false
//            return AlertDialog.Builder(activity)
//                .setMessage(R.string.permission_rationale_location)
//                .setPositiveButton(android.R.string.ok) { dialog, which -> // After click on Ok, request the permission.
//                    ActivityCompat.requestPermissions(
//                        activity!!,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        requestCode
//                    )
//                    // Do not finish the Activity while requesting permission.
//                    finishActivity = false
//                }
//                .setNegativeButton(android.R.string.cancel, null)
//                .create()
//        }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}