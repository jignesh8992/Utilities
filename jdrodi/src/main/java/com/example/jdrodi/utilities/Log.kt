@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.util.Log
import com.example.jdrodi.BuildConfig

fun i(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, message)
    }
}

fun i(tag: String, message: Int) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, message.toString())
    }
}


fun d(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, message)
    }
}

fun d(tag: String, message: Int) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, message.toString())
    }
}


fun w(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.w(tag, message)
    }
}

fun w(tag: String, message: Int) {
    if (BuildConfig.DEBUG) {
        Log.w(tag, message.toString())
    }
}


fun e(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message)
    }
}

fun e(tag: String, message: Int) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message.toString())
    }
}

fun e(tag: String, error: Exception) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, error.message!!)
    }
}