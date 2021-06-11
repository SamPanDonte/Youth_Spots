package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo

open class BaseViewModel : ViewModel() {
    protected val navigateToFragment = MutableLiveData<Event<NavigationInfo>>()
    val navigationEvent: LiveData<Event<NavigationInfo>>
        get() = navigateToFragment
}