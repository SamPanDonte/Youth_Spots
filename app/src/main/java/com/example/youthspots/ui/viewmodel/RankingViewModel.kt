package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.User
import com.example.youthspots.utils.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RankingViewModel : BaseViewModel() {
    private val scroll = MutableLiveData<Event<Int>>()
    val scrollEvent: LiveData<Event<Int>>
        get() = scroll

    val users: LiveData<List<User>> = Repository.getUsers().asLiveData()

    fun scrollTop() {
        scroll.value = Event(0)
    }

    fun scrollMe() {
        scroll.value = Event(
            users.value?.indexOfFirst { it.username == Repository.fromSP(Repository.LOGIN_TAG, "") }!!
        )
    }

    fun refresh() = GlobalScope.launch { Repository.syncUsers() }
}