package com.example.jdrodi.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.example.jdrodi.R

/**
 * This Class provides A feature to lock the ViewPager
 * Whether in Java : setSwipeLocked(true)
 * Or in Xml: lock_swipe = "true"
 */
class LockableViewPager : ViewPager {
    var swipeLocked = false
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        // the code below is for initializing swipeLocked from xml
        val a = context.obtainStyledAttributes(attrs, R.styleable.LockableViewPager)
        swipeLocked = try {
            a.getBoolean(R.styleable.LockableViewPager_lock_swipe, false)
        } finally {
            a.recycle()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return !swipeLocked && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return !swipeLocked && super.onInterceptTouchEvent(event)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return !swipeLocked && super.canScrollHorizontally(direction)
    }
}