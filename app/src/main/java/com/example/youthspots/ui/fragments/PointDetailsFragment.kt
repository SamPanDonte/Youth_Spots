package com.example.youthspots.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.youthspots.R
import com.example.youthspots.databinding.FragmentPointDetailsBinding
import com.example.youthspots.ui.viewmodel.PointDetailsViewModel

class PointDetailsFragment : BaseFragment() {

    private val args: PointDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentPointDetailsBinding
    private val mViewModel: PointDetailsViewModel by viewModels {
        PointDetailsViewModel.provideFactory(args.pointId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_point_details, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
}