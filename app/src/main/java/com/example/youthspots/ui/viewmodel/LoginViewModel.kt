package com.example.youthspots.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.youthspots.MainApplication
import com.example.youthspots.data.Repository
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo

class LoginViewModel : BaseViewModel() {
    val name : MutableLiveData<String> = MutableLiveData()
    val password : MutableLiveData<String> = MutableLiveData()

    fun register() {
        if (name.value == null || name.value == "" || password.value == null || password.value == null) {
            Toast.makeText(MainApplication.context, "Fulfill all fields!", Toast.LENGTH_LONG).show()
            return
        }
        //Register todo
        Repository.saveInSharedPreferences(Repository.LOGIN_TAG, name.value!!)
        Repository.saveInSharedPreferences(Repository.API_KEY_TAG, "TestVal")
        navigateToFragment.value = Event(NavigationInfo(0))
    }

    fun login() {
        if (name.value == null || name.value == "" || password.value == null || password.value == null) {
            Toast.makeText(MainApplication.context, "Fulfill all fields!", Toast.LENGTH_LONG).show()
            return
        }
        //Login todo
        Repository.saveInSharedPreferences(Repository.LOGIN_TAG, name.value!!)
        Repository.saveInSharedPreferences(Repository.API_KEY_TAG, "TestVal")
        navigateToFragment.value = Event(NavigationInfo(0))
    }
}