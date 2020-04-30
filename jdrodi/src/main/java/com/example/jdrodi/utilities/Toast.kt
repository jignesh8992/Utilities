package com.example.jdrodi.utilities

import android.content.Context
import android.widget.Toast

/**
 * Toast.kt - A simple class for show Toast messages.
 * @author  Jignesh N Patel
 * @date 07-11-2019
 */
@Suppress("unused")
object Toast {

    fun short(mContext: Context, msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun short(mContext: Context, resID: Int) {
        short(mContext, mContext.getString(resID))
    }

    fun short(mContext: Context, isTrue: Boolean) {
        short(mContext, isTrue.toString())
    }

    fun long(mContext: Context, msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
    }

    fun long(mContext: Context, resID: Int) {
        long(mContext, mContext.getString(resID))
    }

    fun long(mContext: Context, isTrue: Boolean) {
        long(mContext, isTrue.toString())
    }
}