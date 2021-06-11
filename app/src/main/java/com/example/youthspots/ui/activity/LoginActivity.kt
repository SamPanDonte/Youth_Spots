package com.example.youthspots.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.youthspots.R
import com.example.youthspots.databinding.ActivityLoginBinding
import com.example.youthspots.ui.viewmodel.BaseViewModel
import com.example.youthspots.ui.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val mViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.apply {
            viewModel = mViewModel
            lifecycleOwner = lifecycleOwner
        }
        observeModelNavigation(mViewModel)
    }

    private fun observeModelNavigation(model : BaseViewModel) {
        model.navigationEvent.observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}