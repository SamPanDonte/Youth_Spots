package com.example.youthspots.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.ui.viewmodel.SharedViewModel
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Repository.userLoggedIn()) {
            setContentView(R.layout.activity_main)
            MobileAds.initialize(this) { viewModel.adsLoaded = true }
            navController = findNavController(R.id.nav_host_fragment_container)
            bottomNav = findViewById(R.id.bottomNavigationView)
            bottomNav.setupWithNavController(navController)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.save()
        if (!Repository.autoLogin()) { Repository.logOut() }
    }
}