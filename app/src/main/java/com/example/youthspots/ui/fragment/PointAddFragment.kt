package com.example.youthspots.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.youthspots.R
import com.example.youthspots.databinding.PointAddFragmentBinding
import com.example.youthspots.ui.viewmodel.PointAddViewModel
import com.example.youthspots.utils.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class PointAddFragment : BaseFragment(), AdapterView.OnItemSelectedListener {

    private val mViewModel: PointAddViewModel by viewModels()
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var mBinding: PointAddFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.point_add_fragment, container, false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }
        val typeList = arrayListOf<String>()
        mBinding.editType.onItemSelectedListener = this
        val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, typeList)
        mBinding.editType.adapter = adapter
        mViewModel.pointTypes.observe(viewLifecycleOwner) {
            for (el in it) {
                typeList.add(el.name)
            }
            adapter.notifyDataSetChanged()
        }

        locationProvider = LocationServices.getFusedLocationProviderClient(activity as Activity)
        observeModelNavigation(mViewModel)
        if (PermissionUtils.checkAndRequestPermissions(
                activity as AppCompatActivity,
                R.string.permission_rationale_add_point,
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                        it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                        getLocation()
                    }
                },
                { _, _ ->
                    Toast.makeText(context, getString(R.string.add_point_failure), Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_pointAddFragment_to_mapsFragment)
                },
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )) {
            getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationProvider.lastLocation.addOnSuccessListener {
            mViewModel.location = it
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mViewModel.pointType = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        mViewModel.pointType = null
    }
}