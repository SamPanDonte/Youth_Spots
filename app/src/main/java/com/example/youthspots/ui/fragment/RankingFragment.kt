package com.example.youthspots.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.youthspots.R
import com.example.youthspots.databinding.FragmentRankingBinding
import com.example.youthspots.ui.adapter.UserAdapter
import com.example.youthspots.ui.viewmodel.RankingViewModel

class RankingFragment : BaseFragment() {

    private val adapter = UserAdapter()
    private lateinit var binding: FragmentRankingBinding
    private val mViewModel: RankingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_ranking, container, false
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

        mViewModel.users.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        mViewModel.scrollEvent.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
                binding.recyclerView.scrollToPosition(it)
            }
        }
    }
}