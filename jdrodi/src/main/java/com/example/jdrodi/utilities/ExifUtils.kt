package com.example.jdrodi.utilities

import android.text.TextUtils
import androidx.exifinterface.media.ExifInterface
import java.lang.Exception

fun getExifRotation(str: String): Int {
    return try {
        val attribute = android.media.ExifInterface(str).getAttribute(ExifInterface.TAG_ORIENTATION)
        if (TextUtils.isEmpty(attribute)) {
            return 0
        }
        val parseInt = attribute!!.toInt()
        if (parseInt == 1) {
            return 0
        }
        if (parseInt == 3) {
            return 180
        }
        if (parseInt == 6) {
            return 90
        }
        if (parseInt != 8) {
            0
        } else 270
    } catch (unused: Exception) {
        0
    }
}