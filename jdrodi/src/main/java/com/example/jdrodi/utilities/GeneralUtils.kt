@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*


/**
 * Toast.kt - A simple class for common extensive functions.
 * @author  Jignesh N Patel
 * @date 07-11-2019
 */


/**
 * Check weather internet connection is running or not
 *
 * @return true if working otherwise false
 */
@Suppress("DEPRECATION")
fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                -> return true
                else -> false
            }
        }
    } else {
        try {
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            Log.e("isNetworkAvailable", e.toString())
        }
    }
    return false
}


/**
 * Generate random number from range
 *
 * @return random number from given range
 */
fun rand(start: Int, end: Int): Int {
    require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
    return Random(System.nanoTime()).nextInt(end - start + 1) + start
}


/**
 * Convert String into camel case
 *
 * @return string in camel case format
 */
fun camelCaseString(string: String): String {
    val words = string.split(" ").toMutableList()
    var output = ""
    for (word in words) {
        output += word.capitalize() + " "
    }
    return output.trim()
}


/**
 * Truncate String to specif length
 *
 * @param  str to truncate
 * @param len to truncate string to specif length
 * @return The truncate string
 */
fun truncate(str: String, len: Int): String {
    return if (str.length > len) {
        str.substring(0, len) + "..."
    } else {
        str
    }
}


/**
 *  Return Current date in given Pattern
 */
fun getToday(): String {
    return SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
}


/**
 * Change font style of tab layout
 *
 * @param  tabs The tab layout
 * @param fontPath The font path
 */

fun Context.changeTabsFont(tabs: TabLayout, fontPath: String) {
    val vg = tabs.getChildAt(0) as ViewGroup
    val tabsCount = vg.childCount
    for (j in 0 until tabsCount) {
        val vgTab = vg.getChildAt(j) as ViewGroup
        val tabChildsCount = vgTab.childCount
        for (i in 0 until tabChildsCount) {
            val tabViewChild: View = vgTab.getChildAt(i)
            if (tabViewChild is TextView) {
                try {
                    val mgr = assets
                    val tf = Typeface.createFromAsset(mgr, fontPath)
                    //Font file in /assets
                    tabViewChild.typeface = tf
                } catch (e: java.lang.Exception) {
                    Log.e("Error", e.toString())
                }
            }
        }
    }
}



fun String.addCharAtIndex(char: Char, index: Int) = StringBuilder(this).apply { insert(index, char) }.toString()


fun Context.getDeviceId(): String? {
    return Settings.Secure.getString(contentResolver, "android_id")
}

