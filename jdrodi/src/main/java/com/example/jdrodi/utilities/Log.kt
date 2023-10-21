@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.util.Log
import com.example.jdrodi.AppController

/**
 * Log.kt - A simple log class to print logs in debug mode only
 * @author  Jignesh N Patel
 * @date 14-04-2020
 */

fun i(tag: String, message: String) {
    if (AppController().isDebuggable()) {
        Log.i(tag, message)
    }
}

fun i(tag: String, message: Int) {
    if (AppController().isDebuggable()) {
        Log.i(tag, message.toString())
    }
}


fun d(tag: String, message: String) {
    if (AppController().isDebuggable()) {
        Log.d(tag, message)
    }
}

fun d(tag: String, message: Int) {
    if (AppController().isDebuggable()) {
        Log.d(tag, message.toString())
    }
}


fun w(tag: String, message: String) {
    if (AppController().isDebuggable()) {
        Log.w(tag, message)
    }
}

fun w(tag: String, message: Int) {
    if (AppController().isDebuggable()) {
        Log.w(tag, message.toString())
    }
}


fun e(tag: String, message: String) {
    if (AppController().isDebuggable()) {
        Log.e(tag, message)
    }
}

fun e(tag: String, message: Int) {
    if (AppController().isDebuggable()) {
        Log.e(tag, message.toString())
    }
}

fun e(tag: String, error: Exception) {
    if (AppController().isDebuggable()) {
        Log.e(tag, error.message!!)
    }
}