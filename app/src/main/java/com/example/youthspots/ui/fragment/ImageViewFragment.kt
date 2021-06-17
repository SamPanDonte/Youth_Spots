package com.example.youthspots.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.databinding.FragmentImagesViewBinding
import com.example.youthspots.ui.adapter.ImageAdapter
import com.example.youthspots.ui.viewmodel.ImageViewModel


class ImageViewFragment : BaseFragment() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private val adapter = ImageAdapter()
    private val args: ImageViewFragmentArgs by navArgs()
    private lateinit var binding: FragmentImagesViewBinding
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

        mViewModel.imagePickEvent.observe(viewLifecycleOwner) {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        try {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE) //TODO
//            val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
//                it
//            }
            //getContent.launch("TEST")
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this.requireContext(), getString(R.string.picture_error), Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Repository.addImage(args.pointId, data?.extras?.get("data") as Bitmap)
        }
    }
}