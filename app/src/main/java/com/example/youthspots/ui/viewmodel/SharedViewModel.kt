package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class SharedViewModel : ViewModel() {
    lateinit var locationProvider: FusedLocationProviderClient
}