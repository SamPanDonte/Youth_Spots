package com.example.youthspots.ui.viewmodel

import androidx.lifecycle.*
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
        users.observe(lifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }

    fun topRank() {
        adapter.submitList(usersTop.value)
        adapter.notifyDataSetChanged()
        usersTop.observe(lifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }
}