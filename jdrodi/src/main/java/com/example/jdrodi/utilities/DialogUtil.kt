@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.jdrodi.R


/**
 * DialogUtil.kt - A simple class for creating alert dialog
 * @author  Jignesh N Patel
 * @date 12-10-2020
 */

fun Context.showAlert(title: String? = null, msg: String? = null, positiveText: String? = null, negativeText: String? = null, fontPath: String? = null, positive: OnPositive? = null) {

    val dialog = AlertDialog.Builder(this)
    var alert: AlertDialog? = null


    dialog.setCancelable(false)
    if (title != null) {
        // Initialize a new foreground color span instance
        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        val ssBuilder = SpannableStringBuilder(title)
        ssBuilder.setSpan(foregroundColorSpan, 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        dialog.setTitle(ssBuilder)
    }
    if (msg != null) {
        dialog.setMessage(msg)
    }
    if (positiveText != null) {
        dialog.setPositiveButton(positiveText) { _, _ ->
            alert?.dismiss()
            positive?.onYes()
        }
    }
    if (negativeText != null) {
        dialog.setNegativeButton(negativeText) { _, _ ->
            alert?.dismiss()
            positive?.onNo()
        }
    }

    alert = dialog.create()
    alert.show()


    if (fontPath != null) {
        val textView = alert.findViewById<TextView>(android.R.id.message)
        try {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            val face = Typeface.createFromAsset(assets, fontPath)
            textView.typeface = face
        } catch (e: Exception) {
            Log.e("showAlert", e.toString())
        }
    }
}


interface OnPositive {
    fun onYes()
    fun onNo() {}
}

