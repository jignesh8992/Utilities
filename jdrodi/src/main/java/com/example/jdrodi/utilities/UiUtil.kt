@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * UiUtil.kt - A simple helper class to make ui responsive.
 *
 * @author Jignesh N Patel
 * @date 14-11-2019
 */

private const val SOURCE_SCALE_WIDTH = 1080 // scale width of ui
private const val SOURCE_SCALE_HEIGHT = 1920 // scale height of ui

/**
 * ToDo.. Set width and height of view
 *
 * @param view     The view whose width and height are to be set
 * @param v_width  The width to be set
 * @param v_height The height to be set
 */
fun Context.setHeightWidth(view: View, v_width: Int, v_height: Int) {
    if (v_width == v_height) {
        setHeightWidth(view, v_width)
    } else {
        val dm = resources.displayMetrics
        val width = dm.widthPixels * v_width / SOURCE_SCALE_WIDTH
        val height = dm.heightPixels * v_height / SOURCE_SCALE_HEIGHT
        view.layoutParams.width = width
        view.layoutParams.height = height
    }
}

/**
 * ToDo.. Set width and height of view
 *
 * @param view     The view whose width and height are to be set
 * @param v_width_height  The width and height to be set
 */
fun Context.setHeightWidth(view: View, v_width_height: Int) {
    val dm = resources.displayMetrics
    val width = dm.widthPixels * v_width_height / SOURCE_SCALE_WIDTH
    val height = dm.widthPixels * v_width_height / SOURCE_SCALE_WIDTH
    view.layoutParams.width = width
    view.layoutParams.height = height
}


/**
 * ToDo.. Set width and height of view
 *
 * @param view     The view whose width and height are to be set
 * @param v_width  The width to be set
 */
fun Context.setWidth(view: View, v_width: Int) {
    val dm = resources.displayMetrics
    val width = dm.widthPixels * v_width / SOURCE_SCALE_WIDTH
    view.layoutParams.width = width
}

/**
 * ToDo.. Set height of view
 *
 * @param view     The view whose width and height are to be set
 * @param v_height The height to be set
 */
fun Context.setHeight(view: View, v_height: Int) {
    val dm = resources.displayMetrics
    val height = dm.heightPixels * v_height / SOURCE_SCALE_HEIGHT
    view.layoutParams.height = height
}


/**
 * ToDo.. Set padding to view
 *
 * @param view     The view whose width and height are to be set
 * @param p_left   The left padding to be set
 * @param p_top    The top padding to be set
 * @param p_right  The right padding to be set
 * @param p_bottom The bottom padding to be set
 */
fun Context.setPadding(view: View, p_left: Int, p_top: Int, p_right: Int, p_bottom: Int) {
    val dm = resources.displayMetrics
    val left = dm.widthPixels * p_left / SOURCE_SCALE_WIDTH
    val top = dm.heightPixels * p_top / SOURCE_SCALE_HEIGHT
    val right = dm.widthPixels * p_right / SOURCE_SCALE_WIDTH
    val bottom = dm.heightPixels * p_bottom / SOURCE_SCALE_HEIGHT
    view.setPadding(left, top, right, bottom)
}

/**
 * ToDo.. Set padding to view
 *
 * @param view     The view whose width and height are to be set
 * @param padding  The padding to be set
 */
fun Context.setPadding(view: View, padding: Int) {
    val dm = resources.displayMetrics
    val leftRight = dm.widthPixels * padding / SOURCE_SCALE_WIDTH
    val topBottom = dm.heightPixels * padding / SOURCE_SCALE_HEIGHT
    view.setPadding(leftRight, topBottom, leftRight, topBottom)
}


/**
 * ToDo.. Set left padding to view
 *
 * @param view     The view whose width and height are to be set
 * @param p_left   The left padding to be set
 */
fun Context.setPaddingLeft(view: View, p_left: Int) {
    val dm = resources.displayMetrics
    val left = dm.widthPixels * p_left / SOURCE_SCALE_WIDTH
    view.setPadding(left, 0, 0, 0)
}

/**
 * ToDo.. Set top padding to view
 *
 * @param view     The view whose width and height are to be set
 * @param p_top    The top padding to be set
 */
