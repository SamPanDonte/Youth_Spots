package com.example.youthspots.utils

class Event<out T>(private val content: T) {
    var handled = false
        private set

    fun getContent() : T? {
        return if (handled) {
            null
        } else {
            handled = true
            content
        }
    }
}