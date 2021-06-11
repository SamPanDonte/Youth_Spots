package com.example.youthspots.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.youthspots.R
import com.example.youthspots.data.entity.PointComment
import com.example.youthspots.databinding.CommentHolderBinding

class CommentAdapter : ListAdapter<PointComment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentViewHolder(val binding: CommentHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup) : CommentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: CommentHolderBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.comment_holder, parent, false
                )
                return CommentViewHolder(binding)
            }
        }

        fun bind(comment: PointComment) {
            binding.comment = comment
            binding.executePendingBindings()
        }
    }
}

private class CommentDiffCallback : DiffUtil.ItemCallback<PointComment>() {
    override fun areItemsTheSame(oldItem: PointComment, newItem: PointComment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PointComment, newItem: PointComment): Boolean {
        return oldItem == newItem
    }
}