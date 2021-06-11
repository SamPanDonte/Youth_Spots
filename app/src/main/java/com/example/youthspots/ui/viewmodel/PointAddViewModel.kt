package com.example.youthspots.ui.viewmodel

import android.location.Location
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.youthspots.MainApplication
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.Point
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo

class PointAddViewModel : BaseViewModel() {
    var location: Location? = null
    val name: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val pointTypes = Repository.getPointTypes().asLiveData()
    var pointType: Int? = null
    private val author = Repository.getFromSharedPreferences(Repository.LOGIN_TAG)

    fun submit() {
        if (location == null || name.value == null || name.value == "" || description.value == null ||
                description.value == "" || pointType == null) {
            Toast.makeText(MainApplication.context, "Fulfill all fields!", Toast.LENGTH_LONG).show()
            return
        }

        Repository.addPoint(Point(name.value!!, description.value!!, author, location!!.longitude, location!!.latitude, 0, pointTypes.value?.get(pointType!!)?.id!!))
        navigateToFragment.value = Event(NavigationInfo(R.id.action_pointAddFragment_to_mapsFragment))
    }
}