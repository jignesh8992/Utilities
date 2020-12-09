package com.example.jdrodi.utilities

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener

fun View.onViewDrawn(viewDraw: OnViewDraw) {
    val vto = viewTreeObserver
    vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            val width = measuredWidth
            val height = measuredHeight
            viewDraw.onDraw(width, height)
        }
    })
}

interface OnViewDraw {
    fun onDraw(width: Int, height: Int)
}