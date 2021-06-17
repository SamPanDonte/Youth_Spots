package com.example.youthspots.utils

import android.os.Bundle

data class NavigationInfo(
    val action: Int, val parameters: List<Pair<String, Any>> = arrayListOf()
) {
    fun getBundledParameters() : Bundle {
        val bundle = Bundle()

        parameters.forEach {
            when (it.second) {
                is String -> bundle.putString(it.first, it.second as String)
                is Int -> bundle.putInt(it.first, it.second as Int)
                is Long -> bundle.putLong(it.first, it.second as Long)
            }
        }

        return bundle
    }
}