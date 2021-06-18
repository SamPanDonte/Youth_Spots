package com.example.youthspots.ui.fragment

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.databinding.FragmentImagesViewBinding
import com.example.youthspots.ui.adapter.ImageAdapter
import com.example.youthspots.ui.viewmodel.ImageViewModel


class ImageViewFragment : BaseFragment() {

    private val adapter = ImageAdapter()
    private val args: ImageViewFragmentArgs by navArgs()
    private lateinit var binding: FragmentImagesViewBinding
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val result = if (it.data?.extras?.get("data") is Bitmap) {
            it.data?.extras?.get("data")
        } else {
            if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver, it.data?.data!!
                )
            } else {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(
                    requireActivity().contentResolver, it.data?.data!!
                ))
            }
        } as Bitmap
        Repository.addImage(args.pointId, result)
    }

    private val mViewModel: ImageViewModel by viewModels {
        ImageViewModel.provideFactory(args.pointId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_images_view, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerView.adapter = adapter
        }

        mViewModel.images.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        mViewModel.imagePickEvent.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let { launcher.launch(it) }
        }
    }
}