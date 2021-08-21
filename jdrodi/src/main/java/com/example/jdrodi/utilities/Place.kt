package com.example.jdrodi.utilities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Activity.redirectMap(title: String) {

    // AppOpenManager.isInternalCall=true

    val intentUri = Uri.parse("geo:0,0?q=$title")
    val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    try {
        startActivity(mapIntent)
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    } catch (ex: ActivityNotFoundException) {
        try {
            val unrestrictedIntent = Intent(Intent.ACTION_VIEW, intentUri)
            startActivityForResult(unrestrictedIntent,0)
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        } catch (innerEx: ActivityNotFoundException) {
            Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show()
        }
    }
}