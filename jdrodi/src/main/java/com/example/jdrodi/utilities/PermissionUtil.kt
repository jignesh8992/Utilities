@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jdrodi.R
import java.util.*

/**
 * PermissionUtil.kt - A simple permissions helper class.
 * @author  Jignesh N Patel
 * @date 08-11-2019
 */

// messages
private const val FAILED_SINGLE = "Required Permission not granted"
private const val FAILED_MULTIPLE = "Required Permissions not granted"

@Suppress("unused")
// Required permission for saving video in gallery
val storagePermissions = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

/**
 * ToDo.. Check if permission is already exist
 *
 * @param permission The name of the permission being checked.
 * @return [PackageManager.PERMISSION_GRANTED] if you have the
 * permission, or [PackageManager.PERMISSION_DENIED] if not.
 */
fun Context.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this as Activity, permission) == PackageManager.PERMISSION_GRANTED
}

/**
 * ToDo.. Check if permissions are already exist
 *
 * @param permissions The name of the permission being checked.
 * @return [PackageManager.PERMISSION_GRANTED] if you have the
 * permission, or [PackageManager.PERMISSION_DENIED] if not.
 */
fun Context.hasPermission(permissions: Array<String>): Boolean {
    var result: Int
    val listPermissionsNeeded = ArrayList<String>()
    for (permission in permissions) {
        result = ContextCompat.checkSelfPermission(this, permission)
        if (result != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(permission)
        }
    }
    return listPermissionsNeeded.isEmpty()
}


/**
 * Alert dialog to navigate to app settings
 * to enable necessary permissions
 *
 * @param fontPath The path of font style to change typeface
 */
fun Context.showPermissionsAlert(fontPath: String? = null) {
    val title = getString(R.string.permission_required)
    val message = getString(R.string.permission_msg)
    val positiveText = getString(R.string.permission_goto)
    val negativeText = getString(android.R.string.cancel)
    showAlert(title, message, positiveText, negativeText, fontPath, object : OnPositive {
        override fun onYes() {
            openSettings()
        }
    })
}

/**
 * Alert dialog to navigate to app settings
 * to enable necessary permissions
 *
 * @param permission_msg The message of alert dialog
 * @param fontPath The path of font style to change typeface
 */
fun Context.showPermissionsAlert(permission_msg: String, fontPath: String) {
    val title = getString(R.string.permission_required)
    val positiveText = getString(R.string.permission_goto)
    val negativeText = getString(android.R.string.cancel)
    showAlert(title, permission_msg, positiveText, negativeText, fontPath, object : OnPositive {
        override fun onYes() {
            openSettings()
        }
    })
}

/**
 * Open device app settings to allow user to enable permissions
 */
private fun Context.openSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.fromParts("package", packageName, null)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}


//    public static boolean canDrawOverlays(final Context mContext, boolean openSettings,
//                                          int REQ_DRAW_OVER_PERMISSION) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(mContext)) {
//            //If the draw over permission is not available open the settings screen
//            //to grant the permission.
//            if (openSettings) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
//                        "package:" + mContext.getPackageName()));
//                ((Activity) mContext).startActivityForResult(intent, REQ_DRAW_OVER_PERMISSION);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        Intent act = new Intent(mContext, PermissionActivity.class);
////                        act.putExtra("for", "overlay");
////                        ((Activity) mContext).startActivity(act);
//                    }
//                }, 0);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }


/**
 * ToDo.. Check if Battery Optimization is enabled or not
 *
 * @param openSettings To open system enabled screen if battery optimization is disabled
 * @param IGNORE_BATTERY_OPTIMIZATION_REQUEST The request code
 *
 * @return true if enabled otherwise false
 */
fun Context.isBatteryOptimization(openSettings: Boolean = false, IGNORE_BATTERY_OPTIMIZATION_REQUEST: Int = 101): Boolean {
    val pm = getSystemService(POWER_SERVICE) as PowerManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            if (openSettings) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                (this as Activity).startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST)
            }
            true
        } else {
            false
        }
    } else {
        return false
    }
}

/**
 * ToDo.. Check if Notification Service is enabled or not
 *
 * @param openSettings To open system enabled screen if battery optimization is disabled
 * @param REQ_NOTIFICATION_LISTENER The request code
 *
 * @return true if enabled otherwise false
 */
fun Context.isNotificationServiceRunning(openSettings: Boolean = false, REQ_NOTIFICATION_LISTENER: Int = 102): Boolean {
    val contentResolver = contentResolver
    val enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
    val packageName = packageName
    return if (enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName)) {
        true
    } else {
        if (openSettings) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                (this as Activity).startActivityForResult(intent, REQ_NOTIFICATION_LISTENER)
            }
        }

        false
    }
}

