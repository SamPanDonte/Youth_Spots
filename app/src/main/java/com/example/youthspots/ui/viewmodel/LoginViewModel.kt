package com.example.youthspots.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.youthspots.MainApplication
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.data.ServerDatabase
import com.example.youthspots.data.entity.AuthData
import com.example.youthspots.data.entity.Token
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo
import retrofit2.Call
import retrofit2.Response

class LoginViewModel : BaseViewModel() {
    val name : MutableLiveData<String> = MutableLiveData()
    val password : MutableLiveData<String> = MutableLiveData()

    fun register() {
        if (name.value == null || name.value == "" || password.value == null || password.value == null) {
            Toast.makeText(
                MainApplication.context,
                MainApplication.context.getString(R.string.fulfill_all),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        ServerDatabase.userService.register(AuthData(name.value!!, password.value!!)).enqueue(
            object : retrofit2.Callback<Token> {

                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if (response.isSuccessful) {
                        Repository.saveSP(Repository.LOGIN_TAG, name.value!!)
                        Repository.saveSP(Repository.API_KEY_TAG, response.body()!!.token)
                        navigateToFragment.value = Event(NavigationInfo(0))
                    } else {
                        Toast.makeText(
                            MainApplication.context,
                            MainApplication.context.getString(R.string.register_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(
                        MainApplication.context,
                        MainApplication.context.getString(R.string.server_connection_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    fun login() {
        if (name.value == null || name.value == "" || password.value == null || password.value == null) {
            Toast.makeText(
                MainApplication.context,
                MainApplication.context.getString(R.string.fulfill_all),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        ServerDatabase.userService.login(AuthData(name.value!!, password.value!!)).enqueue(
            object : retrofit2.Callback<Token> {

                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if (response.isSuccessful) {
                        Repository.saveSP(Repository.LOGIN_TAG, name.value!!)
                        Repository.saveSP(Repository.API_KEY_TAG, response.body()!!.token)
                        navigateToFragment.value = Event(NavigationInfo(0))
                    } else {
                        Toast.makeText(
                            MainApplication.context,
                            MainApplication.context.getString(R.string.login_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(
                        MainApplication.context,
                        MainApplication.context.getString(R.string.server_connection_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}