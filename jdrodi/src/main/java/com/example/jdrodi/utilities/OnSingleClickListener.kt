@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.os.SystemClock
import android.view.View

/**
 * OnSingleClickListener.kt - A simple abstract class for preventing multiple clicks of view.
 * @author  Jignesh N Patel
 * @date 14-04-2020
 */

@Suppress("unused")
abstract class OnSingleClickListener : View.OnClickListener {

    private var mLastClickTime: Long = 0

    abstract fun onSingleClick(view: View)

    override fun onClick(view: View) {


        val elapsedTime = SystemClock.uptimeMillis() - mLastClickTime
        mLastClickTime = SystemClock.uptimeMillis()
        if (elapsedTime <= MIN_CLICK_INTERVAL) return
        onSingleClick(view)
    }

    companion object {
        private const val MIN_CLICK_INTERVAL: Long = 1500
    }
}