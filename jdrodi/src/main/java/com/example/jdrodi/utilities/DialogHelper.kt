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
 * DialogHelper.kt - A simple class for creating alert dialog
 * @author  Jignesh N Patel
 * @date 12-10-2020
 */


fun Context.showAlert(
    titleText: String? = null,
    message: String,
    positiveText: String = getString(R.string.dialog_yes),
    negativeText: String = getString(R.string.dialog_no),
    fontPath: String? = null,
    positive: OnPositive? = null
) {

    if (titleText != null) {
        // Initialize a new foreground color span instance
        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimaryDark))

        // Initialize a new spannable string builder instance
        val ssBuilder = SpannableStringBuilder(titleText)
        //  ssBuilder.setSpan(RelativeSizeSpan(0.50f), 0, titleText.length, 0)// set size
        // Apply the text color span
        ssBuilder.setSpan(foregroundColorSpan, 0, titleText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


    val dialog1 = AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveText) { _, _ ->
            positive?.onYes()

        }
        .setNegativeButton(negativeText) { _, _ ->
            // dialog1.dismiss();
        }
        .show()

    if (titleText != null) {
        dialog1.setTitle(titleText)
    }

    try {
        val textView = dialog1.findViewById<TextView>(android.R.id.message)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        val face = Typeface.createFromAsset(assets, fontPath)
        textView.typeface = face
    } catch (e: Exception) {
        Log.e("Error", e.toString())
    }

}

interface OnPositive {
    fun onYes()
}