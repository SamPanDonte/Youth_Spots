package com.example.youthspots.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.youthspots.R
import com.example.youthspots.data.entity.PointImage
import com.example.youthspots.databinding.ImageHolderBinding

class ImageAdapter : ListAdapter<PointImage, ImageAdapter.ImageViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageViewHolder(val binding: ImageHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup) : ImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ImageHolderBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.image_holder, parent, false
                )
                return ImageViewHolder(binding)
            }
        }

        fun bind(image: PointImage) {
            binding.image = image
            binding.executePendingBindings()
            image.setImage(binding.imageView)
        }
    }
}

private class ImageDiffCallback : DiffUtil.ItemCallback<PointImage>() {
    override fun areItemsTheSame(oldItem: PointImage, newItem: PointImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PointImage, newItem: PointImage): Boolean {
        return oldItem == newItem
    }

}