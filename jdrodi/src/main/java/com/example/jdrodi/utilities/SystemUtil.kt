package com.example.jdrodi.utilities

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.example.jdrodi.utilities.SystemUtil
import android.content.Intent
import android.provider.MediaStore
import android.content.pm.PackageManager
import android.annotation.SuppressLint
import android.view.WindowManager
import android.util.DisplayMetrics
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.view.Display
import java.lang.UnsupportedOperationException
import java.util.*

/**
 * Utility class for getting system information.
 */
class SystemUtil

/**
 * Get information if Android version is R (30).
 *
 * @return true if Kitkat.
 */
val isAndroid11: Boolean
    get() = VERSION.SDK_INT >= VERSION_CODES.R

/**
 * Check if Android version is at least the given version.
 *
 * @param version The version
 * @return true if Android version is at least the given version
 */
fun isAtLeastVersion(version: Int): Boolean {
    return VERSION.SDK_INT >= version
}

/**
 * Determine if the device has a camera activity.
 *
 * @return true if the device has a camera activity.
 */
fun hasCameraActivity(mContext: Context): Boolean {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    return takePictureIntent.resolveActivity(mContext.packageManager) != null
}

/**
 * Determine if the device has a camera.
 *
 * @return true if the device has a camera.
 */
fun hasCamera(mContext: Context): Boolean {
    val pm = mContext.packageManager
    return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
}

/**
 * Determine if the device has a flashlight.
 *
 * @return true if the device has a flashlight.
 */
fun hasFlashlight(mContext: Context): Boolean {
    val pm = mContext.packageManager
    return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
}

/**
 * Determine if the device supports a manual sensor.
 *
 * @return true if the device supports a manual sensor.
 */
@SuppressLint("InlinedApi")
fun hasManualSensor(mContext: Context): Boolean {
    val pm = mContext.packageManager
    return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_CAPABILITY_MANUAL_SENSOR)
}

/**
 * Determine if an app is installed.
 *
 * @param appPackage the app package name.
 * @return true if the app is installed.
 */
fun isAppInstalled(mContext: Context, appPackage: String?): Boolean {
    val appIntent = mContext.packageManager.getLaunchIntentForPackage(appPackage!!)
    return appIntent != null
}

/**
 * Determine if the device is a tablet (i.e. it has a large screen).
 *
 * @return true if the app is running on a tablet.
 */
fun isTablet(mContext: Context): Boolean {
    return (mContext.resources.configuration.screenLayout
            and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

/**
 * Retrieve the default display.
 *
 * @return the default display.
 */
private fun getDefaultDisplay(mContext: Context): Display {
    val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.defaultDisplay
}

/**
 * Retrieve the display size in pixels (max of x and y value).
 *
 * @return the display size.
 */
fun getDisplaySize(mContext: Context): Int {
    val p = Point()
    getDefaultDisplay(mContext).getSize(p)
    return Math.max(p.x, p.y)
}

/**
 * Retrieve the display size in inches (min of x and y value).
 *
 * @return the display size.
 */
fun getPhysicalDisplaySize(mContext: Context): Double {
    val dm = DisplayMetrics()
    getDefaultDisplay(mContext).getMetrics(dm)
    val p = Point()
    getDefaultDisplay(mContext).getSize(p)
    val x = (p.x / dm.xdpi).toDouble()
    val y = (p.y / dm.ydpi).toDouble()
    return Math.max(x, y)
}

/**
 * Get ISO 3166-1 alpha-2 country code for this device (or null if not available).
 *
 * @return country code or null
 */
fun getUserCountry(mContext: Context): String? {
    var locale: String? = null
    val tm = mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val simCountry = tm.simCountryIso
    if (simCountry != null && simCountry.length == 2) { // SIM country code is available
        locale = simCountry.toUpperCase(Locale.getDefault())
    } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
        val networkCountry = tm.networkCountryIso
        if (networkCountry != null && networkCountry.length == 2) { // network country code is available
            locale = networkCountry.toLowerCase(Locale.getDefault())
        }
    }
    if (locale == null || locale.length != 2) {
        locale = if (VERSION.SDK_INT >= VERSION_CODES.N) {
            getDefaultLocale24(mContext).country
        } else {
            getDefaultLocale23(mContext).country
        }
    }
    return locale
}

/**
 * Get the default locale for Android version below N.
 *
 * @return The default locale.
 */
private fun getDefaultLocale23(mContext: Context): Locale {
    return mContext.resources.configuration.locale
}

/**
 * Get the default locale for Android version above N.
 *
 * @return The default locale.
 */
@RequiresApi(api = VERSION_CODES.N)
private fun getDefaultLocale24(mContext: Context): Locale {
    return mContext.resources.configuration.locales[0]
}

/**
 * Get the large memory class of the device.
 *
 * @return the memory class - the maximal available memory for the app (in MB).
 */
fun getLargeMemoryClass(mContext: Context): Int {
    val manager = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return manager.largeMemoryClass
}