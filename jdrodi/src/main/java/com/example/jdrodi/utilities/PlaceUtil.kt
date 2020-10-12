@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri


/**
 * PlaceUtil.kt - A simple class for find out nearby places on google map
 * @author  Jignesh N Patel
 * @date 14-04-2020
 */


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