fun Context.setPaddingTop(view: View, p_top: Int) {
    val dm = resources.displayMetrics
    val top = dm.heightPixels * p_top / SOURCE_SCALE_HEIGHT
    view.setPadding(0, top, 0, 0)
}


/**
 * ToDo.. Set right padding to view
 *
 * @param view     The view whose width and height are to be set
 * @param p_right  The right padding to be set
 */
fun Context.setPaddingRight(view: View, p_right: Int) {
    val dm = resources.displayMetrics
    val right = dm.widthPixels * p_right / SOURCE_SCALE_WIDTH
    view.setPadding(0, 0, right, 0)
}

/**
 * ToDo.. Set bottom padding to view
 *
 * @param view     The view whose width and height are to be set
 * @param p_bottom The bottom padding to be set
 */
fun Context.setPaddingBottom(view: View, p_bottom: Int) {
    val dm = resources.displayMetrics
    val bottom = dm.heightPixels * p_bottom / SOURCE_SCALE_HEIGHT
    view.setPadding(0, 0, 0, bottom)
}


/**
 * ToDo.. Set margin to view
 *
 * @param view     The view whose width and height are to be set
 * @param m_left   The left margin to be set
 * @param m_top    The top margin to be set
 * @param m_right  The right margin to be set
 * @param m_bottom The bottom margin to be set
 */
fun Context.setMargins(view: View, m_left: Int, m_top: Int, m_right: Int, m_bottom: Int) {
    val dm = resources.displayMetrics
    // margin
    val left = dm.widthPixels * m_left / SOURCE_SCALE_WIDTH
    val top = dm.heightPixels * m_top / SOURCE_SCALE_HEIGHT
    val right = dm.widthPixels * m_right / SOURCE_SCALE_WIDTH
    val bottom = dm.heightPixels * m_bottom / SOURCE_SCALE_HEIGHT

    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        view.requestLayout()
    }
}

/**
 * ToDo.. Set margin to view
 *
 * @param view     The view whose width and height are to be set
 * @param margin   The margin to be set
 */
fun Context.setMargins(view: View, margin: Int) {
    val dm = resources.displayMetrics
    // margin
    val leftRight = dm.widthPixels * margin / SOURCE_SCALE_WIDTH
    val topBottom = dm.heightPixels * margin / SOURCE_SCALE_HEIGHT

    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(leftRight, topBottom, leftRight, topBottom)
        view.requestLayout()
    }
}


/**
 * ToDo.. Set left margin to view
 *
 * @param view     The view whose width and height are to be set
 * @param m_left   The left margin to be set
 */
fun Context.setMarginLeft(view: View, m_left: Int) {
    val dm = resources.displayMetrics
    // margin
    val left = dm.widthPixels * m_left / SOURCE_SCALE_WIDTH

    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, 0, 0, 0)
        view.requestLayout()
    }
}


/**
 * ToDo.. Set top margin to view
 *
 * @param view     The view whose width and height are to be set
 * @param m_top    The top margin to be set
 */
fun Context.setMarginTop(view: View, m_top: Int) {
    val dm = resources.displayMetrics
    // margin
    val top = dm.heightPixels * m_top / SOURCE_SCALE_HEIGHT
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(0, top, 0, 0)
        view.requestLayout()
    }
}

/**
 * ToDo.. Set right margin to view
 *
 * @param view     The view whose width and height are to be set
 * @param m_right  The right margin to be set
 */
fun Context.setMarginRight(view: View, m_right: Int) {
    val dm = resources.displayMetrics
    // margin
    val right = dm.widthPixels * m_right / SOURCE_SCALE_WIDTH
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(0, 0, right, 0)
        view.requestLayout()
    }
}


/**
 * ToDo.. Set bottom margin to view
 *
 * @param view     The view whose width and height are to be set
 * @param m_bottom The bottom margin to be set
 */
fun Context.setMarginBottom(view: View, m_bottom: Int) {
    val dm = resources.displayMetrics
    // margin
    val bottom = dm.heightPixels * m_bottom / SOURCE_SCALE_HEIGHT
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(0, 0, 0, bottom)
        view.requestLayout()
    }
}
