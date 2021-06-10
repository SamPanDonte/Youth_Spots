package com.example.youthspots.data

import com.example.youthspots.MainApplication

object Repository {
    private val database = CacheDatabase.getInstance(MainApplication.context)

    fun getPoints() = database.getPointDao().getPoints()

    fun getPointTypes() = database.getPointTypeDao().getPointTypes()
}