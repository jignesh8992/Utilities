@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.jdrodi.utilities.Toast.toast

/**
 * ToDo.. Redirect to google map for find out nearby places
 *
 * @param keyword The keyword to find out nearby places
 */
fun Context.redirectMap(keyword: String) {
    val intentUri = Uri.parse("geo:0,0?q=$keyword")
    val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    try {
        startActivity(mapIntent)
    } catch (ex: ActivityNotFoundException) {
        try {
            val unrestrictedIntent = Intent(Intent.ACTION_VIEW, intentUri)
            startActivity(unrestrictedIntent)
        } catch (innerEx: ActivityNotFoundException) {
            toast("Please install a maps application")
        }
    }
}