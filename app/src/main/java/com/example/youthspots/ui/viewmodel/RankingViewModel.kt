package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel
import com.example.youthspots.data.Repository
import com.example.youthspots.data.entity.User
import com.example.youthspots.ui.adapter.UserAdapter

class RankingViewModel(val adapter: UserAdapter, val lifecycleOwner: LifecycleOwner) : BaseViewModel() {
    companion object {
        fun provideFactory(adapter: UserAdapter, lifecycleOwner: LifecycleOwner) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RankingViewModel(adapter, lifecycleOwner) as T
            }
        }
    }

    val usersTop: LiveData<List<User>> = Repository.getTopUsers().asLiveData()
    val users: LiveData<List<User>> = Repository.getMyRanking().asLiveData()

    fun setMyRank() {
        adapter.submitList(users.value)
        adapter.notifyDataSetChanged()
        users.removeObservers(lifecycleOwner)
        users.observe(lifecycleOwner) {
            adapter.submitList(it.reversed())
            adapter.notifyDataSetChanged()
        }
    }

    fun topRank() {
        adapter.submitList(usersTop.value)
        adapter.notifyDataSetChanged()
        usersTop.removeObservers(lifecycleOwner)
        usersTop.observe(lifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }
}