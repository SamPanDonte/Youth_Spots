package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class ActivityViewModel : ViewModel() {
    lateinit var locationProvider: FusedLocationProviderClient
}