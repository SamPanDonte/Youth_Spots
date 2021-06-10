package com.example.youthspots.ui.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.youthspots.data.Repository

class PointAddViewModel : ViewModel() {
    var location: Location? = null
    val name: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val pointTypes = Repository.getPointTypes().asLiveData()
    //TODO chosen point
    val author = "This user" // TODO

    fun submit() {
        //TODO check data completition
        //todo change activity
    }
}