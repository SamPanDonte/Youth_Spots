package com.example.youthspots.ui.viewmodel

import android.location.Location
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.youthspots.MainApplication
import com.example.youthspots.R
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.Point
import com.example.youthspots.utils.Event
import com.example.youthspots.utils.NavigationInfo

class PointAddViewModel : BaseViewModel(), AdapterView.OnItemSelectedListener {
    var location: Location? = null
    val pointTypes = Repository.getPointTypes().asLiveData()
    val typeList: ArrayList<String> = arrayListOf()
    val name: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    private var pointType: Int? = null
    private val author = Repository.fromSP(Repository.LOGIN_TAG, "")

    fun submit() {
        if (location == null || name.value == null || name.value == "" || description.value == null ||
                description.value == "" || pointType == null) {
            Toast.makeText(
                MainApplication.context, MainApplication.context.getText(R.string.fulfill_all),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        Repository.addPoint(Point(
            name.value!!, description.value!!, author, location!!.longitude,
            location!!.latitude, 0, pointTypes.value?.get(pointType!!)?.id!!
        ))
        navigateToFragment.value = Event(NavigationInfo(R.id.action_pointAddFragment_to_mapsFragment))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        pointType = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        pointType = null
    }
}