package com.example.jdrodi.widgets

import android.content.Context
import androidx.cardview.widget.CardView

/**
 *SquareCardView - A simple widget for create square layout.
 * @author  Jignesh N Patel
 * @date 14-04-2020
 */

class SquareCardView(context: Context) : CardView(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}