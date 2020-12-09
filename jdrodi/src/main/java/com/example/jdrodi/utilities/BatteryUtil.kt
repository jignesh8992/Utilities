package com.example.jdrodi.utilities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

/**
 * ToDo.. Get Battery percentage
 *
 * @param mContext The context
 * @return The percentage of battery
 */
fun getBatteryPercentage(mContext: Context): Int {
    var percentage = 0
    val batteryStatus = getBatteryStatusIntent(mContext)
    if (batteryStatus != null) {
        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        percentage = (level / scale.toFloat() * 100).toInt()
    }
    return percentage
}

private fun getBatteryStatusIntent(mContext: Context): Intent? {
    val batFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    return mContext.registerReceiver(null, batFilter)
}