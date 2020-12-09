package com.example.jdrodi.utilities

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

/**
 * Toast.kt - A simple class for show Snackbar.
 * @author  Jignesh N Patel
 * @date 09-12-2020
 */

fun Activity.showSnackbar(msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(window.decorView.rootView, msg, length).show()
}


fun Activity.showSnackbar(resId: Int, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(window.decorView.rootView, resources.getString(resId), length).show()
}