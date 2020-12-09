package com.example.jdrodi.utilities

import android.util.Base64

/**
 * DisplayUtil.kt - A simple class to encode and decode string using Base64.
 * @author  Jignesh N Patel
 * @date 11-11-2020
 */

fun String.decode(): String {
    return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
}

fun String.encode(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.DEFAULT)
}