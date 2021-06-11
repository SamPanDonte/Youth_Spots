package com.example.youthspots.ui.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.youthspots.ui.viewmodel.BaseViewModel

open class BaseFragment : Fragment() {
    protected fun observeModelNavigation(model : BaseViewModel) {
        model.navigationEvent.observe(this.viewLifecycleOwner, {
            it.getContent()?.let { info ->
                findNavController().navigate(info.action, info.getBundledParameters())
            }
        })
    }
}