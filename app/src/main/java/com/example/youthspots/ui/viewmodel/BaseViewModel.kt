package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.youthspots.utils.Event

open class BaseViewModel : ViewModel() {
    protected val navigateToFragment = MutableLiveData<Event<Int>>()
    val navigationEvent: LiveData<Event<Int>>
        get() = navigateToFragment
}