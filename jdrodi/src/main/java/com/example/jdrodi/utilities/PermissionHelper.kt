package com.example.jdrodi.utilities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.example.jdrodi.R

@Suppress("unused")
// Required permission for saving video in gallery
val storagePermissions = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

/**
 * Alert dialog to navigate to app settings
 * to enable necessary permissions
 */
fun Context.showPermissionsAlert() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.permission_required))
        .setMessage(getString(R.string.permission_msg))
        .setPositiveButton(getString(R.string.permission_goto)) { _, _ -> openSettings() }
        .setNegativeButton(getString(android.R.string.cancel)) { _, _ -> }.show()
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
