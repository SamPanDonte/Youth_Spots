package com.example.youthspots.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.youthspots.MainApplication
import com.example.youthspots.data.Repository
import com.example.youthspots.data.Service
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
            Toast.makeText(MainApplication.context, "Fulfill all fields!", Toast.LENGTH_LONG).show()
            return
        }
        Service.service.register(name.value!!, password.value!!).enqueue(object :
            retrofit2.Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                Repository.saveInSharedPreferences(Repository.LOGIN_TAG, name.value!!)
                Repository.saveInSharedPreferences(Repository.API_KEY_TAG, response.body()!!.token)
                navigateToFragment.value = Event(NavigationInfo(0))
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(MainApplication.context, "Failed!", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun login() {
        if (name.value == null || name.value == "" || password.value == null || password.value == null) {
            Toast.makeText(MainApplication.context, "Fulfill all fields!", Toast.LENGTH_LONG).show()
            return
        }
        Service.service.login(name.value!!, password.value!!).enqueue(object :
            retrofit2.Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                Repository.saveInSharedPreferences(Repository.LOGIN_TAG, name.value!!)
                Repository.saveInSharedPreferences(Repository.API_KEY_TAG, response.body()!!.token)
                navigateToFragment.value = Event(NavigationInfo(0))
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(MainApplication.context, "Failed!", Toast.LENGTH_LONG).show()
            }

        })
    }
}