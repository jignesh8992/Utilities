package com.example.jdrodi

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo


/**
 * AppController.kt - A simple application class.
 * @author  Jignesh N Patel
 * @date 07-11-2019
 */

class AppController : Application() {

    private var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    fun isDebuggable(): Boolean {
        val applicationInfo = context!!.packageManager.getApplicationInfo(context!!.packageName, 0)
        return applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }
}