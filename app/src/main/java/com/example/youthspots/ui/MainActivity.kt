package com.example.youthspots.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.youthspots.R
import com.example.youthspots.ui.viewmodel.ActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityViewModel.locationProvider = LocationServices.getFusedLocationProviderClient(this)
        navController = findNavController(R.id.nav_host_fragment_container)
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController) // TODO
    }
}