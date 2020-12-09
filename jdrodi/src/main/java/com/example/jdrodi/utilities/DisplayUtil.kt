@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.app.Activity
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.roundToInt

/**
 * DisplayUtil.kt - A simple class to perform display related task.
 * @author  Jignesh N Patel
 * @date 14-04-2020
 */

/**
 *  Hide status bar
 */
fun Activity.hideStatusBar() {
    this.window.setFlags(1024, 1024)
}

/**
 *  Get Pixel from dp
 *
 * @return The Pixel
 */
fun Activity.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).roundToInt()
}

/**
 *  Get Pixel from sp
 *
 * @return The Pixel
 */
fun Activity.spToPx(sp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics).roundToInt()
}

/**
 *  Get SP from DP
 *
 * @return The SP
 */
fun Activity.dpToSp(dp: Int): Int {
    return (dpToPx(dp) / resources.displayMetrics.scaledDensity).roundToInt()
}

/**
 *  Return height of screen
 *
 * @return The height of screen
 */
fun Activity.getDisplayHeight(): Int {
    val dm = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(dm)
    return dm.heightPixels
}

/**
 *  Return width of screen
 *
 * @return The width of screen
 */
fun Activity.getDisplayWidth(): Int {
    val dm = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}