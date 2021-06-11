package com.example.youthspots.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.youthspots.R
import com.example.youthspots.databinding.FragmentCommentViewBinding
import com.example.youthspots.ui.adapter.CommentAdapter
import com.example.youthspots.ui.viewmodel.CommentViewModel

class CommentViewFragment : BaseFragment() {

    private val adapter = CommentAdapter()
    private val args: CommentViewFragmentArgs by navArgs()
    private lateinit var binding: FragmentCommentViewBinding
    private val mViewModel: CommentViewModel by viewModels {
        CommentViewModel.provideFactory(args.pointId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_comment_view, container, false
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

        mViewModel.comments.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }
}