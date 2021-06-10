package com.example.youthspots

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    companion object {
        private var instance: MainApplication? = null
        val context: Context
            get() = instance!!.applicationContext
    }

    init {
        instance = this
    }
}